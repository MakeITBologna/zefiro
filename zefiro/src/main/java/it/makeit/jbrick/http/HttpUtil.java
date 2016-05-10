/**
 * 
 */
package it.makeit.jbrick.http;

import it.makeit.jbrick.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author mcallegari
 *
 */
public class HttpUtil {
	
	// logger
	private static Log mLog = Log.getInstance(HttpUtil.class);
	
	private static byte[] retrieveContent(String pStrUrl, NameValuePair[] pQueryParams) throws IOException {
		mLog.debug("retrieveContent START");
		//Non posso essere chiamato con un null!
		if(pStrUrl == null) throw new IllegalArgumentException("Null argument not allowed!!");
		byte[] lResponseBody = null;
		int lIntStatusCode = 1;
		// Create an instance of HttpClient.
		HttpClient lHttpClient = new HttpClient();
		// Create a method instance.
		HttpMethod lHttpMethod = new GetMethod(pStrUrl);
		// Setto i parametri di query
		if (pQueryParams!= null) lHttpMethod.setQueryString(pQueryParams);
		// Execute the method.
		try {
			lIntStatusCode = lHttpClient.executeMethod(lHttpMethod);
		} catch (HttpException e) {
			mLog.error(e);
			throw new IOException(e.getMessage());
		} catch (IOException e) {
			mLog.error(e);
			throw e;
		} 
		//Ma il server potrebbe rispondere.. con un 404, ...
		if (lIntStatusCode != HttpStatus.SC_OK) {
			mLog.error("Bad response from server, httpstatus=",lIntStatusCode+"");
			lHttpMethod.releaseConnection();
			throw new IOException("Bad response from server, httpstatus=" + lIntStatusCode);
		}

		// Read the response body.
		try {
			lResponseBody = lHttpMethod.getResponseBody();
		} catch (IOException e) {
			mLog.error(e);
			throw e;
		} finally {
			lHttpMethod.releaseConnection();
		}
		mLog.debug("retrieveContent FINISH");
		return lResponseBody;
	}

	public static byte[] getContent(String pStrUrl) throws IOException {
		mLog.debug("getContent START with URL ", pStrUrl);
		//Non posso essere chiamato con un null!
		if(pStrUrl == null) throw new IllegalArgumentException("Null argument not allowed!!");
		return retrieveContent(pStrUrl, null);
	}

	public static byte[] getContent(String pStrUrl, Map<String,String[]> pParametersMap) throws IOException {
		mLog.debug("getContent START with URL ", pStrUrl);
		//Non posso essere chiamato con un null!
		if(pStrUrl == null || pParametersMap == null) throw new IllegalArgumentException("Null argument not allowed!!");
		return retrieveContent(pStrUrl, buildNameValuePair(pParametersMap));
	}

	private static NameValuePair[] buildNameValuePair(
			Map<String, String[]> pParametersMap) {
		mLog.debug("buildNameValuePair START");
		//Non posso essere chiamato con un null!
		if(pParametersMap == null) throw new IllegalArgumentException("Null argument not allowed!!");
		ArrayList<NameValuePair> lNameValuePairs = new ArrayList<NameValuePair>(pParametersMap.size());
		Set<Map.Entry<String, String[]>> lSet = pParametersMap.entrySet();
		Iterator<Map.Entry<String, String[]>> lIterator = lSet.iterator();
		while (lIterator.hasNext())
		{
			Map.Entry<String, String[]> lEntry = lIterator.next();
		    String key = lEntry.getKey();
		    String[] value =  lEntry.getValue();
		    for (int i=0; i<value.length; i++) {
		    	lNameValuePairs.add(new NameValuePair(key,value[i]));
			    mLog.debug("added parameter key=",key," with value=",value[i]);
		    }
		}		
		mLog.debug("buildNameValuePair FINISH");
		return lNameValuePairs.toArray(new NameValuePair[lNameValuePairs.size()]);
	}
}
