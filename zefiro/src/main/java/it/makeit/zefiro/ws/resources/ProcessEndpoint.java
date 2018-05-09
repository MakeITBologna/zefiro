package it.makeit.zefiro.ws.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.zefiro.service.ProcessService;

@Path("/Process")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ProcessEndpoint extends AbstractEndpoint {

	@GET
	@Path("/processes")
	public Response getProcess() {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.load()).build();
	}

	@GET
	@Path("/startedProcesses")
	public Response getStartedProcess() {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.loadStarted()).build();
	}

	// @GET
	// @Path("/definitions")
	// public Response getWorkflows() {
	// Session lSession = Util.getUserAlfrescoSession(httpRequest);
	// AlfrescoConfig pConfig = Util.getUserAlfrescoConfig(httpRequest);
	// HttpRequestFactory pHttpRequestFactory =
	// AlfrescoHelper.getRequestFactory(pConfig);
	//
	// List<ProcessDefinition> processDefinition =
	// AlfrescoWorkflowHelper.getProcessDefinitions(pHttpRequestFactory,
	// pConfig);
	//
	// Map<String, ProcessDefinition> version = new HashMap<String,
	// ProcessDefinition>();
	// for (ProcessDefinition process : processDefinition) {
	// String key = process.getKey();
	// ProcessDefinition mapProcess = version.get(key);
	// if (mapProcess == null) {
	// version.put(key, process);
	// continue;
	// }
	// if (mapProcess.getVersion() < process.getVersion()) {
	// version.put(key, process);
	// }
	// }
	//
	// return Response.ok(version.values()).build();
	// }
}
