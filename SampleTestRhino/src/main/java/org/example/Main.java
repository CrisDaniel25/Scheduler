package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: Main <script>");
            return;
        }

        String scriptDir = "/opt/cronicle/scripts"; // Directory for script files
        String scriptName = args.length > 1 ? args[1] : null;
        String scriptContent = null;

        try {
            if (scriptName != null) {
                String scriptFilePath = scriptDir + File.separator + scriptName;

                // Validate and read the main script file
                File scriptFile = new File(scriptFilePath);
                if (!scriptFile.exists()) {
                    throw new IOException("Script file not found: " + scriptFilePath);
                }

                scriptContent = new String(Files.readAllBytes(Paths.get(scriptFilePath)));
            } else {
                scriptName = "InlineScript";
                scriptContent = args[0]; // Use inline script content if no file is provided
            }

        } catch (IOException e) {
            System.err.println("Error loading script: " + e.getMessage());
            return;
        }

        Context context = Context.enter();

        try {
            Scriptable scope = context.initStandardObjects();

            // Map the UtilityFunctions class to the scripting environment
            UtilityFunctions utility = new UtilityFunctions();
            Object jsUtility = Context.javaToJS(utility, scope);
            ScriptableObject.putProperty(scope, "UtilityFunctions", jsUtility);

            // Add the `include` function to the scope
            IncludeFunction includeFunction = new IncludeFunction(scriptDir);
            ScriptableObject.putProperty(scope, "include", includeFunction);

            // Execute the main script
            Object result = context.evaluateString(scope, scriptContent, scriptName, 1, null);
            System.out.println("Script result: " + result);
        } finally {
            Context.exit();
        }
    }
}
