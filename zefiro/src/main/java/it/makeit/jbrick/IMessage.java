package it.makeit.jbrick;

import java.util.Locale;

/**
 * Interfaccia che definisce il comportamento di un messaggio.
 * 
 * @author MAKE IT
 */
public interface IMessage {

	/**
	 * Restituisce la chiave del messaggio all'interno del resource bundle.
	 * 
	 * @return chiave del messaggio all'interno del resource bundle.
	 */
	public String getResourceBundleKey();

	/**
	 * Restituisce i valori dei parametri del messaggio, <tt>null</tt> se il
	 * messaggio non ha parametri.
	 * 
	 * @return valori dei parametri del messaggio, <tt>null</tt> se il
	 *         messaggio non ha parametri.
	 */
	public String[] getArgs();

	/**
	 * Restituisce il nome del campo associato al messaggio <tt>null</tt> se
	 * il messaggio non è associato ad alcun parametro.
	 * 
	 * @return nome del campo associato al messaggio <tt>null</tt> se
	 * il messaggio non è associato ad alcun parametro.
	 */
	public String getField();

	
	/**
	 * Restituisce il livello del messaggio (errore o informazione).
	 * 
	 * @return livello del messaggio (errore o informazione).
	 */
	public Level getLevel();
	
	/**
	 * Restituisce il messaggio già composto in base alla locale.
	 * 
	 * @return String messaggio.
	 */
	public String getMessage(Locale pLocale);

}
