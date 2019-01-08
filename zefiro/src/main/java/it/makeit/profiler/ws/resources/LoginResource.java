package it.makeit.profiler.ws.resources;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.JsonObjectParser;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoException;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;
import it.makeit.jbrick.http.SessionUtil;
import it.makeit.jbrick.web.LocaleUtil;
import it.makeit.jbrick.ws.filters.LogFilter;
import it.makeit.jbrick.ws.resources.AbstractResource;
import it.makeit.profiler.dao.UsersBean;
import it.makeit.zefiro.Util;

/**
 * @author MAKE IT
 */
@Path("/Login")
public class LoginResource extends AbstractResource {

	
	private static Log log = Log.getInstance(LoginResource.class);
	//@Inject UserSessionListener userListenener;
	
	
	@GET	
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	//@Context HttpServletRequest pRequest
	public Response login(@Context HttpServletRequest pRequest, @Context HttpServletResponse pResponse) throws UnsupportedEncodingException, JsonProcessingException {

		String authHeader = pRequest.getHeader("Authorization");
		if(authHeader == null)
			throw new JBrickException(JBrickException.FATAL, "Username o password inseriti risultano errati!");
		String token = authHeader.replaceFirst("Basic ", "");
		
		byte[] decoded = null;
		try {
		 decoded = Base64.getMimeDecoder().decode(token.trim().getBytes("UTF-8"));
		}catch(IllegalArgumentException argExcep) {
			throw new JBrickException(JBrickException.FATAL, argExcep.getLocalizedMessage());
		}
		String userpassword = new String(decoded);
		String[] creadentials = userpassword.split(":");
		
		HttpSession lSession = pRequest.getSession();

		String lAction = pRequest.getParameter("action");

		if (!StringUtils.isBlank(lAction) && lAction.equalsIgnoreCase("logout")) {
			lSession.removeAttribute("utente");
			lSession.invalidate();
			SessionUtil.completeSession(pRequest);
			return Response.status(Status.UNAUTHORIZED).build();
		}

		UsersBean lUsersBean;
		/// UsersManager lUsersManager = UsersManager.getInstance();
		// RolesManager lRolesManager = RolesManager.getInstance();
		// ActionsManager lActionsManager = ActionsManager.getInstance();
		// TODO gestione ruoli

		//String lUsername = pRequest.getParameter("username");
		String lUsername = creadentials[0];
		String lPassword = creadentials[1];
		String lRootFolderId = creadentials[2];
				
		log.info("Login dell'utente " + lUsername);
		
		// lUsersBean = lUsersManager.checkLogin(lUsername, lPassword);
		try {
		lUsersBean = checkLogin(lUsername, lPassword);
		}catch (AlfrescoException e) {
			lUsersBean = null;
		}catch (HttpResponseException ex) {
			lUsersBean = null;
		}
		AlfrescoConfig lAlfrescoConfig = Util.getDefaultAlfrescoConfig(lUsername, lPassword);
		JBrickConfigManager configManager = JBrickConfigManager.getInstance();
		String lRootFolderLabel = configManager.getProperty("./alfresco[@rootFolderId='"+ lRootFolderId +"']/@label");

		if (lUsersBean == null) {
			UsersBean lUsersBeanFailed = new UsersBean();
			Map<String, Object> jbrickConfigProperties =  lUsersBeanFailed.getParametersMap();
			if(jbrickConfigProperties == null)
				jbrickConfigProperties = new HashMap<>();
			jbrickConfigProperties.put("readOnly", lAlfrescoConfig.isReadOnly());
			jbrickConfigProperties.put("process", lAlfrescoConfig.isProcess());
			jbrickConfigProperties.put("rootFolderId", lRootFolderId);
			jbrickConfigProperties.put("rootFolderLabel", lRootFolderLabel);
			lUsersBeanFailed.setParametersMap(jbrickConfigProperties);
			return Response
			        .status(Status.FORBIDDEN)
			        .entity(lUsersBeanFailed) // indica che il login è fallito
			        .build();
		}
		
		/** Read user's roles */
		// lUsersBean.setRoles(lRolesManager.getUserRoles(lUsersBean.getIdUser()));
		/** Read user's actions */
		// lUsersBean.setActions(lActionsManager.getUserActions(lUsersBean.getIdUser()));

		// Per visualizzare il Guessed user nel Tomcat Manager
		// (v. http://www.docjar.com/html/api/org/apache/catalina/manager/util/SessionUtils.java.html r. 63 e 168)
		lSession.setAttribute("Login", lUsername);
		lSession.setAttribute("rootFolderId", lRootFolderId);
		
		lSession.setAttribute("alfrescoConfig", lAlfrescoConfig);
		Locale locale = LocaleUtil.getLocale(pRequest.getSession());
		lSession.setAttribute("alfrescoSession", AlfrescoHelper.createSession(lAlfrescoConfig, locale));
		
		//only needed to send jBrickConfig's properties(readonly, process) to client 
		Map<String, Object> jbrickConfigProperties =  lUsersBean.getParametersMap();
		if(jbrickConfigProperties == null)
			jbrickConfigProperties = new HashMap<>();
		jbrickConfigProperties.put("readOnly", lAlfrescoConfig.isReadOnly());
		jbrickConfigProperties.put("process", lAlfrescoConfig.isProcess());
		jbrickConfigProperties.put("rootFolderId", lRootFolderId);
		jbrickConfigProperties.put("rootFolderLabel", lRootFolderLabel);
		lUsersBean.setParametersMap(jbrickConfigProperties);
		lUsersBean.setNotLoggedIn(false);
		lUsersBean.setEnabled(1);
		lSession.setAttribute("utente", lUsersBean);
		
		
		//userListenener.onLogin();
		
		return Response.ok(lUsersBean).build();
		
	}
	

    private UsersBean checkLogin(final String pStrUsername, final String pStrPassword) throws HttpResponseException{    	
    	Person lPerson = null;
    	try {
    		AlfrescoConfig lAlfrescoConfig = Util.getDefaultAlfrescoConfig(pStrUsername,pStrPassword);

    		HttpRequestFactory lHttpRequestFactory = AlfrescoHelper.HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
    			@Override
    			public void initialize(HttpRequest request) {
    				request
    					.setParser(new JsonObjectParser(AlfrescoHelper.JSON_FACTORY))
    					.setInterceptor(new BasicAuthentication(pStrUsername, pStrPassword));
    			}
    	    });
    		
    		lPerson = AlfrescoHelper.getUser(pStrUsername, lHttpRequestFactory, lAlfrescoConfig);    		
		} catch (AlfrescoException e) {
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
    	
    	if (lPerson == null || !lPerson.isEnabled()) {
    		return null;
    	}
    	
    	UsersBean lUsersBean = new UsersBean();
    	lUsersBean.setUsername(pStrUsername);
    	lUsersBean.setName(lPerson.getFirstName());
    	lUsersBean.setSurname(lPerson.getLastName());
    	lUsersBean.setFullName(StringUtils.defaultString(lPerson.getFirstName())+" "+StringUtils.defaultString(lPerson.getLastName()));
    	lUsersBean.setEnabled(1);

    	lUsersBean.setIdUser((int) Calendar.getInstance().getTimeInMillis()); //FIXME?
    	
    	return lUsersBean;
    }
}
