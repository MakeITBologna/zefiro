package it.makeit.alfresco;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.runtime.util.CollectionIterator;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.CapabilityContentStreamUpdates;
import org.apache.chemistry.opencmis.commons.enums.CmisVersion;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import it.makeit.alfresco.publicapi.entities.PeopleUrl;
import it.makeit.alfresco.publicapi.entities.QueriesUrl;
import it.makeit.alfresco.publicapi.model.MultipleEntry;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.alfresco.publicapi.model.PersonList;
import it.makeit.alfresco.publicapi.model.SingleEntry;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.GenericUrlFactory;
import it.makeit.alfresco.webscriptsapi.entities.GroupsUrl;
import it.makeit.alfresco.webscriptsapi.model.GroupsList;
import it.makeit.alfresco.webscriptsapi.services.NodeService;
import it.makeit.jbrick.Log;


public class AlfrescoHelper extends BaseAlfrescoHelper {

	public static enum VersioningMode {
		OVERWRITE, MINOR, MAJOR
	}

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new GsonFactory();

	private static Log mLog = Log.getInstance(AlfrescoHelper.class);

	private static final String ATOMPUB_CMIS11_URL_TEMPLATE = "%1$s/alfresco/api/-default-/public/cmis/versions/1.1/atom";

	private static final String LOGIN_URL_TEMPLATE = "%1$s/alfresco/service/api/login";

	private static final String DEFAULT_BASE_DOC_TYPE = "cmis:document";
	
	private static final Gson mGson = new Gson();

	@Deprecated
	private static String buildUrl(String pTemplate, AlfrescoConfig pConfig, String... pListParameters) {
		try (Formatter formatter = new Formatter()) {
			return formatter.format(pTemplate, pConfig.getHost().toString(), pListParameters).toString();
		}
	}

