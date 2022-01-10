package org.hyperion.rs2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * A configuration parser will parse simple configuration files.
 * <p>
 * An example of one of the files it is capable of parsing is shown below:
 *
 * <code>
 * # this is a comment
 * key1: value1
 * key2: value2
 * key3[subkey1]: value3
 * key3[subkey2]: value4
 * </code>
 *
 * @author Graham Edgecombe
 */
public class ConfigurationParser {

    /**
     * The buffered reader.
     */
    private final BufferedReader reader;

    /**
     * The simple mappings i.e. <code>key:value</code>.
     */
    private final Map<String, String> mappings = new HashMap<>();

    /**
     * The complex mappings i.e. <code>key[index]:value</code>.
     */
    private final Map<String, Map<String, String>> complexMappings = new HashMap<>();

    /**
     * Creates the configuration parser and parses configuration.
     *
     * @param is The input stream.
     * @throws IOException if an I/O error occurs.
     */
    public ConfigurationParser(final InputStream is) throws IOException {
        reader = new BufferedReader(new InputStreamReader(is));
        parse();
    }

    /**
     * Parses the configuration.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void parse() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }
            final String[] parts = line.split(":");
            if (parts.length != 2) {
                continue;
            }
            String key = parts[0].trim();
            final String value = parts[1].trim();
            if (key.endsWith("]")) {
                final int idx = key.indexOf('[');
                if (idx > -1) {
                    final String index = key.substring(idx + 1, key.length() - 1);
                    key = key.substring(0, idx);
                    if (!complexMappings.containsKey(key)) {
                        complexMappings.put(key, new HashMap<>());
                    }
                    complexMappings.get(key).put(index, value);
                } else {
                }
            } else {
                mappings.put(key, value);
            }
        }
        reader.close();
    }

    /**
     * Gets the mappings.
     *
     * @return The simple mappings i.e. <code>key:value</code>.
     */
    public Map<String, String> getMappings() {
        return mappings;
    }

    /**
     * Gets the complex mappings.
     *
     * @return The complex mappings i.e. <code>key[index]:value</code>.
     */
    public Map<String, Map<String, String>> getComplexMappings() {
        return complexMappings;
    }
}
