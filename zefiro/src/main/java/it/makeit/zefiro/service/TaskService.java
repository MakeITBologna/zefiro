package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.api.client.http.HttpRequestFactory;
import com.google.gson.Gson;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.alfresco.restApi.AlfrescoParamPredicate;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoRESTWhereQueryParamsFactory;
import it.makeit.alfresco.restApi.AlfrescoWhereOperatorEnum;
import it.makeit.alfresco.workflow.AlfrescoWorkflowHelper;
import it.makeit.alfresco.workflow.model.Task;
import it.makeit.alfresco.workflow.model.WorkflowProcess;
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

	public TaskService(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		super(pHttpRequestFactory, pConfig);
	}

	private List<TaskComplete> load(Map<String, Object> pParams) {
		List<Task> tasks = AlfrescoWorkflowHelper.getTasks(httpRequestFactory, alfrescoConfig, pParams);
		return buildTaskComplete(tasks);
	}

	private TaskComplete load(String id) {
		return null;/*
		Task task = AlfrescoWorkflowHelper.getTask(id, httpRequestFactory, alfrescoConfig);
		WorkflowProcess workflowProcess = AlfrescoWorkflowHelper.getProcessDefinitionImage(task.getProcessId(),
				httpRequestFactory, alfrescoConfig);
		return buildTaskComplete(task, workflowProcess);*/
	}

	/**
	 * @author Alba Quarto
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

	private List<TaskComplete> buildTaskComplete(List<Task> tasks) {
		ProcessService processService = new ProcessService(httpRequestFactory, alfrescoConfig);
		List<WorkFlowProcessComplete> processes = processService.load();

		Map<String, WorkFlowProcessComplete> processMap = buildProcessMap(processes);
		List<TaskComplete> entities = new ArrayList<TaskComplete>();
		Map<String, Person> people = new HashMap<String, Person>();
		for (Task task : tasks) {
			TaskComplete taskComplete = buildTaskComplete(task, processMap.get(task.getProcessId()));
			addPersonDecoding(task.getAssignee(), taskComplete, people, DecodingType.ASSIGNEE);
			addPersonDecoding(task.getOwner(), taskComplete, people, DecodingType.OWNER);
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
		TaskComplete taskComplete;

		Gson gson = new Gson();
		taskComplete = gson.fromJson(gson.toJson(task), TaskComplete.class);
		Util.decodeField(taskComplete, workflowProcess, DecodingType.PROCESS);

		return taskComplete;
	}
}
