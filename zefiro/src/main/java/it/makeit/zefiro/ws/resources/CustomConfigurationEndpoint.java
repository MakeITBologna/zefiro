package it.makeit.zefiro.ws.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.zefiro.dao.ActionBean;
import it.makeit.zefiro.dao.CustomConfiguration;
import it.makeit.zefiro.dao.StatusBadgeBean;
import it.makeit.zefiro.dao.StatusBadgeOptionType;

@Path("/customConfiguration")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CustomConfigurationEndpoint {
	//TODO: Rinominare Endpoint principale searchProperties -> 
	
	@GET
	@Path("/searchProperties")
	public Response getSearchProperties() {
		List<CustomConfiguration> customConfiguration = new ArrayList<CustomConfiguration>();
		JBrickConfigManager configManager = JBrickConfigManager.getInstance();
		String[] typeNames = configManager.getPropertyList("./types/type", "@name");
		
		if(typeNames != null) {
			for (String type: typeNames) {	
				
				String[] searchField = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/searchField", "@name");
				String[] suggestBox = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/suggestBoxes/suggestBox", "@name");
				String[] searchTableColumn = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/searchTableColumn", "@name");
				
				List<StatusBadgeBean> statusBadge = new ArrayList<StatusBadgeBean>();
				String[] badgeNames = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/statusBadges/statusBadge", "@name");
				if (badgeNames != null) {
					Arrays.asList(badgeNames).forEach(badgeName -> {
						StatusBadgeBean sb = new StatusBadgeBean();
						String[] value = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/statusBadges/statusBadge[@name='"+badgeName+"']/option", "@value");
						String[] style = configManager.getPropertyList("./types/type[@name='"+ type +"']/search/statusBadges/statusBadge[@name='"+badgeName+"']/option", "@style");
						List<StatusBadgeOptionType> options = new ArrayList<StatusBadgeOptionType>();
						for (int i=0; i < value.length; i++) {
							StatusBadgeOptionType option = new StatusBadgeOptionType();
							option.setValue(value[i]);
							option.setStyle(style[i]);
							options.add(option);
						};	
						StatusBadgeOptionType[] optionsArray = new StatusBadgeOptionType[options.size()];
						options.toArray(optionsArray);
						sb.setName(badgeName);
						sb.setOption(optionsArray);
						statusBadge.add(sb);
					});
				};
				
				List<ActionBean> actionList = new ArrayList<ActionBean>();
				String[] actionNames = configManager.getPropertyList("./types/type[@name='"+ type +"']/actions/action", "@name");
				if (actionNames != null) {
					Arrays.asList(actionNames).forEach(actionName -> {
						String portalEvent = configManager.getPropertyList("./types/type[@name='"+ type +"']/actions/action[@name='"+actionName+"']", "@portalEvent")[0];
						ActionBean action = new ActionBean();
						action.setName(actionName);
						action.setPortalEvent(portalEvent);
						actionList.add(action);
					});
				};
				
				CustomConfiguration conf = new CustomConfiguration();
				conf.setType(type);
				conf.setActions(actionList);
				conf.setSearchField(searchField);
				conf.setSearchTableColumn(searchTableColumn);
				conf.setSuggestBox(suggestBox != null? suggestBox : new String[0]);
				conf.setStatusBadge(statusBadge);

				customConfiguration.add(conf);
			}
		}
		return Response.ok(customConfiguration).build();
	}
	
	@GET
	@Path("/rootFolders")
	public Response getRootFolders() {
		
		Map<String, String> rootFoldersConfiguration = new LinkedHashMap<String, String>();
		
		JBrickConfigManager configManager = JBrickConfigManager.getInstance();
		
		String[] labelList = configManager.getPropertyList("./alfresco", "@label");
		String[] rootFolderList = configManager.getPropertyList("./alfresco", "@rootFolderId");
		
		for (int i = 0; i<labelList.length; i++) {
			rootFoldersConfiguration.put(labelList[i], rootFolderList[i]);
		}

		return Response.ok(rootFoldersConfiguration).build();
	}
	

}
