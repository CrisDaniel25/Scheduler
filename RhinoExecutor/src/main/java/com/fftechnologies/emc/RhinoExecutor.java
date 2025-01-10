package com.fftechnologies.emc;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RhinoExecutor {
    public static void main(String[] args) {
        Context context = Context.enter();
        String script = args[0];
        try {
            Scriptable scope = context.initStandardObjects();
            Object result = context.evaluateString(scope, script, "UserScript", 1, null);
            System.out.println(result.toString());
        } finally {
            Context.exit();
        }
    }
}
