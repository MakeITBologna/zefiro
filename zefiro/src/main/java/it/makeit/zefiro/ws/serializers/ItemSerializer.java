package it.makeit.zefiro.ws.serializers;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Item;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("serial")
public class ItemSerializer extends StdSerializer<Item>{
	private static final List<String> DEFAULT_PROPERTIES =
	        Arrays.asList(PropertyIds.OBJECT_ID, PropertyIds.NAME, PropertyIds.DESCRIPTION, PropertyIds.OBJECT_TYPE_ID, PropertyIds.BASE_TYPE_ID,
	                PropertyIds.VERSION_LABEL, PropertyIds.IS_LATEST_VERSION, PropertyIds.IS_MAJOR_VERSION,
	                PropertyIds.CREATION_DATE, PropertyIds.CREATED_BY, PropertyIds.LAST_MODIFICATION_DATE,
	                PropertyIds.LAST_MODIFIED_BY);
	
	public ItemSerializer() {
		super(Item.class);
	}
	
	@Override
	public void serialize(Item value, JsonGenerator gen, SerializerProvider serializers)
	        throws IOException, JsonProcessingException {
		

		gen.writeStartObject();

		gen.writeStringField("id", value.getId());
		gen.writeStringField("name", value.getName());
		gen.writeStringField("description", value.getDescription());
		gen.writeStringField("type", value.getType().getId());
		gen.writeStringField("typeName", value.getType().getDisplayName());
		gen.writeStringField("baseType", value.getBaseType().getId());
		gen.writeStringField("baseTypeName", value.getBaseType().getDisplayName());

		gen.writeObjectField("isLatestVersion", (Boolean) value.getPropertyValue(PropertyIds.IS_LATEST_VERSION));
		gen.writeObjectField("isMajorVersion", (Boolean) value.getPropertyValue(PropertyIds.IS_MAJOR_VERSION));

		gen.writeObjectField("created", value.getCreationDate());
		gen.writeStringField("createdBy", value.getCreatedBy());
		gen.writeObjectField("lastModificationDate", value.getPropertyValue(PropertyIds.LAST_MODIFICATION_DATE));
		gen.writeStringField("lastModifiedBy", (String) value.getPropertyValue(PropertyIds.LAST_MODIFIED_BY));

		// Aspetti
		gen.writeObjectField("secondaryObjectTypeIds", value.getPropertyValue(PropertyIds.SECONDARY_OBJECT_TYPE_IDS));

		// Proprietà
		Map<String, Property<?>> lMapProps = new LinkedHashMap<String, Property<?>>();
		for (Property<?> prop : value.getProperties()) {
			if (DEFAULT_PROPERTIES.contains(prop.getId())) {
				// Inutile rimettere le proprietà predefinite nell'elenco delle proprietà
				continue;
			}
			lMapProps.put(prop.getQueryName(), prop);
		}
		gen.writeObjectField("properties", lMapProps);
		
		gen.writeFieldName("relationships");
		gen.writeStartArray();
		
		List<Relationship> lRelationships = value.getRelationships();
		
		if (lRelationships != null) {
			for (Relationship lRelationship : lRelationships) {
				
				lRelationship.getId();
				
				lRelationship.getProperties();
				gen.writeStartObject();
				
				gen.writeStringField("id", lRelationship.getId());
				gen.writeStringField("name", lRelationship.getName());
				gen.writeStringField("description", lRelationship.getDescription());
				gen.writeFieldName("baseType");
				gen.writeStartObject();
				gen.writeStringField("id", lRelationship.getBaseType().getId());
				gen.writeStringField("name", lRelationship.getBaseType().getDisplayName());
				gen.writeStringField("description", lRelationship.getBaseType().getDescription());
				Map<String, Property<?>> lRelProps = new LinkedHashMap<String, Property<?>>();
				for (Property<?> prop : value.getProperties()) {
					if (prop.getQueryName().equals("cmis:lastModificationDate")) {
						continue;
					}
					lRelProps.put(prop.getQueryName(), prop);
				}
				gen.writeObjectField("properties", lRelProps);
				
				gen.writeEndObject();
				gen.writeEndObject();
				;}
			
		}
				
		gen.writeEndArray();

		gen.writeEndObject();
		
	}
	
	@Override
	public Class<Item> handledType() {
		return Item.class;
	}
}
