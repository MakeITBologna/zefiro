package it.makeit.jbrick.http;

import it.makeit.jbrick.Log;
import it.makeit.jbrick.web.LocaleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

	private static Log mLog = Log.getInstance(SessionUtil.class);
	
	/**
	 * Verifica se la sessione è completa
	 * @param pHttpServletRequest
	 * @return
	 */
	/**
	 * @param pHttpServletRequest
	 * @return
	 */
	public static boolean isSessionComplete(HttpServletRequest pHttpServletRequest) {
		mLog.debug("ENTER isSessionComplete");
		boolean lBol = true;
		if (pHttpServletRequest.getRequestedSessionId()==null) { //Partenza!
			mLog.debug("The requested session id is null. We suppose a fresh start");
			lBol = false;
		}
		else {
			mLog.debug("A specific session is requested: ", pHttpServletRequest.getRequestedSessionId());
			if (!pHttpServletRequest.isRequestedSessionIdValid()) { //la sessione richiesta non è valida
				mLog.debug("The requested session is not valid");
				lBol = false;
			}
			else {
				//FIXME: in Tomcat, se riparte il server, finisco qui!!??
				mLog.debug("The requested session looks complete. anyway I check the locale");
				if (LocaleUtil.getLocale(pHttpServletRequest.getSession()) == null) {
					mLog.info("The  session looks complete. But the locale is null! Tomcat Restart?");
					LocaleUtil.setDefaultLocale(pHttpServletRequest);
				}
				mLog.debug("The requested session is really complete");
			}
		}
		mLog.debug("FINISH isSessionComplete returning ",lBol+"");
		return lBol;
	}

	/**
	 * Completa la sessione
	 * @param pHttpServletRequest
	 */
	public static final void completeSession(HttpServletRequest pHttpServletRequest) {
		mLog.debug("ENTER completeSession");
		//Creo la sessione
		HttpSession lHttpSession = pHttpServletRequest.getSession(true);
		//Setto la default locale
		LocaleUtil.setDefaultLocale(pHttpServletRequest);
		
				    	
    	// Inserisco i patterns a livello di sessione
		ResourceBundle lResource = ResourceBundle.getBundle("ApplicationResources");
		Locale lLocale = LocaleUtil.getLocale(lHttpSession);
		
		lHttpSession.setAttribute("locale", lLocale);
		
		if (lLocale != null) {
			lHttpSession.setAttribute("localePatternInteger",LocaleUtil.getLocalizedNumberPattern(lResource.getString("pattern.integer"),lLocale));
			lHttpSession.setAttribute("localePatternDecimal",LocaleUtil.getLocalizedNumberPattern(lResource.getString("pattern.decimal"),lLocale));
			lHttpSession.setAttribute("localePatternDate",LocaleUtil.getLocalizedDatePattern(lResource.getString("pattern.date"),lLocale));
			lHttpSession.setAttribute("localePatternTime",LocaleUtil.getLocalizedDatePattern(lResource.getString("pattern.time"),lLocale));
			lHttpSession.setAttribute("localePatternTimestamp",LocaleUtil.getLocalizedDatePattern(lResource.getString("pattern.timestamp"),lLocale));
		}
		
		//Sistemiamo il basepath
		String lStrBasePath = pHttpServletRequest.getScheme()+"://"+pHttpServletRequest.getServerName()+":"+pHttpServletRequest.getServerPort()+pHttpServletRequest.getContextPath() + "/";
		
		if (pHttpServletRequest.getHeader("X-Forwarded-Host") != null) {
			String lStrProto = pHttpServletRequest.getHeader("X-Forwarded-Proto") != null ? pHttpServletRequest.getHeader("X-Forwarded-Proto") : "http";
			String lStrPort = pHttpServletRequest.getHeader("X-Forwarded-Port") != null ? ":" + pHttpServletRequest.getHeader("X-Forwarded-Port") : "";
			
			StringBuilder lStringBuilder = new StringBuilder();
			lStringBuilder
					.append(lStrProto).append("://")
					.append(pHttpServletRequest.getHeader("X-Forwarded-Host"))
					.append(lStrPort)
					.append(pHttpServletRequest.getContextPath()).append("/");
			
			lStrBasePath = lStringBuilder.toString();
		}
		
		
		lHttpSession.setAttribute("jbBasePath",lStrBasePath);
		
		mLog.debug("FINISH completeSession");
	}
	
}
