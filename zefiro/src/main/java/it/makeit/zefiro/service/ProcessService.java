package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.publicapi.model.Person;
import it.makeit.alfresco.restApi.AlfrescoParamPredicate;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoRESTWhereQueryParamsFactory;
import it.makeit.alfresco.restApi.AlfrescoWhereOperatorEnum;
import it.makeit.alfresco.webscriptsapi.AlfrescoWorkflowInstanceQueryParamsEnum;
import it.makeit.alfresco.webscriptsapi.model.WorkflowDefinition;
import it.makeit.alfresco.webscriptsapi.model.WorkflowDefinitionList;
import it.makeit.alfresco.webscriptsapi.model.WorkflowInstance;
import it.makeit.alfresco.webscriptsapi.model.WorkflowInstanceList;
import it.makeit.alfresco.workflow.AlfrescoWorkflowHelper;
import it.makeit.alfresco.workflow.model.FormModel;
import it.makeit.alfresco.workflow.model.Item;
import it.makeit.alfresco.workflow.model.ProcessDefinition;
import it.makeit.alfresco.workflow.model.WorkflowProcess;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.WorkFlowProcessComplete;

public class ProcessService extends ZefiroAbstractServcie {

	public ProcessService(AlfrescoConfig pConfig) {
		super(pConfig);
	}

	public List<WorkFlowProcessComplete> load() {
		return load(new HashMap<String, Object>());
	}

	private List<WorkFlowProcessComplete> load(Map<String, Object> pParams) {
		List<WorkflowProcess> workflowProcess = AlfrescoWorkflowHelper.getProcesses(httpRequestFactory, alfrescoConfig,
				pParams);
		List<ProcessDefinition> processDefinition = AlfrescoWorkflowHelper.getProcessDefinitions(httpRequestFactory,
				alfrescoConfig);
		Map<String, ProcessDefinition> definitions = buildDefinitionsMap(processDefinition);
		List<WorkFlowProcessComplete> entities = new ArrayList<WorkFlowProcessComplete>();
		Map<String, Person> people = new HashMap<String, Person>();
		for (WorkflowProcess process : workflowProcess) {
			WorkFlowProcessComplete processComplete = buildProcess(process,
					definitions.get(process.getProcessDefinitionId()));
			addPersonDecoding(process.getStartUserId(), processComplete, people, DecodingType.STARTER);
			entities.add(processComplete);
		}
		return entities;
	}

	public List<WorkFlowProcessComplete> loadStarted() {
		AlfrescoRESTWhereQueryParamsFactory factory = new AlfrescoRESTWhereQueryParamsFactory();
		AlfrescoParamPredicate value = new AlfrescoParamPredicate();
		value.setOperator(AlfrescoWhereOperatorEnum.EQUAL);
		value.addValue(alfrescoConfig.getUsername());
		String where = factory.add("startUserId", value).build();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(AlfrescoRESTQueryParamsEnum.WHERE.getName(), where);
		return this.load(params);
	}

	public WorkFlowProcessComplete load(String id) {
		WorkflowProcess workflowProcess = AlfrescoWorkflowHelper.getProcess(id, httpRequestFactory, alfrescoConfig);
		ProcessDefinition processDefinition = AlfrescoWorkflowHelper
				.getProcessDefinition(workflowProcess.getProcessDefinitionId(), httpRequestFactory, alfrescoConfig);
		WorkFlowProcessComplete workflowProcessComplete = buildProcess(workflowProcess, processDefinition);
		addPersonDecoding(workflowProcess.getStartUserId(), workflowProcessComplete, new HashMap<String, Person>(),
				DecodingType.STARTER);
		return workflowProcessComplete;

	}

	public List<ProcessDefinition> loadDefinitions() {
		List<ProcessDefinition> processDefinition = AlfrescoWorkflowHelper.getProcessDefinitions(httpRequestFactory,
				alfrescoConfig);

		Map<String, ProcessDefinition> lastesVersions = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition process : processDefinition) {
			String key = process.getKey();
			List<String> blackList = Arrays.asList(
					JBrickConfigManager.getInstance().getPropertyList("processDefinitionBlackList/entry", "@key"));
			if (blackList.contains(key)) {
				continue;
			}
			ProcessDefinition mapProcess = lastesVersions.get(key);
			if (mapProcess == null) {
				lastesVersions.put(key, process);
				continue;
			}
			if (mapProcess.getVersion() < process.getVersion()) {
				lastesVersions.put(key, process);
			}
		}
		return new ArrayList<ProcessDefinition>(lastesVersions.values());
	}

	public List<FormModel> loadStartForm(String id) {
		return AlfrescoWorkflowHelper.getProcessDefinitionStartFormModel(id, httpRequestFactory, alfrescoConfig);
	}

	public List<WorkflowInstance> loadWorkflowInstances() {
		return loadWorkflowInstances(new HashMap<String, Object>());
	}

	public WorkflowInstance loadWorkflowInstance(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(AlfrescoWorkflowInstanceQueryParamsEnum.INCLUDE_TASKS.getName(), true);

		return AlfrescoWorkflowHelper.getWorkflowInstance(id, httpRequestFactory, alfrescoConfig, params);
	}

	public List<WorkflowInstance> loadCompletedWorkflows() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(AlfrescoWorkflowInstanceQueryParamsEnum.STATE.getName(), "COMPLETED");

		return loadWorkflowInstances(params);
	}

	private List<WorkflowInstance> loadWorkflowInstances(Map<String, Object> params) {
		params.put(AlfrescoWorkflowInstanceQueryParamsEnum.INITIATOR.getName(), alfrescoConfig.getUsername());

		WorkflowInstanceList objectList = AlfrescoWorkflowHelper.getWorkflowInstances(httpRequestFactory,
				alfrescoConfig, params);
		List<WorkflowInstance> list = objectList.getData();
		if (list == null) {
			list = new ArrayList<WorkflowInstance>();
		}
		return list;
	}

	public List<WorkflowDefinition> loadWorkflowDefinitions() {
		WorkflowDefinitionList objectList = AlfrescoWorkflowHelper.getWorkflowDefinitions(httpRequestFactory,
				alfrescoConfig);
		List<WorkflowDefinition> list = objectList.getData();
		if (list == null) {
			list = new ArrayList<WorkflowDefinition>();
		}
		return list;
	}

	public WorkFlowProcessComplete startProcess(WorkFlowProcessComplete process) {
		Gson gson = new Gson();
		WorkflowProcess alProcess = gson.fromJson(gson.toJson(process), WorkflowProcess.class);
		WorkflowProcess startedProcess = AlfrescoWorkflowHelper.startProcess(alProcess, httpRequestFactory,
				alfrescoConfig);
		return load(startedProcess.getId());
	}

	public List<Item> addItems(String id, List<Item> items) {
		return AlfrescoWorkflowHelper.insertProcessItems(id, items, httpRequestFactory, alfrescoConfig);
	}

	private Map<String, ProcessDefinition> buildDefinitionsMap(List<ProcessDefinition> definitions) {
		Map<String, ProcessDefinition> processMap = new HashMap<String, ProcessDefinition>();

		for (ProcessDefinition definition : definitions) {
			processMap.put(definition.getId(), definition);
		}
		return processMap;
	}

	private WorkFlowProcessComplete buildProcess(WorkflowProcess workflowProcess, ProcessDefinition processDefinition) {
		Gson gson = new Gson();
		WorkFlowProcessComplete process = gson.fromJson(gson.toJson(workflowProcess), WorkFlowProcessComplete.class);
		Util.decodeField(process, processDefinition, DecodingType.DEFINITION);

		return process;
	}
}
