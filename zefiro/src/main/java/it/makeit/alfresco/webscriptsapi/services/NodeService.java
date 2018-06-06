package it.makeit.alfresco.webscriptsapi.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Formatter;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.json.JsonHttpContent;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.webscriptsapi.model.Thumbnail;

// TODO (Alessio): gestione errori (eccezioni o altro)
/**
 * @author Alessio Gaeta <alessio.gaeta@make-it.it>
 *
 */
public class NodeService extends AbstractService {

	// /alfresco/s/api/path/{store_type}/{store_id}/
	// In questo *DEVE* avere '/' finale, in quanto manca il contentId
	private static final String URL_TEMPLATE_BASE = "/alfresco/s/api/node/%1$s/%2$s/";

	private static final String URL_RELATIVE_THUMBNAIL_DEFINITIONS = "/content/thumbnaildefinitions";

	// [GET|POST] <base>/content{property}/thumbnails
	// [GET|PUT|DELETE] <base>/content{property}/thumbnails/{thumbnailname}
	// [GET] <base>/content{property}/thumbnails/{thumbnailname}/{filename}
	// TODO (Alessio): gestire {property}
	private static final String URL_RELATIVE_THUMBNAILS = "/content/thumbnails";

	public NodeService(AlfrescoConfig pConfig, String pStoreType, String pStoreId) {
		super(pConfig);

		Formatter lFormatter = new Formatter();
		mBaseUrl .appendRawPath(lFormatter.format(URL_TEMPLATE_BASE, pStoreType, pStoreId).toString());
		lFormatter.close();
	}

	public NodeService(AlfrescoConfig pConfig) {
		this(pConfig, "workspace", "SpacesStore");
	}

	
	private GenericUrl getContentUrl(String pDocumentId) {
		String lCleanId = pDocumentId.split(";")[0];
		GenericUrl lUrl = new GenericUrl(mBaseUrl.toURL(lCleanId));
		return lUrl;
	}

	/**
	 * Recupera le definizioni di thumbnail valide per il contenuto.
	 * 
	 * @param pContentId
	 *            L'id del contenuto.
	 * 
	 * @return La lista delle definizioni valide.
	 * 
	 * @throws IOException
	 */
	public List<String> getThumbnailDefinitions(String pContentId) throws IOException {
		/*
		 * GET <base>/content{property}/thumbnaildefinitions
		 */

		GenericUrl lUrl = getContentUrl(pContentId);
		lUrl.appendRawPath(URL_RELATIVE_THUMBNAIL_DEFINITIONS);

		HttpRequest lRequest = mHttpRequestFactory.buildGetRequest(lUrl);
		TypeReference<List<String>> lType = new TypeReference<List<String>>() {};
		@SuppressWarnings("unchecked")
		List<String> lResponse = (List<String>) lRequest.execute().parseAs(lType.getType());

		return lResponse;
	}

	/**
	 * Recupera la <i>thumbnail</i> del contenuto.
	 * <p>
	 * Il chiamante dovrebbe invocare {@link InputStream#close} una volta che lo {@link InputStream}
	 * resituito non è più necessario. Esempio d'uso:
	 *
	 * <pre>
	 * InputStream is = nodeService.getThumbnail(lContentId, lThumbDefinition, true);
	 * try {
	 *     // Utilizzo dello stream
	 * } finally {
	 *     is.close();
	 * }
	 * </pre>
	 * 
	 * @param pContentId
	 *            L'id del contenuto.
	 * @param pThumbDefinition
	 *            Il nome della <i>thumbnail</i> desiderata.
	 * @param pForceCreate
	 *            Se {@code true}, viene richiesta la crazione (sincrona) della <i>thumbnail</i> nel
	 *            caso questa non esista.
	 * 
	 * @return Lo {@link InputStream} della <i>thumbnail</i> richiesta o {@code null} se questa non
	 *         esiste.
	 * 
	 * @throws IOException
	 */
	public InputStream getThumbnail(String pContentId, String pThumbDefinition, boolean pForceCreate)
	        throws IOException {
		/*
		 * GET <base>/content{property}/thumbnails/{thumbnailname}?c={queueforcecreate?}&ph={placeholder?}&lastModified={modified?}
		 * [placeholder e lastModified non gestiti; creazione queued non gestita]
		 */
		GenericUrl lUrl = getContentUrl(pContentId);
		lUrl.appendRawPath(URL_RELATIVE_THUMBNAILS);
		lUrl.getPathParts().add(pThumbDefinition);
		if (pForceCreate) {
			lUrl.set("c", Thumbnail.FORCE_CREATE);
		}

		HttpRequest lRequest = mHttpRequestFactory.buildGetRequest(lUrl);

		HttpResponse lResponse;
		try {
			lResponse = lRequest.execute();

		} catch (HttpResponseException e) {
			// TODO (Alessio) logging e gestione più fine degli errori
			return null;
		}

		InputStream lThumbnailStream = lResponse.getContent();
		return lThumbnailStream;
	}

