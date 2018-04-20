package it.makeit.jbrick;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class Log4jMDCFilter extends Filter {

	private String key;
	private String value;
	private int onMatch;
	private int onNoMatch;

	
	@Override
	public int decide(LoggingEvent pLoggingEvent) {
		//Se non è manco configurata la chiave su cui lavorare siamo neutri
		if (key == null) {
			return NEUTRAL;
		}
		
		//Se non ho key decido che non ho match!!
		if (pLoggingEvent.getMDC(key) == null) {
				return onNoMatch;
		} 
		
		//Visto che ho un MDCvalue mi basta quello se value è null o vuoto
		if (value == null || value.equals("")) {
			return onMatch;
		}
		
		//Visto che ho anche un value li confronto e decido
		String lStrMDCValue = String.valueOf(pLoggingEvent.getMDC(key));
		if(lStrMDCValue.equalsIgnoreCase(value)) {
			return onMatch;
		} else {
			return onNoMatch;
		}
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOnMatch() {
		return onMatch;
	}

	public void setOnMatch(int onMatch) {
		this.onMatch = onMatch;
	}

	public int getOnNoMatch() {
		return onNoMatch;
	}

	public void setOnNoMatch(int onNoMatch) {
		this.onNoMatch = onNoMatch;
	}

}
