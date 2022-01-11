package org.hyperion.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStreams {

    @Test
    public void testWriteString() throws IOException {
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Streams.writeString(os, "Hello, World.");
            final byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '.', 0};
            assertArrayEquals(expected, os.toByteArray());
        }
    }

    @Test
    public void testReadString() throws IOException {
        try (final ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '.', 0})) {
            assertEquals("Hello, World.", Streams.readString(is));
        }
    }

    @Test
    public void testWriteLine() throws IOException {
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Streams.writeLine(os, "Hello, World.");
            final byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '.', 10};
            assertArrayEquals(expected, os.toByteArray());
        }
    }

    @Test
    public void testReadLine() throws IOException {
        try (final ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o', 'r', 'l', 'd', '.', 10})) {
            assertEquals("Hello, World.", Streams.readLine(is));
        }
    }

}
