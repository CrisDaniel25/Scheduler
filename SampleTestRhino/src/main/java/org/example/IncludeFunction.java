package org.example;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class IncludeFunction extends BaseFunction {
    private final String scriptDir;
    private final Set<String> loadedScripts = new HashSet<>();

    public IncludeFunction(String scriptDir) {
        this.scriptDir = scriptDir;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length < 1 || !(args[0] instanceof String)) {
            throw Context.reportRuntimeError("include() expects a script file name as a string");
        }

        String scriptName = (String) args[0];
        File scriptFile = new File(scriptDir, scriptName);

        if (!scriptFile.exists()) {
            throw Context.reportRuntimeError("Included file not found: " + scriptFile.getAbsolutePath());
        }

        try {
            // Avoid reloading the same script
            if (!loadedScripts.contains(scriptFile.getCanonicalPath())) {
                loadedScripts.add(scriptFile.getCanonicalPath());

                String scriptContent = new String(Files.readAllBytes(scriptFile.toPath()));
                cx.evaluateString(scope, scriptContent, scriptFile.getName(), 1, null);

                System.out.println("Included script: " + scriptName);
            } else {
                System.out.println("Script already included: " + scriptName);
            }
        } catch (IOException e) {
            throw Context.reportRuntimeError("Error loading script: " + scriptFile.getAbsolutePath());
        }

        return null;
    }

    @Override
    public String getClassName() {
        return "IncludeFunction";
    }
}
