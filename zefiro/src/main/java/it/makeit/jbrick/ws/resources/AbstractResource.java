package it.makeit.jbrick.ws.resources;

import it.makeit.jbrick.Constants;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public abstract class AbstractResource {

	public AbstractResource() {
		super();
	}

	protected Integer getIntegerId(String pStrId) {
		Integer lIntgrHeaderId = null;
		try {
			lIntgrHeaderId = Integer.valueOf(pStrId);
		} catch (NumberFormatException e) {
			// FIXME: definire la gestione degli errori
			throw new NotFoundException(Response.status(Status.NOT_FOUND)
			        .entity("{\"error\":\"Risorsa non esistente\"}")
			        .type(MediaType.APPLICATION_JSON).build());
		}

		return lIntgrHeaderId;
	}

	protected Long getLongId(String pStrId) {
		Long lLongTaskId = null;
		try {
			lLongTaskId = Long.valueOf(pStrId);
		} catch (NumberFormatException e) {
			// FIXME: definire la gestione degli errori
			throw new NotFoundException(Response.status(Status.NOT_FOUND)
			        .entity("{\"error\":\"Risorsa non esistente\"}")
			        .type(MediaType.APPLICATION_JSON).build());
		}
		return lLongTaskId;
	}
	
	protected String getLocalizedMessage(Locale pLocale, String pKey, Object... pArgs) {
		ResourceBundle lResourceBundle = ResourceBundle.getBundle(Constants.RESOURCEBUNDLE, pLocale);
		MessageFormat lMessageFormat = new MessageFormat(lResourceBundle.getString(pKey));
		return lMessageFormat.format(pArgs);
	}

}
