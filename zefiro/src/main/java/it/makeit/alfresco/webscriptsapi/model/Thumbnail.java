package it.makeit.alfresco.webscriptsapi.model;

import com.google.api.client.util.Key;

public class Thumbnail {

	public static final String FORCE_CREATE = "force";
	public static final String QUEUE_CREATE = "queue";

	@Key
	public String thumbnailName;

	@Key
	public String url;

	public Thumbnail() {}

	public Thumbnail(String pThumbnailName) {
		this.thumbnailName = pThumbnailName;
	}
}
