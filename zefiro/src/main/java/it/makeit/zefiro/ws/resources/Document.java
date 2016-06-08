package it.makeit.zefiro.ws.resources;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.CmisQueryBuilder;
import it.makeit.alfresco.CmisQueryPredicate;
import it.makeit.alfresco.CmisQueryPredicate.Operator;
import it.makeit.alfresco.webscriptsapi.services.ThumbnailDefinitions;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.print.PrintFormat;
import it.makeit.jbrick.print.PrintUtil;
import it.makeit.jbrick.web.LocaleUtil;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.DocumentBean;
import it.makeit.zefiro.dao.DocumentPropertyBean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;

@Path("/Document")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Document {

	private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	private static final Tika TIKA = new Tika();

	private static final String CONTAINS_FIELD = "contains";
	private static final String EQ = "eq";
	private static final String FROM = "ge";
	private static final String TO = "le";
	private static final String LIKE = "lk";

	private static final String mAlfrescoRootFolderID = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@rootFolderId");
	private static final String mAlfrescoBaseTypeId = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@baseTypeId");

	@Context
	private HttpServletRequest httpRequest;

	private List<QueryFilter> getQueryFiltersMap(Map<String, String[]> pMapParams) {
		List<QueryFilter> lListFilters = new LinkedList<QueryFilter>();
		for (String prop : pMapParams.keySet()) {

			// Decodifica FROM/TO
			String[] lDecodedProp = prop.split("\\|");
			String lStrPropertyId = lDecodedProp[0];
			String lStrOperator = lDecodedProp.length > 1 ? lDecodedProp[lDecodedProp.length-1].toLowerCase() : "default";

			// Non gestiti i parametri con cardinalità multipla
			String lStrParamValue = pMapParams.get(prop)[0];
			
			// Eventuali criteri per valori nulli o vuoti vanno gestiti ad hoc
			if (StringUtils.isBlank(lStrParamValue)) {
				continue;
			}

			QueryFilter lQueryFilter = new QueryFilter();
			lQueryFilter.propertyId = lStrPropertyId;
			// Se è stato specificato un operatore
			switch (lStrOperator) {
			case EQ:
				lQueryFilter.operator = Operator.EQ;
				break;
			case FROM:
				lQueryFilter.operator = Operator.GE;
				break;
			case TO:
				lQueryFilter.operator = Operator.LE;
				break;
			case LIKE:
				lQueryFilter.operator = Operator.LIKE;
				break;
			default:
				// Niente: verrà usaro l'operatore predefinito per il tipo di dato
				break;
			}

			if (lStrPropertyId.equals(CONTAINS_FIELD)) {
				lQueryFilter.operator = Operator.CONTAINS;
			}

			lQueryFilter.value = lStrParamValue;

			lListFilters.add(lQueryFilter);
		}

		return lListFilters;
	}

	private List<CmisQueryPredicate<?>> getQueryPredicates(TypeDefinition pTypeDef, Map<String, String[]> pMapParams)
	        throws ParseException {

		List<CmisQueryPredicate<?>> lList = new LinkedList<CmisQueryPredicate<?>>();

		Map<String, PropertyDefinition<?>> lMapProperties = pTypeDef.getPropertyDefinitions();
		List<QueryFilter> lListFilters = getQueryFiltersMap(pMapParams);
		for (QueryFilter queryFilter : lListFilters) {
			String lStrPropertyId = queryFilter.propertyId;
			
			CmisQueryPredicate<?> lPredicate = null;
			
			if (lStrPropertyId.equals(CONTAINS_FIELD)) {
				lPredicate = CmisQueryPredicate.contains(queryFilter.value);
				lList.add(lPredicate);
				continue;
			}

			PropertyDefinition<?> lPropDef = lMapProperties.get(lStrPropertyId);
			if (lPropDef == null) {
				// La proprietà richiesta come filtro non è definita per il tipo documento
				continue;
			}

			switch (lPropDef.getPropertyType()) {
			case BOOLEAN:
				lPredicate = CmisQueryPredicate.eqTo(lStrPropertyId, Boolean.valueOf(queryFilter.value));
				break;

			case DATETIME:
				// XXX (Alessio): usare un'istanza statica di SimpleDateFormat?
				SimpleDateFormat lFormatter = new SimpleDateFormat(DATE_PATTERN);
				Date lDate = lFormatter.parse(queryFilter.value);
				if (queryFilter.operator == Operator.LE) {
					lPredicate = CmisQueryPredicate.between(lStrPropertyId, null, lDate);

				} else if (queryFilter.operator == Operator.GE) {
						lPredicate = CmisQueryPredicate.between(lStrPropertyId, lDate, null);

				} else {
					lPredicate = CmisQueryPredicate.eqTo(lStrPropertyId, lDate);
				}
				break;

			case DECIMAL:
			case INTEGER:
				BigDecimal lBD = new BigDecimal(queryFilter.value);
				if (queryFilter.operator == Operator.LE) {
					lPredicate = CmisQueryPredicate.between(lStrPropertyId, null, lBD);

				} else if (queryFilter.operator.equals(Operator.GE)) {
						lPredicate = CmisQueryPredicate.between(lStrPropertyId, lBD, null);

				} else {
					lPredicate = CmisQueryPredicate.eqTo(lStrPropertyId, lBD);
				}
				break;

			case STRING:
				if (queryFilter.operator == Operator.EQ) {
					lPredicate = CmisQueryPredicate.eqTo(lStrPropertyId, queryFilter.value);
				} else {
					lPredicate = CmisQueryPredicate.like(lStrPropertyId, "%" + queryFilter.value + "%");
				}
				break;

			default:
				throw new IllegalArgumentException("Tipo di dato non supportato: " + lPropDef.getPropertyType());
			}

			lList.add(lPredicate);
		}

		// La ricerca viene limitata alla cartella radice
		lList.add(CmisQueryPredicate.inTree(mAlfrescoRootFolderID));

		return lList;
	}

	private String getOrCreateFolder(Session pSession,String pStrDocumentType){
		String lFileName = pStrDocumentType.substring(pStrDocumentType.lastIndexOf(':')+1);
		String lFolderPath = AlfrescoHelper.getFolderById(pSession,mAlfrescoRootFolderID).getPath() + "/" + lFileName;
		Folder lFolder = AlfrescoHelper.getFolderByPath(pSession, lFolderPath );
		if(lFolder == null){
			return AlfrescoHelper.createFolder(pSession, mAlfrescoRootFolderID, lFileName);
		}
		return lFolder.getId();
	}
	
	private List<org.apache.chemistry.opencmis.client.api.Document> searchDocuments() throws ParseException {
		// Copia mappa parametri poiché l'originale è immutabile
		Map<String, String[]> lMapParams = new HashMap<String, String[]>(httpRequest.getParameterMap());

		Session lSession = Util.getUserAlfrescoSession(httpRequest);

		String[] lStrTypeFilter = lMapParams.remove("type");
		String lStrTypeId =
				lStrTypeFilter != null && StringUtils.isNotBlank(lStrTypeFilter[0])
		                ? lStrTypeFilter[0]
		                : mAlfrescoBaseTypeId;

		// TODO (Alessio): gestione CmisNotFound
		TypeDefinition lTypeDef = lSession.getTypeDefinition(lStrTypeId);
		List<CmisQueryPredicate<?>> lListPredicates = getQueryPredicates(lTypeDef, lMapParams);

		CmisQueryBuilder lQB = new CmisQueryBuilder(lSession);
		String lStrQuery = lQB.selectFrom(lStrTypeId, (String[]) null).where(lListPredicates).build();

		List<org.apache.chemistry.opencmis.client.api.Document> lList =
		        AlfrescoHelper.searchDocuments(lSession, lStrQuery);		
		return lList;
	}
	
	@GET
	public Response getDocuments() throws ParseException {
		return Response.ok(searchDocuments()).build();
	}

	@GET
	@Path("/{id}")
	public Response getDocumentById(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		org.apache.chemistry.opencmis.client.api.Document lDocument =
		        AlfrescoHelper.getDocumentById(lSession, pStrId, true);

		return Response.ok(lDocument).build();
	}

	@GET
	@Path("/{id}/content")
	public Response getDocumentFile(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		org.apache.chemistry.opencmis.client.api.Document lDocument = AlfrescoHelper.getDocumentById(lSession, pStrId);
		ContentStream lContentStream =  lDocument.getContentStream();
		if(lContentStream == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		String lMimeType = lContentStream.getMimeType();
		return Response.ok(lContentStream.getStream()).type(lMimeType).build();
	}

	@GET
	@Path("/{id}/preview")
	public Response getDocumentPreview(@PathParam("id") String pStrId,  @Context ServletContext pServletContext) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);

		InputStream lInputStream = null;
		StreamingOutput lStreamingOutput = null;
		String lMimeType = null;
		org.apache.chemistry.opencmis.client.api.Document lDocument = AlfrescoHelper.getDocumentById(lSession, pStrId);
		if (lDocument != null) {
			String lStrDocMimeType = lDocument.getContentStreamMimeType();
			if (lStrDocMimeType == null) {
				lInputStream = null;

			} else if (lStrDocMimeType.equals("application/pdf")) {
				// Il documento è già un PDF: lo si serve direttamente
				// FIXME (Alessio): si assume che il documento abbia un contenuto: potenziale NPE
				lInputStream = lDocument.getContentStream().getStream();
				lMimeType = "application/pdf";

			} else if (lStrDocMimeType.startsWith("image")) {
				// Il documento è un'immagine: si serve l'anteprima immagine
				lInputStream =
				        AlfrescoHelper.getThumbnail(Util.getUserAlfrescoConfig(httpRequest), pStrId,
				                ThumbnailDefinitions.IMAGE, true);
				// Le preview immagini di Alfresco sono JPG. Probabilmente andrebbe definito altrove
				// (tipo ThumbnailsDefinitions).
				lMimeType = "image/jpg";

			} else {
				// Il documento è altro: si serve l'anteprima PDF
				lInputStream =
				        AlfrescoHelper.getThumbnail(Util.getUserAlfrescoConfig(httpRequest), pStrId,
				                ThumbnailDefinitions.PDF, true);
				lMimeType = "application/pdf";
			}

			if (lInputStream != null) {
				lStreamingOutput = new StreamStreamingOutput(lInputStream);
			}
		}

		if (lStreamingOutput == null) {
			
			lMimeType = "image/jpg";
			String lBasePath = pServletContext.getRealPath("/document/");
			try {
				lStreamingOutput = new StreamStreamingOutput(new BufferedInputStream(new FileInputStream(lBasePath + "/preview_unavailable.jpg")));
			} catch (FileNotFoundException e) {
				throw new JBrickException(e, JBrickException.FATAL);
			}	
		}
		return Response.ok(lStreamingOutput).type(lMimeType).build();
	}

	@GET
	@Path("/{id}/versions")
	public Response getDocumentVersions(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		List<org.apache.chemistry.opencmis.client.api.Document> lList =
		        AlfrescoHelper.getDocumentVersions(lSession, pStrId);

		return Response.ok(lList).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insert(DocumentBean pDocumentBean, @Context ServletContext pServletContext) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);

		// populate property map
		Map<String, Object> lMapProperties = new HashMap<String, Object>();

		// common properties
		lMapProperties.put(PropertyIds.NAME, pDocumentBean.getName());
		lMapProperties.put(PropertyIds.DESCRIPTION, pDocumentBean.getDescription());

		// type properties
		Map<String, DocumentPropertyBean> lDocumentProperties = pDocumentBean.getProperties();
		for (Map.Entry<String, DocumentPropertyBean> lProperty : lDocumentProperties.entrySet()) {
			Object lObject = lProperty.getValue().getValue();
			lMapProperties.put(lProperty.getKey(), lObject);
		}

		// get uploaded file and create document
		InputStream lInputStream = null;
		org.apache.chemistry.opencmis.client.api.Document lDocument = null;
		File lFile = null;
		String lFileContentType = null;
		String lFileName = pDocumentBean.getUploadedFileName();

		try {
			// XXX (Alessio): inspiegabilmente il servizio SPI di Tika non viene registrato,
			// rendendo impossibile l'utilizzo di java.nio.file.Files.probeContentType()
			if(lFileName != null){
				String lBasePath = pServletContext.getRealPath("/");
				lFile = new File(lBasePath, lFileName);
				lFileContentType = TIKA.detect(lFile);
				lInputStream = new FileInputStream(lFile);
				
				lDocument =
				        AlfrescoHelper.createDocument(lSession, getOrCreateFolder(lSession, pDocumentBean.getType()),
				                pDocumentBean.getName(), lFile.length(),
				                lFileContentType, lInputStream, lMapProperties, null, pDocumentBean.getType());
			} else {
				lDocument =
				        AlfrescoHelper.createDocument(lSession, getOrCreateFolder(lSession, pDocumentBean.getType()),
				                pDocumentBean.getName(), -1,
				                null, null, lMapProperties, null, pDocumentBean.getType());
			}
			

		} catch (Exception e) {
			// TODO: log ...
			throw new JBrickException(e, JBrickException.FATAL);
		}

		return Response.ok(lDocument).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateMetaData(@PathParam("id") String pStrId, DocumentBean pDocumentBean) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		// Map contenente le proprietà aggiornate
		Map<String, Object> lMapProperties = new HashMap<String, Object>();

		// common properties
		lMapProperties.put(PropertyIds.NAME, pDocumentBean.getName());
		lMapProperties.put(PropertyIds.DESCRIPTION, pDocumentBean.getDescription());

		// type properties
		Map<String, DocumentPropertyBean> lDocumentProperties = pDocumentBean.getProperties();
		for (Map.Entry<String, DocumentPropertyBean> lProperty : lDocumentProperties.entrySet()) {
			Object lObject = lProperty.getValue().getValue();
			lMapProperties.put(lProperty.getKey(), lObject);
		}

		org.apache.chemistry.opencmis.client.api.Document lDocument = null;
		try {
			lDocument = AlfrescoHelper.updateDocumentProperties(lSession, pStrId, lMapProperties);
		} catch (Exception e) {
			// TODO: log ...
			throw new JBrickException(e, JBrickException.FATAL);
		}

		return Response.ok(lDocument).build();
	}

	@PUT
	@Path("/{id}/content")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateContent(@PathParam("id") String pStrId, DocumentBean pDocumentBean,
	        @Context ServletContext pServletContext) {

		Session lSession = Util.getUserAlfrescoSession(httpRequest);

		// get uploaded file and create document
		InputStream lInputStream = null;
		File lFile = null;
		String lFileName = pDocumentBean.getUploadedFileName();
		

		org.apache.chemistry.opencmis.client.api.Document lDocument = null;
		try {
			
			if(lFileName != null){
				String lBasePath = pServletContext.getRealPath("/");
				lFile = new File(lBasePath, lFileName);
				String lFileContentType = TIKA.detect(lFile);
				lInputStream = new FileInputStream(lFile);
			
				//Ottengo il documento attuale in modo da eliminare le relazioni da esso prima della creazione della nuova versione
				lDocument = AlfrescoHelper.getDocumentById(lSession, pStrId, true);
				List<Relationship> lListRelationship = lDocument.getRelationships();
				if(lListRelationship != null){
					for (Relationship lrelationship : lListRelationship) {
						if(lrelationship != null)
							AlfrescoHelper.deleteObject(lSession, lrelationship.getId());
					}
				}
				
				// In pDocumentBean.version mi aspetto di trovare il valore che specifica che tipo di
				// nuova versione sarà. in pDocumentBean.name il commento
				lDocument =
				        AlfrescoHelper.updateDocumentContent(lSession, pStrId, lFileName, lFile.length(), lFileContentType,
				                lInputStream, AlfrescoHelper.VersioningMode.valueOf(pDocumentBean.getVersion()),
				                pDocumentBean.getName());
				
				if(lListRelationship != null){
					String lNewDocumentID = lDocument.getId();
					//Inserisco nel nuovo documento le relazioni eliminate per la creazione della versione
					for (Relationship lrelationship : lListRelationship) {
						//Sostituisco il vecchio id con quello della nuova versione
						String lSourceId = (pStrId.equals(lrelationship.getSourceId().getId()) ? lNewDocumentID : lrelationship.getSourceId().getId());
						String lTargetId = (pStrId.equals(lrelationship.getTargetId().getId()) ? lNewDocumentID : lrelationship.getTargetId().getId());
						AlfrescoHelper.createRelation(lSession, lrelationship.getType().getId(), lSourceId, lTargetId);
					}
				}
			} else {
				lDocument =
				        AlfrescoHelper.updateDocumentContent(lSession, pStrId, null, -1, null,
				                null, null, null);
			}
			
		} catch (Exception e) {
			// TODO: log...
			throw new JBrickException(e, JBrickException.FATAL);
		}

		return Response.ok(lDocument).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteById(@PathParam("id") String pStrId){
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		AlfrescoHelper.deleteDocument(lSession, pStrId, true );
		return Response.ok().build();
	}
	
    @GET
    @Path("/print/xls/")
    public Response printXls (@Context
    HttpServletRequest pRequest, @Context
    ServletContext pServletContext) throws IOException {
        
        StreamingOutput lStreamingOutput = print(pRequest, pServletContext,
                PrintFormat.XLS.toString());

        return Response.ok(lStreamingOutput)
                       .header("Content-Disposition",
            "attachment; filename=Document.xls").build();
    }
    
    private StreamingOutput print(HttpServletRequest pRequest, ServletContext pServletContext, String pFormat) {
        final String lDataFormat =  (String) pRequest.getSession().getAttribute("localePatternTimestamp"); 

        //Prendo i nomi dei campi da mostrare nella tabella di output
        Map<String, String[]> lMapParams = new HashMap<String, String[]>(pRequest.getParameterMap());
        String lDocumentType = lMapParams.get("type")[0];
        String[] lPropertyNameParameter = (String[]) lMapParams.get("propertyNames");
        String[] lPropertyNames = {};
        if(lPropertyNameParameter != null){
        	lPropertyNames = lPropertyNameParameter[0].split(",");
        }
        
        
        // Nome base dei file di report NON deve essere presente l'estensione .jrxml o .jasper
        final String lStrReportFile = pServletContext.getRealPath(
                "/report/Document");

        // Hashmap che verrÃ  passata al generatore del report. Qui si possono aggiungere parametrizzazioni a piacere
        final HashMap<String, Object> lParametersMap = new HashMap<String, Object>();

        //  Basepath di esecuzione che torna spesso utile
        final String lStrBasePath = pServletContext.getRealPath("report/") +
            System.getProperty("file.separator");
        lParametersMap.put(PrintUtil.C_BASEPATH, lStrBasePath);

        // locale
        final Locale lLocale = LocaleUtil.getLocale(pRequest.getSession());

        List<org.apache.chemistry.opencmis.client.api.Document> lDocumentList = null;;
		try {
			lDocumentList = searchDocuments();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        		
        final org.apache.chemistry.opencmis.client.api.Document[] lDocumentArray = (org.apache.chemistry.opencmis.client.api.Document[]) lDocumentList.toArray(new org.apache.chemistry.opencmis.client.api.Document[lDocumentList.size()]);

        StreamingOutput lStreamingOutput = null;

        if ((pFormat.equalsIgnoreCase(PrintFormat.PDF.toString()))) {
            // Si vuole esportare un file PDF
            lStreamingOutput = new StreamingOutput() {
                        @Override
                        public void write(OutputStream pOutputStream)
                            throws IOException {
                            PrintUtil.printBeanArray(lStrReportFile,
                            		lDocumentArray, lParametersMap, lLocale,
                                pOutputStream, PrintFormat.PDF);
                        }
                    };
        } else if ((pFormat.equalsIgnoreCase(PrintFormat.XLS.toString()))) {
            // Si vuole esportare un file XLS
        	
            // Definisco l'array d'intestazione colonne e l'array per il fill dei dati
            final ArrayList<String> lArrayStrHeader = new ArrayList<String>();
            final ArrayList<Map> lArrayList = new ArrayList<Map>();
            
            //	Faccio il fill dei dati in entrambi gli array
            ResourceBundle lResourceBundle = null;
            if(lLocale == null){
            	Locale lResBundleLocale = pRequest.getLocale();
            	if(lResBundleLocale != null){
            		lResourceBundle = ResourceBundle.getBundle("ApplicationResources",lResBundleLocale);
            	} else {
            		lResourceBundle = ResourceBundle.getBundle("ApplicationResources");
            	}
            } else {
            	lResourceBundle = ResourceBundle.getBundle("ApplicationResources",lLocale);
            }
       
            

            //Inserimento degli header presenti in tutti i tipi di documento
            lArrayStrHeader.add("Id");
            lArrayStrHeader.add(lResourceBundle.getString("jsp.document.description.label"));
            lArrayStrHeader.add(lResourceBundle.getString("jsp.document.type.label"));
            lArrayStrHeader.add(lResourceBundle.getString("jsp.document.version.label"));
            lArrayStrHeader.add(lResourceBundle.getString("jsp.document.created.label"));
            lArrayStrHeader.add(lResourceBundle.getString("jsp.document.createdBy.label"));
            
            //Scorre i documenti per inserrire i valori nella tabella
            for (int i = 0; i < lDocumentArray.length; i++) {
                LinkedHashMap<Integer, Object> lMap = new LinkedHashMap<Integer, Object>();
                //Inserimento dei valori presenti in tutti i tipi di documento
                lMap.put(1, lDocumentArray[i].getId());
                lMap.put(2, lDocumentArray[i].getDescription());
                lMap.put(3, lDocumentArray[i].getType().getId());
                lMap.put(4, lDocumentArray[i].getVersionLabel());
                GregorianCalendar lCreationDate = lDocumentArray[i].getCreationDate();
                lMap.put(5, lCreationDate==null ? null : lCreationDate.getTime());
                lMap.put(6, lDocumentArray[i].getCreatedBy());
                //Proprietà dinamiche
                if(lDocumentType != ""){
                    int lindex = 7;
                    //prende la lista delle proprietà dinamiche
                   List<Property<?>> lDocumentProperties = lDocumentArray[i].getProperties(); 
                   //Scorre le proprietà da stampare
                   for (String lString : lPropertyNames) {
                	   //Cerca il loro valore dalla lista delle proprietà
                	   for (Property lProperty : lDocumentProperties) {
                		   //Aggiunge i label
                		   if(lString.equals(lProperty.getQueryName())){
                			   if(i==0)
                				   lArrayStrHeader.add(lProperty.getDisplayName());
                			   //Converte il valore da inserire in base al tipo
	            			   switch (lProperty.getType()) {
									case DECIMAL:
										BigDecimal lBigDecimal = (BigDecimal) lProperty.getValue();
										lMap.put(lindex, lBigDecimal == null ? null : lBigDecimal.doubleValue());
										break;
									case DATETIME:
										GregorianCalendar lGregorianCalendar = (GregorianCalendar) lProperty.getValue();
										lMap.put(lindex,lGregorianCalendar==null ? null : lGregorianCalendar.getTime());
										break;
									case INTEGER:
										lMap.put(lindex,(Integer)lProperty.getValue());
										break;
									case BOOLEAN:
										lMap.put(lindex,(Boolean)lProperty.getValue());
										break;
									case STRING:
										lMap.put(lindex,(String)lProperty.getValue());
										break;
									default:
										throw new IllegalArgumentException("Tipo di dato non supportato:" + lProperty.getType());
								}  
	            		   }
                	   }  
                	   lindex++;
                   }
				}
                lArrayList.add(lMap);                
            }
                
              
            lStreamingOutput = new StreamingOutput() {
                    @Override
                    public void write(OutputStream pOutputStream)
                        throws IOException {
                        PrintUtil.printXLSBeanArray(lStrReportFile,
                            lArrayStrHeader, lArrayList, lLocale,
                            pOutputStream,lDataFormat);
                    }
                };
                        
        }
        return lStreamingOutput;
    }

	private static class QueryFilter {

		private String propertyId;
		private Operator operator;
		private String value;
	}

	// XXX (Alessio): classe a sè?
	private static class StreamStreamingOutput implements StreamingOutput {

		private InputStream contentStream;

		public StreamStreamingOutput(InputStream lInputStream) {
			this.contentStream = lInputStream;
		}

		@Override
		public void write(OutputStream output) throws IOException {
			try (InputStream is = contentStream; OutputStream os = output) {
				int bytes;
				while ((bytes = is.read()) != -1) {
					os.write(bytes);
				}
			}
		}
	}

}
