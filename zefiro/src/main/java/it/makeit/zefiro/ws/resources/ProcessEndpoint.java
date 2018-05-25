package it.makeit.zefiro.ws.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
}
