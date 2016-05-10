/**
 * 
 */
package it.makeit.jbrick.web;

import it.makeit.jbrick.Log;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author frametta
 *
 */
public class RequestUtil {
	
	// logger
	private static Log mLog=Log.getInstance(RequestUtil.class);
	
	 /**
     * Funzione che prova a reperire il bean identificato dal beanName, passato come informazione, 
     * dai tre possibili scope: request, session o application.
     * In caso di esito negativo viene restituito un valore null.
     * 
     * @param pRequest Request da cui ricavare i parametri di lavoro.
     * @param pStrBeanName nome del parametro
     * @return Bean rilevato 
     */
	public static Object getBean(HttpServletRequest pRequest, String pStrBeanName) 
	throws IllegalStateException, IllegalArgumentException
	{
		if(pRequest!=null)
		{
			Object lObjBeanToRetrieve=null;
			
			//Provo a reperire il bean dallo scope di request
			lObjBeanToRetrieve = pRequest.getAttribute(pStrBeanName);
			if (lObjBeanToRetrieve != null) {
				mLog.debug("bean ",pStrBeanName," correctly retrieved from request rescope.");
				return lObjBeanToRetrieve;
			}
			
			//Provo reperire il bean dallo scope di session
			lObjBeanToRetrieve = pRequest.getSession().getAttribute(pStrBeanName);
			if (lObjBeanToRetrieve != null) {
				mLog.debug("bean ",pStrBeanName," correctly retrieved from session scope.");
				return lObjBeanToRetrieve;
			}

			//Provo reperire il bean dallo scope di application
			lObjBeanToRetrieve = pRequest.getSession().getServletContext().getAttribute(pStrBeanName);
			if (lObjBeanToRetrieve != null) {
				mLog.debug("bean ",pStrBeanName," correctly retrieved from application scope.");
				return lObjBeanToRetrieve;
			} 
			mLog.debug("bean ",pStrBeanName," not found in any scope.");
			return lObjBeanToRetrieve;
		}
		else
		{
			mLog.error("request has null value.");
			// rilancio l'eccezione a livello superiore
			throw new IllegalArgumentException("request has null value.");
		}
	}

	/**
     * Funzione che memorizza il beanToStore passato nello scope appropriato, in
     * base al parametro beanScope passato come informazione. Il 
     * beanToStore viene memorizzato come attribute utilizzando la key 
     * specificata tramite il parametro beanName passato come informazione.
     * 
     * @param pObjBeanToStore Bean da memorizzare nello scope appropriato con la
     * chiave appropriata.
     * @param pHttpServletRequest Request da cui ricavare i parametri necessari 
     * all'operazione richiesta.
     * @param pStrBeanScope Scope in cui memorizzare il Bean
     * @param pStrBeanName La chiave con cui memorizzare il Bean
     * 
     * @throws IllegalArgumentException
     */
    public static void setBean(Object pObjBeanToStore, HttpServletRequest pHttpServletRequest,
    		WebScope pWebScope, String pStrBeanName)
    	throws IllegalArgumentException
    {

    	if(pObjBeanToStore!=null && pHttpServletRequest!=null && pWebScope!=null && pStrBeanName!=null)
    	{
	    	
	    	if(pWebScope.equals(WebScope.REQUEST))
	    	{
	    		// memorizzo il bean passato nello scope di request
	    		pHttpServletRequest.setAttribute(pStrBeanName, pObjBeanToStore);
	    	}
	    	else if(pWebScope.equals(WebScope.SESSION))
	    	{
	    		// memorizzo il bean passato nello scope di session
	    		pHttpServletRequest.getSession().setAttribute(pStrBeanName, pObjBeanToStore);
	    	}
	    	else if(pWebScope.equals(WebScope.APPLICATION))
	    	{
	    		// memorizzo il bean passato nello scope di session
	    		pHttpServletRequest.getSession().getServletContext().setAttribute(pStrBeanName, pObjBeanToStore);
	    		
	    	}
	    	mLog.debug(pStrBeanName, " correctly stored in ", pWebScope.toString(), " scope.");
    	}
    	else
    	{
    		mLog.error("beanToStore or request has null value.");
    		throw new IllegalArgumentException(
    			"beanToStore or request has null value.");
    	}
    }
    
    public static String getHeaders(HttpServletRequest pHttpServletRequest) throws IllegalArgumentException {
    	//Vediamo se la chiamata ha senso!
    	if(pHttpServletRequest==null) {
    		mLog.error("pHttpServletRequest has null value.");
    		throw new IllegalArgumentException("beanToStore or request has null value.");
    	}
    	StringBuffer lStringBuffer = new StringBuffer();
	    //Dump degli headers
		Enumeration<?> lEnumHeaderNames = pHttpServletRequest.getHeaderNames();
	    while(lEnumHeaderNames.hasMoreElements()) {
	      String headerName = (String)lEnumHeaderNames.nextElement();
	      lStringBuffer.append(headerName);
	      lStringBuffer.append("=");
	      lStringBuffer.append(pHttpServletRequest.getHeader(headerName));
	      lStringBuffer.append(" ");
	    }
	    return lStringBuffer.toString();
    }
}
