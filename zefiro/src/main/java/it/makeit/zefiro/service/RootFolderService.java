package it.makeit.zefiro.service;

import java.util.LinkedHashMap;
import java.util.Map;

import it.makeit.jbrick.JBrickConfigManager;

public class RootFolderService {
	
	public Map<String, String> getRootFolders(){
		Map<String, String> rootFoldersConfiguration = new LinkedHashMap<String, String>();
		
		JBrickConfigManager configManager = JBrickConfigManager.getInstance();
		
		String[] labelList = configManager.getPropertyList("./alfresco", "@label");
		String[] rootFolderList = configManager.getPropertyList("./alfresco", "@rootFolderId");
		
		for (int i = 0; i<labelList.length; i++) {
			rootFoldersConfiguration.put(labelList[i], rootFolderList[i]);
		}
		
		return rootFoldersConfiguration;
	}
	
}
