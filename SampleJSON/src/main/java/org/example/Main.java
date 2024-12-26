package org.example;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0)
            System.out.println("Argument 0: ".concat(String.valueOf(args[0])));
        if (args.length > 1)
            System.out.println("Argument 1: ".concat(String.valueOf(args[1])));
        if (args.length > 2)
            System.out.println("Argument 2: ".concat(String.valueOf(args[2])));
        JSONObject result = new JSONObject();
        result.put("complete", 1);
        result.put("code", 0);
        result.put("description", "Job Completed Successfully.");
        System.out.println(result.toString());
        // complete: 1,
        // code: 0,
        // description: "Success!",
    }
}