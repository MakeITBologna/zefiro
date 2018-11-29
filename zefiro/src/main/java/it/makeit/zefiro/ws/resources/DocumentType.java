package it.makeit.zefiro.ws.resources;

import it.makeit.alfresco.AlfrescoHelper;
import it.makeit.jbrick.JBrickConfigManager;
import it.makeit.zefiro.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

@Path("/DocumentType")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class DocumentType {
	// Ottengo la lista delle proprietà che non voglio visualizzare sul client
	private String[] mPropertyBlacklist = JBrickConfigManager.getInstance().getPropertyList("propertyBlacklist/entry",	"@name");
	private static final String mAlfrescoBaseTypeId = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@baseTypeId");
	private static final String mAlfrescoBaseTypeItemId = JBrickConfigManager.getInstance().getMandatoryProperty("alfresco/@baseTypeItemId");

	@Context
	private HttpServletRequest httpRequest;

	// TODO (Alessio): mantenere una sessione Alfresco permanente per interrogare i
	// tipi documento,
	// posto che faccia caching anche dei tipi documento (da verificare).
	// Si tratta di dati stabili e che variano solo per intervento di un utente
	// privilegiato
	// mediante una maschera apposita: alla variazione si potrebbe fare un refresh
	// dell'oggetto
	// Session.

	@GET
	@Path("/")
	public Response getDocTypes() {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		
		List<ObjectType> lObjectTypeTreeLeaves = AlfrescoHelper.getTypesTreeLeaves(lSession, mAlfrescoBaseTypeId, true);
		
		for (ObjectType lObjectType : lObjectTypeTreeLeaves)
			addAspects(lSession, lObjectType);
		
		
		List<ObjectType> itemTreeLeaves = AlfrescoHelper.getTypesTreeLeaves(lSession, mAlfrescoBaseTypeItemId, true);
		lObjectTypeTreeLeaves.addAll(itemTreeLeaves);

		return Response.ok(lObjectTypeTreeLeaves).build();
	}

	@GET
	@Path("/{id}")
	public Response getDocTypeById(@PathParam("id") String pStrId) {
		Session lSession = Util.getUserAlfrescoSession(httpRequest);
		ObjectType lObjectType = AlfrescoHelper.getTypeDefinition(lSession, pStrId);
		addAspects(lSession, lObjectType);
		return Response.ok(lObjectType).build();

	}

	private void addAspects(Session lSession, ObjectType lObjectType) {
		for (CmisExtensionElement ce : lObjectType.getExtensions()) {
			if (ce.getName().equals("mandatoryAspects")) {

				for (CmisExtensionElement aspect : ce.getChildren()) {

					TypeDefinition aspectType = lSession.getTypeDefinition(aspect.getValue());

					for (Entry<String, PropertyDefinition<?>> e : aspectType.getPropertyDefinitions().entrySet())
						if (!lObjectType.getPropertyDefinitions().containsKey(e.getKey())) {
							lObjectType.getPropertyDefinitions().put(e.getKey(), e.getValue());
						}
				}
			}
		}
	}

	@Path("/{id}/relation")
	public RelationType getAllowedRelationTypes(@PathParam("id") String pStrId) {
		return new RelationType(pStrId, httpRequest, mAlfrescoBaseTypeId, mAlfrescoBaseTypeItemId);
	}

	
}
