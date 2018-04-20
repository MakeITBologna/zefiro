package it.makeit.jbrick.ws.filters;

import it.makeit.jbrick.Log;
import it.makeit.profiler.dao.UsersBean;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;

@Priority(Priorities.AUTHENTICATION)
public class LogFilter implements ContainerRequestFilter {
	
	private static Log mLog = Log.getInstance(LogFilter.class);
	
	@Context
	private HttpServletRequest mRequest;
 
    @Override
    public void filter(ContainerRequestContext pRequestContext) throws IOException {
    	
    	mLog.debug("LogFilter start");

		Log.mdcPut("remoteIP", mRequest.getRemoteAddr());
		
		if (mRequest.getRequestedSessionId() != null) {
			Log.mdcPut("sessionid", mRequest.getRequestedSessionId().substring(0,5));
			
			if (mRequest.getSession(false) != null && mRequest.getSession().getAttribute("utente") != null) {
				UsersBean lUsersBean = (UsersBean) mRequest.getSession().getAttribute("utente");
				Log.mdcPut("username", lUsersBean.getUsername());
			}
		}
		
		try	{
			String lrequestURI = mRequest.getRequestURI();
			String lrequestQuery = mRequest.getQueryString();
			
			if(lrequestQuery != null) {
				lrequestURI = lrequestURI.concat("?" + lrequestQuery);
			}
			
			mLog.info("Requested URI ", lrequestURI);
		}
		finally {
			Log.mdcRemove("remoteIP");
			Log.mdcRemove("sessionid");
			Log.mdcRemove("username");
		}
    	
    	mLog.debug("LogFilter end");
    }
}
