package it.makeit.zefiro.ws.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.zefiro.dao.CustomConfiguration;
	

@Path("/customConfiguration")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CustomConfigurationEndpoint {
	
	@GET
	@Path("/")
	public Response get() {
		List<CustomConfiguration> customConfiguration = new ArrayList<CustomConfiguration>();
		JBrickConfigManager configManager = JBrickConfigManager.getInstance();
		
		String[] typeNames = configManager.getPropertyList("./types/type", "@name");
		
		if(typeNames != null) {
			for (String type: typeNames) {	
				String[] searchField = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/searchField", "@name");
				String[] searchTableColumn = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/searchTableColumn", "@name");
				
				CustomConfiguration conf = new CustomConfiguration();
				conf.setType(type);
				conf.setSearchField(searchField);
				conf.setSearchTableColumn(searchTableColumn);
				
				customConfiguration.add(conf);
			}
	
		}
		
		
		return Response.ok(customConfiguration).build();
	}

}
