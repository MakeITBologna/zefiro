package it.makeit.zefiro.ws.resources;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.zefiro.Util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;


@Path("/RelationType")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RelationType {
	
	private String mDocumentTypeId = null;

	@Context
	private HttpServletRequest httpRequest;

	public RelationType(String pStrDocumentTypeId, HttpServletRequest pHttpRequest) {
		mDocumentTypeId = pStrDocumentTypeId;
		httpRequest = pHttpRequest;
	}

	@GET
	@Path("/")
	public Response getRelationshipTypes() {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);

		List<RelationshipType> lRelationshipTypes =
			(mDocumentTypeId == null)
			? AlfrescoHelper.getRelationshipTypes(lSession, BaseTypeId.CMIS_RELATIONSHIP.value())
		    : AlfrescoHelper.getAllowedRelationshipTypes(lSession, BaseTypeId.CMIS_RELATIONSHIP.value(), mDocumentTypeId);

		return Response.ok(lRelationshipTypes).build();
	}
}
