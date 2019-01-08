package it.makeit.jbrick.ws.filters;

import it.makeit.jbrick.Log;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private static Log mLog = Log.getInstance(AuthenticationFilter.class);
	
	@Context
	private HttpServletRequest mRequest;
 
    @Override
    public void filter(ContainerRequestContext pRequestContext) throws IOException {
    	mLog.debug("AuthenticationFilter start");
    	boolean isNotLogin = !pRequestContext.getUriInfo().getPath().endsWith("Login") && mRequest.getSession().getAttribute("utente") == null;
    	boolean isNotRootFolders = !pRequestContext.getUriInfo().getPath().endsWith("rootFolders");
    	    	
    	if (isNotLogin && isNotRootFolders) {
    		
    		mLog.info("User not logged in!");
    		    		
    		pRequestContext.abortWith(
    			Response
    			.status(Response.Status.FORBIDDEN)
    			.entity("{\"notLoggedIn\": \"1\"}")
    			.build()
    		);
		}
    	
    	mLog.debug("AuthenticationFilter end");
    }
}
