package it.makeit.zefiro.ws.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.Session;

import com.google.api.client.http.HttpRequestFactory;
import com.google.gson.Gson;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.workflow.AlfrescoWorkflowHelper;
import it.makeit.alfresco.workflow.model.ProcessDefinition;
import it.makeit.alfresco.workflow.model.WorkflowProcess;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.WorkFlowProcessComplete;

@Path("/Process")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Process {

	@Context
	private HttpServletRequest httpRequest;

	@GET
	public Response getProcess() {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		AlfrescoConfig pConfig = Util.getUserAlfrescoConfig(httpRequest);
		HttpRequestFactory pHttpRequestFactory = AlfrescoHelper.getRequestFactory(pConfig);
		List<WorkflowProcess> workflowProcess = AlfrescoWorkflowHelper.getProcesses(pHttpRequestFactory, pConfig);
		List<ProcessDefinition> processDefinition = AlfrescoWorkflowHelper.getProcessDefinitions(pHttpRequestFactory,
				pConfig);

		Map<String, ProcessDefinition> definitions = buildDefinitionsMap(processDefinition);
		List<WorkFlowProcessComplete> processes = new ArrayList<WorkFlowProcessComplete>();
		for (WorkflowProcess process : workflowProcess) {
			ProcessDefinition definition = definitions.get(process.getProcessDefinitionId());
			processes.add(buildProcess(process, definitions.get(process.getProcessDefinitionId())));
		}

		return Response.ok(processes).build();
	}

	@GET
	@Path("/definition")
	public Response getWorkflows() {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		AlfrescoConfig pConfig = Util.getUserAlfrescoConfig(httpRequest);
		HttpRequestFactory pHttpRequestFactory = AlfrescoHelper.getRequestFactory(pConfig);

		List<ProcessDefinition> processDefinition = AlfrescoWorkflowHelper.getProcessDefinitions(pHttpRequestFactory,
				pConfig);

		Map<String, ProcessDefinition> version = new HashMap<String, ProcessDefinition>();
		for (ProcessDefinition process : processDefinition) {
			String key = process.getKey();
			ProcessDefinition mapProcess = version.get(key);
			if (mapProcess == null) {
				version.put(key, process);
				continue;
			}
			if (mapProcess.getVersion() < process.getVersion()) {
				version.put(key, process);
			}
		}

		return Response.ok(version.values()).build();
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
