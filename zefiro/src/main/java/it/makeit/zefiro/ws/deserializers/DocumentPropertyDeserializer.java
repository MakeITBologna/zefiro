package it.makeit.zefiro.ws.deserializers;

import it.makeit.zefiro.dao.DocumentPropertyBean;

import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class DocumentPropertyDeserializer extends StdDeserializer<DocumentPropertyBean> {

	public DocumentPropertyDeserializer() {
		super(DocumentPropertyBean.class);
	}

	@Override
	public DocumentPropertyBean deserialize(JsonParser pJsonParser, DeserializationContext pContext)
			throws IOException, JsonProcessingException {
		
		DocumentPropertyBean lDocumentPropertyBean = new DocumentPropertyBean();
		
		JsonNode lJsonNode = pJsonParser.getCodec().readTree(pJsonParser);
		
		JsonNode lJsonNodeQueryName = lJsonNode.get("queryName");
		JsonNode lJsonNodePropertyType = lJsonNode.get("propertyType");
		JsonNode lJsonNodeValue = lJsonNode.get("value");
		
		String lQueryName = (lJsonNodeQueryName.isNull())? "": lJsonNodeQueryName.textValue();
		String lPropertyType = (lJsonNodePropertyType.isNull())? "": lJsonNodePropertyType.textValue();
		
		if (lJsonNodeValue == null || lJsonNodeValue.isNull()) {
			lDocumentPropertyBean = new DocumentPropertyBean(lQueryName, lPropertyType, null);
		} else if (lPropertyType.equals("INTEGER")) {
			lDocumentPropertyBean = new DocumentPropertyBean(lQueryName, lPropertyType, lJsonNodeValue.longValue());
		} else if (lPropertyType.equals("DECIMAL")) {
			lDocumentPropertyBean = new DocumentPropertyBean(lQueryName, lPropertyType, lJsonNodeValue.decimalValue());
		} else if (lPropertyType.equals("BOOLEAN")) {
			lDocumentPropertyBean = new DocumentPropertyBean(lQueryName, lPropertyType, lJsonNodeValue.booleanValue());
		} else if (lPropertyType.equals("DATETIME") || lPropertyType.equals("DATE")) {
			String lStrDatetimeISO = lJsonNodeValue.textValue();
			Calendar lCalendar = javax.xml.bind.DatatypeConverter.parseDateTime(lStrDatetimeISO);
			lDocumentPropertyBean = new DocumentPropertyBean(lQueryName, lPropertyType, lCalendar.getTime());
		} else {
			lDocumentPropertyBean = new DocumentPropertyBean(lQueryName, lPropertyType, lJsonNodeValue.textValue());
		}
		
		return lDocumentPropertyBean;
	}
}
