package it.makeit.zefiro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.lang.StringUtils;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.addon.ZefiroProperties;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;
import it.makeit.zefiro.dao.BaseData;

public final class Util {

	private Util() {
	}

	public static AlfrescoConfig getDefaultAlfrescoConfig(String pUsername, String pPassword) {
		URL lUrlHost;
		try {
			lUrlHost = new URL(JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@host"));
		} catch (MalformedURLException e) {
			// FIXME (Alessio): definire una gestione degli errori per l'intera
			// applicazione...
			throw new JBrickException(e, JBrickException.FATAL);
		}

		String lStrUsername = pUsername;
		String lStrPassword = pPassword;
		String lStrRootFolderId = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@rootFolderId");
		boolean toProcess = Boolean.valueOf(JBrickConfigManager.getInstance().getMandatoryProperty("properties/@process"));
		boolean readOnly =  Boolean.valueOf(JBrickConfigManager.getInstance().getMandatoryProperty("properties/@readOnly"));
		//return new AlfrescoConfig(lUrlHost, lStrUsername, lStrPassword, lStrRootFolderId);
		return new AlfrescoConfig(lUrlHost, lStrUsername, lStrPassword, lStrRootFolderId, toProcess, readOnly);
	}

	public static AlfrescoConfig getUserAlfrescoConfig(HttpServletRequest httpRequest) {
		AlfrescoConfig aConfig = (AlfrescoConfig) httpRequest.getSession().getAttribute("alfrescoConfig");
		String acceptedLanguages = httpRequest.getHeader(javax.ws.rs.core.HttpHeaders.ACCEPT_LANGUAGE);
		if (StringUtils.isNotBlank(acceptedLanguages)) {
			aConfig.setAcceptedLanguages(acceptedLanguages);
		}
		return aConfig;
	}

	public static Session getUserAlfrescoSession(HttpServletRequest httpRequest) {
		return (Session) httpRequest.getSession().getAttribute("alfrescoSession");
	}

	/**
	 * @author Alba Quarto
	 * @param data
	 *            to decode
	 * @param decodeing
	 * @param decodingType
	 */
	public static void decodeField(BaseData data, Object decoding, DecodingType decodingType) {

		for (Method method : data.getClass().getMethods()) {

			if (!isSetter(method)) {
				continue;
			}

			DecodedFieldNote decodedFieldNote = method.getAnnotation(DecodedFieldNote.class);
			if (decodedFieldNote == null) {
				continue;
			}
			if (decodedFieldNote.decodingType() != decodingType) {
				continue;
			}

			String beanGetter = String.format("get%s", StringUtils.capitalize(decodedFieldNote.value()));

			try {
				method.invoke(data, decoding.getClass().getMethod(beanGetter).invoke(decoding));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				// TODO rilanciare una eccezione
			} catch (NoSuchMethodException ex) {
				continue;
			}

		}
	}

	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set")) {
			return false;
		}

		if (method.getParameterTypes().length == 0) {
			return false;
		}

		if (!void.class.equals(method.getReturnType())) {
			return false;
		}

		return true;
	}

	public static Map<String, Object> createPropertiesMapFromRequest(HttpServletRequest httpRequest) {
		Map<String, Object> properties = new HashMap<String, Object>();
		AlfrescoConfig config = getUserAlfrescoConfig(httpRequest);
		properties.put(ZefiroProperties.USER, config.getUsername());
		
		String remoteHost = httpRequest.getRemoteHost();
        String remoteAddr = httpRequest.getRemoteAddr();
        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            try {
				InetAddress localip = java.net.InetAddress.getLocalHost();
	            remoteAddr = localip.getHostAddress();
	            remoteHost = localip.getHostName();
			} catch (UnknownHostException e) {
				throw new JBrickException(e, JBrickException.FATAL);
			}
            
        }
        	
        properties.put(ZefiroProperties.REMOTE_ADDRESS, remoteAddr);
		properties.put(ZefiroProperties.REMOTE_HOST, remoteHost);
		
		
		return properties;
	}
}
