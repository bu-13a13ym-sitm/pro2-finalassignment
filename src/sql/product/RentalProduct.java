package sql.product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class RentalProduct extends Product {
    private int rentalProductID;
    private int userID;
    private LocalDate rentalStart;
    private LocalDate rentalDeadline;

    public RentalProduct(int rentalProductID, int userID, int productID, int rentalFee, LocalDate rentalStart, LocalDate rentalDeadline, String productName, String description, int totalStock, int currentStock, int numReservation, String imageURL, int rentalPeriod, LocalDate earliestRentalStart) {
        super(productID, productName, description, totalStock, currentStock, numReservation, rentalFee, imageURL, rentalPeriod, earliestRentalStart);

        this.rentalProductID = rentalProductID;
        this.userID = userID;
        this.rentalStart = rentalStart;
        this.rentalDeadline = rentalDeadline;
    }

    public RentalProduct(ResultSet rs1, ResultSet rs2) throws SQLException {
        super(rs2);

        this.rentalProductID = rs1.getInt("RENTAL_PRODUCT_ID");
        this.userID = rs1.getInt("USER_ID");
        String startDateStr = rs1.getString("RENTAL_START");
        this.rentalStart = (startDateStr != null) ? LocalDate.parse(startDateStr) : null;
        String deadlineDateStr = rs1.getString("RENTAL_DEADLINE");
        this.rentalDeadline = (deadlineDateStr != null) ? LocalDate.parse(deadlineDateStr) : null;
    }

    public int getRentalProductID() {return this.rentalProductID;}
    public int getUserID() {return this.userID;}
    public LocalDate getRentalStart() {return this.rentalStart;}
    public LocalDate getRentalDeadLine() {return this.rentalDeadline;}
}
