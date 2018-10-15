package it.makeit.alfresco.addon;

import org.apache.chemistry.opencmis.client.api.Document;

public interface DocumentListenener {
	void onDocumentDownload(Document document);

	void onDocumentDelete(Document lDocument);

	void onDocumentUpload(Document lDocument);

}
