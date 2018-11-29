package it.makeit.jbrick.web;

import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author mcallegari
 * Questo wrapper va normalmente usato nelle Servlet e permette di catturare 
 * in una stringa una generica richiesta fatta a Tomcat per poi essere elaborata/salvata/...:
 * 
 * Casi d'uso:
 * - In GD la usano per costruire un manuale HTML che va ricorsivamente su delle cartelle.
 *   Le richieste passano per tomcat per avere risolti i problemi i18n e poi manipolato con Jericho HTML Parser
 * - In Fosfitalia è usato per fare dei PDF a partire dal Xhtml utilizzando xhtmlrenderer
 * 
 * Codice di Esempio:
 * GenericResponseWrapper lGenericResponseWrapper = new GenericResponseWrapper((HttpServletResponse)pResponse);
 * pRequest.getRequestDispatcher(pStrPath + "/content.jsp").include(pRequest, lGenericResponseWrapper);
 * StringBuffer lStringBuffer = new StringBuffer(lGenericResponseWrapper.toString());
 */
public class GenericResponseWrapper extends HttpServletResponseWrapper {
	private static Log mLog=Log.getInstance(GenericResponseWrapper.class);

	private ByteArrayOutputStream output;
	private int contentLength;
	private String contentType;

	public GenericResponseWrapper(HttpServletResponse theResponse) {
		super(theResponse);
		this.output = new ByteArrayOutputStream();
	}

	public byte[] getData() {
		return output.toByteArray();
	}
	public String toString() {
		try {
			return output.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			mLog.error(e);
			throw new JBrickException(e,JBrickException.FATAL);
		}
	}

	
	public ServletOutputStream getOutputStream() {
		return new FilterServletOutputStream(this.output);
	}

	public void setContentLength(int theLength) {
		this.contentLength = theLength;
		super.setContentLength(theLength);
	}

	public int getContentLength() {
		return this.contentLength;
	}

	public void setContentType(String theType) {
		this.contentType = theType;
		super.setContentType(theType);
	}

	public String getContentType() {
		return this.contentType;
	}

	public PrintWriter getWriter() {
		return new PrintWriter(getOutputStream(), true);
	}
}

final class FilterServletOutputStream extends ServletOutputStream {

	private DataOutputStream stream;

	public FilterServletOutputStream(OutputStream output) {
		stream = new DataOutputStream(output);
	}

	public void write(int b) throws IOException {
		stream.write(b);
	}

	public void write(byte[] b) throws IOException {
		stream.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		stream.write(b, off, len);
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener wl) {
		//
	}

}
