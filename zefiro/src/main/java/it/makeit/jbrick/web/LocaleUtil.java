package it.makeit.jbrick.web;

import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

/**
 * Provides a centralized handling of Locale.
 * 
 * <p>-- Unified Locale handling mechanism which
 * is able to spread Locale setting to 
 * <li>JSTL
 * <li>{@link BeanFilter}
  * and to mantain them coherent during all execution.
 * 
 * <p>-- Hide Locale real code.
 * 
 * <p>-- Provide adeguate set, get, read methods. Use them, plz.
 * 
 * <p>-- Implements Fallback to Default values 
 * 
 * @author tbinci
 * @version 1.0
 */
public final class LocaleUtil {
	
	/* IMPLEMENTATION DESCRIPTION
	 * 
	 * v1.0:
	 * - put a reference in Session, locale is coherent for all calls of curr user.
	 * - the reference used has key=FMT_LOCALE (from JSTL): easyness!
	 * - Struts is currently out-of-project. we don't care of.
	 * - default values are stored in web.xml as context-param: they are stable during app run.
	 */
	
	private static Log mLog=Log.getInstance(LocaleUtil.class);

	private static final String INIT_PARAM_DEFAULT_LOCALE = "default-locale";
	
	/**
	 * Ritorna locale di default
	 * 
	 * @param httpSession
	 */
	public static Locale getDefaultLocale(HttpServletRequest pHttpServletRequest) {
		if (pHttpServletRequest==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER getDeaultLocale(Session)");
		//String lStrDefaultLocale = pHttpSession.getServletContext().getInitParameter(INIT_PARAM_DEFAULT_LOCALE);
		//Locale lLocale = countryToLocale(lStrDefaultLocale);
		//Ottengo la default locale dalla request non la prendo più dal web.xml
		Locale lLocale = pHttpServletRequest.getLocale();
		//LocaleUtil.setLocale(lLocale, lHttpSession);
		mLog.debug("FINISH getDeaultLocale(Session)");
		return lLocale;
	}

	/**
	 * Setta a session scope la locale di default con chiave di attributo coerente a JSTL
	 * 
	 * @param httpSession
	 */
	public static void setDefaultLocale(HttpServletRequest pHttpServletRequest){
		if (pHttpServletRequest==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER setDeaultLocale(Session)");
		Locale lLocale = getDefaultLocale(pHttpServletRequest);
		setLocale(lLocale, pHttpServletRequest.getSession());
		mLog.debug("FINISH setDeaultLocale(Session)");
	}

	/**
	 * Setta a session scope la locale passata con chiave di attributo coerente a JSTL
	 * @param pLocale
	 * @param pHttpSession
	 */
	public static void setLocale(Locale pLocale, HttpSession pHttpSession) {
		if (pHttpSession==null || pLocale==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER setLocale(Session) con locale ",pLocale.toString());
		Config.set(pHttpSession, Config.FMT_LOCALE, pLocale);
		mLog.debug("FINISH setLocale(Session)");
	}

	/**
	 * Ritorna la Locale attualmente valida per quella sessione.
	 * Viene ipotizzata la solita coerenza con JSTL.
	 * Ritorna null nel caso la sessione non abbia una locale settata. 
	 * 
	 * @param httpSession
	 */
	public static Locale getLocale(HttpSession pHttpSession) {
		if (pHttpSession==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER getLocale(Session)");
		Locale lLocale = (Locale) Config.get(pHttpSession, Config.FMT_LOCALE);
		if (lLocale != null) 
			mLog.debug("EXIT getLocale(Session) returning ",lLocale.toString());
		else mLog.debug("EXIT getLocale(Session) returning null");
		return lLocale;
	}

	/**
	 * From a country (as String, e.g: ITALY) to Locale object (es: it_IT)
	 * 
	 * @return <code>null</code> if not-parsable.
	 * @param pCountryCode: a String representing a country name. e.g: UK
	 */
	private static Locale countryToLocale(String pCountryCode) {
		if (pCountryCode==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER countryToLocale with country code ", pCountryCode);
		Locale lLocale;
		try {
			lLocale = (Locale) Locale.class.getField(pCountryCode).get(Locale.US);
		} catch (IllegalArgumentException e) {
			mLog.error(e);
			throw new JBrickException(e, JBrickException.FATAL);
		} catch (SecurityException e) {
			mLog.error(e);
			throw new JBrickException(e, JBrickException.FATAL);
		} catch (IllegalAccessException e) {
			mLog.error(e);
			throw new JBrickException(e, JBrickException.FATAL);
		} catch (NoSuchFieldException e) {
			mLog.error(e);
			throw new JBrickException(e, JBrickException.FATAL);
		}
		mLog.debug("FINISH countryToLocale returning ", lLocale.toString());
		return lLocale; 
	}
	
	public static String getLocalizedDatePattern(String pStrPattern, Locale pLocale) {
		if (pStrPattern==null || pLocale==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER getLocalizedDatePattern ", pStrPattern, " in ", pLocale.toString());
		String lStrLocalizedPattern = new SimpleDateFormat(pStrPattern, pLocale).toLocalizedPattern();
		mLog.debug("FINISH getLocalizedDatePattern returning ", lStrLocalizedPattern);
		return lStrLocalizedPattern; 
	}	
	public static String getLocalizedNumberPattern(String pStrPattern, Locale pLocale) {
		if (pStrPattern==null || pLocale==null) throw new JBrickException(JBrickException.FATAL);
		mLog.debug("ENTER getLocalizedDatePattern ", pStrPattern, " in ", pLocale.toString());
		DecimalFormat lDecimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance(pLocale);
		lDecimalFormat.applyPattern(pStrPattern);
		String lStrLocalizedPattern = lDecimalFormat.toLocalizedPattern();
		mLog.debug("FINISH getLocalizedDatePattern returning ", lStrLocalizedPattern);
		return lStrLocalizedPattern; 
	}	
}
