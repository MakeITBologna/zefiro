package it.makeit.zefiro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Alba Quarto
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DecodedFieldNote {

	public enum DecodingType {
		PROCESS, STARTER, OWNER, ASSIGNEE, DEFINITION
	}

	DecodingType decodingType();

	String value();
}