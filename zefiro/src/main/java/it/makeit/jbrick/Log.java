package it.makeit.jbrick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public final class Log {
	
    private org.slf4j.Logger realLog;

    /**
     * Private constructor to force getInstance.
     */
    private Log() {
    }

    /**
     * Private constructor which creates a new Log instance wrapping the commons Log instance
     * provided.  Only used by the static getInstance() method on this class.
     */
    private Log(org.slf4j.Logger realLog) {
        this.realLog = realLog;
    }

    /**
     * Get a Log instance to perform logging within the Class specified.  Returns an instance
     * of this class which wraps an instance of the commons logging Log class.
     * @param clazz the Class which is going to be doing the logging
     * @return a Log instance with which to log
     */
    public static Log getInstance(Class<?> clazz) {
        return new Log(LoggerFactory.getLogger(clazz));
    }

    /**
     * Get a Log instance to perform logging within the String specified.  Returns an instance
     * of this class which wraps an instance of the commons logging Log class.
     * @param clazz the Class which is going to be doing the logging
     * @return a Log instance with which to log
     */
    public static Log getInstance(String name) {
        return new Log(LoggerFactory.getLogger(name));
    }

    /**
     * Logs a Throwable and optional message parts at level error.
     * @param throwable an instance of Throwable that should be logged with stack trace
     * @param messageParts zero or more objects which should be combined, by calling toString()
     *        to form the log message.
     */
    public final void error(Throwable throwable, Object... messageParts) {
        if (this.realLog.isErrorEnabled()) {
            this.realLog.error(combineParts(messageParts), throwable);
        }
    }

    /**
     * Logs one or more message parts at level error.
     * @param messageParts one or more objects which should be combined, by calling toString()
     *        to form the log message.
     */
    public final void error(Object... messageParts) {
        if (this.realLog.isErrorEnabled()) {
            this.realLog.error(combineParts(messageParts));
        }
    }

    /**
     * Logs one or more message parts at level info.
     * @param messageParts one or more objects which should be combined, by calling toString()
     *        to form the log message.
     */
    public final void info(Object... messageParts) {
        if (this.realLog.isInfoEnabled()) {
            this.realLog.info(combineParts(messageParts));
        }
    }

    /**
     * Logs one or more message parts at level debug.
     * @param messageParts one or more objects which should be combined, by calling toString()
     *        to form the log message.
     */
    public final void debug(Object... messageParts) {
        if (this.realLog.isDebugEnabled()) {
            this.realLog.debug(combineParts(messageParts));
        }
    }

    /**
     * Combines all the message parts handed in to the logger in order to pass them in to
     * the commons logging interface.
     */
    private String combineParts(Object[] messageParts) {
        StringBuilder builder = new StringBuilder(128);
        for (Object part : messageParts) {
            builder.append(String.valueOf(part));
        }
        return builder.toString();
    }

	/**
	 * Restituisce il valore di un oggetto in forma di stringa per utilizzo nei
	 * log. In particolare:<br>
	 * 1) Se il campo è null restituisce NULL<br>
	 * 2) Se il campo è di tipo stringa restituisce il valore del campo tra
	 * apici singoli (') <br>
	 * 3) Se il campo è di altro tipo restituisce la rappresentazione in forma
	 * di stringa del campo ottenuta mediante la chiamata al metodo
	 * <tt>toString<tt>.
	 * 
	 * @param pObject valore del campo
	 * @return valore di un campo in forma di stringa per utilizzo nei log.
	 */
	public static String getValue4Log(Object pObject) {
		return String.valueOf(pObject);
	}
	
	
		/**
	 * @return
	 * @see org.apache.log4j.Category#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return realLog.isDebugEnabled();
	}

	/**
	 * @return
	 * @see org.apache.log4j.Category#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return realLog.isInfoEnabled();
	}
	 
	/*
    public void reloadConfiguration(String pStrFilePath) {
    	info("start Configuration reset ");
    	LogManager.resetConfiguration();
    	DOMConfigurator.configure(pStrFilePath);
    	info("finish Configuration reset");
    }*/
    
    public static void mdcPut(String pStrKey, String pStrValue) {
    	MDC.put(pStrKey,pStrValue);
    }
    public static void mdcRemove(String pStrKey) {
    	MDC.remove(pStrKey);
    }
    public static void mdcGet(String pStrKey) {
    	MDC.get(pStrKey);
    }
}
