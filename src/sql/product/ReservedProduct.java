package sql.product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class ReservedProduct extends Product {
    private int reservedProductID;
    private int userID;

    public ReservedProduct(int reservedProductID, int userID, int productID, int rentalFee, LocalDate rentalStart, LocalDate rentalDeadline, String productName, String description, int totalStock, int currentStock, int numReservation, String imageURL, int rentalPeriod, LocalDate earliestRentalStart) {
        super(productID, productName, description, totalStock, currentStock, numReservation, rentalFee, imageURL, rentalPeriod, earliestRentalStart);

        this.reservedProductID = reservedProductID;
        this.userID = userID;
    }

    public ReservedProduct(ResultSet rs1, ResultSet rs2) throws SQLException {
        super(rs2);
        
        this.reservedProductID = rs1.getInt("RESERVED_PRODUCT_ID");
        this.userID = rs1.getInt("USER_ID");
    }

    public int getReservedProductID() {return this.reservedProductID;}
    public int getUserID() {return this.userID;}
}
