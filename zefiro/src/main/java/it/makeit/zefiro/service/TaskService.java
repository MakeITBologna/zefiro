package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.api.client.http.HttpRequestFactory;
import com.google.gson.Gson;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.restApi.AlfrescoParamPredicate;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoRESTWhereQueryParamsFactory;
import it.makeit.alfresco.restApi.AlfrescoWhereOperatorEnum;
import it.makeit.alfresco.workflow.AlfrescoWorkflowHelper;
import it.makeit.alfresco.workflow.model.Task;
import it.makeit.zefiro.dao.TaskComplete;
import it.makeit.zefiro.dao.WorkFlowProcessComplete;

/**
 *
 * @author Alba Quarto
 *
 */
public class TaskService extends AbstractServcie {

	public TaskService(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		super(pHttpRequestFactory, pConfig);
	}

	public List<TaskComplete> load() {
		return load(new HashMap<String, Object>());
	}

	private List<TaskComplete> load(Map<String, Object> pParams) {
		List<Task> tasks = AlfrescoWorkflowHelper.getTasks(httpRequestFactory, alfrescoConfig, pParams);
		return buildTaskComplete(tasks);
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
		for (Task task : tasks) {
			entities.add(buildTask(task, processMap.get(task.getProcessId())));
			// TODO aggiungere decodifica nome cognome owner e assignee
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

	private TaskComplete buildTask(Task task, WorkFlowProcessComplete workflowProcess) {
		TaskComplete taskComplete;

		Gson gson = new Gson();
		taskComplete = gson.fromJson(gson.toJson(task), TaskComplete.class);
		taskComplete.setProcessBusinessKey(workflowProcess.getBusinessKey());
		taskComplete.setProcessDescription(workflowProcess.getDescription());
		taskComplete.setProcessName(workflowProcess.getName());
		taskComplete.setProcessStartUserId(workflowProcess.getStartUserId());
		taskComplete.setProcessTitle(workflowProcess.getTitle());

		return taskComplete;
	}
}
