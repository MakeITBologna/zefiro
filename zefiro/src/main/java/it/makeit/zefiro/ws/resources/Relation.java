package it.makeit.zefiro.ws.resources;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.jbrick.web.LocaleUtil;
import it.makeit.zefiro.Util;
import it.makeit.zefiro.dao.MessageBean;
import it.makeit.zefiro.dao.RelationBean;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;


@Path("/Relation")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class Relation {

	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response insert(RelationBean pRelationBean, @Context
		    HttpServletRequest pRequest) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);

		Relationship lRelationship = null;
		try{
			lRelationship =
					AlfrescoHelper.createRelation(lSession, pRelationBean.getTypeId(), pRelationBean.getSourceId(), pRelationBean.getTargetId());
		} catch (Exception e) {
			if(e instanceof CmisConstraintException ){
				//Prendo il resourceBundle per le label degli errori
				final Locale lLocale = LocaleUtil.getLocale(pRequest.getSession());
				ResourceBundle lResourceBundle = null;
            	if(lLocale != null){
            		lResourceBundle = ResourceBundle.getBundle("ApplicationResources",lLocale);
            	} else {
            		lResourceBundle = ResourceBundle.getBundle("ApplicationResources");
            	}
				
				return Response.status(422).entity(new MessageBean(lResourceBundle.getString("js.error.integrity"))).build(); 
			} else {
				throw e;
			}
		}
		

		return Response.ok(lRelationship).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		AlfrescoHelper.deleteObject(lSession, pStrId);

		return Response.ok().build();
	}
}
