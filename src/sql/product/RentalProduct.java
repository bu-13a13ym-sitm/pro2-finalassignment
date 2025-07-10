package sql.product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class RentalProduct {
    private int rentalProductID;
    private int userID;
    private int productID;
    private int rentalFee;
    private LocalDate rentalStart;
    private LocalDate rentalDeadline;

    public RentalProduct(int rentalProductID, int userID, int productID, int rentalFee, LocalDate rentalStart, LocalDate rentalDeadline) {
        this.rentalProductID = rentalProductID;
        this.userID = userID;
        this.productID = productID;
        this.rentalFee = rentalFee;
        this.rentalStart = rentalStart;
        this.rentalDeadline = rentalDeadline;
    }

    public RentalProduct(ResultSet rs) throws SQLException {
        this.rentalProductID = rs.getInt("RENTAL_PRODUCT_ID");
        this.userID = rs.getInt("USER_ID");
        this.productID = rs.getInt("PRODUCT_ID");
        this.rentalFee = rs.getInt("RENTAL_FEE");
        String startDateStr = rs.getString("RENTAL_START");
        this.rentalStart = (startDateStr != null) ? LocalDate.parse(startDateStr) : null;
        String deadlineDateStr = rs.getString("RENTAL_DEADLINE");
        this.rentalDeadline = (deadlineDateStr != null) ? LocalDate.parse(deadlineDateStr) : null;
    }

    public int getRentalProductID() {return this.rentalProductID;}
    public int getUserID() {return this.userID;}
    public int getProductID() {return this.productID;}
    public int getRentalFee() {return this.rentalFee;}
    public LocalDate getRentalStart() {return this.rentalStart;}
    public LocalDate getRentalDeadLine() {return this.rentalDeadline;}
}
