package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.alfresco.restApi.AlfrescoParamPredicate;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoRESTWhereQueryParamsFactory;
import it.makeit.alfresco.restApi.AlfrescoWhereOperatorEnum;
import it.makeit.alfresco.workflow.AlfrescoWorkflowHelper;
import it.makeit.alfresco.workflow.model.FormModel;
import it.makeit.alfresco.workflow.model.Item;
import it.makeit.alfresco.workflow.model.Task;
import it.makeit.alfresco.workflow.model.Variable;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.TaskComplete;
import it.makeit.zefiro.dao.WorkFlowProcessComplete;

/**
 *
 * @author Alba Quarto
 *
 */
public class TaskService extends ZefiroAbstractServcie {

	private final String STATE = "state";
	private final String VARIABLES = "variables";
	private final String ASSIGNEE = "assignee";
	private final String SEPARATOR = ",";

	public TaskService(AlfrescoConfig pConfig) {
		super(pConfig);
	}

	public TaskComplete load(String id) {
		Task task = AlfrescoWorkflowHelper.getTask(id, httpRequestFactory, alfrescoConfig);
		ProcessService service = new ProcessService(alfrescoConfig);
		TaskComplete taskComplete = buildTaskComplete(task, service.load(task.getProcessId()));
		Map<String, Person> people = new HashMap<String, Person>();
		addPersonDecoding(task.getAssignee(), taskComplete, people, DecodingType.ASSIGNEE);
		addPersonDecoding(task.getOwner(), taskComplete, people, DecodingType.OWNER);
		taskComplete.setCandidates(
				AlfrescoWorkflowHelper.getTaskCandidates(task.getId(), httpRequestFactory, alfrescoConfig));

		return taskComplete;
	}

	/**
	 * @return a list of task assigned to logger user or to which the user is a
	 *         candidate
	 */
	@SuppressWarnings("unchecked")
	public List<TaskComplete> loadAssigned() {
		AlfrescoRESTWhereQueryParamsFactory factory = new AlfrescoRESTWhereQueryParamsFactory();
		AlfrescoParamPredicate value = new AlfrescoParamPredicate();
		value.setOperator(AlfrescoWhereOperatorEnum.EQUAL);
		value.addValue(alfrescoConfig.getUsername());
		String where = factory.add("assignee", value).build();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(AlfrescoRESTQueryParamsEnum.WHERE.getName(), where);
		List<Task> tasksAssigned = AlfrescoWorkflowHelper.getTasks(httpRequestFactory, alfrescoConfig, params);

		factory = new AlfrescoRESTWhereQueryParamsFactory();
		value = new AlfrescoParamPredicate();
		value.setOperator(AlfrescoWhereOperatorEnum.EQUAL);
		value.addValue(alfrescoConfig.getUsername());
		where = factory.add("candidateUser", value).build();
		params = new HashMap<String, Object>();
		params.put(AlfrescoRESTQueryParamsEnum.WHERE.getName(), where);
		List<Task> tasksCandidated = AlfrescoWorkflowHelper.getTasks(httpRequestFactory, alfrescoConfig, params);

		return buildTaskComplete((List<Task>) CollectionUtils.union(tasksAssigned, tasksCandidated));
	}

	public List<FormModel> loadFormModel(String id) {
		return AlfrescoWorkflowHelper.getTaskFormModel(id, httpRequestFactory, alfrescoConfig);
	}

	public List<Variable> loadVariables(String id) {
		return AlfrescoWorkflowHelper.getTaskVariables(id, httpRequestFactory, alfrescoConfig);
	}

	public List<Item> loadItems(String id) {
		return AlfrescoWorkflowHelper.getTaskItems(id, httpRequestFactory, alfrescoConfig);
	}

