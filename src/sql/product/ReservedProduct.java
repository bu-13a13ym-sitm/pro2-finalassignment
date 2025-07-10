package sql.product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;

public class ReservedProduct {
    private int reservedProductID;
    private int userID;
    private int productID;
    private LocalDate earliestRentalStart;

    public ReservedProduct(int reservedProductID, int userID, int productID, LocalDate earliestRentalStart) {
        this.reservedProductID = reservedProductID;
        this.userID = userID;
        this.productID = productID;
        this.earliestRentalStart = earliestRentalStart;
    }

    public ReservedProduct(ResultSet rs) throws SQLException {
        this.reservedProductID = rs.getInt("RESERVED_PRODUCT_ID");
        this.userID = rs.getInt("USER_ID");
        this.productID = rs.getInt("PRODUCT_ID");
        String dateStr = rs.getString("EARLIEST_RENTAL_START");
        this.earliestRentalStart = (dateStr != null) ? LocalDate.parse(dateStr) : null;
    }

    public int getReservedProductID() {return this.reservedProductID;}
    public int getUserID() {return this.userID;}
    public int getProductID() {return this.productID;}
    public LocalDate getEarliestRentalStart() {return this.earliestRentalStart;}
}
