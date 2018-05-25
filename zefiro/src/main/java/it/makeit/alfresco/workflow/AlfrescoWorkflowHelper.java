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

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoException;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.GsonHttpContent;
import it.makeit.alfresco.restApi.GenericUrlFactory;
import it.makeit.alfresco.workflow.enties.CandidatesUrl;
import it.makeit.alfresco.workflow.enties.DeploymentsUrl;
import it.makeit.alfresco.workflow.enties.ImageUrl;
import it.makeit.alfresco.workflow.enties.ItemsUrl;
import it.makeit.alfresco.workflow.enties.ProcessDefinitionsUrl;
import it.makeit.alfresco.workflow.enties.ProcessesUrl;
import it.makeit.alfresco.workflow.enties.StartFormModelUrl;
import it.makeit.alfresco.workflow.enties.TaskFormModelUrl;
import it.makeit.alfresco.workflow.enties.TasksUrl;
import it.makeit.alfresco.workflow.enties.VariablesUrl;
import it.makeit.alfresco.workflow.model.AlfrescoError;
import it.makeit.alfresco.workflow.model.Candidate;
import it.makeit.alfresco.workflow.model.Deployment;
import it.makeit.alfresco.workflow.model.FormModel;
import it.makeit.alfresco.workflow.model.Item;
import it.makeit.alfresco.workflow.model.ProcessDefinition;
import it.makeit.alfresco.workflow.model.Task;
import it.makeit.alfresco.workflow.model.Variable;
import it.makeit.alfresco.workflow.model.WorkflowProcess;

public class AlfrescoWorkflowHelper {

	private static final Logger mLog = LoggerFactory.getLogger(AlfrescoHelper.class);

	private static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();

	public static Deployment getDeployment(String id, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		DeploymentsUrl deployment = new DeploymentsUrl(pConfig.getHost());
		deployment.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(deployment)).build();