	public TaskComplete update(String id, TaskComplete task, String updatedProperties) {
		Task alfrescoTask = toTask(task);
		String updated[] = {};
		if (StringUtils.isNotBlank(updatedProperties)) {
			updated = StringUtils.splitByWholeSeparator(updatedProperties, SEPARATOR);
		}
		List<String> updatedList = new ArrayList<String>();
		for (int i = 0; i < updated.length; i++) {
			updatedList.add(updated[i]);
		}

		// faccio la'update prima delle variabili
		if (updatedList.contains(VARIABLES)) {
			List<Variable> variables = task.getVariables();
			if (variables != null && variables.size() > 0) {
				AlfrescoWorkflowHelper.insertTaskVariable(id, variables, httpRequestFactory, alfrescoConfig);
			}
			updatedList.remove(updatedList.indexOf(VARIABLES));
		}
		// veifico se è stato fatto l'update dello state
		boolean updatedState = false;
		if (updatedList.contains(STATE)) {
			updatedState = true;
			updatedList.remove(updatedList.indexOf(STATE));
		}
		// fa l'update di tutte le altre proprietà
		String updatedString = updatedList.size() > 0 ? String.join(SEPARATOR, updatedList) : "";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(updatedString)) {
			params.put(AlfrescoRESTQueryParamsEnum.SELECT.getName(),
					updatedProperties != null ? updatedProperties : "");
		}
		Task updatedTask = AlfrescoWorkflowHelper.updateTask(id, alfrescoTask, httpRequestFactory, alfrescoConfig,
				params);
		// fa per ultimo l'update dello state
		if (updatedState) {
			updatedTask = updateTaskSate(id, alfrescoTask);
		}
		ProcessService service = new ProcessService(alfrescoConfig);
		return buildTaskComplete(updatedTask, service.load(updatedTask.getProcessId()));
	}

	private Task updateTaskSate(String id, Task task) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(AlfrescoRESTQueryParamsEnum.SELECT.getName(), String.format("%s%s%s", ASSIGNEE, SEPARATOR, STATE));
		return AlfrescoWorkflowHelper.updateTask(id, task, httpRequestFactory, alfrescoConfig, params);
	}

	private List<TaskComplete> buildTaskComplete(List<Task> tasks) {
		ProcessService processService = new ProcessService(alfrescoConfig);
		List<WorkFlowProcessComplete> processes = processService.load();

		Map<String, WorkFlowProcessComplete> processMap = buildProcessMap(processes);
		List<TaskComplete> entities = new ArrayList<TaskComplete>();
		Map<String, Person> people = new HashMap<String, Person>();
		for (Task task : tasks) {
			WorkFlowProcessComplete process = processMap.get(task.getProcessId());
			if (process == null) {
				process = processService.load(task.getProcessId());
			}
			TaskComplete taskComplete = buildTaskComplete(task, process);
			addPersonDecoding(task.getAssignee(), taskComplete, people, DecodingType.ASSIGNEE);
			addPersonDecoding(task.getOwner(), taskComplete, people, DecodingType.OWNER);
			taskComplete.setCandidates(
					AlfrescoWorkflowHelper.getTaskCandidates(task.getId(), httpRequestFactory, alfrescoConfig));
			entities.add(taskComplete);
		}
		return entities;
	}

	private Map<String, WorkFlowProcessComplete> buildProcessMap(List<WorkFlowProcessComplete> processes) {
		Map<String, WorkFlowProcessComplete> processMap = new HashMap<String, WorkFlowProcessComplete>();

		for (WorkFlowProcessComplete process : processes) {
			processMap.put(process.getId(), process);
		}
		return processMap;
	}

	private TaskComplete buildTaskComplete(Task task, WorkFlowProcessComplete workflowProcess) {
		TaskComplete taskComplete = gson.fromJson(gson.toJson(task), TaskComplete.class);
		Util.decodeField(taskComplete, workflowProcess, DecodingType.PROCESS);

		return taskComplete;
	}

	private Task toTask(TaskComplete taskComplete) {
		return gson.fromJson(gson.toJson(taskComplete), Task.class);
	}
}
