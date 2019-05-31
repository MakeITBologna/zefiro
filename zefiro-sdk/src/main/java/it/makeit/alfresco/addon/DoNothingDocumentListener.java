package it.makeit.alfresco.addon;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;

public class DoNothingDocumentListener implements DocumentListenener {

	public void onDocumentDownload(Document document, Map<String, Object> properties) {
		
	}

	public void onDocumentDelete(Document document, Map<String, Object> properties) {
		
	}

	public void onDocumentUpload(Document document, Map<String, Object> properties) {
		
	}
	
}
