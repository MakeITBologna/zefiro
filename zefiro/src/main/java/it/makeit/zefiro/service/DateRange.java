package it.makeit.zefiro.service;

import java.util.Map;

public class DateRange {
	public String from;
	public String to;
	public Map<String, Object> params;

	public DateRange(String from, String to, Map<String, Object> params) {
		this.from = from;
		this.to = to;
		this.params = params;
	}
}