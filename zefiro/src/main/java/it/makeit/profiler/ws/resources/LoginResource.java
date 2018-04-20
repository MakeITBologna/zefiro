package it.makeit.profiler.ws.resources;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.jbrick.http.SessionUtil;
import it.makeit.jbrick.ws.resources.AbstractResource;
import it.makeit.profiler.dao.UsersBean;
import it.makeit.zefiro.Util;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonObjectParser;

/**
 * @author MAKE IT
 */
@Path("/Login")
public class LoginResource extends AbstractResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response login(@Context HttpServletRequest pRequest) {

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

		String lUsername = pRequest.getParameter("username");
		String lPassword = pRequest.getParameter("password");

		// lUsersBean = lUsersManager.checkLogin(lUsername, lPassword);
		lUsersBean = checkLogin(lUsername, lPassword);

		if (lUsersBean == null) {
			UsersBean lUsersBeanFailed = new UsersBean();
			lUsersBeanFailed.setUsername(lUsername);
			return Response
			        .status(Status.FORBIDDEN)
			        .entity(lUsersBeanFailed) // indica che il login è fallito
			        .build();
		}

		/** Read user's roles */
		// lUsersBean.setRoles(lRolesManager.getUserRoles(lUsersBean.getIdUser()));
		/** Read user's actions */
		// lUsersBean.setActions(lActionsManager.getUserActions(lUsersBean.getIdUser()));

		lSession.setAttribute("utente", lUsersBean);
		// Per visualizzare il Guessed user nel Tomcat Manager
		// (v. http://www.docjar.com/html/api/org/apache/catalina/manager/util/SessionUtils.java.html r. 63 e 168)
		lSession.setAttribute("Login", lUsername);

		AlfrescoConfig lAlfrescoConfig = Util.getDefaultAlfrescoConfig(lUsername, lPassword);
		lSession.setAttribute("alfrescoConfig", lAlfrescoConfig);
		lSession.setAttribute("alfrescoSession", AlfrescoHelper.createSession(lAlfrescoConfig));

		return Response.ok(lUsersBean).build();
	}

    private UsersBean checkLogin(final String pStrUsername, final String pStrPassword){    	
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
		} catch (Exception e) {
			return null; // TODO: gestire meglio
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
