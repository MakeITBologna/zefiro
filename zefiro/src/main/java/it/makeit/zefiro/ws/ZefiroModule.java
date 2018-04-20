package it.makeit.zefiro.ws;

import it.makeit.zefiro.dao.DocumentPropertyBean;
import it.makeit.zefiro.ws.deserializers.DocumentPropertyDeserializer;
import it.makeit.zefiro.ws.serializers.DocumentSerializer;
import it.makeit.zefiro.ws.serializers.ObjectTypeSerializer;
import it.makeit.zefiro.ws.serializers.PropertyDefinitionSerializer;
import it.makeit.zefiro.ws.serializers.PropertyDefinitionSerializer.PropertyDecimalDefinitionSerializer;
import it.makeit.zefiro.ws.serializers.PropertyDefinitionSerializer.PropertyIntegerDefinitionSerializer;
import it.makeit.zefiro.ws.serializers.PropertyDefinitionSerializer.PropertyStringDefinitionSerializer;
import it.makeit.zefiro.ws.serializers.PropertySerializer;
import it.makeit.zefiro.ws.serializers.RelationshipSerializer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;


// XXX (Alessio): sarebbe utile creare un modulo di estensione a sè stante per il supporto ai tipi
// CMIS di Chemistry. In questo modo si avrebbe un'estensione di Jersey per lavorare con i dati di
// Apache Chemistry.
public class ZefiroModule extends SimpleModule {

	private static final int majorVersion = 0;
	private static final int minorVersion = 1;
	private static final int patchLevel = 0;

	private static final long serialVersionUID = majorVersion * 100L + minorVersion * 10L + patchLevel;

	public ZefiroModule() {
		super("ZefiroModule", new Version(majorVersion, minorVersion, patchLevel, null, "it.makeit.zefiro",
		        "jersey-module-zefiro"));

		addSerializer(new ObjectTypeSerializer());
		addSerializer(new DocumentSerializer());
		addSerializer(new PropertySerializer());
		addSerializer(new RelationshipSerializer());

		// PropertyDefinition
		addSerializer(new PropertyDefinitionSerializer());
		addSerializer(new PropertyDecimalDefinitionSerializer());
		addSerializer(new PropertyIntegerDefinitionSerializer());
		addSerializer(new PropertyStringDefinitionSerializer());
		
		addDeserializer(DocumentPropertyBean.class, new DocumentPropertyDeserializer());
	}
}
