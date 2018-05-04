package it.makeit.alfresco.workflow;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.GenericUrl;
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

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoException;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.restApi.GenericUrlFactory;
import it.makeit.alfresco.workflow.enties.DeploymentsUrl;
import it.makeit.alfresco.workflow.enties.ImageUrl;
import it.makeit.alfresco.workflow.enties.ProcessDefinitionsUrl;
import it.makeit.alfresco.workflow.enties.ProcessesUrl;
import it.makeit.alfresco.workflow.enties.TasksUrl;
import it.makeit.alfresco.workflow.enties.VariablesUrl;
import it.makeit.alfresco.workflow.model.AlfrescoError;
import it.makeit.alfresco.workflow.model.Deployment;
import it.makeit.alfresco.workflow.model.ProcessDefinition;
import it.makeit.alfresco.workflow.model.Task;
import it.makeit.alfresco.workflow.model.WorkflowProcess;

public class AlfrescoWorkflowHelper {

	private static final Logger mLog = LoggerFactory.getLogger(AlfrescoHelper.class);

	private static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();

	public static List<Deployment> getDeployments(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		return getDeployments(pHttpRequestFactory, pConfig, null);
	}

	public static List<Deployment> getDeployments(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new DeploymentsUrl(pConfig.getHost()))).build(buildParams(pParams));

		try {
			HttpRequest request = pHttpRequestFactory.buildGetRequest(url);
			debugRequest(request);

			HttpResponse response = request.execute();
			String responseAsString = response.parseAsString();

			debugResponse(response, responseAsString);

			return parseList(responseAsString, Deployment.class);

		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), "error", AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {

			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static List<WorkflowProcess> getProcesses(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		return getProcesses(pHttpRequestFactory, pConfig, null);
	}

	public static List<WorkflowProcess> getProcesses(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new ProcessesUrl(pConfig.getHost()))).build(buildParams(pParams));

		try {
			HttpRequest request = pHttpRequestFactory.buildGetRequest(url);

			debugRequest(request);

			HttpResponse response = request.execute();
			String responseAsString = response.parseAsString();

			debugResponse(response, responseAsString);

			return parseList(responseAsString, WorkflowProcess.class);

		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), "error", AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {

			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static List<Task> getTasks(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		return getTasks(pHttpRequestFactory, pConfig, null);
	}

	public static List<Task> getTasks(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new TasksUrl(pConfig.getHost()))).build(buildParams(pParams));

		try {
			HttpRequest request = pHttpRequestFactory.buildGetRequest(url);

			debugRequest(request);

			HttpResponse response = request.execute();
			String responseAsString = response.parseAsString();

			debugResponse(response, responseAsString);

			return parseList(responseAsString, Task.class);

		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), "error", AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {

			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static List<Task> getTaskVariables(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			String taskId) {
		return getTaskVariables(pHttpRequestFactory, pConfig, taskId, null);
	}

	public static List<Task> getTaskVariables(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			String taskId, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		VariablesUrl varUrl = new VariablesUrl(host);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(varUrl).build(buildParams(pParams));

		try {
			HttpRequest request = pHttpRequestFactory.buildGetRequest(url);

			debugRequest(request);

			HttpResponse response = request.execute();
			String responseAsString = response.parseAsString();

			debugResponse(response, responseAsString);

			return parseList(responseAsString, Task.class);

		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), "error", AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {

			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static List<ProcessDefinition> getProcessDefinitions(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		return getProcessDefinitions(pHttpRequestFactory, pConfig, null);
	}

	public static List<ProcessDefinition> getProcessDefinitions(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new ProcessDefinitionsUrl(pConfig.getHost())))
				.build(buildParams(pParams));
		try {
			HttpRequest request = pHttpRequestFactory.buildGetRequest(url);

			debugRequest(request);

			HttpResponse response = request.execute();
			String responseAsString = response.parseAsString();

			debugResponse(response, responseAsString);

			return parseList(responseAsString, ProcessDefinition.class);

		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), "error", AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {

			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	public static InputStream getProcessDefinitionImage(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			String processDefinitionId) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessDefinitionsUrl processUrl = new ProcessDefinitionsUrl(host);
		processUrl.addStringPathParam(processDefinitionId);
		ImageUrl imageUrl = new ImageUrl(host);
		GenericUrl url = (new GenericUrlFactory(processUrl)).add(imageUrl).build();

		try {
			HttpRequest request = pHttpRequestFactory.buildGetRequest(url);

			debugRequest(request);

			HttpResponse response = request.execute();

			debugResponse(response, "IMAGE");

			return response.getContent();

		} catch (HttpResponseException e) {
			mLog.error(e.getMessage(), e);

			AlfrescoError error = parse(e.getContent(), "error", AlfrescoError.class);
			throw new AlfrescoWorkflowException(error.getStatusCode(), error.getErrorKey());
		} catch (Exception e) {

			mLog.error(e.getMessage(), e);
			throw new AlfrescoException(e, AlfrescoException.GENERIC_EXCEPTION);
		}
	}

	// UTILS METHODS BELOW
	public static void debugRequest(HttpRequest req) {
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
		default: {
			throw new IllegalArgumentException(req.getRequestMethod() + " non supported for logging.");
		}
		}

	}

	private static void debugResponse(HttpResponse response, String responseAsString) {
		mLog.debug("RESPONSE " + response.getStatusCode() + " " + responseAsString);
	}

	private static <T> T parse(String json, String key, Class<T> clz) {
		if (key == null) {
			return gson.fromJson(json, clz);
		} else {
			JsonObject obj = gson.fromJson(json, JsonObject.class);
			JsonObject delegate = obj.getAsJsonObject(key);
			return gson.fromJson(delegate, clz);
		}
	}

	private static <T> List<T> parseList(String lResponse, Class<T> clz) {
		List<T> resList = new LinkedList<>();
		JsonObject obj = gson.fromJson(lResponse, JsonObject.class);
		JsonObject list = obj.getAsJsonObject("list");
		if (list != null) {
			JsonArray entries = list.getAsJsonArray("entries");
			for (JsonElement entry : entries) {
				resList.add(gson.fromJson(entry.getAsJsonObject().get("entry"), clz));
			}

		}
		return resList;
	}

	public static void setDefaultDateFormat(String dateFormat) {
		// per cambiare il default dateformat nel caso fosse configurato
		// diversamento all'interno di Alfresco
		AlfrescoWorkflowHelper.dateFormat = dateFormat;
		gson = new GsonBuilder().setDateFormat(dateFormat).create();
	}

	private static Map<String, Object> buildParams(Map<String, Object> pParams) {
		if (pParams == null) {
			pParams = new HashMap<String, Object>();
		}
		return pParams;
	}

}
