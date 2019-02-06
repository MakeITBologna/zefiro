package it.makeit.zefiro.ws.resources;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.zefiro.dao.CustomConfiguration;
import it.makeit.zefiro.service.RootFolderService;
import it.makeit.zefiro.service.TypeConfigurationService;

@Path("/configuration")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ConfigurationEndpoint {
	//TODO: Rinominare Endpoint principale searchProperties -> 
	
	@GET
	@Path("/type")
	public Response getSearchProperties() {
		List<CustomConfiguration> typeConfiguration = new TypeConfigurationService().getTypeConfigurations(); 
		return Response.ok(typeConfiguration).build();
	}
	
	@GET
	@Path("/rootFolders")
	public Response getRootFolders() {
		Map<String, String> rootFoldersConfiguration = new RootFolderService().getRootFolders();
		return Response.ok(rootFoldersConfiguration).build();
	}
	

}
