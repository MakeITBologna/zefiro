package it.makeit.alfresco.restApi;

import java.io.IOException;
import java.io.OutputStream;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.json.Json;
import com.google.api.client.util.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonHttpContent extends AbstractHttpContent {

	private Gson gson;
	private Object data;

	public GsonHttpContent(Gson gson, Object data) {
		super(Json.MEDIA_TYPE);
		this.gson = Preconditions.checkNotNull(gson);
		this.data = Preconditions.checkNotNull(data);
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		String json = gson.toJson(data, TypeToken.get(data.getClass()).getType());
		if (json != null) {
			String encoding = getCharset().name();
			if (encoding == null) {
				out.write(json.getBytes());
			} else {
				out.write(json.getBytes(encoding));
			}
		}
	}

}
