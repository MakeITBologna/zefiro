package it.makeit.zefiro.ws.resources;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.zefiro.Util;

import java.util.ArrayList;
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
	private String alfrescobasetypeid = null;
	private String alfrescobasetypeitemid = null;

	@Context
	private HttpServletRequest httpRequest;

	public RelationType(String pStrDocumentTypeId, HttpServletRequest pHttpRequest, String malfrescobasetypeid, String malfrescobasetypeitemid) {
		mDocumentTypeId = pStrDocumentTypeId;
		httpRequest = pHttpRequest;
		
		this.alfrescobasetypeid = malfrescobasetypeid;
		this.alfrescobasetypeitemid = malfrescobasetypeitemid;
		
	}

	@GET
	@Path("/")
	public Response getRelationshipTypes() {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		
		if(mDocumentTypeId == null) {
			List<RelationshipType> lRelationshipTypes   = AlfrescoHelper.getRelationshipTypes(lSession, BaseTypeId.CMIS_RELATIONSHIP.value());
			return Response.ok(lRelationshipTypes).build(); 
		} else {
			List<RelationshipType> lRelationshipTypes = new ArrayList<>(); 
			
			lRelationshipTypes.addAll(AlfrescoHelper.getAllowedRelationshipTypes(lSession, BaseTypeId.CMIS_RELATIONSHIP.value(), mDocumentTypeId));
		
			List<String> parentTypeIds = AlfrescoHelper.getParentTypeIds(lSession, mDocumentTypeId);
			
			for(String parentTypeId: parentTypeIds) {
				
				
				lRelationshipTypes.addAll(AlfrescoHelper.getAllowedRelationshipTypes(lSession, BaseTypeId.CMIS_RELATIONSHIP.value(), parentTypeId));
				
				if(parentTypeId.equals(alfrescobasetypeid) || parentTypeId.equals(alfrescobasetypeitemid)) {
					break; // non controlliamo i tipi al di fuori della root
				}
				
			}
			
			return Response.ok(lRelationshipTypes).build();
		}
		
	}
}
