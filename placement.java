import org.json.JSONObject;
import java.util.*;
import java.math.BigInteger;

public class ShamirSecretSharing {
    
    // Method to decode a value from a given base
    public static BigInteger decodeValue(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return new BigInteger(value, baseInt);
    }

    // Method to calculate the constant term (secret) using Lagrange Interpolation
    public static BigInteger lagrangeInterpolation(List<BigInteger> xValues, List<BigInteger> yValues) {
        BigInteger secret = BigInteger.ZERO;
        int n = xValues.size();

        // Loop through all points (x, y)
        for (int i = 0; i < n; i++) {
            BigInteger term = yValues.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    BigInteger xi = xValues.get(i);
                    BigInteger xj = xValues.get(j);

                    BigInteger denominator = xi.subtract(xj);
                    BigInteger numerator = BigInteger.ONE;

                    // Calculate the product (x - xj) for all j != i
                    numerator = numerator.multiply(xi.negate());  // (xi - xj)
                    term = term.multiply(numerator).divide(denominator);
                }
            }
            secret = secret.add(term);
        }
        
        return secret.mod(BigInteger.valueOf(256)); // Secret in a 256-bit number range.
    }

    public static void main(String[] args) {
        // Sample JSON input
        String jsonString = "{\n" +
                "\"keys\": {\n" +
                "\"n\": 10,\n" +
                "\"k\": 7\n" +
                "},\n" +
                "\"1\": {\n" +
                "\"base\": \"7\",\n" +
                "\"value\": \"420020006424065463\"\n" +
                "},\n" +
                "\"2\": {\n" +
                "\"base\": \"7\",\n" +
                "\"value\": \"10511630252064643035\"\n" +
                "},\n" +
                "\"3\": {\n" +
                "\"base\": \"2\",\n" +
                "\"value\": \"101010101001100101011100000001000111010010111101100100010\"\n" +
                "},\n" +
                "\"4\": {\n" +
                "\"base\": \"8\",\n" +
                "\"value\": \"31261003022226126015\"\n" +
                "},\n" +
                "\"5\": {\n" +
                "\"base\": \"7\",\n" +
                "\"value\": \"2564201006101516132035\"\n" +
                "},\n" +
                "\"6\": {\n" +
                "\"base\": \"15\",\n" +
                "\"value\": \"a3c97ed550c69484\"\n" +
                "},\n" +
                "\"7\": {\n" +
                "\"base\": \"13\",\n" +
                "\"value\": \"134b08c8739552a734\"\n" +
                "},\n" +
                "\"8\": {\n" +
                "\"base\": \"10\",\n" +
                "\"value\": \"23600283241050447333\"\n" +
                "},\n" +
                "\"9\": {\n" +
                "\"base\": \"9\",\n" +
                "\"value\": \"375870320616068547135\"\n" +
                "},\n" +
                "\"10\": {\n" +
                "\"base\": \"6\",\n" +
                "\"value\": \"30140555423010311322515333\"\n" +
                "}\n" +
                "}";

        // Parse JSON
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Lists to store the decoded x and y values
        List<BigInteger> xValues = new ArrayList<>();
        List<BigInteger> yValues = new ArrayList<>();

        // Extract the roots and decode the y values
        for (String key : jsonObject.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject root = jsonObject.getJSONObject(key);
            String base = root.getString("base");
            String value = root.getString("value");

            BigInteger decodedValue = decodeValue(base, value);
            xValues.add(new BigInteger(key));
            yValues.add(decodedValue);
        }

        // Calculate the constant term 'c' (the secret)
        BigInteger secret = lagrangeInterpolation(xValues, yValues);
        
        // Print the result
        System.out.println("The secret (constant term) of the polynomial is: " + secret);
    }
}

