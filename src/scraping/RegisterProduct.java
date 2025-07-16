import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import sql.*;
import sql.product.*;
import sql.exception.*;

public class RegisterProduct {
    public static void main(String[] args) {
        String path = "src/scraping/productList.csv";
        try {
            SQL sql = new SQL();
            List<String> lines = Files.readAllLines(Paths.get("images/" + path));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] fields = line.split(",");
                int productID = 0;
                String productName = fields[0].trim();
                String description = fields[1].trim().replace("_", ",");
                int totalStock = Integer.parseInt(fields[2].trim());
                int currentStock = totalStock;
                int numReservation = 0;
                int rentalFee = Integer.parseInt(fields[3].trim());
                String imageURL = fields[4].trim();
                int rentalPeriod = Integer.parseInt(fields[5].trim());
                LocalDate earliestRentalStart = null;

                Product product = new Product(productID, productName, description, totalStock, currentStock, numReservation, rentalFee, imageURL, rentalPeriod, earliestRentalStart);
                try {
                    sql.addNewProduct(product);
                } catch (DatabaseException e) {
                    System.out.println("Failed to add product at row " + i + line);
                }
            }
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
