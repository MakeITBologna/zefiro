package it.makeit.alfresco.workflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.RenditionKinds;
import it.makeit.alfresco.restApi.AlfrescoApiPath;
import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;
import it.makeit.zefiro.Util;

/**
 * @author Alseny Cisse
 */
public final class AlfrescoRendition {

	private static final com.google.api.client.json.JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final Log mLog = Log.getInstance(AlfrescoRendition.class);
	public static final int RENDITION_ALREADY_EXISTS = 409;

	/**
	 * For Alfresco 5.2.* and next
	 *
	 * @author Alseny Cisse
	 * @param docRef
	 *            document reference
	 * @param httpRequest
	 * @return statusCode
	 * @throws IOException
	 */
	public static int createRendition(String docRef, HttpServletRequest httpRequest) {
		mLog.debug("creating rendition");
		AlfrescoConfig alfrescoConfig = Util.getUserAlfrescoConfig(httpRequest);

		HttpRequestFactory httpRequestFactory = AlfrescoHelper.getRequestFactory(alfrescoConfig);
		HttpContent content = new JsonHttpContent(JSON_FACTORY, content());
		HttpRequest request = null;
		HttpResponse response = null;
		int status = -1;
		try {
			request = httpRequestFactory.buildPostRequest(buildUrl(docRef, alfrescoConfig), content)
					.setHeaders(headers());
			status = request.execute().getStatusCode();
		} catch (IOException e) {
			HttpResponseException httpResponseException = (HttpResponseException) e;
			status = httpResponseException.getStatusCode();
		} finally {
			handleDisconnect(response);
		}
		mLog.debug("End creating rendition");
		return status;
	}

	private static void handleDisconnect(HttpResponse response) {
		if (response != null) {
			try {
				response.disconnect();
			} catch (IOException e) {
				mLog.debug("End creating rendition with error");
				throw new JBrickException(e, JBrickException.IO_EXCEPTION);
			}
		}
	}

	private static Map<String, String> content() {
		Map<String, String> data = new HashMap<>();
		data.put("id", RenditionKinds.PDF);
		return data;
	}

	private static HttpHeaders headers() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(MediaType.APPLICATION_JSON).setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}

	private static GenericUrl buildUrl(String docRef, AlfrescoConfig alfrescoConfig) {
		GenericUrl url = new GenericUrl(alfrescoConfig.getHost());
		url.appendRawPath(AlfrescoApiPath.ALFRESCO.getPath());
		url.appendRawPath("/nodes/");
		url.appendRawPath(docRef);
		url.appendRawPath("/renditions");
		return url;
	}	
	
}
