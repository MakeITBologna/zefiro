package it.makeit.zefiro.ws.serializers;

import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Relationship;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * Imlpementazione di un serializzatore per {@link ObjectType} CMIS.
 *
 */
@SuppressWarnings("serial")
public class RelationshipSerializer extends StdSerializer<Relationship> {

	public RelationshipSerializer() {
		super(Relationship.class);
	}

	@Override
	public void serialize(Relationship value, JsonGenerator gen, SerializerProvider serializers)
	        throws IOException, JsonProcessingException {
		
		gen.writeStartObject();
		
		gen.writeStringField("id", value.getId());
		gen.writeStringField("name", value.getName());
		gen.writeStringField("description",  value.getDescription());
		
		gen.writeObjectField("baseType", value.getBaseType());
		gen.writeObjectField("baseTypeId", value.getBaseTypeId());
		
		gen.writeObjectField("type", value.getType());
		
		gen.writeObjectField("source", value.getSource());
		gen.writeObjectField("target", value.getTarget());
		gen.writeObjectField("sourceId", value.getSourceId());
		gen.writeObjectField("targetId", value.getTargetId());
		
		gen.writeEndObject();
	}

	@Override
	public Class<Relationship> handledType() {
		return Relationship.class;
	}
}
