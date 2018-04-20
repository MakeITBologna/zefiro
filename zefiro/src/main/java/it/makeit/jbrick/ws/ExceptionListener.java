package it.makeit.jbrick.ws;

import it.makeit.jbrick.Log;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;


public class ExceptionListener implements ApplicationEventListener {

	@Override
	public void onEvent(ApplicationEvent event) {

	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		return new ExceptionRequestEventListener();
	}

	public static class ExceptionRequestEventListener implements RequestEventListener {

		private final Log mLog;

		public ExceptionRequestEventListener() {
			mLog = Log.getInstance(getClass());
		}

		@Override
		public void onEvent(RequestEvent event) {
			switch (event.getType()) {
			case ON_EXCEPTION:
				Throwable t = event.getException();
				mLog.error(event.getType(), t);
				t.printStackTrace();
			}
		}
	}

}
