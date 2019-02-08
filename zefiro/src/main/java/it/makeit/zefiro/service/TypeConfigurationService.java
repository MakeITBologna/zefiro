package it.makeit.zefiro.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.zefiro.dao.ActionBean;
import it.makeit.zefiro.dao.CustomConfiguration;
import it.makeit.zefiro.dao.StatusBadgeBean;
import it.makeit.zefiro.dao.StatusBadgeOptionType;

public class TypeConfigurationService {
	
	public List<CustomConfiguration> getTypeConfigurations() {
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
						String alfrescoDir = configManager.getPropertyList("./types/type[@name='"+ type +"']/actions/action[@name='"+actionName+"']", "@alfrescoDir")[0];
						ActionBean action = new ActionBean();
						action.setName(actionName);
						action.setPortalEvent(portalEvent);
						action.setAlfrescoDir(alfrescoDir);
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
		return customConfiguration;
	}
		
}
