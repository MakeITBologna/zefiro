package it.makeit.jbrick;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


public class MessageUtil {

	// logger
	private static Log mLog=Log.getInstance(MessageUtil.class);
	private static final String MESSAGES = "messages";

	/**Check if we have messages for the user
	 *  
	 * @param pRequest - request from which retrieve information
	 * @throws IllegalArgumentException
	 */
	public static boolean isMessages(HttpServletRequest pRequest)
	throws IllegalArgumentException {
		if(pRequest!=null){
			List<IMessage> lListMessages = getMessages(pRequest);
			if (lListMessages.size() > 0) return true;
			else return false;
		}
		else
		{
			mLog.error("request has null value.");
			throw new IllegalArgumentException(
			"request has null value.");
		}
	}

	/**Insert single message in request messages
	 *  
	 * @param pIMessage - single message to add
	 * @param pRequest - request from which retrieve information
	 * @throws IllegalArgumentException
	 */
	public static void addMessage(IMessage pIMessage, HttpServletRequest pRequest)
	throws IllegalArgumentException {
		if(pIMessage!=null && pRequest!=null){
			List<IMessage> lListMessages = getMessages(pRequest);
			lListMessages.add(pIMessage);
			setMessages(pRequest, lListMessages);
		}
		else
		{
			mLog.error("message or request has null value.");
			throw new IllegalArgumentException(
			"message or request has null value.");
		}
	}
	
	
	/**Insert multiple messages in request messages
	 * 
	 * @param pRequest - request from which retrieve information
	 * @param pListIMessage - list of messages to add
	 * @throws IllegalArgumentException
	 */
	public static void addMessages(HttpServletRequest pRequest, List<IMessage> pListIMessage)
	throws IllegalArgumentException{
		
		if(pListIMessage!=null && pRequest!=null){
			List<IMessage> lListMessages = getMessages(pRequest);
			lListMessages.addAll(pListIMessage);
			setMessages(pRequest, lListMessages);
		}
		else
		{
			mLog.error("messages or request has null value.");
			throw new IllegalArgumentException(
			"messages or request has null value.");
		}
	}
	
	/**Retrieve message list from "message" request attribute
	 * 
	 * @param pServletRequest - request from which retrieve information
	 * @return List of messages
	 */
	@SuppressWarnings("unchecked")
	private static List<IMessage> getMessages(ServletRequest pServletRequest){
		List<IMessage> lListReturn; 
		lListReturn = (List<IMessage>)pServletRequest.getAttribute(MessageUtil.MESSAGES);
		//se non sono presenti messaggi, creo una nuova lista
		if (lListReturn==null){
			lListReturn = new ArrayList<IMessage>();
		}
		return lListReturn;
	}
	
	/**Update Request with new message List
	 * 
	 * @param pRequest - request to update
	 * @param pListIMessage - message list to insert
	 **/
	private static void setMessages(ServletRequest pRequest, List<IMessage> pListIMessage) {
		pRequest.setAttribute(MESSAGES, pListIMessage);
	}
}
