package it.makeit.zefiro.ws.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.zefiro.service.AuthorityService;

@Path("/Authority")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AuthorityEndpoint extends AbstractEndpoint {

	@GET
	@Path("/users/{charsSeq}")
	public Response getUsers(@PathParam("charsSeq") String charsSeq) {
		AuthorityService service = (AuthorityService) getServiceInstance(AuthorityService.class);
		return Response.ok(service.loadUsers(charsSeq)).build();
	}

	@GET
	@Path("/groups/{charsSeq}")
	public Response getGroups(@PathParam("charsSeq") String charsSeq) {
		AuthorityService service = (AuthorityService) getServiceInstance(AuthorityService.class);
		return Response.ok(service.loadGroups(charsSeq)).build();
	}
}
