import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FlowerShop {
    private static final String PRODUCTS_FILE = "products.txt";

    public static void main(String[] args) {
        Map<String, Flower> flowers = readFlowersFromFile(PRODUCTS_FILE);
        if (flowers.isEmpty()) {
            System.out.println("No flowers available.");
            return;
        }

        Map<String, Flower> cart = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Flower Shop!");
            System.out.println("Select an option:");
            System.out.println("1. View available flowers");
            System.out.println("2. Purchase flowers");
            System.out.println("3. View cart");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    printAllFlowerInfo(flowers);
                    break;
                case 2:
                    purchaseFlowers(flowers, cart, scanner);
                    break;
                case 3:
                    displayCart(cart);
                    break;
                case 4:
                    System.out.println("Exiting... Thank you for visiting the Flower Shop!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void printAllFlowerInfo(Map<String, Flower> flowers) {
        System.out.println("Available flowers:");
        for (Flower flower : flowers.values()) {
            System.out.println("- " + flower.getName() + ", Price: $" + flower.getPrice() + ", Quantity: " + flower.getQuantity());
        }
    }

    private static void purchaseFlowers(Map<String, Flower> flowers, Map<String, Flower> cart, Scanner scanner) {
        System.out.println("Available flowers:");
        for (Flower flower : flowers.values()) {
            System.out.println("- " + flower.getName());
        }

        System.out.print("Enter the flower you want to purchase: ");
        String chosenFlower = scanner.nextLine().toLowerCase(); // Convert to lowercase

        if (!flowers.containsKey(chosenFlower)) {
            System.out.println("Sorry, we don't have that flower available.");
            return;
        }

        Flower selectedFlower = flowers.get(chosenFlower);

        System.out.print("Enter the quantity you want: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (quantity > selectedFlower.getQuantity()) {
            System.out.println("Sorry, we don't have enough quantity available for your request.");
            return;
        }

        double totalPrice = selectedFlower.getPrice() * quantity;
        System.out.println("\n--- Order Summary ---");
        System.out.println("Flower: " + selectedFlower.getName());
        System.out.println("Price per unit: $" + selectedFlower.getPrice());
        System.out.println("Quantity: " + quantity);
        System.out.println("Total Price: $" + totalPrice);

        System.out.print("\nConfirm purchase (yes/no): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            // Reduce the quantity of the selected flower
            selectedFlower.reduceQuantity(quantity);
            System.out.println("Thank you for your purchase!");

            // Add purchased flower to the cart
            addToCart(selectedFlower, quantity, cart);
        }
    }

    private static void addToCart(Flower flower, int quantity, Map<String, Flower> cart) {
        if (cart.containsKey(flower.getName())) {
            Flower existingFlower = cart.get(flower.getName());
            existingFlower.setQuantity(existingFlower.getQuantity() + quantity);
        } else {
            cart.put(flower.getName(), new Flower(flower.getName(), flower.getPrice(), quantity));
        }
    }

    private static void displayCart(Map<String, Flower> cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("\n--- Your Cart ---");
        for (Flower flower : cart.values()) {
            System.out.println("- " + flower.getName() + ", Quantity: " + flower.getQuantity() + ", Total Price: $" + (flower.getPrice() * flower.getQuantity()));
        }
    }

    private static Map<String, Flower> readFlowersFromFile(String fileName) {
        Map<String, Flower> flowers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String flowerName = parts[0].trim().toLowerCase(); // Convert to lowercase
                    double price = Double.parseDouble(parts[1].trim());
                    int quantity = Integer.parseInt(parts[2].trim());
                    flowers.put(flowerName, new Flower(flowerName, price, quantity));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return flowers;
    }

    static class Flower {
        private String name;
        private double price;
        private int quantity;

        public Flower(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void reduceQuantity(int amount) {
            quantity -= amount;
        }
    }
}

