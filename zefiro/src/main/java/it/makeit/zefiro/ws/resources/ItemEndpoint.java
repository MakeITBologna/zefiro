package it.makeit.zefiro.ws.resources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Item;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.alfresco.addon.DocumentListenener;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.DocumentBean;
import it.makeit.zefiro.dao.DocumentPropertyBean;



@Path("/Item")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ItemEndpoint {
	
	private static final String mAlfrescoRootFolderID = JBrickConfigManager.getInstance()
			.getMandatoryProperty("alfresco/@rootFolderId");
	private static Log mLog = Log.getInstance(Item.class);	
	private static final Tika TIKA = new Tika();

	@Inject DocumentListenener documentListenener;
	
	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Path("/{id}")
	public Response getDocumentById(@PathParam("id") String pStrId) {
		Session session = Util.getUserAlfrescoSession(httpRequest);
		
		OperationContext operationContext = session.createOperationContext();
		operationContext.setIncludeRelationships(IncludeRelationships.BOTH);
		
		Item lItem = (Item) session.getObject(pStrId, operationContext);

		return Response.ok(lItem).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insert(DocumentBean itemBean, @Context ServletContext servletContext) {
		Session session = Util.getUserAlfrescoSession(httpRequest);

		// populate property map
		Map<String, Object> mapProperties = new HashMap<String, Object>();

		// common properties
		mapProperties.put(PropertyIds.NAME, itemBean.getName());
		mapProperties.put(PropertyIds.DESCRIPTION, itemBean.getDescription());
		mapProperties.put(PropertyIds.OBJECT_TYPE_ID, itemBean.getType());

		ObjectType typeDef = AlfrescoHelper.getTypeDefinition(session, itemBean.getType());
		
		Map<String, PropertyDefinition<?>> lMapProperties = typeDef.getPropertyDefinitions();
		Map<String, String> propertiesNameKeys = new HashMap<>(); // possono essere differenti: as4:filespool_x002d_as4 -> as4:filespool-x002d-as4 oppure  makeit:citt_x00e0_
		for(Entry<String, PropertyDefinition<?>> e: lMapProperties.entrySet()) {
			propertiesNameKeys.put(e.getValue().getQueryName(), e.getKey());
		}
		
		// type properties
		Map<String, DocumentPropertyBean> documentProperties = itemBean.getProperties();
		for (Map.Entry<String, DocumentPropertyBean> lproperty : documentProperties.entrySet()) {
			Object object = lproperty.getValue().getValue();
			mapProperties.put(propertiesNameKeys.get(lproperty.getKey()), object);
		}

		// get uploaded file and create document
		org.apache.chemistry.opencmis.client.api.Item document = null;
		File lFile = null;
		
		try {
			session.createItem(mapProperties, getFolder(session, itemBean.getType()));
			
			
			
		} catch (Exception e) {
			// TODO: log ...
			throw new JBrickException(e, JBrickException.FATAL);
		} finally {
			deleteFile(lFile);
		}

		return Response.ok(document).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateMetaData(@PathParam("id") String itemId, DocumentBean itemBean) {
		Session session = Util.getUserAlfrescoSession(httpRequest);
		
		Map<String, Object> mapProperties = new HashMap<String, Object>();
		mapProperties.put(PropertyIds.NAME, itemBean.getName());
		mapProperties.put(PropertyIds.DESCRIPTION, itemBean.getDescription());
		
		
		ObjectType typeDef = AlfrescoHelper.getTypeDefinition(session, itemBean.getType());
		
		Map<String, PropertyDefinition<?>> lMapProperties = typeDef.getPropertyDefinitions();
		Map<String, String> propertiesNameKeys = new HashMap<>(); // possono essere differenti: as4:filespool_x002d_as4 -> as4:filespool-x002d-as4 oppure  makeit:citt_x00e0_
		for(Entry<String, PropertyDefinition<?>> e: lMapProperties.entrySet()) {
			propertiesNameKeys.put(e.getValue().getQueryName(), e.getKey());
		}
		

		Map<String, DocumentPropertyBean> documentProperties = itemBean.getProperties();
		for (Map.Entry<String, DocumentPropertyBean> property : documentProperties.entrySet()) {
			Object object = property.getValue().getValue();
			mapProperties.put(propertiesNameKeys.get(property.getKey()), object);
		}			

		Item item = (Item) session.getObject(itemId);
		item = (Item) item.updateProperties(mapProperties);

		return Response.ok(item).build();
	}
	
	private void deleteFile(File lFile) {
		if (lFile != null)
			try {
				FileUtils.forceDelete(lFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private ObjectId getFolder(Session pSession, String pStrDocumentType) {
		String lFileName = pStrDocumentType.substring(pStrDocumentType.lastIndexOf(':') + 1);
		String lFolderPath = AlfrescoHelper.getFolderById(pSession, mAlfrescoRootFolderID).getPath() + "/" + lFileName;
		Folder lFolder = AlfrescoHelper.getFolderByPath(pSession, lFolderPath);
		if (lFolder == null) {
			AlfrescoHelper.createFolder(pSession, mAlfrescoRootFolderID, lFileName);
			lFolder = AlfrescoHelper.getFolderByPath(pSession, lFolderPath);
		}
		
		return lFolder;
	}
}
