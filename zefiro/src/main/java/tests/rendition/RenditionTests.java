package tests.rendition;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Rendition;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;

import com.sun.research.ws.wadl.Doc;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.RenditionKinds;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;
import it.makeit.zefiro.Util;

public class RenditionTests {
	private static final String ATOMPUB_CMIS11_URL_TEMPLATE = "%1$s/alfresco/api/-default-/public/cmis/versions/1.1/atom";
	@Before
	public void init() {
		
		
	}
	
	@Test
	public void testRenditions() {
		AlfrescoConfig alfrescoConfig = getDefaultAlfrescoConfig("admin", "admin");
		Locale loc = new Locale("it", "IT");
		Session session = createSession(alfrescoConfig, loc);
		
		OperationContext operationContxt = session.getDefaultContext();
		operationContxt.setRenditionFilterString("*");
		
		CmisObject lCmisObject = session.getObject("9dcf7401-6a10-42bd-af84-7fd16ace951b;1.0", operationContxt );
		
		for(Rendition r: lCmisObject.getRenditions())
			System.out.println("Size: "+r.getKind() + ": " + r.getTitle());
		//Document doc = getDocumentById(session, "85ddbb92-2dce-4000-9a64-ee5458af77f8");
		
	}
	
	public static Document getDocumentById(Session pSession, String pStrId) {

		Document lDocument = null;

		
		try {
			OperationContext lOperationContext = pSession.createOperationContext();
			
				lOperationContext.setIncludeRelationships(IncludeRelationships.NONE);
				lDocument = (Document) pSession.getObject(pStrId, lOperationContext);
			

		} catch (CmisObjectNotFoundException e) {
			
		}


		return lDocument;
	}
	
	public static Session createSession(AlfrescoConfig config, Locale locale) {

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

		return lSession;
	}
	

	private static String buildUrl(String pTemplate, AlfrescoConfig pConfig, String... pListParameters) {
		try (Formatter formatter = new Formatter()) {
			return formatter.format(pTemplate, pConfig.getHost().toString(), pListParameters).toString();
		}
	}
	
	public static AlfrescoConfig getDefaultAlfrescoConfig(String pUsername, String pPassword) {

		URL lUrlHost;
		try {
			lUrlHost = new URL("http://10.232.1.211:8080/");
		} catch (MalformedURLException e) {
			// FIXME (Alessio): definire una gestione degli errori per l'intera
			// applicazione...
			throw new JBrickException(e, JBrickException.FATAL);
		}

		String lStrUsername = pUsername;
		String lStrPassword = pPassword;
		String lStrRootFolderId = "0e9621a2-1e16-4a6b-97c3-53bda3e49de7";

		return new AlfrescoConfig(lUrlHost, lStrUsername, lStrPassword, lStrRootFolderId);
	}
}
