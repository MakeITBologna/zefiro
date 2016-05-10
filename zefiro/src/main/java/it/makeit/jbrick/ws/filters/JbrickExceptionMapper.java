package it.makeit.jbrick.ws.filters;

import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class JbrickExceptionMapper implements ExceptionMapper<Exception> {
	
	private static Log mLog = Log.getInstance(JbrickExceptionMapper.class);

	@Override
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response toResponse(Exception pException) {
		
		mLog.debug("JbrickExceptionMapper start");
		
		JBrickException lJBrickException;
		
		if (pException instanceof JBrickException) {
			lJBrickException = (JBrickException) pException;
			mLog.error("Caught JBrickException"); //Non la loggo perchè è nostra e deve essere già stata loggata!
		} else {
			lJBrickException = new JBrickException(pException, "jBrickException.errorFilter.unhandledException", pException.getMessage());
			mLog.error(pException, "Caught Unhandled Exception");
		}
		
		mLog.debug("JbrickExceptionMapper end");
		
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(lJBrickException).build();
    }
}
