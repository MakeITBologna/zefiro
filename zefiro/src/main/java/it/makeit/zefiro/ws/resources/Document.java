package it.makeit.zefiro.ws.resources;

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
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
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
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;

import com.google.api.client.http.HttpStatusCodes;

import it.makeit.alfresco.AlfrescoException;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.CmisQueryBuilder;
import it.makeit.alfresco.CmisQueryPredicate;
import it.makeit.alfresco.CmisQueryPredicate.Operator;
import it.makeit.alfresco.RenditionKinds;
import it.makeit.alfresco.addon.DocumentListenener;
import it.makeit.alfresco.webscriptsapi.services.ThumbnailDefinitions;
import it.makeit.alfresco.workflow.AlfrescoRendition;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;
import it.makeit.jbrick.print.PrintFormat;
import it.makeit.jbrick.print.PrintUtil;
import it.makeit.jbrick.web.LocaleUtil;
import it.makeit.zefiro.MimeType;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.DocumentBean;
import it.makeit.zefiro.dao.DocumentPropertyBean;

@Path("/Document")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Document {

	private static final Tika TIKA = new Tika();



	private static final String mAlfrescoRootFolderID = JBrickConfigManager.getInstance()
			.getMandatoryProperty("alfresco/@rootFolderId");
	private static Log mLog = Log.getInstance(Document.class);

	@Context
	private HttpServletRequest httpRequest;
	
	
	@Inject DocumentListenener documentListenener;
	

	

	private String getOrCreateFolder(Session pSession, String pStrDocumentType) {
		String lFileName = pStrDocumentType.substring(pStrDocumentType.lastIndexOf(':') + 1);
		String lFolderPath = AlfrescoHelper.getFolderById(pSession, mAlfrescoRootFolderID).getPath() + "/" + lFileName;
		Folder lFolder = AlfrescoHelper.getFolderByPath(pSession, lFolderPath);
		if (lFolder == null) {
			return AlfrescoHelper.createFolder(pSession, mAlfrescoRootFolderID, lFileName);
		}
		return lFolder.getId();
	}


	@GET
	@Path("/{id}")
	public Response getDocumentById(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		org.apache.chemistry.opencmis.client.api.Document lDocument = AlfrescoHelper.getDocumentById(lSession, pStrId,
				true);

		documentListenener.onDocumentDownload(lDocument);
		return Response.ok(lDocument).build();
	}

	@GET
	@Path("/{id}/content")
	public Response getDocumentFile(@PathParam("id") PathSegment pSegment, @Context ServletContext pServletContext) {
		String pStrId = pSegment.getPath();
		for (String key : pSegment.getMatrixParameters().keySet()) {
			pStrId = String.format("%s;%s", pStrId, key);
		}
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		org.apache.chemistry.opencmis.client.api.Document lDocument = AlfrescoHelper.getDocumentById(lSession, pStrId);
		ContentStream lContentStream = lDocument.getContentStream();
		String lMimeType = "";
		if (lContentStream == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		lMimeType = lContentStream.getMimeType();
		
		documentListenener.onDocumentDownload(lDocument);
		return Response.ok(lContentStream.getStream()).type(lMimeType).build();
	}

	@GET
	@Path("/{id}/preview")
	public Response getDocumentPreview(@PathParam("id") String pStrId, @Context ServletContext pServletContext)
			throws IOException {
		mLog.debug("begin preview document");
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
				// FIXME (Alessio): si assume che il documento abbia un
				// contenuto: potenziale NPE
				lInputStream = lDocument.getContentStream().getStream();
				lMimeType = "application/pdf";

			} else if (lStrDocMimeType.startsWith("image")) {
				// Il documento è un'immagine: si serve l'anteprima immagine
				lInputStream = AlfrescoHelper.getThumbnail(Util.getUserAlfrescoConfig(httpRequest), pStrId,
						ThumbnailDefinitions.IMAGE, true);
				// Le preview immagini di Alfresco sono JPG. Probabilmente
				// andrebbe definito altrove
				// (tipo ThumbnailsDefinitions).
				lMimeType = "image/jpg";

			} else {

				List<Rendition> renditions = AlfrescoHelper.getDocumentRenditions(lSession, pStrId);

				mLog.debug("retrieving pdf rendition");
				boolean founded = false;
				if (renditions != null)
					for (Rendition r : renditions) {
						if (r.getKind().equalsIgnoreCase(RenditionKinds.PDF)) {
							lInputStream = r.getContentStream().getStream();
							founded = true;
							lMimeType = MimeType.PDF.value();
							break;
						}
					}

				if (!founded) {
					int statusCode = -1;

					statusCode = AlfrescoRendition.createRendition(pStrId, httpRequest);

					if (HttpStatusCodes.isSuccess(statusCode)) {
						int count = 0;
						while (count < 5) {
							mLog.debug("get rendition from alfresco, tentativo: " + (count + 1));
							renditions = AlfrescoHelper.getDocumentRenditions(lSession, pStrId);
							if (renditions != null)
								for (Rendition r : renditions) {
									if (r.getKind().equalsIgnoreCase(RenditionKinds.PDF))
										lInputStream = r.getContentStream().getStream();
								}

							if (lInputStream != null)
								break;
							try {
								TimeUnit.MILLISECONDS.sleep(1000);
							} catch (InterruptedException e) {
								throw new AlfrescoException(AlfrescoException.GENERIC_EXCEPTION);
							}
							count++;
						}

					} else if (statusCode == 409) {
						mLog.debug("pdf rendition already exists.");
						lMimeType = "image/jpg";
						lStreamingOutput = createContentStreamForPreviewUnavailable(pServletContext, lStreamingOutput);

					}
				}
			}

			if (lInputStream != null) {
				lStreamingOutput = new StreamStreamingOutput(lInputStream);
			}
		}

		if (lStreamingOutput == null) {

			lMimeType = "image/jpg";
			lStreamingOutput = createContentStreamForPreviewUnavailable(pServletContext, lStreamingOutput);
		}
		mLog.debug("end of preview document");
		return Response.ok(lStreamingOutput).type(lMimeType).build();
	}

	private StreamingOutput createContentStreamForPreviewUnavailable(ServletContext pServletContext,
			StreamingOutput lStreamingOutput) {
		String lBasePath = pServletContext.getRealPath("/document/");
		File file = new File(lBasePath + "/preview_unavailable.jpg");
		try {
			lStreamingOutput = new StreamStreamingOutput(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new JBrickException(e, JBrickException.FATAL);
		} finally {
			deleteFile(file);
		}
		return lStreamingOutput;
	}

	@GET
	@Path("/{id}/versions")
	public Response getDocumentVersions(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		List<org.apache.chemistry.opencmis.client.api.Document> lList = AlfrescoHelper.getDocumentVersions(lSession,
				pStrId);

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
			// XXX (Alessio): inspiegabilmente il servizio SPI di Tika non viene
			// registrato,
			// rendendo impossibile l'utilizzo di
			// java.nio.file.Files.probeContentType()
			if (lFileName != null) {
				String lBasePath = pServletContext.getRealPath("/");
				lFile = new File(lBasePath, lFileName);
				lFileContentType = TIKA.detect(lFile);
				lInputStream = new FileInputStream(lFile);

				lMapProperties.put(PropertyIds.CONTENT_STREAM_MIME_TYPE, lFileContentType);

				lDocument = AlfrescoHelper.createDocument(lSession,
						getOrCreateFolder(lSession, pDocumentBean.getType()), pDocumentBean.getName(), lFile.length(),
						lFileContentType, lInputStream, lMapProperties, null, pDocumentBean.getType());
			} else {
				lDocument = AlfrescoHelper.createDocument(lSession,
						getOrCreateFolder(lSession, pDocumentBean.getType()), pDocumentBean.getName(), -1, null, null,
						lMapProperties, null, pDocumentBean.getType());
			}
			
			documentListenener.onDocumentUpload(lDocument);

		} catch (Exception e) {
			// TODO: log ...
			throw new JBrickException(e, JBrickException.FATAL);
		} finally {
			deleteFile(lFile);
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

			if (lFileName != null) {
				String lBasePath = pServletContext.getRealPath("/");
				lFile = new File(lBasePath, lFileName);
				String lFileContentType = TIKA.detect(lFile);
				lInputStream = new FileInputStream(lFile);

				// Ottengo il documento attuale in modo da eliminare le
				// relazioni da esso prima della creazione della nuova versione
				lDocument = AlfrescoHelper.getDocumentById(lSession, pStrId, true);
				List<Relationship> lListRelationship = lDocument.getRelationships();
				if (lListRelationship != null) {
					for (Relationship lrelationship : lListRelationship) {
						if (lrelationship != null) {
							AlfrescoHelper.deleteObject(lSession, lrelationship.getId());
						}
					}
				}

				// In pDocumentBean.version mi aspetto di trovare il valore che
				// specifica che tipo di
				// nuova versione sarà. in pDocumentBean.name il commento
				lDocument = AlfrescoHelper.updateDocumentContent(lSession, pStrId, lFileName, lFile.length(),
						lFileContentType, lInputStream,
						AlfrescoHelper.VersioningMode.valueOf(pDocumentBean.getVersion()), pDocumentBean.getName());

				if (lListRelationship != null) {
					String lNewDocumentID = lDocument.getId();
					// Inserisco nel nuovo documento le relazioni eliminate per
					// la creazione della versione
					for (Relationship lrelationship : lListRelationship) {
						// Sostituisco il vecchio id con quello della nuova
						// versione
						String lSourceId = (pStrId.equals(lrelationship.getSourceId().getId()) ? lNewDocumentID
								: lrelationship.getSourceId().getId());
						String lTargetId = (pStrId.equals(lrelationship.getTargetId().getId()) ? lNewDocumentID
								: lrelationship.getTargetId().getId());
						AlfrescoHelper.createRelation(lSession, lrelationship.getType().getId(), lSourceId, lTargetId);
					}
				}
			} else {
				lDocument = AlfrescoHelper.updateDocumentContent(lSession, pStrId, null, -1, null, null, null, null);
			}

		} catch (Exception e) {
			// TODO: log...
			throw new JBrickException(e, JBrickException.FATAL);
		} finally {
			deleteFile(lFile);
		}

		return Response.ok(lDocument).build();
	}

	private void deleteFile(File lFile) {
		if (lFile != null)
			try {
				FileUtils.forceDelete(lFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteById(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		org.apache.chemistry.opencmis.client.api.Document lDocument = AlfrescoHelper.deleteDocument(lSession, pStrId, true);
		
		documentListenener.onDocumentDelete(lDocument);
		
		return Response.ok().build();
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
