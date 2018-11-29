package it.makeit.alfresco.addon;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;

public interface DocumentListenener {
	
	void onDocumentDelete(Document document, Map<String, Object> properties);

	void onDocumentDownload(Document lDocument, Map<String, Object> properties);

	void onDocumentUpload(Document lDocument, Map<String, Object> properties);

}
