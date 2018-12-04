package it.makeit.zefiro.dao;

public class CustomConfiguration {
	
	private String type;
	private String[] searchField;
	private String[] searchTableColumn;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getSearchField() {
		return searchField;
	}
	public void setSearchField(String[] searchField) {
		this.searchField = searchField;
	}
	public String[] getSearchTableColumn() {
		return searchTableColumn;
	}
	public void setSearchTableColumn(String[] searchTableColumn) {
		this.searchTableColumn = searchTableColumn;
	}
}
