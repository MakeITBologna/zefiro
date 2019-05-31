package it.makeit.zefiro.ws.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Item;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.commons.lang.StringUtils;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.CmisQueryBuilder;
import it.makeit.alfresco.CmisQueryPredicate;
import it.makeit.alfresco.CmisQueryPredicate.Operator;
import it.makeit.alfresco.addon.DocumentListenener;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.Log;
import it.makeit.jbrick.print.PrintFormat;
import it.makeit.jbrick.print.PrintUtil;
import it.makeit.jbrick.web.LocaleUtil;
import it.makeit.zefiro.Util;

@Path("/Search")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SearchEndpoint {
	private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	
	@Context
	private HttpServletRequest httpRequest;
	
	
	@Inject DocumentListenener documentListenener;
	
	private static final String CONTAINS_FIELD = "contains";
	private static final String EQ = "eq";
	private static final String FROM = "ge";
	private static final String TO = "le";
	private static final String LIKE = "lk";

	private static final String mAlfrescoBaseTypeId = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@baseTypeId");
	private static final String mAlfrescoBaseTypeItemId = JBrickConfigManager.getInstance().getProperty("alfresco/@baseTypeItemId");
	private static Log mLog = Log.getInstance(SearchEndpoint.class);
	
	private static final Integer ALFRESCORESULTSLIMIT = 100;
	
	@GET
	@Path("/")
	public Response getDocuments() throws ParseException {
		Session session = Util.getUserAlfrescoSession(httpRequest);
		String rootFolderId = (String) httpRequest.getSession().getAttribute("rootFolderId");

		Map<String, String[]> mapParams = new HashMap<String, String[]>(httpRequest.getParameterMap());
		
		if(mAlfrescoBaseTypeItemId != null) {
			String typeId = mapParams.get("type")==null? mAlfrescoBaseTypeId:mapParams.get("type")[0];
			String parentId = session.getTypeDefinition(typeId).getParentTypeId();
			
            while (parentId != null) {
                if (parentId.equals(mAlfrescoBaseTypeItemId)){
                        return Response.ok(searchItems(typeId, rootFolderId, session)).build();
                };
                parentId = session.getTypeDefinition(parentId).getParentTypeId();
            };
		};
		return Response.ok(searchDocuments(mapParams, rootFolderId, session)).build();
	}
	

	@GET
	@Path("/print/xls/")
	public Response printXls(@Context HttpServletRequest pRequest, @Context ServletContext pServletContext)
			throws IOException {
		String rootFolderId = (String) httpRequest.getSession().getAttribute("rootFolderId");
		StreamingOutput lStreamingOutput = print(pRequest, pServletContext, PrintFormat.XLS.toString(), rootFolderId);

		return Response.ok(lStreamingOutput).header("Content-Disposition", "attachment; filename=Document.xls").build();
	}

	
	private static class QueryFilter {

		private String propertyId;
		private Operator operator;
		private String value;
	}
	

	private List<QueryFilter> getQueryFiltersMap(Map<String, String[]> pMapParams) {
		List<QueryFilter> lListFilters = new LinkedList<QueryFilter>();
		for (String prop : pMapParams.keySet()) {

			// Decodifica FROM/TO
			String[] lDecodedProp = prop.split("\\|");
			String lStrPropertyId = lDecodedProp[0];
			String lStrOperator = lDecodedProp.length > 1 ? lDecodedProp[lDecodedProp.length - 1].toLowerCase()
					: "default";

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
				// Niente: verrà usaro l'operatore predefinito per il tipo di
				// dato
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

	private List<CmisQueryPredicate<?>> getQueryPredicates(TypeDefinition pTypeDef, String rootFolderId, Map<String, String[]> pMapParams)
			throws ParseException {

		List<CmisQueryPredicate<?>> lList = new LinkedList<CmisQueryPredicate<?>>();

		
		Map<String, PropertyDefinition<?>> lMapProperties = pTypeDef.getPropertyDefinitions();
		
		Map<String, String> propertyQueryNames = new HashMap<>(); // possono essere differenti: as4:filespool_x002d_as4 -> as4:filespool-x002d-as4
		for(Entry<String, PropertyDefinition<?>> e: lMapProperties.entrySet()) {
			propertyQueryNames.put(e.getValue().getQueryName(), e.getKey());
		}
		
		List<QueryFilter> lListFilters = getQueryFiltersMap(pMapParams);
		for (QueryFilter queryFilter : lListFilters) {
			String propertyQueryName = queryFilter.propertyId;
			String propertyId = propertyQueryNames.get(propertyQueryName);
			
			
			CmisQueryPredicate<?> lPredicate = null;

			if (propertyQueryName.equals(CONTAINS_FIELD)) {
				lPredicate = CmisQueryPredicate.contains(queryFilter.value);
				lList.add(lPredicate);
				continue;
			}
			
			PropertyDefinition<?> lPropDef = lMapProperties.get(propertyId);
			if (lPropDef == null) {
				// La proprietà richiesta come filtro non è definita per il tipo
				// documento
				continue;
			}

			
			switch (lPropDef.getPropertyType()) {
			case BOOLEAN:
				lPredicate = CmisQueryPredicate.eqTo(propertyId, Boolean.valueOf(queryFilter.value));
				break;

			case DATETIME:
				// XXX (Alessio): usare un'istanza statica di SimpleDateFormat?
				SimpleDateFormat lFormatter = new SimpleDateFormat(DATE_PATTERN);
				Date lDate = lFormatter.parse(queryFilter.value);
				
				if (queryFilter.operator == Operator.LE) {
					lPredicate = CmisQueryPredicate.between(propertyId, null, this.getEndOfDay(lDate));

				} else if (queryFilter.operator == Operator.GE) {
					lPredicate = CmisQueryPredicate.between(propertyId, this.getStartOfDay(lDate), null);

				} else {
					lPredicate = CmisQueryPredicate.eqTo(propertyId, lDate);
				}
				break;

			case DECIMAL:
			case INTEGER:
				BigDecimal lBD = new BigDecimal(queryFilter.value);
				if (queryFilter.operator == Operator.LE) {
					lPredicate = CmisQueryPredicate.between(propertyId, null, lBD);

				} else if (queryFilter.operator.equals(Operator.GE)) {
					lPredicate = CmisQueryPredicate.between(propertyId, lBD, null);

				} else {
					lPredicate = CmisQueryPredicate.eqTo(propertyId, lBD);
				}
				break;

			case STRING:
				if (queryFilter.operator == Operator.EQ) {
					lPredicate = CmisQueryPredicate.eqTo(propertyId, queryFilter.value);
				} else {
					lPredicate = CmisQueryPredicate.like(propertyId, "%" + queryFilter.value + "%");
				}
				break;

			default:
				throw new IllegalArgumentException("Tipo di dato non supportato: " + lPropDef.getPropertyType());
			}

			lList.add(lPredicate);
		}

		// La ricerca viene limitata alla cartella radice
		lList.add(CmisQueryPredicate.inTree(rootFolderId));

		return lList;
	}
	
	
	private List<org.apache.chemistry.opencmis.client.api.Document> searchDocuments(Map<String, String[]> mapParams, String rootFolderId,Session lSession) throws ParseException {
		// Copia mappa parametri poiché l'originale è immutabile
		String[] lStrTypeFilter = mapParams.remove("type");
		String lStrTypeId = lStrTypeFilter != null && StringUtils.isNotBlank(lStrTypeFilter[0]) ? lStrTypeFilter[0]
				: mAlfrescoBaseTypeId;

		// TODO (Alessio): gestione CmisNotFound
		TypeDefinition lTypeDef = lSession.getTypeDefinition(lStrTypeId);
		List<CmisQueryPredicate<?>> lListPredicates = getQueryPredicates(lTypeDef, rootFolderId, mapParams);

		CmisQueryBuilder lQB = new CmisQueryBuilder(lSession);
		String lStrQuery = lQB.selectFrom(lStrTypeId, (String[]) null).where(lListPredicates).build();
		System.out.println("QUERY"+lStrQuery);
		List<org.apache.chemistry.opencmis.client.api.Document> lList = AlfrescoHelper.searchDocuments(lSession, lStrQuery);

		return lList;
	}
	
	private List<Item> searchItems(String typeId, String rootFolderId, Session session){
		List<Item> itemsList = new ArrayList<Item>();

		CmisQueryBuilder queryBuilder = new CmisQueryBuilder(session);
		String strTypeId = typeId != null ? typeId : mAlfrescoBaseTypeItemId;
		String query = queryBuilder.selectFrom(strTypeId, (String[]) null)
									.where(CmisQueryPredicate.inTree(rootFolderId))
									.build();

		ItemIterable<QueryResult> results = session.query(query, false);
		Iterator<QueryResult> iterator = results.iterator();
		Integer count = 0;

		while (iterator.hasNext()) {
			QueryResult qResult = iterator.next();
			PropertyData<?> propData = qResult.getPropertyById("cmis:objectId");
			
			if (propData != null) {
				String objectId = (String) propData.getFirstValue();
				CmisObject cmisObj = session.getObject(session.createObjectId(objectId)); 
				itemsList.add((Item) cmisObj);
			};
			
			count++;
			if (count == ALFRESCORESULTSLIMIT) {
				break;
			}
		};

		return itemsList;
	}


	
	private StreamingOutput print(HttpServletRequest pRequest, ServletContext pServletContext, String pFormat, String rootFolderId) {
		final String lDataFormat = (String) pRequest.getSession().getAttribute("localePatternTimestamp") != null? 
				(String) pRequest.getSession().getAttribute("localePatternTimestamp") : "dd/MM/yyyy HH:mm:ss" ;
		// Prendo i nomi dei campi da mostrare nella tabella di output
		Map<String, String[]> lMapParams = new HashMap<String, String[]>(pRequest.getParameterMap());
		String lDocumentType = lMapParams.get("type")[0];
		String[] lPropertyNameParameter = lMapParams.get("propertyNames");
		String[] lPropertyNames = {};
		if (lPropertyNameParameter != null) {
			lPropertyNames = lPropertyNameParameter[0].split(",");
		}

		// Nome base dei file di report NON deve essere presente l'estensione
		// .jrxml o .jasper
		final String lStrReportFile = pServletContext.getRealPath("/report/Document");

		// Hashmap che verrÃ  passata al generatore del report. Qui si possono
		// aggiungere parametrizzazioni a piacere
		final HashMap<String, Object> lParametersMap = new HashMap<String, Object>();

		// Basepath di esecuzione che torna spesso utile
		final String lStrBasePath = pServletContext.getRealPath("report/") + System.getProperty("file.separator");
		lParametersMap.put(PrintUtil.C_BASEPATH, lStrBasePath);

		// locale
		final Locale lLocale = LocaleUtil.getLocale(pRequest.getSession());

		List<org.apache.chemistry.opencmis.client.api.Document> lDocumentList = null;
		
		try {
			Session session = Util.getUserAlfrescoSession(httpRequest);
			lDocumentList = searchDocuments(lMapParams, rootFolderId, session);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final org.apache.chemistry.opencmis.client.api.Document[] lDocumentArray = lDocumentList
				.toArray(new org.apache.chemistry.opencmis.client.api.Document[lDocumentList.size()]);

		StreamingOutput lStreamingOutput = null;

		if ((pFormat.equalsIgnoreCase(PrintFormat.PDF.toString()))) {
			// Si vuole esportare un file PDF
			lStreamingOutput = new StreamingOutput() {
				@Override
				public void write(OutputStream pOutputStream) throws IOException {
					PrintUtil.printBeanArray(lStrReportFile, lDocumentArray, lParametersMap, lLocale, pOutputStream,
							PrintFormat.PDF);
				}
			};
		} else if ((pFormat.equalsIgnoreCase(PrintFormat.XLS.toString()))) {
			// Si vuole esportare un file XLS

			// Definisco l'array d'intestazione colonne e l'array per il fill
			// dei dati
			final ArrayList<String> lArrayStrHeader = new ArrayList<String>();
			final ArrayList<Map> lArrayList = new ArrayList<Map>();

			// Faccio il fill dei dati in entrambi gli array
			ResourceBundle lResourceBundle = null;
			if (lLocale == null) {
				Locale lResBundleLocale = pRequest.getLocale();
				if (lResBundleLocale != null) {
					lResourceBundle = ResourceBundle.getBundle("ApplicationResources", lResBundleLocale);
				} else {
					lResourceBundle = ResourceBundle.getBundle("ApplicationResources");
				}
			} else {
				lResourceBundle = ResourceBundle.getBundle("ApplicationResources", lLocale);
			}

			// Inserimento degli header presenti in tutti i tipi di documento
			lArrayStrHeader.add("Id");
			lArrayStrHeader.add(lResourceBundle.getString("jsp.document.description.label"));
			lArrayStrHeader.add(lResourceBundle.getString("jsp.document.type.label"));
			lArrayStrHeader.add(lResourceBundle.getString("jsp.document.version.label"));
			lArrayStrHeader.add(lResourceBundle.getString("jsp.document.created.label"));
			lArrayStrHeader.add(lResourceBundle.getString("jsp.document.createdBy.label"));

			// Scorre i documenti per inserrire i valori nella tabella
			for (int i = 0; i < lDocumentArray.length; i++) {
				LinkedHashMap<Integer, Object> lMap = new LinkedHashMap<Integer, Object>();
				// Inserimento dei valori presenti in tutti i tipi di documento
				lMap.put(1, lDocumentArray[i].getId());
				lMap.put(2, lDocumentArray[i].getDescription());
				lMap.put(3, lDocumentArray[i].getType().getId());
				lMap.put(4, lDocumentArray[i].getVersionLabel());
				GregorianCalendar lCreationDate = lDocumentArray[i].getCreationDate();
				lMap.put(5, lCreationDate == null ? null : lCreationDate.getTime());
				lMap.put(6, lDocumentArray[i].getCreatedBy());
				// Proprietà dinamiche
				if (lDocumentType != "") {
					int lindex = 7;
					// prende la lista delle proprietà dinamiche
					List<Property<?>> lDocumentProperties = lDocumentArray[i].getProperties();
					// Scorre le proprietà da stampare
					for (String lString : lPropertyNames) {
						// Cerca il loro valore dalla lista delle proprietà
						for (Property lProperty : lDocumentProperties) {
							// Aggiunge i label
							if (lString.equals(lProperty.getQueryName())) {
								if (i == 0) {
									lArrayStrHeader.add(lProperty.getDisplayName());
								}
								// Converte il valore da inserire in base al
								// tipo
								switch (lProperty.getType()) {
								case DECIMAL:
									BigDecimal lBigDecimal = (BigDecimal) lProperty.getValue();
									lMap.put(lindex, lBigDecimal == null ? null : lBigDecimal.doubleValue());
									break;
								case DATETIME:
									GregorianCalendar lGregorianCalendar = (GregorianCalendar) lProperty.getValue();
									lMap.put(lindex, lGregorianCalendar == null ? null : lGregorianCalendar.getTime());
									break;
								case INTEGER:
									lMap.put(lindex, lProperty.getValue());
									break;
								case BOOLEAN:
									lMap.put(lindex, lProperty.getValue());
									break;
								case STRING:
									lMap.put(lindex, lProperty.getValue());
									break;
								default:
									throw new IllegalArgumentException(
											"Tipo di dato non supportato:" + lProperty.getType());
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
				public void write(OutputStream pOutputStream) throws IOException {
					PrintUtil.printXLSBeanArray(lStrReportFile, lArrayStrHeader, lArrayList, lLocale, pOutputStream,
							lDataFormat);
				}
			};

		}
		return lStreamingOutput;
	}
	
	private Date getEndOfDay(Date date) {
	   Calendar calendar = Calendar.getInstance();
	   calendar.setTime(date);
	  
	   int year = calendar.get(Calendar.YEAR);
	   int month = calendar.get(Calendar.MONTH);
	   int day = calendar.get(Calendar.DATE);
	   
	   calendar.set(year, month, day, 23, 59, 59);
	   return calendar.getTime();
	  	   
	}
	
	private Date getStartOfDay(Date date) {
		   Calendar calendar = Calendar.getInstance();
		   calendar.setTime(date);
		   
		   int year = calendar.get(Calendar.YEAR);
		   int month = calendar.get(Calendar.MONTH);
		   int day = calendar.get(Calendar.DATE);
		   
		   calendar.set(year, month, day, 0, 0, 0);
		   return calendar.getTime();
		}

}