		return loadObject(pHttpRequestFactory, url, Deployment.class);
	}

	public static List<Deployment> getDeployments(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getDeployments(pHttpRequestFactory, pConfig, null);
	}

	public static List<Deployment> getDeployments(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new DeploymentsUrl(pConfig.getHost()))).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Deployment.class);
	}

	public static void deleteDeployments(String id, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		DeploymentsUrl deployment = new DeploymentsUrl(pConfig.getHost());
		deployment.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(deployment)).build();

		ask(pHttpRequestFactory, url, HttpMethods.DELETE);
	}

	public static ProcessDefinition getProcessDefinition(String id, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		ProcessDefinitionsUrl processDefinitionsUrl = new ProcessDefinitionsUrl(pConfig.getHost());
		processDefinitionsUrl.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(processDefinitionsUrl)).build();

		return loadObject(pHttpRequestFactory, url, ProcessDefinition.class);
	}

	public static List<ProcessDefinition> getProcessDefinitions(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getProcessDefinitions(pHttpRequestFactory, pConfig, null);
	}

	public static List<ProcessDefinition> getProcessDefinitions(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new ProcessDefinitionsUrl(pConfig.getHost())))
				.build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, ProcessDefinition.class);
	}

	public static List<FormModel> getProcessDefinitionStartFormModel(String processDefinitionId,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessDefinitionsUrl processUrl = new ProcessDefinitionsUrl(host);
		processUrl.addStringPathParam(processDefinitionId);
		StartFormModelUrl startFormUrl = new StartFormModelUrl(host);
		GenericUrl url = (new GenericUrlFactory(processUrl)).add(startFormUrl).build();

		return loadList(pHttpRequestFactory, url, FormModel.class);
	}

	public static InputStream getProcessDefinitionImage(String processDefinitionId,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
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

	public static WorkflowProcess getProcess(String id, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		ProcessesUrl processUrl = new ProcessesUrl(pConfig.getHost());
		processUrl.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(processUrl)).build();

		return loadObject(pHttpRequestFactory, url, WorkflowProcess.class);
	}

	public static List<WorkflowProcess> getProcesses(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getProcesses(pHttpRequestFactory, pConfig, null);
	}

	public static List<WorkflowProcess> getProcesses(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new ProcessesUrl(pConfig.getHost()))).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, WorkflowProcess.class);
	}

	public static WorkflowProcess startProcess(WorkflowProcess process, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		ProcessesUrl processUrl = new ProcessesUrl(pConfig.getHost());
		GenericUrl url = (new GenericUrlFactory(processUrl)).build();

		return insert(pHttpRequestFactory, url, process, WorkflowProcess.class);
	}

	public static List<WorkflowProcess> startProcesses(List<WorkflowProcess> processes,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		ProcessesUrl processUrl = new ProcessesUrl(pConfig.getHost());
		GenericUrl url = (new GenericUrlFactory(processUrl)).build();

		return insertList(pHttpRequestFactory, url, processes, WorkflowProcess.class);
	}

	public static void deleteProcess(String id, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		ProcessesUrl processUrl = new ProcessesUrl(pConfig.getHost());
		processUrl.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(processUrl)).build();

		ask(pHttpRequestFactory, url, HttpMethods.DELETE);
	}

	public static List<Task> getProcessTask(String processId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getProcessTask(processId, pHttpRequestFactory, pConfig, null);
	}

	public static List<Task> getProcessTask(String processId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processUrl = new ProcessesUrl(host);
		processUrl.addStringPathParam(processId);
		TasksUrl tasksUrl = new TasksUrl(host);
		GenericUrl url = (new GenericUrlFactory(processUrl)).add(tasksUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Task.class);
	}

	public static List<Item> getProcessItems(String processId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getProcessItems(processId, pHttpRequestFactory, pConfig, null);
	}

	public static List<Item> getProcessItems(String processId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processUrl = new ProcessesUrl(host);
		processUrl.addStringPathParam(processId);
		ItemsUrl itemsUrl = new ItemsUrl(host);
		GenericUrl url = (new GenericUrlFactory(processUrl)).add(itemsUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Item.class);
	}

	public static List<Item> insertProcessItems(String processId, List<Item> items,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processesUrl = new ProcessesUrl(host);
		processesUrl.addStringPathParam(processId);
		ItemsUrl itemUrl = new ItemsUrl(host);
		GenericUrl url = (new GenericUrlFactory(processesUrl)).add(itemUrl).build();

		return insertList(pHttpRequestFactory, url, items, Item.class);
	}

	public static void deleteProcessItem(String processId, Item Item, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processesUrl = new ProcessesUrl(host);
		processesUrl.addStringPathParam(processId);
		ItemsUrl itemsUrl = new ItemsUrl(host);
		itemsUrl.addStringPathParam(Item.getId());
		GenericUrl url = (new GenericUrlFactory(processesUrl)).add(itemsUrl).build();

		ask(pHttpRequestFactory, url, HttpMethods.DELETE);
	}

	public static List<Variable> getProcessVariables(String processId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getProcessVariables(processId, pHttpRequestFactory, pConfig, null);
	}

	public static List<Variable> getProcessVariables(String processId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processUrl = new ProcessesUrl(host);
		processUrl.addStringPathParam(processId);
		VariablesUrl variableUrl = new VariablesUrl(host);
		GenericUrl url = (new GenericUrlFactory(processUrl)).add(variableUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Variable.class);
	}

	public static List<Variable> insertProcessVariable(String processId, List<Variable> variables,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processesUrl = new ProcessesUrl(host);
		processesUrl.addStringPathParam(processId);
		VariablesUrl varUrl = new VariablesUrl(host);
		GenericUrl url = (new GenericUrlFactory(processesUrl)).add(varUrl).build();

		return insertList(pHttpRequestFactory, url, variables, Variable.class);
	}

	public static Variable updateProcessVariable(String processId, Variable variable,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processesUrl = new ProcessesUrl(host);
		processesUrl.addStringPathParam(processId);
		VariablesUrl varUrl = new VariablesUrl(host);
		varUrl.addStringPathParam(variable.getName());
		GenericUrl url = (new GenericUrlFactory(processesUrl)).add(varUrl).build();

		return update(pHttpRequestFactory, url, variable, Variable.class);
	}

	public static void deleteProcessVariable(String processId, Variable variable,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		ProcessesUrl processesUrl = new ProcessesUrl(host);
		processesUrl.addStringPathParam(processId);
		VariablesUrl varUrl = new VariablesUrl(host);
		varUrl.addStringPathParam(variable.getName());
		GenericUrl url = (new GenericUrlFactory(processesUrl)).add(varUrl).build();

		ask(pHttpRequestFactory, url, HttpMethods.DELETE);
	}

	public static Task getTask(String id, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		TasksUrl taskUrl = new TasksUrl(pConfig.getHost());
		taskUrl.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).build();

		return loadObject(pHttpRequestFactory, url, Task.class);
	}

	public static List<Task> getTasks(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getTasks(pHttpRequestFactory, pConfig, null);
	}

	public static List<Task> getTasks(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		GenericUrl url = (new GenericUrlFactory(new TasksUrl(pConfig.getHost()))).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Task.class);
	}

	public static Task updateTask(String id, Task task, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		TasksUrl taskUrl = new TasksUrl(pConfig.getHost());
		taskUrl.addStringPathParam(id);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).build(pParams);

		return update(pHttpRequestFactory, url, task, Task.class);
	}

	public static List<Variable> getTaskVariables(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getTaskVariables(taskId, pHttpRequestFactory, pConfig, null);
	}

	public static List<Variable> getTaskVariables(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		VariablesUrl varUrl = new VariablesUrl(host);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(varUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Variable.class);
	}

	public static List<Variable> insertTaskVariable(String taskId, List<Variable> variables,
			HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		VariablesUrl varUrl = new VariablesUrl(host);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(varUrl).build();

		return insertList(pHttpRequestFactory, url, variables, Variable.class);
	}

	public static Variable updateTaskVariable(String taskId, Variable variable, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		VariablesUrl varUrl = new VariablesUrl(host);
		varUrl.addStringPathParam(variable.getName());
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(varUrl).build();

		return update(pHttpRequestFactory, url, variable, Variable.class);
	}

	public static void deleteTaskVariable(String taskId, Variable variable, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl tasksUrl = new TasksUrl(host);
		tasksUrl.addStringPathParam(taskId);
		VariablesUrl varUrl = new VariablesUrl(host);
		varUrl.addStringPathParam(variable.getName());
		GenericUrl url = (new GenericUrlFactory(tasksUrl)).add(varUrl).build();

		ask(pHttpRequestFactory, url, HttpMethods.DELETE);
	}

	public static List<Candidate> getTaskCandidates(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getTaskCandidates(taskId, pHttpRequestFactory, pConfig, null);
	}

	public static List<Candidate> getTaskCandidates(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		CandidatesUrl candidateUrl = new CandidatesUrl(host);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(candidateUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Candidate.class);
	}

	public static List<FormModel> getTaskFormModel(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getTaskFormModel(taskId, pHttpRequestFactory, pConfig, null);
	}

	public static List<FormModel> getTaskFormModel(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		TaskFormModelUrl taskFormModelUrl = new TaskFormModelUrl(host);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(taskFormModelUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, FormModel.class);
	}

	public static List<Item> getTaskItems(String taskId, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getTaskItems(taskId, pHttpRequestFactory, pConfig, null);
	}

	public static List<Item> getTaskItems(String taskId, HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig,
			Map<String, Object> pParams) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl taskUrl = new TasksUrl(host);
		taskUrl.addStringPathParam(taskId);
		ItemsUrl itemsUrl = new ItemsUrl(host);
		GenericUrl url = (new GenericUrlFactory(taskUrl)).add(itemsUrl).build(buildParams(pParams));

		return loadList(pHttpRequestFactory, url, Item.class);
	}

	public static List<Item> insertTaskItems(String taskId, List<Item> items, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl tasksUrl = new TasksUrl(host);
		tasksUrl.addStringPathParam(taskId);
		ItemsUrl itemUrl = new ItemsUrl(host);
		GenericUrl url = (new GenericUrlFactory(tasksUrl)).add(itemUrl).build();

		return insertList(pHttpRequestFactory, url, items, Item.class);
	}

	public static void deleteTaskItem(String taskId, Item Item, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		URL host = pConfig.getHost();
		TasksUrl tasksUrl = new TasksUrl(host);
		tasksUrl.addStringPathParam(taskId);
		ItemsUrl itemsUrl = new ItemsUrl(host);
		itemsUrl.addStringPathParam(Item.getId());
		GenericUrl url = (new GenericUrlFactory(tasksUrl)).add(itemsUrl).build();

		ask(pHttpRequestFactory, url, HttpMethods.DELETE);
	}

	// UTILS METHODS BELOW
	private static <T> T loadObject(HttpRequestFactory pHttpRequestFactory, GenericUrl url, Class<T> clz) {
		return parse(ask(pHttpRequestFactory, url, HttpMethods.GET), ResponseBodyPartEnum.ENTRY.partName(), clz);
	}

	private static <T> List<T> loadList(HttpRequestFactory pHttpRequestFactory, GenericUrl url, Class<T> clz) {
		return parseList(ask(pHttpRequestFactory, url, HttpMethods.GET), clz);
	}

	private static <T> T update(HttpRequestFactory pHttpRequestFactory, GenericUrl url, T body, Class<T> clz) {

		HttpContent content = new GsonHttpContent(gson, body);
		return parse(ask(pHttpRequestFactory, url, HttpMethods.PUT, content), ResponseBodyPartEnum.ENTRY.partName(),
				clz);
	}

	private static <T> T insert(HttpRequestFactory pHttpRequestFactory, GenericUrl url, T body, Class<T> clz) {
		HttpContent content = new GsonHttpContent(gson, body);
		return parse(ask(pHttpRequestFactory, url, HttpMethods.POST, content), ResponseBodyPartEnum.ENTRY.partName(),
				clz);
	}

	private static <T> List<T> insertList(HttpRequestFactory pHttpRequestFactory, GenericUrl url, List<T> body,
			Class<T> clz) {
		HttpContent content = new GsonHttpContent(gson, body);
		return parseList(ask(pHttpRequestFactory, url, HttpMethods.POST, content), clz);
	}

	private static String ask(HttpRequestFactory pHttpRequestFactory, GenericUrl url, String method) {
		HttpContent content = new GsonHttpContent(gson, "");
		return ask(pHttpRequestFactory, url, method, content);
	}

	private static String ask(HttpRequestFactory pHttpRequestFactory, GenericUrl url, String method,
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
