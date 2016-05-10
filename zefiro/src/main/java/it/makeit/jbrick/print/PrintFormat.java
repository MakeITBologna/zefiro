/**
 * 
 */
package it.makeit.jbrick.print;

/**
 * @author frametta
 *
 */
public enum PrintFormat {
	HTML,
	PDF,
	XLS,
	RTF;
	
	
	public String toString() {
		return this.name().toLowerCase();
	}
}
