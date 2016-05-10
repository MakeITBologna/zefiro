package it.makeit.jbrick;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Eccezione utilizzata per indicare una condizione di errore causata da uno dei
 * componenti di jbrick2; tale eccezione discende da RuntimeException, pertanto
 * lo sviluppatore non è costretto ad gestirla; la gestione di tale eccezione è
 * demandata al filtro <tt>it.makeit.jbrick.filters.ErrorFilter</tt>
 * 
 * @author MAKE IT
 * 
 */
public class JBrickException extends RuntimeException implements IMessage {

	/**
	 * Chiave del messaggio associato ad una eccezione fatale all'interno del
	 * resource bundle.
	 */
	public static final String FATAL = "jBrickException.fatal";

	/**
	 * Chiave del messaggio associato ad una SQLException all'interno del
	 * resource bundle.
	 */
	public static final String SQL_EXCEPTION = "jBrickException.SQLException";

	/**
	 * Chiave del messaggio associato ad una IOException all'interno del
	 * resource bundle.
	 */
	public static final String IO_EXCEPTION = "jBrickException.IOException";

	/**
	 * Chiave del messaggio associato ad una IOException all'interno del
	 * resource bundle.
	 */
	public static final String REFLECTION_EXCEPTION = "jBrickException.ReflectionException";

	private static final long serialVersionUID = 1L;

	/**
	 * Chiave del messaggio associato all'eccezione all'interno del resource
	 * bundle.
	 */
	private String mStrResourceBundleKey;

	/**
	 * Valori dei parametri del messaggio associato all'eccezione, <tt>null</tt>
	 * se il messaggio non ha parametri.
	 */
	private String[] mStrArgs;

	/**
	 * Costruisce un'istanza di jBrickException con la chiave del messaggio
	 * associato all'eccezione all'interno del resource bundle.
	 * 
	 * @param pStrResourceBundleKey
	 *            chiave del messaggio associato all'eccezione all'interno del
	 *            resource bundle.
	 * @param pObjArgs
	 *            valori dei parametri del messaggio associato all'eccezione.
	 */
	public JBrickException(String pStrResourceBundleKey, String... pStrArgs) {
		mStrResourceBundleKey = pStrResourceBundleKey;
		mStrArgs = pStrArgs;
	}

	/**
	 * Costruisce un'istanza di jBrickException a partire dall'istanza di
	 * Throwable ricevuta in ingresso e dalla chiave del messaggio associato
	 * all'eccezione all'interno del resource bundle.
	 * 
	 * @param pThrowable
	 *            causa dell'eccezione.
	 * @param pStrResourceBundleKey
	 *            chiave del messaggio associato all'eccezione all'interno del
	 *            resource bundle.
	 * @param pObjArgs
	 *            valori dei parametri del messaggio associato all'eccezione.
	 */
	public JBrickException(Throwable pThrowable, String pStrResourceBundleKey, String... pStrArgs) {
		super(pThrowable);
		mStrResourceBundleKey = pStrResourceBundleKey;
		mStrArgs = pStrArgs;
	}

	/**
	 * Restituisce la chiave del messaggio associato all'eccezione all'interno
	 * del resource bundle.
	 * 
	 * @return chiave del messaggio associato all'eccezione all'interno del
	 *         resource bundle.
	 */
	public String getResourceBundleKey() {
		return mStrResourceBundleKey;
	}

	/**
	 * Restituisce i valori dei parametri del messaggio associato all'eccezione,
	 * <tt>null</tt> se il messaggio non ha parametri.
	 * 
	 * @return valori dei parametri del messaggio associato all'eccezione,
	 *         <tt>null</tt> se il messaggio non ha parametri.
	 */
	public String[] getArgs() {
		return mStrArgs;
	}

	/**
	 * Restituisce il nome del campo associato al messaggio, che per
	 * <tt>jBrickException</tt> è sempre <tt>null</tt>.
	 * 
	 * @return nome del campo associato al messaggio, che per
	 *         <tt>jBrickException</tt> è sempre <tt>null</tt>.
	 */
	public String getField() {
		return null;
	}

	/**
	 * Restituisce il livello del messaggio (errore o informazione) che per
	 * <tt>jBrickException</tt> è sempre Level.ERROR.
	 * 
	 * @return livello del messaggio (errore o informazione) che per
	 *         <tt>jBrickException</tt> è sempre Level.ERROR.
	 */
	public Level getLevel() {
		return Level.ERROR;
	}

	/**
	 * Restituisce il messaggio già composto in base alla locale.
	 * 
	 * @return String messaggio.
	 */
	public String getMessage(Locale pLocale) {
		ResourceBundle lResourceBundle = ResourceBundle.getBundle(Constants.RESOURCEBUNDLE, pLocale);
		String lStrMessage = MessageFormat.format(lResourceBundle.getString(getResourceBundleKey()), getArgs());
		return lStrMessage;
	}
}
