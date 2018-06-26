package it.makeit.zefiro.ws.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.alfresco.webscriptsapi.AlfrescoWorkflowInstanceQueryParamsEnum;
import it.makeit.alfresco.workflow.model.Item;
import it.makeit.zefiro.dao.WorkFlowProcessComplete;
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

	@POST
	@Path("/processes")
	public Response getProcess(WorkFlowProcessComplete process) {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.startProcess(process)).build();
	}

	@GET
	@Path("/startedProcesses")
	public Response getStartedProcess() {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.loadStarted()).build();
	}

	@GET
	@Path("/definitions")
	public Response getWorkflows() {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.loadDefinitions()).build();
	}

	@GET
	@Path("/definitions/{id}/startForm")
	public Response getStartForm(@PathParam("id") String id) {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.loadStartForm(id)).build();
	}

	@POST
	@Path("/processes/{id}/items")
	public Response getStartForm(@PathParam("id") String id, List<Item> items) {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.addItems(id, items)).build();
	}

	@GET
	@Path("workflowInstances")
	public Response getWorkflowInstances(@QueryParam("start") String from, @QueryParam("end") String to) {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);
		
		return Response.ok(service.loadWorkflowInstances(from, to)).build();
	}

	@GET
	@Path("workflowInstances/{id}")
	public Response getWorkflowInstance(@PathParam("id") String id) {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);
		return Response.ok(service.loadWorkflowInstance(id)).build();
	}

	@GET
	@Path("workflowDefinitions")
	public Response getWorkflowDefinitions() {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);

		return Response.ok(service.loadWorkflowDefinitions()).build();
	}

	@GET
	@Path("completedWorkflow")
	public Response getCompletedWorkflow(@QueryParam("start") String from, @QueryParam("end") String to) {
		ProcessService service = (ProcessService) getServiceInstance(ProcessService.class);
		System.out.println("date range : (" + from +"," + to +")");
		return Response.ok(service.loadCompletedWorkflows(from, to)).build();
	}

}
