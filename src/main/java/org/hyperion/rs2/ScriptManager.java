package org.hyperion.rs2;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages server scripts.
 *
 * @author blakeman8192
 * @author Omar Assadi
 */
public class ScriptManager {

    /**
     * The singleton of this class.
     */
    private static final ScriptManager INSTANCE = new ScriptManager();
    /**
     * The ScriptEngineManager.
     */
    private final ScriptEngineManager mgr;
    /**
     * The JavaScript Engine.
     */
    private final ScriptEngine jsEngine;
    /**
     * The logger for this manager.
     */
    private final Logger logger = Logger.getLogger(this.toString());

    /**
     * Creates the script manager.
     */
    private ScriptManager() {
        mgr = new ScriptEngineManager();
        jsEngine = mgr.getEngineByName("JavaScript");
        logger.info("Loading scripts...");
    }

    /**
     * Gets the ScriptManager singleton.
     *
     * @return The ScriptManager singleton.
     */
    public static ScriptManager getScriptManager() {
        return INSTANCE;
    }

    /**
     * Invokes a JavaScript function.
     *
     * @param identifier The identifier of the function.
     * @param args       The function arguments.
     */
    public void invoke(final String identifier, final Object... args) {
        final Invocable invEngine = (Invocable) jsEngine;
        try {
            invEngine.invokeFunction(identifier, args);
        } catch (final NoSuchMethodException ex) {
            logger.log(Level.WARNING, "No such method: " + identifier, ex);
        } catch (final ScriptException ex) {
            logger.log(Level.WARNING, "ScriptException thrown!", ex);
        }
    }

    /**
     * Loads JavaScript files into the JavaScript ScriptEngine from the argued
     * path.
     *
     * @param dirPath The path of the directory to load the JavaScript source files
     *                from.
     */
    public void loadScripts(final String dirPath) throws IOException {
        final Path scriptsDir = Paths.get(dirPath);
        if (!Files.exists(scriptsDir)) {
            Files.createDirectories(scriptsDir);
        }
        if (!Files.isDirectory(scriptsDir)) {
            throw new IOException("Scripts directory: [dirPath=" + dirPath + "] " +
                "is not a directory! Unable to read scripts");
        }
        final Path bootstrapPath = scriptsDir.resolve("bootstrap.js");
        if (!Files.exists(bootstrapPath)) {
            throw new FileNotFoundException("Unable to find script bootstrap in the " +
                "scripts directory! Unable load scripts [dirPath=" + dirPath + "]");
        }

        try {
            jsEngine.eval(new InputStreamReader(Files.newInputStream(bootstrapPath)));
        } catch (final ScriptException e) {
            logger.log(Level.SEVERE, "An error occurred while bootstrapping " +
                "scripts! Unable to load scripts.", e);
            return;
        }
        Files.walkFileTree(scriptsDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                final var fileName = file.getFileName().toString();
                if (!fileName.endsWith(".js") || fileName.equalsIgnoreCase("bootstrap.js")) {
                    return FileVisitResult.CONTINUE;
                }
                logger.log(Level.FINE, "Loading script: " + fileName);
                try {
                    jsEngine.eval(new InputStreamReader(Files.newInputStream(file)));
                } catch (final ScriptException ex) {
                    logger.log(Level.SEVERE, "Unable to load script!", ex);
                } catch (final FileNotFoundException ex) {
                    logger.log(Level.SEVERE, "Unable to find script!", ex);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
