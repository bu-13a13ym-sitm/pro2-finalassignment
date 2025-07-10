package sql.product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Product {
    private int productID;
    private String productName;
    private String description;
    private int totalStock;
    private int currentStock;
    private int numReservation;
    private int rentalFee;
    private String imageURL;
    private int rentalPeriod;
    private LocalDate earliestRentalStart;

    public Product(int productID, String productName, String description, int totalStock, int currentStock, int numReservation, int rentalFee, String imageURL, int rentalPeriod, LocalDate earliestRentalStart) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.totalStock = totalStock;
        this.currentStock = currentStock;
        this.numReservation = numReservation;
        this.rentalFee = rentalFee;
        this.imageURL = imageURL;
        this.rentalPeriod = rentalPeriod;
        this.earliestRentalStart = earliestRentalStart;
    }

    public Product(ResultSet rs) throws SQLException{
        this.productID = rs.getInt("PRODUCT_ID");
        this.productName = rs.getString("PRODUCT_NAME");
        this.description = rs.getString("DESCRIPTION");
        this.totalStock = rs.getInt("TOTAL_STOCK");
        this.currentStock = rs.getInt("CURRENT_STOCK");
        this.numReservation = rs.getInt("NUM_RESERVATION");
        this.rentalFee = rs.getInt("RENTAL_FEE");
        this.imageURL = rs.getString("IMAGE_URL");
        this.rentalPeriod = rs.getInt("RENTAL_PERIOD");
        String dateStr = rs.getString("EARLIEST_RENTAL_START");
        this.earliestRentalStart = (dateStr != null) ? LocalDate.parse(dateStr) : null;
    }

    public int getProductID() {return this.productID;}
    public String getProductName() {return this.productName;}
    public String getDescription() {return this.description;}
    public int getTotalStock() {return this.totalStock;}
    public int getCurrentStock() {return this.currentStock;}
    public int getNumReservation() {return this.numReservation;}
    public int getRentalFee() {return this.rentalFee;}
    public String getImageURL() {return this.imageURL;}
    public int getRentalPeriod() {return this.rentalPeriod;}
    public LocalDate getEarliestRentalStart() {return this.earliestRentalStart;}
}
