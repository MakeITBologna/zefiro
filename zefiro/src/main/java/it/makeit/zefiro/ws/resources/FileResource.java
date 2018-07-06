package it.makeit.zefiro.ws.resources;

import it.makeit.profiler.dao.UsersBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
@Path("/File")
public class FileResource {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response insert(@FormDataParam("file") FormDataBodyPart pBodyPart, @Context HttpServletRequest pRequest, @Context ServletContext pServletContext) {
		
		UsersBean lUsersBean = (UsersBean) pRequest.getSession().getAttribute("utente");
		String lFileType = pBodyPart.getMediaType().toString();
		InputStream lFileInputStream = pBodyPart.getEntityAs(InputStream.class);
		OutputStream lFileOutput = null;
		String lFileName = lUsersBean.getUsername() + Calendar.getInstance().getTimeInMillis() + ".tmp";
		String lBasePath = pServletContext.getRealPath("/document/");
		File file = null;
		try{
			file = new File(lBasePath , lFileName);
			lFileOutput = new FileOutputStream(file);
			IOUtils.copy(lFileInputStream, lFileOutput);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while uploading file",e);
		} finally {
			if(lFileInputStream != null){
				try{
					lFileInputStream.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(lFileOutput != null){
				try{
					lFileOutput.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return Response.ok("{\"fileName\": \""+ lFileName +"\"}").build();
	}

}
