package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.HttpRequestFactory;
import com.google.gson.Gson;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.restApi.AlfrescoParamPredicate;
import it.makeit.alfresco.restApi.AlfrescoRESTQueryParamsEnum;
import it.makeit.alfresco.restApi.AlfrescoRESTWhereQueryParamsFactory;
import it.makeit.alfresco.restApi.AlfrescoWhereOperatorEnum;
import it.makeit.alfresco.workflow.AlfrescoWorkflowHelper;
import it.makeit.alfresco.workflow.model.ProcessDefinition;
import it.makeit.alfresco.workflow.model.WorkflowProcess;
import it.makeit.zefiro.dao.WorkFlowProcessComplete;

public class ProcessService extends AbstractServcie {

	public ProcessService(HttpRequestFactory pHttpRequestFactory, AlfrescoConfig pConfig) {
		super(pHttpRequestFactory, pConfig);
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
		for (WorkflowProcess process : workflowProcess) {
			entities.add(buildProcess(process, definitions.get(process.getProcessDefinitionId())));
			// TODO aggiungere decodifica nome cognome startUser
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

	private Map<String, ProcessDefinition> buildDefinitionsMap(List<ProcessDefinition> definitions) {
		Map<String, ProcessDefinition> processMap = new HashMap<String, ProcessDefinition>();

		for (ProcessDefinition definition : definitions) {
			processMap.put(definition.getId(), definition);
		}
		return processMap;
	}

	private WorkFlowProcessComplete buildProcess(WorkflowProcess workflowProcess, ProcessDefinition processDefinition) {
		WorkFlowProcessComplete process = null;

		Gson gson = new Gson();
		process = gson.fromJson(gson.toJson(workflowProcess), WorkFlowProcessComplete.class);
		process.setName(processDefinition.getName());
		process.setDescription(processDefinition.getDescription());

		return process;
	}
}
