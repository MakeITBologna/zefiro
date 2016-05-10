package it.makeit.zefiro;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.client.api.Session;


public final class Util {

	private Util() {}

	public static AlfrescoConfig getDefaultAlfrescoConfig(String pUsername, String pPassword) {

		URL lUrlHost;
		try {
			lUrlHost = new URL(JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@host"));
		} catch (MalformedURLException e) {
			// FIXME (Alessio): definire una gestione degli errori per l'intera applicazione...
			throw new JBrickException(e, JBrickException.FATAL);
		}

		String lStrUsername = pUsername;
		String lStrPassword = pPassword;
		String lStrRootFolderId = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@rootFolderId");

		
		return new AlfrescoConfig(lUrlHost, lStrUsername, lStrPassword, lStrRootFolderId);
	}

	public static AlfrescoConfig getUserAlfrescoConfig(HttpServletRequest httpRequest){
		return (AlfrescoConfig) httpRequest.getSession().getAttribute("alfrescoConfig");
	}

	public static Session getUserAlfrescoSession(HttpServletRequest httpRequest){
		return (Session) httpRequest.getSession().getAttribute("alfrescoSession");
	}
}
