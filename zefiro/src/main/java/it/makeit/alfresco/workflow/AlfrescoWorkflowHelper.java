package it.makeit.alfresco.workflow;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoException;
import it.makeit.alfresco.BaseAlfrescoHelper;
import it.makeit.alfresco.restApi.GenericUrlFactory;
import it.makeit.alfresco.webscriptsapi.entities.WorkflowDefinitionsUrl;
import it.makeit.alfresco.webscriptsapi.entities.WorkflowInstanceUrl;
import it.makeit.alfresco.webscriptsapi.model.WorkflowDefinitionList;
import it.makeit.alfresco.webscriptsapi.model.WorkflowInstance;
import it.makeit.alfresco.webscriptsapi.model.WorkflowInstanceList;
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

public class AlfrescoWorkflowHelper extends BaseAlfrescoHelper {

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

		if (items.size() == 1) {
			Item item = insert(pHttpRequestFactory, url, items.get(0), Item.class);
			List<Item> insertedItems = new ArrayList<Item>();
			insertedItems.add(item);
			return insertedItems;
		}
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

		if (items.size() == 1) {
			Item item = insert(pHttpRequestFactory, url, items.get(0), Item.class);
			List<Item> insertedItems = new ArrayList<Item>();
			insertedItems.add(item);
			return insertedItems;
		}
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

	public static WorkflowDefinitionList getWorkflowDefinitions(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getWorkflowDefinitions(pHttpRequestFactory, pConfig, null);
	}

	public static WorkflowDefinitionList getWorkflowDefinitions(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		WorkflowDefinitionsUrl wfiUrl = new WorkflowDefinitionsUrl(pConfig.getHost());
		GenericUrl url = new GenericUrlFactory(wfiUrl).build(buildParams(pParams));

		return (WorkflowDefinitionList) loadWebscriptList(pHttpRequestFactory, url, WorkflowDefinitionList.class);
	}

	public static WorkflowInstanceList getWorkflowInstances(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getWorkflowInstances(pHttpRequestFactory, pConfig, null);
	}

	public static WorkflowInstanceList getWorkflowInstances(HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		WorkflowInstanceUrl wfiUrl = new WorkflowInstanceUrl(pConfig.getHost());
		GenericUrl url = new GenericUrlFactory(wfiUrl).build(buildParams(pParams));

		return (WorkflowInstanceList) loadWebscriptList(pHttpRequestFactory, url, WorkflowInstanceList.class);
	}

	public static WorkflowInstance getWorkflowInstance(String id, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig) {
		mLog.info("Start");

		return getWorkflowInstance(id, pHttpRequestFactory, pConfig, null);
	}

	public static WorkflowInstance getWorkflowInstance(String id, HttpRequestFactory pHttpRequestFactory,
			AlfrescoConfig pConfig, Map<String, Object> pParams) {
		mLog.info("Start");

		WorkflowInstanceUrl wfiUrl = new WorkflowInstanceUrl(pConfig.getHost());
		wfiUrl.addStringPathParam(id);
		GenericUrl url = new GenericUrlFactory(wfiUrl).build(buildParams(pParams));

		return loadWebscriptObject(pHttpRequestFactory, url, WorkflowInstance.class);
	}

}