	/**
	 * Recupera la <i>thumbnail</i> del contenuto.
	 * <p>
	 * Il chiamante dovrebbe invocare {@link InputStream#close} una volta che lo {@link InputStream}
	 * resituito non è più necessario.
	 * <p>
	 * Equivale alla chiamata {@code getThumbnail(pContentId, pName, false)}.
	 * 
	 * @param pContentId
	 *            L'id del contenuto.
	 * @param pThumbDefinition
	 *            Il nome della <i>thumbnail</i> desiderata.
	 * 
	 * @return Lo {@link InputStream} della <i>thumbnail</i> richiesta o {@code null} se questa non
	 *         esiste.
	 * 
	 * @see #getThumbnail(String, String, boolean)
	 * 
	 * @throws IOException
	 */
	public InputStream getThumbnail(String pContentId, String pName) throws IOException {
		return getThumbnail(pContentId, pName, false);
	}

	/**
	 * Richiede ad Alfresco la creazione di una <i>thumbnail</i>.
	 * <p>
	 * Si tenga presente che in caso di creazione asincrona la <i>thumbnail</i> potrebbe non essere
	 * subito disponibile anche se il metodo ha restituito informazioni valide.
	 * 
	 * @param pContentId
	 *            L'id del contenuto.
	 * @param pThumbDefinition
	 *            Il nome della <i>thumbnail</i> di cui si richiede la crezione.
	 * @param pAsync
	 *            Se la crazione deve essere sincrona ({@code true} o asincrona ({@false}).
	 * 
	 * @return La <i>thumbnail</i> richiesta o {@code null} se il tipo di <i>thumbnail</i> di cui si
	 *         è richiesta la creazione non è valido per il contenuto specificato.
	 * 
	 * @throws IOException
	 */
	public Thumbnail createThumbnail(String pContentId, String pThumbDefinition, boolean pAsync) throws IOException {
		/*
		 * POST <base>/content{property}/thumbnails?as={async?}
		 * 
		 * {
		 *     "thumbnailName": <name>
		 * }
		 */
		GenericUrl lUrl = getContentUrl(pContentId);
		lUrl.appendRawPath(URL_RELATIVE_THUMBNAILS);
		lUrl.set("as", pAsync);

		// Recupero delle definizioni valide
		// Purtroppo Alfresco restituisce successo anche se viene richiesta la generazione di una
		// thumbnail non possibile. Controllando preventivamente si può restituire null.
		List<String> lThumbDefinitions = getThumbnailDefinitions(pContentId);
		if (!lThumbDefinitions.contains(pThumbDefinition)) {
			return null;
		}

		JsonHttpContent lContent = new JsonHttpContent(JSON_FACTORY, new Thumbnail(pThumbDefinition));

		HttpHeaders lRequestHeaders = new HttpHeaders().setContentType("application/json");
		HttpRequest lRequest =
		        mHttpRequestFactory.buildPostRequest(lUrl, lContent).setHeaders(lRequestHeaders);

		HttpResponse lResponse = lRequest.execute();
		Thumbnail lThumbnail = lResponse.parseAs(Thumbnail.class);

		return lThumbnail;
	}
}
