package it.makeit.jbrick;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Classe che rappresenta un messaggio definito nel resource bundle.
 * 
 * @author MAKE IT
 */
public final class Message implements IMessage, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3666580080951333976L;

	/**
	 * Chiave del messaggio all'interno del resource bundle.
	 */
	private String mStrResourceBundleKey;

	/**
	 * Valori dei parametri del messaggio, <tt>null</tt> se il messaggio non
	 * ha parametri.
	 */
	private String[] mStrArgs;

	/**
	 * Nome del campo associato al messaggio <tt>null</tt> se il messaggio non
	 * è associato ad alcun parametro.
	 */
	private String mStrField;

	/**
	 * Livello del messaggio (errore o informazione).
	 */
	private Level mLevel;

	/**
	 * Costruisce un'istanza di Message di un certo livello associato ad un certo campo
	 * 
	 * @param pStrField
	 *            Campo associato al messaggio
	 * @param pLevel
	 *            Livello del messaggio
	 * @param pStrResourceBundleKey
	 *            chiave del messaggio all'interno del resource bundle.
	 * @param pStrArgs
	 *            valori dei parametri del messaggio.
	 */
	public Message(String pStrField, Level level, String strResourceBundleKey, String... pStrArgs) {
		mStrResourceBundleKey = strResourceBundleKey;
		mStrArgs = pStrArgs;
		mStrField = pStrField;
		mLevel = level;
	}

	/**
	 * Costruisce un'istanza di Message di un certo livello
	 * 
	 * @param pLevel
	 *            Livello del messaggio
	 * @param pStrResourceBundleKey
	 *            chiave del messaggio all'interno del resource bundle.
	 * @param pStrArgs
	 *            valori dei parametri del messaggio.
	 */
	public Message(Level pLevel, String pStrResourceBundleKey, String... pStrArgs) {
		mLevel = pLevel;
		mStrResourceBundleKey = pStrResourceBundleKey;
		mStrArgs = pStrArgs;
		mStrField = null;
	}

	/**
	 * Costruisce un'istanza di Message
	 * 
	 * @param pStrResourceBundleKey
	 *            chiave del messaggio all'interno del resource bundle.
	 * @param pStrArgs
	 *            valori dei parametri del messaggio.
	 */
	public Message(String pStrResourceBundleKey, String... pStrArgs) {
		mStrResourceBundleKey = pStrResourceBundleKey;
		mStrArgs = pStrArgs;
		mStrField = null;
		mLevel = Level.INFO;
	}

	/**
	 * Restituisce la chiave del messaggio all'interno del resource bundle.
	 * 
	 * @return chiave del messaggio all'interno del resource bundle.
	 */
	public String getResourceBundleKey() {
		return mStrResourceBundleKey;
	}

	/**
	 * Restituisce i valori dei parametri del messaggio, <tt>null</tt> se il
	 * messaggio non ha parametri.
	 * 
	 * @return valori dei parametri del messaggio, <tt>null</tt> se il
	 *         messaggio non ha parametri.
	 */
	public String[] getArgs() {
		return mStrArgs;
	}

	/**
	 * Restituisce il nome del campo associato al messaggio <tt>null</tt> se
	 * il messaggio non è associato ad alcun parametro.
	 * 
	 * @return nome del campo associato al messaggio <tt>null</tt> se il
	 *         messaggio non è associato ad alcun parametro.
	 */
	public String getField() {
		return mStrField;
	}

	/**
	 * Restituisce il livello del messaggio (errore o informazione).
	 * 
	 * @return livello del messaggio (errore o informazione).
	 */
	public Level getLevel() {
		return mLevel;
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
