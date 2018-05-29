package it.makeit.alfresco;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.makeit.alfresco.workflow.AlfrescoWorkflowException;
import it.makeit.alfresco.workflow.ResponseBodyPartEnum;
import it.makeit.alfresco.workflow.model.AlfrescoError;

public class BaseAlfrescoHelper {
	protected static final Logger mLog = LoggerFactory.getLogger(AlfrescoHelper.class);

	private static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	protected static Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();

	// UTILS METHODS BELOW
	protected static <T> T loadObject(HttpRequestFactory pHttpRequestFactory, GenericUrl url, Class<T> clz) {
		return parse(ask(pHttpRequestFactory, url, HttpMethods.GET), ResponseBodyPartEnum.ENTRY.partName(), clz);
	}

	protected static <T> List<T> loadList(HttpRequestFactory pHttpRequestFactory, GenericUrl url, Class<T> clz) {
		return parseList(ask(pHttpRequestFactory, url, HttpMethods.GET), clz);
	}

	protected static <T> T update(HttpRequestFactory pHttpRequestFactory, GenericUrl url, T body, Class<T> clz) {

		HttpContent content = new GsonHttpContent(gson, body);
		return parse(ask(pHttpRequestFactory, url, HttpMethods.PUT, content), ResponseBodyPartEnum.ENTRY.partName(),
				clz);
	}

	protected static <T> T insert(HttpRequestFactory pHttpRequestFactory, GenericUrl url, T body, Class<T> clz) {
		HttpContent content = new GsonHttpContent(gson, body);
		return parse(ask(pHttpRequestFactory, url, HttpMethods.POST, content), ResponseBodyPartEnum.ENTRY.partName(),
				clz);
	}

	protected static <T> List<T> insertList(HttpRequestFactory pHttpRequestFactory, GenericUrl url, List<T> body,
			Class<T> clz) {
		HttpContent content = new GsonHttpContent(gson, body);
		return parseList(ask(pHttpRequestFactory, url, HttpMethods.POST, content), clz);
	}

	protected static String ask(HttpRequestFactory pHttpRequestFactory, GenericUrl url, String method) {
		HttpContent content = new GsonHttpContent(gson, "");
		return ask(pHttpRequestFactory, url, method, content);
	}

	protected static String ask(HttpRequestFactory pHttpRequestFactory, GenericUrl url, String method,
			HttpContent content) {
		try {
			HttpRequest request;
			switch (method) {
			case HttpMethods.PUT:
				request = pHttpRequestFactory.buildPutRequest(url, content);
				break;
			case HttpMethods.POST:
				request = pHttpRequestFactory.buildPostRequest(url, content);
				break;
			case HttpMethods.DELETE:
				request = pHttpRequestFactory.buildDeleteRequest(url);
				break;
			default:
				request = pHttpRequestFactory.buildGetRequest(url);
			}

			debugRequest(request);

			HttpResponse response = request.execute();

			String responseAsString = response.parseAsString();

			debugResponse(response, responseAsString);

			return responseAsString;
		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), ResponseBodyPartEnum.ERROR.partName(), AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {
			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	protected static void debugRequest(HttpRequest req) {
		switch (req.getRequestMethod()) {
		case HttpMethods.GET: {
			mLog.debug("REQUEST: " + req.getRequestMethod() + " " + req.getUrl());
			break;
		}
		case HttpMethods.POST: {
			mLog.debug("REQUEST: " + req.getRequestMethod() + " " + req.getUrl());
			mLog.debug("REQUEST BODY: " + req.getRequestMethod() + " " + req.getContent());
			break;
		}
		case HttpMethods.PUT: {
			mLog.debug("REQUEST: " + req.getRequestMethod() + " " + req.getUrl());
			mLog.debug("REQUEST BODY: " + req.getRequestMethod() + " " + req.getContent());
			break;
		}
		default: {
			throw new IllegalArgumentException(req.getRequestMethod() + " non supported for logging.");
		}
		}

	}

	protected static void debugResponse(HttpResponse response, String responseAsString) {
		mLog.debug("RESPONSE " + response.getStatusCode() + " " + responseAsString);
	}

	protected static <T> T parse(String json, String key, Class<T> clz) {
		if (key == null) {
			return gson.fromJson(json, clz);
		} else {
			JsonObject obj = gson.fromJson(json, JsonObject.class);
			JsonObject delegate = obj.getAsJsonObject(key);
			return gson.fromJson(delegate, clz);
		}
	}

	protected static <T> List<T> parseList(String lResponse, Class<T> clz) {
		List<T> resList = new LinkedList<>();
		JsonObject obj = gson.fromJson(lResponse, JsonObject.class);
		JsonObject list = obj.getAsJsonObject(ResponseBodyPartEnum.LIST.partName());
		if (list != null) {
			JsonArray entries = list.getAsJsonArray(ResponseBodyPartEnum.ENTRIES.partName());
			for (JsonElement entry : entries) {
				resList.add(parse(entry.toString(), ResponseBodyPartEnum.ENTRY.partName(), clz));
			}
		}
		return resList;
	}

	public static void setDefaultDateFormat(String dateFormat) {
		// per cambiare il default dateformat nel caso fosse configurato
		// diversamento all'interno di Alfresco
		BaseAlfrescoHelper.dateFormat = dateFormat;
		gson = new GsonBuilder().setDateFormat(dateFormat).create();
	}

	protected static Map<String, Object> buildParams(Map<String, Object> pParams) {
		if (pParams == null) {
			pParams = new HashMap<String, Object>();
		}
		return pParams;
	}
}