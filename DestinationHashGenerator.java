// Import necessary libraries
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONTokener;

// Main class for hash generation
public class DestinationHashGenerator {

    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length != 2) {
            System.out.println("Usage: java DestinationHashGenerator <roll_number> <json_file_path>");
            return;
        }

        String rollNumber = args[0];
        String jsonFilePath = args[1];

        try {
            // Parse JSON file and extract the destination value
            JSONTokener tokener = new JSONTokener(new FileReader(jsonFilePath));
            JSONObject jsonObject = new JSONObject(tokener);
            String destinationValue = findDestination(jsonObject);

            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                return;
            }

            // Generate a random 8-character alphanumeric string
            String randomString = generateRandomString(8);

            // Concatenate values and generate MD5 hash
            String inputString = rollNumber + destinationValue + randomString;
            String hash = generateMD5(inputString);

            // Print result
            System.out.println(hash + ";" + randomString);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Function to find the first "destination" key in JSON
    private static String findDestination(JSONObject jsonObject) {
        for (Iterator<String> it = jsonObject.keys(); it.hasNext();) {
            String key = it.next();
            Object value = jsonObject.get(key);

            if (key.equals("destination")) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String found = findDestination((JSONObject) value);
                if (found != null) return found;
            }
        }
        return null;
    }

    // Function to generate MD5 hash
    private static String generateMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Function to generate a random alphanumeric string
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
