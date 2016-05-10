package it.makeit.jbrick.ws;

import it.makeit.jbrick.ws.filters.AuthenticationFilter;
import it.makeit.jbrick.ws.filters.JbrickExceptionMapper;
import it.makeit.jbrick.ws.filters.LogFilter;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * Definizione dell'applicazione JAX-RS relativa alle funzionalità di JBrick
 * 
 * @author alessio
 * 
 */
@ApplicationPath("/a/*")
public class JbrickWSApplication extends ResourceConfig {

	public JbrickWSApplication() {
		// Registrazione del package contenente le risorse
		packages("it.makeit.jbrick.ws.resources"); 
		packages("it.makeit.profiler.ws.resources");
		packages("it.makeit.jbrick.configdb.ws.resources");
		packages("it.makeit.jbrick.scheduler.ws.resources");
		packages("it.makeit.zefiro.ws.resources");

		// Registrazione di jackson-media-provider e configurazione serializzatore
		register(JacksonFeature.class);
		register(JbrickJacksonConfigurator.class);

		// Registrazione del listener delle eccezioni
		register(ExceptionListener.class);
		
		// Registrazione exception mapper
		register(JbrickExceptionMapper.class);
		
		// Registrazione Multipart Feature per upload file
		register(MultiPartFeature.class);
		
		// Registrazione filtri
		register(AuthenticationFilter.class);
		register(LogFilter.class);
	}

}
