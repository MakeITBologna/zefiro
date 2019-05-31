package it.makeit.zefiro.dao;

import java.util.List;

public class CustomConfiguration {
	
	private String type;
	private String[] searchField;
	private String[] searchTableColumn;
	private String[] suggestBox;
	private List<StatusBadgeBean> statusBadge;
	private List<ActionBean> actions;
	
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
	public String[] getSuggestBox() {
		return suggestBox;
	}
	public void setSuggestBox(String[] suggestBox) {
		this.suggestBox = suggestBox;
	}
	public List<StatusBadgeBean> getStatusBadge() {
		return statusBadge;
	}
	public void setStatusBadge(List<StatusBadgeBean> statusBadge) {
		this.statusBadge = statusBadge;
	}
	public List<ActionBean> getActions() {
		return actions;
	}
	public void setActions(List<ActionBean> actions) {
		this.actions = actions;
	}

}


