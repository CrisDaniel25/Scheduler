package org.example;

import org.json.JSONObject;

public class UtilityFunctions {
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }

    public int addNumbers(int a, int b) {
        return a + b;
    }

    public JSONObject getRandomQuote() throws Exception {
        return QuoteFetcher.fetchRandomQuote();
    }
}
