package it.makeit.zefiro.ws.serializers;

import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.Property;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


@SuppressWarnings("serial")
public class PropertySerializer extends StdSerializer<Property<?>> {

	public PropertySerializer() {
		super(Property.class, true);
	}

	@Override
	public void serialize(Property<?> value, JsonGenerator jgen, SerializerProvider provider)
	        throws IOException, JsonGenerationException {

		jgen.writeStartObject();

		jgen.writeStringField("queryName", value.getQueryName());
		jgen.writeStringField("displayName", value.getDisplayName());
		jgen.writeStringField("propertyType", value.getType().toString());

		jgen.writeFieldName("value");
		jgen.writeObject(value.getValue());

		jgen.writeObjectField("definition", value.getDefinition());

		jgen.writeEndObject();
	}

}
