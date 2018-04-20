package it.makeit.alfresco;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Eccezione utilizzata per indicare una condizione di errore causata da uno dei
 * componenti di jbrick2; tale eccezione discende da RuntimeException, pertanto
 * lo sviluppatore non è costretto ad gestirla; la gestione di tale eccezione è
 * demandata al filtro <tt>it.makeit.jbrick2.filters.ErrorFilter</tt>
 *
 * @author MAKE IT
 *
 */
/**
 * @author alessio
 *
 */
public class AlfrescoException extends RuntimeException {

	private static final String RESOURCE_BUNDLE = "messages";
	/**
	 * Chiave del messaggio associato ad una eccezione generica all'interno del
	 * resource bundle.
	 */
	public static final String GENERIC_EXCEPTION = "exception.generic";

	/**
	 * Chiave del messaggio associato all'errore di creazione di utente duplicato
	 */
	public static final String DUPLICATED_USER_EXCEPTION = "exception.duplicatedUser";

	/**
	 * Chiave del messaggio associato all'errore di creazione di utente duplicato
	 */
	public static final String FOLDER_ALREADY_EXISTS_EXCEPTION = "exception.duplicatedFolderName";
	
	/**
	 * Chiave del messaggio associato all'errore di creazione di utente duplicato
	 */
	public static final String FOLDER_NOT_FOUND_EXCEPTION = "exception.folderNotFound";

	/**
	 * Chiave del messaggio associato all'errore di creazione di utente duplicato
	 */
	public static final String DOCUMENT_NOT_FOUND_EXCEPTION = "exception.documentNotFound";


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
	 * Costruisce un'istanza di AlfrescoException con la chiave del messaggio
	 * associato all'eccezione all'interno del resource bundle.
	 *
	 * @param pStrResourceBundleKey
	 *            chiave del messaggio associato all'eccezione all'interno del
	 *            resource bundle.
	 * @param pObjArgs
	 *            valori dei parametri del messaggio associato all'eccezione.
	 */
	public AlfrescoException(String pStrResourceBundleKey, String... pStrArgs) {
		mStrResourceBundleKey = pStrResourceBundleKey;
		mStrArgs = pStrArgs;
	}

	/**
	 * Costruisce un'istanza di AlfrescoException a partire dall'istanza di
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
	public AlfrescoException(Throwable pThrowable, String pStrResourceBundleKey, String... pStrArgs) {
		super(pThrowable);
		mStrResourceBundleKey = pStrResourceBundleKey;
		mStrArgs = pStrArgs;
	}

	/* (non-Javadoc)
	 * @see it.makeit.jbrick2.IMessage#getResourceBundleKey()
	 */
	public String getResourceBundleKey() {
		return mStrResourceBundleKey;
	}

	/* (non-Javadoc)
	 * @see it.makeit.jbrick2.IMessage#getArgs()
	 */
	public String[] getArgs() {
		return mStrArgs;
	}

	/**
	 * Restituisce il nome del campo associato al messaggio, che per
	 * <tt>AlfrescoException</tt> è sempre <tt>null</tt>.
	 *
	 * @return nome del campo associato al messaggio (sempre <tt>null</tt>).
	 */
	public String getField() {
		return null;
	}

	/* (non-Javadoc)
	 * @see it.makeit.jbrick2.IMessage#getMessage(java.util.Locale)
	 */
	public String getMessage(Locale pLocale) {
		ResourceBundle lResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, pLocale);
		String lStrMessage =
		        MessageFormat.format(lResourceBundle.getString(getResourceBundleKey()), (Object[]) getArgs());

		return lStrMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return getMessage(Locale.getDefault());
	}
}
