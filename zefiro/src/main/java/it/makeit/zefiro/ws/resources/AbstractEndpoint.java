package it.makeit.zefiro.ws.resources;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.jbrick.JBrickException;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.service.ZefiroAbstractServcie;

public class AbstractEndpoint {
	@Context
	protected HttpServletRequest httpRequest;

	protected ZefiroAbstractServcie getServiceInstance(Class<? extends ZefiroAbstractServcie> sClass) {
		AlfrescoConfig pConfig = Util.getUserAlfrescoConfig(httpRequest);
		try {
			httpRequest.setCharacterEncoding("UTF-8");
			return sClass.getDeclaredConstructor(AlfrescoConfig.class).newInstance(pConfig);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | UnsupportedEncodingException e) {
			throw new JBrickException(e, JBrickException.REFLECTION_EXCEPTION);
		}
	}
}
