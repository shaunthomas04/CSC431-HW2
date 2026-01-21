import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class DataIntegrity {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Choose input type (1 for plaintext or 2 for file): ");

            int choice = Integer.parseInt(scanner.nextLine());
            byte[] originalData;

            // Input handling
            if (choice == 1) {
                System.out.print("Enter plaintext message: ");
                String message = scanner.nextLine();
                originalData = message.getBytes();

            } else if (choice == 2) {
                System.out.print("Enter file path: ");
                String path = scanner.nextLine();
                originalData = readFile(path);

            } else {
                System.out.println("Invalid choice.");
                return;
            }

            // create original hash
            String originalHash = convertToSHA256(originalData);
            System.out.println("Original Hash:");
            System.out.println(originalHash);

            // Simulate modification
            System.out.print("Simulate data modification? (y/n): ");
            String simulate = scanner.nextLine().trim().toLowerCase();

            byte[] newData = originalData;

            if (simulate.equals("y")) {
                if (choice == 1) {
                    System.out.print("Enter modified plaintext message: ");
                    newData = scanner.nextLine().getBytes();
                } else {
                    System.out.println("Modify the file externally, then press Enter.");
                    scanner.nextLine();
                    System.out.print("Re-enter file path: ");
                    newData = readFile(scanner.nextLine());
                }
            }

            // Compute new hash
            String newHash = convertToSHA256(newData);
            System.out.println("New Hash:");
            System.out.println(newHash);

            // Integrity verification
            if (originalHash.equals(newHash)) {
                System.out.println("Integrity Check PASSED: Data has not been altered.");
            } else {
                System.out.println("Integrity Check FAILED: Data has been modified.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    //function to convert bytes into sha256 hash
    private static String convertToSHA256(byte[] data)
            throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data);

        // Convert bytes to hexadecimal
        StringBuilder hex = new StringBuilder();
        for (byte b : hashBytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    //function to read file into bytes
    private static byte[] readFile(String path) throws IOException {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found");
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] data = new byte[(int) file.length()];
            int bytesRead = bis.read(data);
            if (bytesRead != data.length) {
                throw new IOException("Could not read entire file.");
            }
            return data;
        }
    }
}
