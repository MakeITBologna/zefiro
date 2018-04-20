package it.makeit.jbrick.ws;

import it.makeit.zefiro.ws.ZefiroModule;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;


@Provider
@Produces("application/json")
public class JbrickJacksonConfigurator implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper = new ObjectMapper();

	public JbrickJacksonConfigurator() {
		// Serializzazione date in formato ISO-8601: YYYY-MM-DDThh:mm:ss.sss+hhmm
		// v. http://stackoverflow.com/a/5234682/1178235
		// v. http://wiki.fasterxml.com/JacksonFAQDateHandling
		mapper.setDateFormat(new ISO8601DateFormat());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Registrazione del modulo dei serializzatori personalizzati
		mapper.registerModule(new ZefiroModule());
	}

	@Override
	public ObjectMapper getContext(Class<?> arg0) {
		return mapper;
	}

}
