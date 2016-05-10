package it.makeit.zefiro.ws.serializers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.chemistry.opencmis.commons.definitions.Choice;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDecimalDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyIntegerDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyStringDefinition;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


@SuppressWarnings("serial")
public class PropertyDefinitionSerializer extends StdSerializer<PropertyDefinition<?>> {

	// TODO (Alessio): logging

	public PropertyDefinitionSerializer() {
		super(PropertyDefinition.class, true);
	}

	protected PropertyDefinitionSerializer(Class<? extends PropertyDefinition<?>> t) {
		super(t, true);
	}

	protected final void writeCommonProperties(PropertyDefinition<?> value, JsonGenerator gen,
	        SerializerProvider serializers)
	        throws IOException {

		gen.writeStringField("name", value.getId());
		gen.writeStringField("queryName", value.getQueryName());
		gen.writeStringField("propertyType", getPropertyType(value.getPropertyType()));

		gen.writeFieldName("defaultValue");
		List<?> lListDefaults = value.getDefaultValue();
		gen.writeObject(lListDefaults.isEmpty() ? null : lListDefaults.get(0));

		gen.writeArrayFieldStart("choices");
		writeChoices(gen, value);
		gen.writeEndArray();

		gen.writeStringField("displayName", value.getDisplayName());
		gen.writeStringField("description", value.getDescription());
		gen.writeBooleanField("required", value.isRequired());
		gen.writeBooleanField("queryable", value.isQueryable());
		
		gen.writeStringField("cardinality", (value.getCardinality() == null)? null : value.getCardinality().value());
	}

	// Necessario solo perché si gestiscono meno tipi di quelli esistenti
	private String getPropertyType(final PropertyType pPropertyType) {
		// I tipi id, html e URI sono (almeno per ora) restituiti come stringhe
		switch (pPropertyType) {
		case ID:
		case HTML:
		case URI:
			return PropertyType.STRING.toString();

		default:
			return pPropertyType.toString();
		}
	}

	protected void writeChoices(final JsonGenerator gen, final PropertyDefinition<?> pPropDef) throws IOException {
		for (Choice<?> choice : pPropDef.getChoices()) {
			// FIXME (Alessio): non vengono gestite le scelte di proprietà multivalore. In questo
			// caso viene restituito il primo valore della lista.

			if (choice.getValue().isEmpty()) {
				// Chissà se è un caso possibile...
				continue;
			}

			Object lValue = choice.getValue().get(0);
			switch (pPropDef.getPropertyType()) {
			case BOOLEAN:
				gen.writeBoolean((boolean) lValue);
				break;

			case DATETIME:
				// TODO (Alessio)
				break;

			case DECIMAL:
				gen.writeNumber((BigDecimal) lValue);
				break;

			case INTEGER:
				gen.writeNumber((BigInteger) lValue);
				break;

			case STRING:
			case ID:
			case HTML:
			case URI:
				gen.writeString((String) lValue);
				break;
			}
		}
	}

	@Override
	public void serialize(PropertyDefinition<?> value, JsonGenerator gen, SerializerProvider serializers)
	        throws IOException, JsonProcessingException {

		gen.writeStartObject();
		writeCommonProperties(value, gen, serializers);
		gen.writeEndObject();
	}

	public static class PropertyDecimalDefinitionSerializer extends PropertyDefinitionSerializer {

		public PropertyDecimalDefinitionSerializer() {
			super(PropertyDecimalDefinition.class);
		}

		@Override
		public void serialize(PropertyDefinition<?> value, JsonGenerator gen, SerializerProvider serializers)
		        throws IOException, JsonProcessingException {

			gen.writeStartObject();

			writeCommonProperties(value, gen, serializers);

			gen.writeNumberField("minValue", ((PropertyDecimalDefinition) value).getMinValue());
			gen.writeNumberField("maxValue", ((PropertyDecimalDefinition) value).getMaxValue());
			// XXX (Alessio): manca precision

			gen.writeEndObject();
		}
	}

	public static class PropertyIntegerDefinitionSerializer extends PropertyDefinitionSerializer {

		public PropertyIntegerDefinitionSerializer() {
			super(PropertyIntegerDefinition.class);
		}

		@Override
		public void serialize(PropertyDefinition<?> value, JsonGenerator gen, SerializerProvider serializers)
		        throws IOException, JsonProcessingException {

			gen.writeStartObject();

			writeCommonProperties(value, gen, serializers);

			gen.writeFieldName("minValue");
			gen.writeNumber(((PropertyIntegerDefinition) value).getMinValue());
			gen.writeFieldName("maxValue");
			gen.writeNumber(((PropertyIntegerDefinition) value).getMaxValue());

			gen.writeEndObject();
		}
	}

	public static class PropertyStringDefinitionSerializer extends PropertyDefinitionSerializer {

		public PropertyStringDefinitionSerializer() {
			super(PropertyStringDefinition.class);
		}

		@Override
		public void serialize(PropertyDefinition<?> value, JsonGenerator gen, SerializerProvider serializers)
		        throws IOException, JsonProcessingException {

			gen.writeStartObject();

			writeCommonProperties(value, gen, serializers);

			gen.writeFieldName("maxLength");
			gen.writeNumber(((PropertyStringDefinition) value).getMaxLength());

			gen.writeEndObject();
		}
	}
}
