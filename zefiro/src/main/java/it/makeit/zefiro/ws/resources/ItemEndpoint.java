package it.makeit.zefiro.ws.resources;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.Item;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;

import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.DocumentBean;
import it.makeit.zefiro.dao.DocumentPropertyBean;

@Path("/Item")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ItemEndpoint {

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
	

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateMetaData(@PathParam("id") String itemId, DocumentBean itemBean) {
		Session session = Util.getUserAlfrescoSession(httpRequest);
		
		Map<String, Object> mapProperties = new HashMap<String, Object>();
		mapProperties.put(PropertyIds.NAME, itemBean.getName());
		mapProperties.put(PropertyIds.DESCRIPTION, itemBean.getDescription());

		Map<String, DocumentPropertyBean> documentProperties = itemBean.getProperties();
		for (Map.Entry<String, DocumentPropertyBean> property : documentProperties.entrySet()) {
			mapProperties.put(property.getKey(), property.getValue().getValue());
		}			

		Item item = (Item) session.getObject(itemId);
		item = (Item) item.updateProperties(mapProperties);

		return Response.ok(item).build();
	}
}
