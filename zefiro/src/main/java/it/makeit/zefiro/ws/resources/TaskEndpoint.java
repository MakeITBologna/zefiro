package it.makeit.zefiro.ws.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.api.client.http.HttpRequestFactory;

import it.makeit.alfresco.AlfrescoConfig;
import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.service.TaskService;

@Path("/Task")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class TaskEndpoint {

	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Path("/")
	public Response getProcess() {
		AlfrescoConfig pConfig = Util.getUserAlfrescoConfig(httpRequest);
		HttpRequestFactory pHttpRequestFactory = AlfrescoHelper.getRequestFactory(pConfig);
		TaskService service = new TaskService(pHttpRequestFactory, pConfig);

		return Response.ok(service.loadAssigned()).build();
	}
}
