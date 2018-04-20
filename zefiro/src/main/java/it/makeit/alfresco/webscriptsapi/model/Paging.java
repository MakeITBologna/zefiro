package it.makeit.alfresco.webscriptsapi.model;

import com.google.api.client.util.Key;


public class Paging {

	@Key
	private Integer maxItems;

	@Key
	private Integer skipCount;

	@Key
	private Integer totalItems;

	@Key
	private Integer totalItemsRangeEnd;

	@Key
	private String confidence;


	public Integer getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(Integer maxItems) {
		this.maxItems = maxItems;
	}

	public Integer getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(Integer skipCount) {
		this.skipCount = skipCount;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public Integer getTotalItemsRangeEnd() {
		return totalItemsRangeEnd;
	}

	public void setTotalItemsRangeEnd(Integer totalItemsRangeEnd) {
		this.totalItemsRangeEnd = totalItemsRangeEnd;
	}

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
}
