package org.example;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class Main {
    public static void main(String[] args) {
        Context context = Context.enter();
        try {
            // Initialize standard objects
            Scriptable scope = context.initStandardObjects();

            // JavaScript code to execute
            String script = "var x = 10; var y = 20; x + y;";

            // Execute the script
            Object result = context.evaluateString(scope, script, "Script", 1, null);

            // Convert and print the result
            System.out.println("Result: " + Context.jsToJava(result, Integer.class));
        } finally {
            Context.exit();
        }
    }
}