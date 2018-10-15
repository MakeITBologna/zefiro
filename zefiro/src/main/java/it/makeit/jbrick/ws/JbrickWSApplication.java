package it.makeit.jbrick.ws;

import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import it.makeit.alfresco.addon.DoNothingDocumentListener;
import it.makeit.alfresco.addon.DoNothingUserSessionListener;
import it.makeit.alfresco.addon.DocumentListenener;
import it.makeit.alfresco.addon.UserSessionListener;
import it.makeit.alfresco.addon.ZefiroExtension;
import it.makeit.jbrick.ws.filters.AuthenticationFilter;
import it.makeit.jbrick.ws.filters.JbrickExceptionMapper;
import it.makeit.jbrick.ws.filters.LogFilter;


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
		
		register(new SdkBinder());
		
		
	}

	public static class SdkBinder extends AbstractBinder {

		@Override
		protected void configure() {
			Reflections reflections = new Reflections();

			Set<Class<? extends DocumentListenener>> documentListenerClasses = reflections.getSubTypesOf(DocumentListenener.class);
			
			documentListenerClasses = Sets.filter(documentListenerClasses, zefiroExtension());
			
			switch(documentListenerClasses.size()) {
				case 0: {bind(DoNothingDocumentListener.class).to(DocumentListenener.class); break;	} // dafult do nothing
				case 1: {bind(documentListenerClasses.iterator().next()).to(DocumentListenener.class); break;} // zefiro extension
				default: {throw new IllegalArgumentException("Sono stati definiti 2 o più DocumentListenener");	} // errore
			}
			
			
			
			
			Set<Class<? extends UserSessionListener>> userSessionListenerClasses = reflections.getSubTypesOf(UserSessionListener.class);
			
			userSessionListenerClasses = Sets.filter(userSessionListenerClasses, zefiroExtension());
			
			switch(userSessionListenerClasses.size()) {
				case 0: {bind(DoNothingUserSessionListener.class).to(UserSessionListener.class); break;	} // dafult do nothing
				case 1: {bind(userSessionListenerClasses.iterator().next()).to(UserSessionListener.class); break;} // zefiro extension
				default: {throw new IllegalArgumentException("Sono stati definiti 2 o più UserSessionListener");	} // errore
			}
			
		}
		
	}
	
	
	private static <T> Predicate<Class<? extends T>> zefiroExtension(){
		return new Predicate<Class<? extends T>>() {
			@Override
			public boolean apply(Class<? extends T> c) {
				return c.isAnnotationPresent(ZefiroExtension.class);
			}
		};
	}
	
}
