package it.makeit.zefiro.ws.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.lang.StringUtils;

import it.makeit.alfresco.CmisQueryBuilder;
import it.makeit.alfresco.CmisQueryPredicate;
import it.makeit.zefiro.Util;


@Path("/suggest")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SuggestEndpoint {
	
	@Context
	private HttpServletRequest httpRequest;
	
	@GET
	@Path("/{type}/{property}")
	public Response getSuggestion(@PathParam("type") String type, @PathParam("property") String property, @QueryParam("hint") String hint){
		
		Session alfrescoSession = Util.getUserAlfrescoSession(httpRequest);
		String rootFolderId = (String) httpRequest.getSession().getAttribute("rootFolderId");
		
		CmisQueryBuilder queryBuilder = new CmisQueryBuilder(alfrescoSession);

		String query = queryBuilder.selectFrom(type, (String[]) null)
									.where(CmisQueryPredicate.like(property, "%" + hint + "%"), CmisQueryPredicate.inTree(rootFolderId))
									.build();
		
		ItemIterable<QueryResult> results = alfrescoSession.query(query, false);
		Iterator<QueryResult> result = results.iterator();
		List<String> suggestions = new ArrayList<String>();

		Integer counter = 0;
		Integer queryLimit = 1000;
		Integer resultLimit = 5;
		
		while (result.hasNext() && counter < queryLimit && suggestions.size() < resultLimit){			
			QueryResult r = result.next();			
			String suggestion = r.getPropertyValueById(property);
			if (!suggestions.contains(suggestion) && StringUtils.containsIgnoreCase(suggestion, hint)) {
				suggestions.add(suggestion);
			};
			counter++;
		}
				
		return Response.ok(suggestions).build();
	}
	
}
