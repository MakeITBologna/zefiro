package it.makeit.zefiro.ws.resources;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.zefiro.dao.TaskComplete;
import it.makeit.zefiro.service.TaskService;

@Path("/Task")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class TaskEndpoint extends AbstractEndpoint {

	@GET
	@Path("/")
	public Response getProcess() {
		TaskService service = (TaskService) getServiceInstance(TaskService.class);
		return Response.ok(service.loadAssigned()).build();
	}

	@GET
	@Path("/{id}")
	public Response getProcess(@PathParam("id") String id) {
		TaskService service = (TaskService) getServiceInstance(TaskService.class);
		return Response.ok(service.load(id)).build();
	}

	@GET
	@Path("/{id}/formModel")
	public Response getFormModel(@PathParam("id") String id) {
		TaskService service = (TaskService) getServiceInstance(TaskService.class);
		return Response.ok(service.loadFormModel(id)).build();
	}

	@GET
	@Path("/{id}/variables")
	public Response getVariables(@PathParam("id") String id) {
		TaskService service = (TaskService) getServiceInstance(TaskService.class);
		return Response.ok(service.loadVariables(id)).build();
	}

	@GET
	@Path("/{id}/items")
	public Response getItems(@PathParam("id") String id) {
		TaskService service = (TaskService) getServiceInstance(TaskService.class);
		return Response.ok(service.loadItems(id)).build();
	}

	@PUT
	@Path("/{id}")
	public Response updateTask(@PathParam("id") String id, TaskComplete task) {
		TaskService service = (TaskService) getServiceInstance(TaskService.class);
		return Response.ok(service.update(id, task, httpRequest.getHeader("updated"))).build();
	}

}
