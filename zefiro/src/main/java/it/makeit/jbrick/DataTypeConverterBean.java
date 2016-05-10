package it.makeit.jbrick;

/**
 * Contiene tutti i mapping tra il tipo di dato, il pattern con cui convertirlo e la classe deputata alla conversione.
 * 
 * @author MAKE IT
 */
public final class DataTypeConverterBean {

	/**
	 * Nome della classe del tipo di dato da convertire.
	 */
	private String mStrDataType;
	
	/**
	 * Chiave con cui è identificato univocamente il pattern all'interno del resource bundle.
	 */
	private String mStrPattern;
	
	/**
	 * Nome della classe del convertitore.
	 */
	private String mStrConverter;
	
	public DataTypeConverterBean(String pStrDataType, String pStrPattern, String pStrConverter) {
		super();
		this.mStrDataType = pStrDataType;
		this.mStrPattern = pStrPattern;
		this.mStrConverter = pStrConverter;
	}
	
	/**
	 * Ritorna il Nome della classe del tipo di dato da convertire.
	 * 
	 * @return Nome della classe del tipo di dato da convertire.
	 */
	public String getDataType() {
		return mStrDataType;
	}
	
	/**
	 * Ritorna il Chiave con cui è identificato univocamente il pattern all'interno del resource bundle.
	 * 
	 * @return Chiave con cui è identificato univocamente il pattern all'interno del resource bundle.
	 */
	public String getPattern() {
		return mStrPattern;
	}
	
	/**
	 * Ritorna il Nome della classe del convertitore.
	 * 
	 * @returnthe Nome della classe del convertitore. 
	 */
	public String getConverter() {
		return mStrConverter;
	}
	
	
}
