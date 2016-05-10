package it.makeit.jbrick.web;

//elenca i possibili tipi di Scope
public enum WebScope {
	PAGE,
	SESSION,
	REQUEST,
	APPLICATION;
	
	public String toString() {
		return this.name().toLowerCase();
	}
}
