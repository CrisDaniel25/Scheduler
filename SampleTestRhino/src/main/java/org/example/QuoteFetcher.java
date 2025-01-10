package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class QuoteFetcher {
    private static final String API_URL = "https://api.quotable.io/random";

    /**
     * Fetches a random quote from the Quotable API.
     *
     * @return Quote object containing the content and author.
     * @throws Exception if an error occurs during the API call or JSON parsing.
     */
    public static JSONObject fetchRandomQuote() throws Exception {
        // Disable SSL verification
        SSLUtils.disableSSLVerification();
        
        // Open connection to the API
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        // Parse the JSON response
        JSONObject json = new JSONObject(response.toString());

        return json;
    }
}
