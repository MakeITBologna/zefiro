package it.makeit.zefiro.ws.serializers;

import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.RelationshipType;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * Imlpementazione di un serializzatore per {@link ObjectType} CMIS.
 * 
 * @author Alessio Gaeta
 *
 */
@SuppressWarnings("serial")
public class ObjectTypeSerializer extends StdSerializer<ObjectType> {

	// TODO (Alessio): logging

	public ObjectTypeSerializer() {
		super(ObjectType.class);
	}

	@Override
	public void serialize(ObjectType value, JsonGenerator gen, SerializerProvider serializers)
	        throws IOException, JsonProcessingException {

		gen.writeStartObject();

		gen.writeStringField("id", value.getId());
		gen.writeStringField("name", value.getDisplayName());
		gen.writeStringField("description", value.getDescription());
		gen.writeStringField("baseType", value.getBaseType() != null ? value.getBaseType().getId() : /*solo */ value.getId()); 
		
		gen.writeBooleanField("isSecondary",
			(value.getBaseTypeId() == null)? false : BaseTypeId.CMIS_SECONDARY.value().equals(value.getBaseTypeId().value()));
		
		if (value instanceof RelationshipType) {
			gen.writeFieldName("allowedSourceTypes");
			gen.writeObject(((RelationshipType) value).getAllowedSourceTypes());
			
			gen.writeFieldName("allowedTargetTypes");
			gen.writeObject(((RelationshipType) value).getAllowedTargetTypes());
		}
		
		// Proprietà
		gen.writeFieldName("properties");
		gen.writeObject(value.getPropertyDefinitions());
		
		// Aspetti
		gen.writeArrayFieldStart("aspects");
		if (value.getExtensions() != null) {
			for (CmisExtensionElement lExtension : value.getExtensions()) {
				if (lExtension.getName().matches("(?i:.*aspect.*)")) {
					for (CmisExtensionElement lAspectExtension : lExtension.getChildren()) {
						gen.writeString(lAspectExtension.getValue());
					}
				}
			}
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}

	@Override
	public Class<ObjectType> handledType() {
		return ObjectType.class;
	}
}
