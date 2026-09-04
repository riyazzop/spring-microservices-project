package com.employee.exceptions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Custom Jackson serializer for {@link HttpStatus}.
 * <p>
 * By default, Jackson calls {@code HttpStatus.toString()} which produces
 * strings like {@code "404 NOT_FOUND"}. When another service tries to
 * deserialize that back into an {@code HttpStatus} enum, Jackson fails
 * because it expects just the enum name (e.g. {@code "NOT_FOUND"}).
 * <p>
 * This serializer writes only the enum name so that deserialization works
 * correctly across service boundaries.
 */
public class HttpStatusSerializer extends StdSerializer<HttpStatus> {

    public HttpStatusSerializer() {
        super(HttpStatus.class);
    }

    @Override
    public void serialize(HttpStatus value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeString(value.name());   // e.g. "NOT_FOUND" instead of "404 NOT_FOUND"
    }
}