	public static HttpRequestFactory getRequestFactory(final AlfrescoConfig pConfig) {
		return HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {

			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY))
						.setInterceptor(new BasicAuthentication(pConfig.getUsername(), pConfig.getPassword()));
			}
		});
	}

	/*
	 * ------ Non CMIS
	 * -------------------------------------------------------------------------
	 * ----
	 */
	@Deprecated
	public static String getLoginTicket(AlfrescoConfig pConfig) {
		mLog.debug("Start getLoginTicket(String, String)");

		String lStrLoginTicket = null;
		HttpClient lHttpClient = new HttpClient();

		// Creo oggetto json di input
		JsonObject lJsonObject = new JsonObject();
		lJsonObject.addProperty("username", pConfig.getUsername());
		lJsonObject.addProperty("password", pConfig.getPassword());

		try (Formatter formatter = new Formatter()) {
			PostMethod lPostMethod = new PostMethod(buildUrl(LOGIN_URL_TEMPLATE, pConfig));
			lPostMethod.setRequestEntity(new StringRequestEntity(lJsonObject.toString(), "application/json", "UTF8"));

			lHttpClient.executeMethod(lPostMethod);

			if (lPostMethod.getStatusCode() == HttpStatus.SC_OK) {
				JsonObject lJsonObjectResponse = mGson.fromJson(lPostMethod.getResponseBodyAsString(),
						JsonObject.class);
				lStrLoginTicket = lJsonObjectResponse.getAsJsonObject("data").get("ticket").getAsString();
				mLog.debug("Login ticket: ", lStrLoginTicket);

			} else {
				mLog.error("Unexpected failure: " + lPostMethod.getStatusLine().toString());
			}

		} catch (Throwable t) {
			mLog.error("Errore nell'esecuzione di getLoginTicket(String, String)", t);
			throw new AlfrescoException(t, AlfrescoException.GENERIC_EXCEPTION);
		}

		mLog.debug("End getLoginTicket(String, String)");
		return lStrLoginTicket;
	}

	/**
	 * For Alfresco 5.2.* and next
	 *
	 * @author Alba Quarto
	 * @param pHttpRequestFactory
	 * @param pConfig
	 * @param pParams
	 * @return
	 */
	public static PersonList getUsers(String charSeq, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.debug("Start");

		Map<String, Object> params = buildParams(pParams);
		params.put(AlfrescoRESTQueryParamsEnum.TERM.getName(), charSeq);
		QueriesUrl queriesUrl = new QueriesUrl(pConfig.getHost());
		PeopleUrl peopleUrl = new PeopleUrl(pConfig.getHost());
		GenericUrl url = (new GenericUrlFactory(queriesUrl)).add(peopleUrl).build(params);

		return (PersonList) loadList(pHttpRequestFactory, url, buildHeaders(pConfig), PersonList.class);
	}

	/**
	 * @deprecated Using getUsers(HttpRequestFactory pHttpRequestFactory,
	 *             AlfrescoConfig pConfig, Map<String, Object> pParams) for
	 *             Alfresco 5.2.* and next
	 * @param pHttpRequestFactory
	 * @param pConfig
	 * @return
	 */
	@Deprecated
	public static List<Person> getUsers(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.debug("START getUsers()");

		// FIXME (Alessio): Alfresco non supporta questo metodo!

		PeopleUrl lPeopleUrl = new PeopleUrl(pConfig.getHost());
		try {
			HttpRequest lRequest = pHttpRequestFactory.buildGetRequest(lPeopleUrl);
			@SuppressWarnings("unchecked")
			MultipleEntry<Person> lResponse = (MultipleEntry<Person>) lRequest.execute()
					.parseAs((new TypeReference<MultipleEntry<Person>>() {
					}).getType());
			mLog.debug("END getUsers()");
			return lResponse.getEntries();

		} catch (Exception e) {
			// TODO (Alessio): gestione decente delle eccezioni
			mLog.error("Unexpected failure", e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static Person getUser(String pStrUserId, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.debug("START getUser(String)");

		PeopleUrl lPeopleUrl = new PeopleUrl(pConfig.getHost());
		lPeopleUrl.setUserId(pStrUserId);
		try {
			HttpRequest lRequest = pHttpRequestFactory.buildGetRequest(lPeopleUrl);

			@SuppressWarnings("unchecked")
			SingleEntry<Person> lResponse = (SingleEntry<Person>) lRequest.execute()
					.parseAs((new TypeReference<SingleEntry<Person>>() {
					}).getType());
			mLog.debug("END getUser(String)");
			return lResponse.getEntry();

		}catch (HttpResponseException ex) {
			mLog.error("Unexpected failure", ex);
			throw new AlfrescoException(ex, AlfrescoException.GENERIC_EXCEPTION);
		}
		catch (Exception e) {
			mLog.error("Unexpected failure", e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static GroupsList getGroups(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.debug("START getGroups()");

		GroupsUrl lUrl = new GroupsUrl(pConfig.getHost());
		try {
			HttpRequest lRequest = pHttpRequestFactory.buildGetRequest(lUrl);
			GroupsList lResponse = lRequest.execute().parseAs(GroupsList.class);
			mLog.debug("END getGroups()");
			return lResponse;

		} catch (Exception e) {
			// TODO (Alessio): gestione decente delle eccezioni
			mLog.error("Unexpected failure", e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static boolean addChildToGroup(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			String pStrGroupId, String pStrChildId) {
		mLog.debug("START addChildToGroup(String, String)");

		boolean lRet = false;

		GroupsUrl lUrl = new GroupsUrl(pConfig.getHost());
		lUrl.addChild(pStrGroupId, pStrChildId);
		try {
			// FIXME (Alessio): bisognerebbe trasferire altrove questo codice
			// ripetitivo
			HttpHeaders lRequestHeaders = new HttpHeaders().setContentType("application/json");
			HttpRequest lRequest = pHttpRequestFactory.buildPostRequest(lUrl, new EmptyContent())
					.setHeaders(lRequestHeaders);

			// TODO (Alessio): la risposta è completamente ignorata! Vanno
			// gestiti i diversi casi
			// possibili.
			// L'implementazione dovrebbe prima controllare se l'utente è già
			// parte del gruppo,
			// quindi eventualmente effettuare l'aggiunta.
			int lStatusCode = lRequest.execute().getStatusCode();
			if (200 <= lStatusCode && lStatusCode < 300) {
				lRet = true;
			}

		} catch (Exception e) {
			// TODO (Alessio): gestione decente delle eccezioni
			// mLog.error("Unexpected failure", e);
			lRet = false;
		}

		mLog.debug("END addChildToGroup(String, String)");
		return lRet;
	}

	public static InputStream getThumbnail(AlfrescoConfig pConfig, String pStrDocumentId,
			String pStrThumbnailDefinition, boolean pBlnForceCreate) {
		mLog.debug("START getThumbnail(String, String, boolean)");
		mLog.debug("Recupero thumbnail '{}' per il documento {} (creazione forzata {})", pStrThumbnailDefinition,
				pStrDocumentId, pBlnForceCreate);

		NodeService lNodeService = new NodeService(pConfig);
		InputStream lIS;
		try {
			lIS = lNodeService.getThumbnail(pStrDocumentId, pStrThumbnailDefinition, pBlnForceCreate);

		} catch (Exception e) {
			// TODO (Alessio): gestione decente delle eccezioni
			mLog.error("Unexpected failure", e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}

		if (lIS != null) {
			mLog.debug("Thumbnail {} trovata", pStrThumbnailDefinition);

		} else {
			mLog.debug("Thumbnail {} non trovata", pStrThumbnailDefinition);
		}

		mLog.debug("END getThumbnail(String, String, boolean)");
		return lIS;
	}

	/*
	 * ------ CMIS
	 * -------------------------------------------------------------------------
	 * --------
	 */
	private static void updatePropertiesWithAspects(Session pSession, Map<String, Object> pMapProperties,
			String pStrDocTypeId, Collection<String> pCollectionAspects) {
		// Creazione mappa proprietà secondo la versione CMIS del repository
		if (pSession.getRepositoryInfo().getCmisVersion() == CmisVersion.CMIS_1_1) {
			pMapProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, pCollectionAspects);

		} else {
			// Creazione dell'elenco degli aspetti
			StringBuilder lSBAspects = new StringBuilder(pStrDocTypeId);
			for (String aspect : pCollectionAspects) {
				lSBAspects.append(", ").append(aspect);
			}
			mLog.debug("[CMIS 1.0] Aspetti del documento: {}", lSBAspects.toString());

			// XXX (Alessio): funziona senza usare l'estensione Chemistry per
			// Alfresco? In generale,
			// bisognerebbe prevedere la gestione secondo la versione CMIS in
			// tutto l'AlfrescoHelper,
			// per supportare le diverse versioni di Alfresco.
			pMapProperties.put(PropertyIds.OBJECT_TYPE_ID, lSBAspects.toString());
		}

	}

	public static Session createSession(AlfrescoConfig config, Locale locale) {
		mLog.debug("Start createSession()");

		Map<String, String> lMapParameter = new HashMap<String, String>();

		// I parametri di connessione vengono impostati per usare il binding
		// AtomPub CMIS 1.1
		lMapParameter.put(SessionParameter.USER, config.getUsername());
		lMapParameter.put(SessionParameter.PASSWORD, config.getPassword());
		lMapParameter.put(SessionParameter.ATOMPUB_URL, buildUrl(ATOMPUB_CMIS11_URL_TEMPLATE, config));
		lMapParameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		//lMapParameter.put(SessionParameter.LOCALE_ISO639_LANGUAGE, config.getAcceptedLanguageAsString());
		//session Locale
		if(locale != null) {
			lMapParameter.put(SessionParameter.LOCALE_ISO3166_COUNTRY, locale.getCountry());
			lMapParameter.put(SessionParameter.LOCALE_ISO639_LANGUAGE, locale.getLanguage());
		}
		// creo la session factory
		SessionFactory lSessionFactory = SessionFactoryImpl.newInstance();

		// creo la sessione connessa al repository
		Session lSession = lSessionFactory.getRepositories(lMapParameter).get(0).createSession();

		mLog.debug("End createSession()");
		return lSession;
	}

	/**
	 * Crea un nuovo documento in Alfresco.
	 * <p>
	 * Lo stream in {@code pInputStream} sarà concumato ma non chiuso.
	 *
	 * @param pSession
	 *            La sessione da utilizzare.
	 * @param pStrParentFolderId
	 *            L'id della cartella in cui creare il nuovo documento.
	 * @param pStrFileName
	 *            Il nome del file da utilizzare.
	 * @param pLngFileLength
	 *            La lunghezza in byte dello stream, o -1 se ignota.
	 * @param pStrContentType
	 *            Il <i>content type</i> del file. Se nullo sarà impostato a
	 *            {@code application/octet-stream}
	 * @param pInputStream
	 *            Lo {@code InputStream} del nuovo contenuto.
	 * @param pMapProperties
	 *            L'eventuale mappa delle proprietà del nuovo documento.
	 * @param pCollectionAspects
	 *            L'eventuale lista degli aspetti da applicare al nuovo
	 *            documento.
	 * @param pStrObjectType
	 *            L'eventuale tipo documento da utizzare (predefinito:
	 *            {@code cmis:document}.
	 * @return Il documento creato.
	 */
	// XXX (Alessio): ha senso consentire già qui di impostare gli aspetti?
	public static Document createDocument(Session pSession, String pStrParentFolderId, String pStrFileName,
			long pLngFileLength, String pStrContentType, InputStream pInputStream, Map<String, Object> pMapProperties,
			Collection<String> pCollectionAspects, String pStrObjectType) {

		mLog.debug("START createDocument(String, String, String, long, InputStream)");

		String lStrEffectiveContentType = pStrContentType != null ? pStrContentType : "application/octet-stream";
		mLog.debug("Creazione documento '{}'({} - {} B)  nella cartella '{}')", pStrFileName, pStrContentType,
				pLngFileLength, pStrParentFolderId);

		// recupero la cartella padre
		// TODO (Alessio): gestire CmisObjectNotFoundException
		Folder lFolder = (Folder) pSession.getObject(pStrParentFolderId);

		// creo il content stream, se necessario
		ContentStream lContentStream = null;
		if (pInputStream != null) {
			lContentStream = pSession.getObjectFactory().createContentStream(pStrFileName, pLngFileLength,
					lStrEffectiveContentType, pInputStream);
		}

		if (pMapProperties == null) {
			// La mappa delle proprietà è necessaria per la creazione
			pMapProperties = new HashMap<String, Object>();
		}

		// Aggiornamento proprietà con tipo ed eventuali aspetti
		String lStrObjectType = (pStrObjectType == null) ? "cmis:document" : pStrObjectType;
		pMapProperties.put(PropertyIds.OBJECT_TYPE_ID, lStrObjectType);
		if (pCollectionAspects != null) {
			updatePropertiesWithAspects(pSession, pMapProperties, pStrObjectType, pCollectionAspects);
		}
		pMapProperties.put(PropertyIds.NAME, pStrFileName);

		// Creazione effettiva
		Document lDocument = lFolder.createDocument(pMapProperties, lContentStream, VersioningState.MAJOR);

		mLog.debug("END createDocument(String, String, String, long, InputStream)");
		return lDocument;
	}

	/**
	 * Aggiorna le proprietà di un documento.
	 *
	 * @param pSession
	 *            La sessione da utilizzare.
	 * @param pStrDocumentId
	 *            L'id del documento da aggiornare.
	 * @param pMapProperties
	 *            La mappa delle proprietà da aggiornare.
	 *
	 * @return Il documento aggiornato.
	 */
	public static Document updateDocumentProperties(Session pSession, String pStrDocumentId,
			Map<String, Object> pMapProperties) {

		// TODO (Alessio): gestione CmisObjectNotFoundException
		Document lDocument = (Document) pSession.getObject(pStrDocumentId);

		if (pMapProperties == null) {
			// Nessuna nuova proprietà impostata: si restituisce il documento
			// immutato
			mLog.info("pMapProperties nulla: nessuna proprietà modificata, il documento è immutato");
			return lDocument;
		}

		// TODO: al momento in aggiornamento gli aspetti vengono ignorati (si
		// assumono immutati)
		// if (pCollectionAspects != null) {
		// AlfrescoDocument lAlfrescoDocument = (AlfrescoDocument) lDocument;
		// // Aggiornamento degli aspetti
		// HashSet<String> lSetCurrentAspects = new HashSet<String>();
		// for (ObjectType objectType : lAlfrescoDocument.getAspects()) {
		// lSetCurrentAspects.add(objectType.getId());
		// }
		// HashSet<String> lSetAspectsToAdd = new
		// HashSet<String>(pCollectionAspects);
		// HashSet<String> lSetAspectsToRemove = new
		// HashSet<String>(lSetCurrentAspects);
		// lSetCurrentAspects.retainAll(pCollectionAspects);
		// lSetAspectsToAdd.removeAll(lSetCurrentAspects);
		// lSetAspectsToRemove.removeAll(lSetCurrentAspects);
		//
		// lAlfrescoDocument.addAspect(lSetAspectsToAdd.toArray(new
		// String[lSetAspectsToAdd.size()]));
		// lAlfrescoDocument.removeAspect(lSetAspectsToRemove.toArray(new
		// String[lSetAspectsToRemove.size()]));
		// }

		lDocument = (Document) lDocument.updateProperties(pMapProperties);

		return lDocument;
	}

	/**
	 * Aggiorna il contenuto binario del documento.
	 * <p>
	 * Consente di sostituire il documento con una nuova versione, con la
	 * possibilità di specificare se questa debba essere <i>minor</i> o
	 * <i>major</i>.
	 * <p>
	 * Lo stream in {@code pInputStream} sarà concumato ma non chiuso.
	 *
	 * @param pSession
	 *            La sessione da utilizzare.
	 * @param pStrDocumentId
	 *            L'id del documento da aggiornare.
	 * @param pStrFileName
	 *            Il nome del file da utilizzare.
	 * @param pLngFileLength
	 *            La lunghezza in byte dello stream, o -1 se ignota.
	 * @param pStrContentType
	 *            Il <i>content type</i> del file. Se nullo sarà impostato a
	 *            {@code application/octet-stream}
	 * @param pInputStream
	 *            Lo {@code InputStream} del nuovo contenuto.
	 * @param pVersioningMode
	 *            Definisce se la modifica al contenuto del documento debba
	 *            crerare una nuova versione <i>minor</i> o <i>major</i>. Si
	 *            tenga presente che Alfresco creerà in ogni caso una nuova
	 *            versione del documento, pertanto pur specificando
	 *            {@code VersioningMode.OVERWRITE} si avrà comunque una nuova
	 *            versione <i>minor</i>.
	 * @param pStrCheckinComment
	 *            Il commento associato alla sostituzione del contenuto. Sarà
	 *            ignorato in caso di {@code VersioningMode.OVERWRITE}.
	 *
	 * @return Il documento aggiornato.
	 */
	public static Document updateDocumentContent(Session pSession, String pStrDocumentId, String pStrFileName,
			long pLngFileLength, String pStrContentType, InputStream pInputStream, VersioningMode pVersioningMode,
			String pStrCheckinComment) {

		// TODO (Alessio): gestione CmisObjectNotFoundException
		Document lDocument = getDocumentById(pSession, pStrDocumentId, false);

		if (pInputStream == null) {
			// Nessun nuovo content stream impostato: si restituisce il
			// documento immutato
			mLog.info("pInputStream nullo: nessun nuovo content stream impostato, il documento è immutato");
			return lDocument;
		}

		ContentStream lContentStream = pSession.getObjectFactory().createContentStream(pStrFileName, pLngFileLength,
				pStrContentType, pInputStream);

		// verifico se il documento è versionabile
		if (((DocumentType) (lDocument.getType())).isVersionable()
				&& !pVersioningMode.equals(VersioningMode.OVERWRITE)) {
			// il documento è versionabile e non va sovrascritto: aggiorno la
			// versione

			Document lDocumentPWC = (Document) pSession.getObject(lDocument.checkOut());
			ObjectId lNewVersionId;
			try {
				boolean pBlnMajorVersion = pVersioningMode.equals(VersioningMode.MAJOR) ? true : false;
				lNewVersionId = lDocumentPWC.checkIn(pBlnMajorVersion, null, lContentStream, pStrCheckinComment);

			} catch (Throwable t) {
				mLog.error("Errore nell'esecuzione durante il check-in del documento", t);
				lDocumentPWC.cancelCheckOut();
				throw new AlfrescoException(t, AlfrescoException.GENERIC_EXCEPTION);
			}

			// rileggo il documento per avere l'id aggiornato con la nuova
			// versione
			lDocument = (Document) pSession.getObject(lNewVersionId);

		} else {
			// il documento non è versionabile o va sovrascritto: aggiorno il
			// contenuto
			if (!pSession.getRepositoryInfo().getCapabilities().getContentStreamUpdatesCapability()
					.equals(CapabilityContentStreamUpdates.ANYTIME)) {
				// FIXME: probabilmente si dovrebbe lanciare un'eccezione
				mLog.error("update without checkout not supported in this repository");

			} else {
				mLog.debug("updating content stream");
				lDocument.setContentStream(lContentStream, true);
				lDocument = lDocument.getObjectOfLatestVersion(false);
			}
		}

		return lDocument;
	}

	public static Document setDocumentAspects(Session pSession, String pStrDocumentId,
			Collection<String> pCollectionAspects) {
		mLog.debug("START setDocumentAspects(String, Collection)");

		// TODO (Alessio): gestione CmisObjectNotFoundException
		Document lDocument = (Document) pSession.getObject(pStrDocumentId);
		Map<String, Object> lMapProperties = new LinkedHashMap<>();

		updatePropertiesWithAspects(pSession, lMapProperties, lDocument.getType().getId(), pCollectionAspects);

		// Effettivo aggiornamento del documento
		// TODO (Alessio): gestione del fallimento (dovrebbe essere una regola
		// di vita...)
		lDocument = (Document) lDocument.updateProperties(lMapProperties);

		mLog.debug("END setDocumentAspects(String, Collection)");
		return lDocument;
	}

	public static Document getDocumentById(Session pSession, String pStrId, boolean pIncludeRelations) {

		Document lDocument = null;

		mLog.debug("Start getDocumentById(Session, String, Boolean); id: ", pStrId, " - includeRelations: ",
				pIncludeRelations);

		try {
			OperationContext lOperationContext = pSession.createOperationContext();
			if (pIncludeRelations) {
				lOperationContext.setIncludeRelationships(IncludeRelationships.BOTH);
				lDocument = (Document) pSession.getObject(pStrId, lOperationContext);
			} else {
				lOperationContext.setIncludeRelationships(IncludeRelationships.NONE);
				lDocument = (Document) pSession.getObject(pStrId, lOperationContext);
			}

		} catch (CmisObjectNotFoundException e) {
			mLog.debug("Documento non trovato");
		}

		mLog.debug("End getDocumentById(Session, pStrId)");

		return lDocument;
	}

	public static Document getDocumentById(Session pSession, String pStrId) {
		return getDocumentById(pSession, pStrId, false);
	}

	public static void renameDocument(Session pSession, String pStrNodeRef, String pStrNewName) {
		mLog.debug("ENTER renameDocument(<Session>, " + pStrNodeRef + ", " + pStrNewName + ")");

		Document lDocument = (Document) pSession.getObject(pStrNodeRef);

		Map<String, String> lMapProperties = new HashMap<String, String>();
		lMapProperties.put(PropertyIds.NAME, pStrNewName);

		lDocument.updateProperties(lMapProperties, true);

		mLog.debug("EXIT renameDocument(<Session>, " + pStrNodeRef + ", " + pStrNewName + ")");
	}

	public static void deleteDocument(Session pSession, String pStrNodeRef, boolean pBlnAllVersions) {
		mLog.debug("ENTER deleteDocument(<Session>, " + pStrNodeRef + ", " + pBlnAllVersions + ")");

		Document lDocument = (Document) pSession.getObject(pStrNodeRef);
		lDocument.delete(pBlnAllVersions);

		mLog.debug("EXIT deleteDocument(<Session>, " + pStrNodeRef + ", " + pBlnAllVersions + ")");
	}

	public static Document getDocumentByPath(Session pSession, String pStrPath) {

		Document lDocument = null;

		mLog.debug("Start getDocumentByPath(Session, String); path: ", pStrPath);

		try {
			lDocument = (Document) pSession.getObjectByPath(pStrPath);
		} catch (CmisObjectNotFoundException e) {
			mLog.debug("Documento non trovato");
		}

		mLog.debug("End getDocumentByPath(Session, String)");

		return lDocument;
	}

	public static String createFolder(Session pSession, String pStrParentFolderId, String pStrFolderName) {

		String lStrFolderId = null;

		mLog.debug("Start createFolder(", pSession, ",", pStrParentFolderId, ",", pStrFolderName, ")");

		// recupero la cartella padre
		Folder lFolder = (Folder) pSession.getObject(pStrParentFolderId);

		// creo la cartella
		Map<String, String> lMapProperties = new HashMap<String, String>();
		lMapProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		lMapProperties.put(PropertyIds.NAME, pStrFolderName);
		Folder lFolderNew;
		try {
			lFolderNew = lFolder.createFolder(lMapProperties);
			lStrFolderId = lFolderNew.getId();

		} catch (CmisContentAlreadyExistsException e) {
			mLog.error("Il documento esiste già", e);
			throw new AlfrescoException(e, AlfrescoException.FOLDER_ALREADY_EXISTS_EXCEPTION);
		}

		mLog.debug("End createFolder(", pSession, ",", pStrParentFolderId, ",", pStrFolderName, "): return ",
				lStrFolderId);

		return lStrFolderId;
	}

	public static void renameFolder(Session pSession, String pStrNodeRef, String pStrNewName) {
		mLog.debug("ENTER renameFolder(<Session>, " + pStrNodeRef + ", " + pStrNewName + ")");

		Folder lFolder = (Folder) pSession.getObject(pStrNodeRef);

		Map<String, String> lMapProperties = new HashMap<String, String>();
		lMapProperties.put(PropertyIds.NAME, pStrNewName);

		try {
			lFolder.updateProperties(lMapProperties, true);
		} catch (CmisContentAlreadyExistsException e) {
			mLog.error("Impossibile aggiornare le proprietà del documento", e);
			throw new AlfrescoException(e, AlfrescoException.FOLDER_ALREADY_EXISTS_EXCEPTION);
		}

		mLog.debug("EXIT renameFolder(<Session>, " + pStrNodeRef + ", " + pStrNewName + ")");
	}

	public static void deleteTree(Session pSession, String pStrFolderId) {

		mLog.debug("Start deleteTree(", pStrFolderId, ")");

		Folder lFolder = (Folder) pSession.getObject(pStrFolderId);
		lFolder.deleteTree(true, UnfileObject.DELETE, true);

		mLog.debug("End deleteTree()");
	}

	public static Relationship createRelation(Session pSession, String pStrRelationTypeId, String pStrSourceId,
			String pStrTargetId) {

		mLog.debug("Start createRelation(<Session>, " + pStrRelationTypeId + ", " + pStrSourceId + ", " + pStrTargetId
				+ ")");

		Map<String, Serializable> lRelationProperties = new HashMap<String, Serializable>();
		lRelationProperties.put(PropertyIds.SOURCE_ID, pStrSourceId);
		lRelationProperties.put(PropertyIds.TARGET_ID, pStrTargetId);
		lRelationProperties.put(PropertyIds.OBJECT_TYPE_ID, pStrRelationTypeId);
		ObjectId lRelationId = pSession.createRelationship(lRelationProperties);

		mLog.debug("End createRelation()");
		return (Relationship) pSession.getObject(lRelationId);
	}

	public static void deleteObject(Session pSession, String pStrObjectId) {

		mLog.debug("Start deleteObject(", pStrObjectId, ")");

		ObjectId lObjectId = pSession.createObjectId(pStrObjectId);
		pSession.delete(lObjectId);

		mLog.debug("End deleteObject()");
	}

	public static Folder getFolderByPath(Session pSession, String pStrPath) {
		mLog.debug("Start getFolderByPath(Session, String); path: ", pStrPath);
		Folder lFolder = null;

		try {
			lFolder = (Folder) pSession.getObjectByPath(pStrPath);
		} catch (CmisObjectNotFoundException e) {
			mLog.debug("Cartella non trovata");
		}

		mLog.debug("End getFolderByPath(Session, String)");
		return lFolder;
	}

	public static Folder getFolderById(Session pSession, String pStrNoderef) {
		mLog.debug("Start getFolderByPath(Session, String); path: ", pStrNoderef);
		Folder lFolder = null;

		try {
			lFolder = (Folder) pSession.getObject(pStrNoderef);
		} catch (CmisObjectNotFoundException e) {
			mLog.debug("Cartella non trovata");
		}

		mLog.debug("End getFolderByPath(Session, String)");
		return lFolder;
	}

	public static long getFolderTreeSize(Session pSession, String pStrNodeRef) {
		mLog.debug("ENTER getFolderSize(pFolder)");
		long lLngSize = 0;

		Folder lFolder = getFolderById(pSession, pStrNodeRef);
		for (Tree<FileableCmisObject> tree : lFolder.getDescendants(-1)) {
			lLngSize += doGetFolderTreeSize(tree);
		}

		mLog.debug("--- Dimensione totale contenuto di " + lFolder.getName() + ": " + lLngSize);
		mLog.debug("EXIT getFolderSize(pFolder)");
		return lLngSize;
	}

	private static long doGetFolderTreeSize(Tree<FileableCmisObject> pTree) {
		long lLngSize = 0;

		FileableCmisObject lCurCmisObject = pTree.getItem();
		if (lCurCmisObject instanceof Document) {
			long lLngCurSize = ((Document) lCurCmisObject).getContentStreamLength();
			if (lLngCurSize > -1) {
				lLngSize += lLngCurSize;
			}
			mLog.debug("---------- Dimensione di " + lCurCmisObject.getName() + ": " + lLngCurSize);

		} else {
			for (Tree<FileableCmisObject> tree : pTree.getChildren()) {
				lLngSize += doGetFolderTreeSize(tree);
			}
			mLog.debug("------ Dimensione totale contenuto di " + lCurCmisObject.getName() + ": " + lLngSize);
		}

		return lLngSize;
	}

	public static List<Document> searchDocuments(Session pSession, String pTypeId, String pWhereClause) {
		mLog.debug("START searchDocuments(String, String");

		// per evitare il problema dei documenti duplicati
		LinkedHashMap<String, Document> lHashMapResults = new LinkedHashMap<String, Document>();

		// Temporanea disabilitazione della cache
		OperationContext lOperationContext = pSession.createOperationContext();
		lOperationContext.setCacheEnabled(false);

		ItemIterable<CmisObject> lResult = pSession.queryObjects(pTypeId, pWhereClause, false, lOperationContext);
		for (CmisObject cmisObject : lResult) {
			// TODO (Alessio) gestione paginazione (in entrata e in uscita,
			// anche se probabilmente è
			// già gestita internamente in queryObjects)
			lHashMapResults.put(cmisObject.getId(), (Document) cmisObject);
		}

		mLog.debug("END searchDocuments(String, String");
		return new ArrayList<Document>(lHashMapResults.values());
	}

	public static List<Document> searchDocuments(Session pSession, String pQuery) {
		mLog.debug("START searchDocuments(String");
		mLog.debug("CMIS query: {}", pQuery);

		// per evitare il problema dei documenti duplicati
		LinkedHashMap<String, Document> lHashMapResults = new LinkedHashMap<String, Document>();

		ItemIterable<QueryResult> lResults = pSession.query(pQuery, false);
		// XXX (Alessio): sarà il modo giusto? Prestazioni?

		if (lResults != null) {
			int i = 0;
			//
			for (Iterator<QueryResult> iterator = lResults.iterator(); i < ((CollectionIterator<QueryResult>) iterator)
					.getTotalNumItems();) {
				QueryResult qResult = iterator.next();

				// } (QueryResult qResult : lResults) {
				if (qResult != null) {
					PropertyData<?> lPropData = qResult.getPropertyById("cmis:objectId");

					if (lPropData != null) {
						String lObjectId = (String) lPropData.getFirstValue();
						CmisObject lObj = pSession.getObject(pSession.createObjectId(lObjectId)); 
						lHashMapResults.put(lObjectId, (Document) lObj);
					}
				}

				i++;
			}
		}

		mLog.debug("END searchDocuments(String");
		return new ArrayList<Document>(lHashMapResults.values());
	}

	public static List<String> fullTextQuery(Session lSession, String pStrText) {

		List<String> lListResults = null;
		int lIntDocumentsFound = 0;

		mLog.debug("Start fullTextQuery(String) - ", pStrText);

		ItemIterable<QueryResult> lItemIterableQueryResult = lSession
				.query("SELECT cmis:objectId FROM cmis:document where CONTAINS('" + pStrText + "')", false);

		lListResults = new ArrayList<String>();
		for (QueryResult lQueryResult : lItemIterableQueryResult) {
			for (PropertyData<?> lPropertyData : lQueryResult.getProperties()) {
				lListResults.add((String) lPropertyData.getFirstValue());
			}
		}

		if (lListResults != null) {
			lIntDocumentsFound = lListResults.size();
		}

		mLog.debug("End fullTextQuery(String) - ", pStrText, " - found ", lIntDocumentsFound, " documents.");

		return lListResults;
	}

	/**
	 * Recupera l'albero dei tipi documento a partire da un determinato tipo
	 * base.
	 * <p>
	 * Il recupero dei tipi può essere limitato nella profondità.
	 * <p>
	 * L'albero è senza radice, perciò di fatto è una lista di alberi (ovvero
	 * gli alberi sottostanti i figli diretti del tipo base).
	 *
	 * @param pSession
	 *            la sessione da usare.
	 * @param pStrBaseType
	 *            il tipo base da cui far partire la ricerca.
	 * @param pIntDepth
	 *            la profondità massima dell'albero. Deve essere un valore
	 *            maggiore di 0, o -1 per una profondità illimitata.
	 * @param pBlnIncludeProperties
	 *            se {@code true} il risultato comprenderà anche il dettaglio
	 *            della definizione delle proprietà.
	 *
	 * @return L'albero dei tipi documento.
	 */
	public static List<Tree<ObjectType>> getTypesTree(Session pSession, String pStrBaseType, int pIntDepth,
			boolean pBlnIncludeProperties) {
		mLog.debug("START getTypesTree(String, int, boolean)");

		List<Tree<ObjectType>> lTypeTree = null;
		try {
			// XXX: Sono dati tendenzialmente stabili, si usa la cache:
			// corretto?
			lTypeTree = pSession.getTypeDescendants(pStrBaseType, pIntDepth, pBlnIncludeProperties);

		} catch (CmisObjectNotFoundException e) {
			// Niente: il tipo non esiste e si restituisce null
			mLog.info("Tipo base '{}' non trovato", pStrBaseType);
		}

		mLog.debug("END getTypesTree(String, int, boolean)");
		return lTypeTree;
	}

	/**
	 * Recupera l'albero dei tipi documento a partire da un determinato tipo
	 * base.
	 * <p>
	 * L'albero è senza radice, perciò di fatto è una lista di alberi (ovvero
	 * gli alberi sottostanti i figli diretti del tipo base).
	 *
	 * @param pSession
	 *            la sessione da usare
	 * @param pStrBaseType
	 *            il tipo base da cui far partire la ricerca
	 * @param pBlnIncludeProperties
	 *            se {@code true} il risultato comprenderà anche il dettaglio
	 *            della definizione delle proprietà.
	 *
	 * @return L'albero dei tipi documento
	 *
	 * @see #getTypesTree(Session, String, int, boolean)
	 */
	public static List<Tree<ObjectType>> getTypesTree(Session pSession, String pStrBaseType,
			boolean pBlnIncludeProperties) {

		return getTypesTree(pSession, pStrBaseType, -1, pBlnIncludeProperties);
	}

	/**
	 * Recupera l'albero dei tipi documento a partire dal tipo documento
	 * predefinito ({@code cmis:document}).
	 * <p>
	 * L'albero è senza radice, perciò di fatto è una lista di alberi (ovvero
	 * gli alberi sottostanti i figli diretti del tipo base).
	 *
	 * @param lSession
	 *            la sessione da usare
	 * @param pBlnIncludeProperties
	 *            se {@code true} il risultato comprenderà anche il dettaglio
	 *            della definizione delle proprietà.
	 *
	 * @return L'albero dei tipi documento
	 *
	 * @see #getTypesTree(Session, String, int, boolean)
	 */
	public static List<Tree<ObjectType>> getTypesTree(Session lSession, boolean pBlnIncludeProperties) {
		return getTypesTree(lSession, DEFAULT_BASE_DOC_TYPE, pBlnIncludeProperties);
	}

	/**
	 * Recupera la lista dei tipi documento finali a partire da un determinato
	 * tipo base.
	 * <p>
	 * I tipi documento restituiti sono le foglie dell'albero dei tipi
	 * discendenti dal tipo base specificato, ovvero i suoi ultimi discendenti.
	 *
	 * @param pSession
	 *            la sessione da usare.
	 * @param pStrBaseType
	 *            il tipo base da cui far partire la ricerca.
	 * @param pBlnIncludeProperties
	 *            se {@code true} il risultato comprenderà anche il dettaglio
	 *            della definizione delle proprietà.
	 *
	 * @return La lista dei tipi documento.
	 */
	public static List<ObjectType> getTypesTreeLeaves(Session pSession, String pStrBaseType,
			boolean pBlnIncludeProperties) {
		mLog.debug("START getTypesTreeLeaves(String, int, boolean)");

		List<ObjectType> lLeaves = new LinkedList<ObjectType>();

		List<Tree<ObjectType>> lTypesTree = getTypesTree(pSession, pStrBaseType, -1, pBlnIncludeProperties);
		doGetTypesTreeLeaves(lTypesTree, lLeaves);

		mLog.debug("END getTypesTreeLeaves(String, int, boolean)");
		return lLeaves;
	}

	private static void doGetTypesTreeLeaves(List<Tree<ObjectType>> pTypesTree, List<ObjectType> pLeaves) {
		for (Tree<ObjectType> tree : pTypesTree) {
			List<Tree<ObjectType>> lChildren = tree.getChildren();

			if (lChildren.isEmpty()) {
				pLeaves.add(tree.getItem());

			} else {
				doGetTypesTreeLeaves(lChildren, pLeaves);
			}
		}
	}

	public static ObjectType  getTypeDefinition(Session pSession, String pStrId) {
		mLog.debug("START getDocumentType(String)");

		ObjectType lType = null;
		try {
			lType = pSession.getTypeDefinition(pStrId, true);

		} catch (CmisObjectNotFoundException e) {
			// Niente: il tipo non esiste e si restituisce null
			mLog.debug("Tipo '{}' non trovato", pStrId);
		}

		mLog.debug("END getDocumentType(String)");
		return lType;
	}

	public static List<RelationshipType> getRelationshipTypes(Session pSession, String pRelationsRootId) {
		mLog.debug("START getRelationshipTypes(Session, String)");

		List<RelationshipType> lRelationTypes = new ArrayList<RelationshipType>();

		for (ObjectType lObjectType : pSession.getTypeChildren(pRelationsRootId, true)) {
			lRelationTypes.add((RelationshipType) lObjectType);
		}

		mLog.debug("END getRelationshipTypes(Session, String)");
		return lRelationTypes;
	}

	public static List<String> getTypeAspectIds(Session pSession, String pStrTypeId) {

		List<String> lFoundAspectIds = new ArrayList<>();

		TypeDefinition lObjectType = getTypeDefinition(pSession, pStrTypeId);

		// cerco gli eventuali mandatoryAspect del tipo
		List<CmisExtensionElement> lExtensions = lObjectType.getExtensions();
		for (CmisExtensionElement lExtension : lExtensions) {
			if (lExtension.getName().matches("(?i:.*aspect.*)")) {
				for (CmisExtensionElement lAspectExtension : lExtension.getChildren()) {
					lFoundAspectIds.add(lAspectExtension.getValue());
				}
			}
		}

		return lFoundAspectIds;
	}
	
	public static List<String> getTypeAspects(ObjectType obj) {

		List<String> lFoundAspectIds = new ArrayList<String>();

		// cerco gli eventuali mandatoryAspect del tipo
		List<CmisExtensionElement> lExtensions = obj.getExtensions();
		for (CmisExtensionElement lExtension : lExtensions) {
			 if(lExtension.getName().equals("mandatoryAspects")) {
				for (CmisExtensionElement lAspectExtension : lExtension.getChildren()) {
					lFoundAspectIds.add(lAspectExtension.getValue());
				}
			}
		}

		return lFoundAspectIds;
	}

	public static List<RelationshipType> getAllowedRelationshipTypes(Session pSession, String pRelationsRootId,
			String pStrTypeId) {
		mLog.debug("START getAllowedRelationshipTypes(Session, String, String)");

		List<RelationshipType> lAllowedRelationTypes = new ArrayList<RelationshipType>();

		List<String> lTypeAspectIds = getTypeAspectIds(pSession, pStrTypeId);

		for (RelationshipType lRelationType : getRelationshipTypes(pSession, pRelationsRootId)) {

			boolean lIsAllowed = false;

			List<ObjectType> lRelationAllowedTypes = new ArrayList<ObjectType>();
			lRelationAllowedTypes.addAll(lRelationType.getAllowedSourceTypes());
			lRelationAllowedTypes.addAll(lRelationType.getAllowedTargetTypes());

			for (ObjectType lObjectType : lRelationAllowedTypes) {
				if (lObjectType.getId().equals(pStrTypeId) || lTypeAspectIds.contains(lObjectType.getId())) {
					lIsAllowed = true;
					break;
				}
			}

			if (lIsAllowed) {
				lAllowedRelationTypes.add(lRelationType);
			}
		}

		mLog.debug("END getAllowedRelationshipTypes(Session, String, String)");
		return lAllowedRelationTypes;
	}

	/**
	 * Recupera l'elenco delle versioni di un documento.
	 * <p>
	 * La lista dei documenti restituiti ha solo un sottoinsieme delle loro
	 * proprietà, ovvero:
	 * <ul>
	 * <li>{@code cmis:objectId}</li>
	 * <li>{@code cmis:name}</li>
	 * <li>{@code cmis:versionLabel}</li>
	 * <li>{@code cmis:isLatestVersion}</li>
	 * <li>{@code cmis:isMajorVersion}</li>
	 * <li>{@code cmis:creationDate}</li>
	 * <li>{@code cmis:createdBy}</li>
	 * <li>{@code cmis:lastModificationDate}</li>
	 * <li>{@code cmis:lastModififiedBy}</li>
	 * </ul>
	 *
	 * @param pSession
	 *            La sessione da utilizzare.
	 * @param pStrDocumentId
	 *            L'id del documento di cui recuperare le versioni.
	 *
	 * @return La lista delle versioni del documento.
	 */
	public static List<Document> getDocumentVersions(Session pSession, String pStrDocumentId) {
		mLog.debug("START getDocumentVersions(String)");
		Document lDocument = (Document) pSession.getObject(pStrDocumentId);

		// Filtro sulle proprietà restituite
		OperationContext lOperationContext = pSession.createOperationContext();
		Set<String> lPropertyFilter = new HashSet<>();
		lPropertyFilter.addAll(Arrays.asList(PropertyIds.OBJECT_ID, PropertyIds.NAME, PropertyIds.VERSION_LABEL,
				PropertyIds.IS_LATEST_VERSION, PropertyIds.IS_MAJOR_VERSION, PropertyIds.CREATION_DATE,
				PropertyIds.CREATED_BY, PropertyIds.LAST_MODIFICATION_DATE, PropertyIds.LAST_MODIFIED_BY));
		lOperationContext.setFilter(lPropertyFilter);
		lOperationContext.setIncludeAllowableActions(false);
		lOperationContext.setIncludePathSegments(false);

		List<Document> lListResults = lDocument.getAllVersions(lOperationContext);
		mLog.debug("Trovate {} versioni per il documento {}", lListResults.size(), pStrDocumentId);

		mLog.debug("END getDocumentVersions(String)");
		return lListResults;
	}

	public static List<Rendition> getDocumentRenditions(Session pSession, String pStrDocumentId, String pStrFilter) {
		mLog.debug("START getDocumentRenditions(String, String)");

		OperationContext operationContext = pSession.createOperationContext();
		operationContext.setFilterString(PropertyIds.NAME);
		operationContext.setRenditionFilterString(pStrFilter);
		CmisObject lCmisObject = pSession.getObject(pStrDocumentId, operationContext);
		List<Rendition> lListResults = lCmisObject.getRenditions();

		mLog.debug("END getDocumentRenditions(String)");
		return lListResults;
	}

	public static List<Rendition> getDocumentRenditions(Session pSession, String pStrDocumentId) {
		mLog.debug("START getDocumentRenditions(String)");
		List<Rendition> lRenditions = getDocumentRenditions(pSession, pStrDocumentId, RenditionKinds.ALL);
		mLog.debug("END getDocumentRenditions(String)");
		return lRenditions;
	}

}
