package sql;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import sql.account.*;
import sql.product.*;
import sql.exception.*;

public class SQL {
    private Connection conn;
    private static final String DRIVER_NAME = "org.sqlite.JDBC";
    private static final String JDBC_URL = "jdbc:sqlite:data/clothesRentalSystem.db";
    private static final String USER_ID = "";
    private static final String USER_PASS = "";

    public SQL() throws DatabaseException {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("No driver found.", e);
        }
        try {
            this.conn = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PASS);
        } catch (Exception e) {
            throw new DatabaseException("Failed to make connection to database.", e);
        }
    }

    public void disconnect() throws DatabaseException {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close connection.", e);
        }
    }

    public Account getCustomerInfo(String email) throws NoResultsFoundException, DatabaseException {
        String sq = "SELECT * FROM CUSTOMER_INFOS WHERE EMAIL = ?";
        try (PreparedStatement ps = conn.prepareStatement(sq)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userID = rs.getInt("USER_ID");
                    String userName = rs.getString("USER_NAME");
                    String password = rs.getString("PASSWORD");

                    return new Account(userID, userName, email, password);
                } else throw new NoResultsFoundException("User not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database exception occurred.", e);
        }
    }

    public boolean addNewCustomer(String userName, String email, String password) throws UserAlreadyExistException, DatabaseException {
        String sq = "INSERT INTO CUSTOMER_INFOS (USER_NAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sq)) {
            ps.setString(1, userName);
            ps.setString(2, email);
            ps.setString(3, password);

            ps.executeUpdate();
        } catch (SQLException e) {
            String message = e.getMessage();
            if (message != null && message.contains("UNIQUE constraint failed:")) throw new UserAlreadyExistException();
            else throw new DatabaseException("Database exception occurred.", e);
        }

        return true;
    }

    public ArrayList<Product> productQuery(Integer minPrice, Integer maxPrice, Boolean immediate) throws NoResultsFoundException, DatabaseException {
        ArrayList<Product> products = new ArrayList<>();
        StringBuilder sq = new StringBuilder("SELECT * FROM PRODUCTS WHERE 1 = 1");
        if (minPrice != null && minPrice > 0) sq.append(" AND RENTAL_FEE >= ?");
        if (maxPrice != null && maxPrice > 0) sq.append(" AND RENTAL_FEE <= ?");
        if (immediate != null && immediate == true) sq.append(" AND EARLIEST_RENTAL_START = ?");
        try (PreparedStatement ps = conn.prepareStatement(sq.toString())) {
            int paramInd = 1;
            if (minPrice != null && minPrice > 0) ps.setInt(paramInd++, minPrice);
            if (maxPrice != null && maxPrice > 0) ps.setInt(paramInd++, maxPrice);
            if (immediate != null && immediate == true) ps.setString(paramInd++, LocalDate.now().toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(rs));
                }
                if (products.isEmpty()) throw new NoResultsFoundException("Products not found.");
                
                return products;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database exception occurred.", e);
        }
    }

    public boolean addNewProduct(Product product) throws DatabaseException {
        String sq = "INSERT INTO PRODUCTS (PRODUCT_NAME, DESCRIPTION, TOTAL_STOCK, CURRENT_STOCK, NUM_RESERVATION, RENTAL_FEE, IMAGE_URL, RENTAL_PERIOD, EARLIEST_RENTAL_START) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sq)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getTotalStock());
            ps.setInt(4, product.getCurrentStock());
            ps.setInt(5, product.getNumReservation());
            ps.setInt(6, product.getRentalFee());
            ps.setString(7, product.getImageURL());
            ps.setInt(8, product.getRentalFee());
            String dateStr = product.getEarliestRentalStart().toString();
            ps.setString(9, dateStr);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert new product int PRODUCTS table.", e);
        }

        return true;
    }

    public boolean addNewRentalProduct(Product product, int userID) throws RentalFailedException, NoResultsFoundException, DatabaseException {
        String sq1 = "UPDATE PRODUCTS SET CURRENT_STOCK  = CURRENT_STOCK - 1 WHERE PRODUCT_ID = ? AND CURRENT_STOCK > 0";
        String sq2 = "INSERT INTO RENTAL_PRODUCTS (USER_ID, PRODUCT_ID, RENTAL_START, RENTAL_DEADLINE) VALUES (?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sq1)) {
                ps1.setInt(1, product.getProductID());

                int affected = ps1.executeUpdate();
                if (affected != 1) {
                    conn.rollback();
                    throw new NoResultsFoundException("Product not found.");
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sq2)) {
                ps2.setInt(1, userID);
                ps2.setInt(2, product.getProductID());
                ps2.setString(3, LocalDate.now().toString());
                ps2.setString(4, LocalDate.now().plusDays(product.getRentalPeriod()).toString());

                ps2.executeUpdate();
            }

            String sq3 = "SELECT * FROM PRODUCTS WHERE PRODUCT_ID = ?";
            String sq4 = "SELECT * FROM RENTAL_PRODUCTS WHERE PRODUCT_ID = ? ORDER BY RENTAL_DEADLINE ASC LIMIT 1";
            try (PreparedStatement ps3 = conn.prepareStatement(sq3)) {
                ps3.setInt(1, product.getProductID());

                try (ResultSet rs3 = ps3.executeQuery()) {
                    if (rs3.next()) {
                        if (rs3.getInt("CURRENT_STOCK") == 0 &&  rs3.getInt( "NUM_RESERVATION") == 0) {
                            try (PreparedStatement ps4 = conn.prepareStatement(sq4)) {
                                ps4.setInt(1, product.getProductID());

                                try (ResultSet rs4 = ps4.executeQuery()) {
                                    if (rs4.next()) {
                                        String sq5 = "UPDATE PRODUCTS SET EARLIEST_RENTAL_START = ? WHERE PRODUCT_ID = ?";
                                        try (PreparedStatement ps5 = conn.prepareStatement(sq5)) {
                                            ps5.setString(1, rs4.getString("RENTAL_DEADLINE"));
                                            ps5.setInt(2, product.getProductID());

                                            ps5.executeUpdate();
                                        }
                                    } else {
                                        conn.rollback();
                                        throw new NoResultsFoundException("Reservation not found.");
                                    }
                                }
                            }
                        }
                    } else {
                        conn.rollback();
                        throw new NoResultsFoundException("Product not found.");
                    }
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseException("Failed to execute rollback.", rollbackEx);
            }
            throw new RentalFailedException("Database exception occurred.", e);
        } finally {
            try {
            conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to set auto commit true.", e);
            }
        }

        return true;
    }

    public boolean addNewReservedProduct(Product product, int userID) throws ReserveFailedException, NoResultsFoundException, DatabaseException {
        String sq1 = "UPDATE PRODUCTS SET NUM_RESERVATION = NUM_RESERVATION + 1 WHERE PRODUCT_ID = ?";
        String sq2 = "INSERT INTO RESERVED_PRODUCTS (USER_ID, PRODUCT_ID, EARLIEST_RENTAL_START) VALUES (?, ?, ?)";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sq1)) {
                ps1.setInt(1, product.getProductID());

                int affected = ps1.executeUpdate();
                if (affected != 1) {
                    conn.rollback();
                    throw new NoResultsFoundException("Product not found.");
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sq2)) {
                ps2.setInt(1, userID);
                ps2.setInt(2, product.getProductID());
                ps2.setString(3, product.getEarliestRentalStart().toString());

                ps2.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseException("Failed to execute rollback.", rollbackEx);
            }
            throw new ReserveFailedException("Database exception occurred.", e);
        } finally {
            try {
            conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to set auto commit true.", e);
            }
        }

        return true;
    }

    public ArrayList<RentalProduct> getRentalProducts(int userID) throws NoResultsFoundException, DatabaseException{
        ArrayList<RentalProduct> rentalProducts = new ArrayList<>();
        String sq1 = "SELECT * FROM RENTAL_PRODUCTS WHERE USER_ID = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(sq1)) {
            ps1.setInt(1, userID);
            try (ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    String sq2 = "SELECT * FROM PRODUCTS WHERE PRODUCT_ID = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(sq2)) {
                        ps2.setInt(1, rs1.getInt("PRODUCT_ID"));
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            rentalProducts.add(new RentalProduct(rs1, rs2));
                        }
                    }
                }
                if (rentalProducts.isEmpty()) throw new NoResultsFoundException("Rental products not found.");
            }

            return rentalProducts;
        } catch (SQLException e) {
            throw new DatabaseException("Database exception occurred.", e);
        }
    }
    
    public ArrayList<ReservedProduct> getReservedProducts(int userID) throws NoResultsFoundException, DatabaseException{
        ArrayList<ReservedProduct> reservedProducts = new ArrayList<>();
        String sq1 = "SELECT * FROM RESERVED_PRODUCTS WHERE USER_ID = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(sq1)) {
            ps1.setInt(1, userID);
            try (ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    String sq2 = "SELECT * FROM PRODUCTS WHERE PRODUCT_ID = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(sq2)) {
                        ps2.setInt(1, rs1.getInt("PRODUCT_ID"));
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            reservedProducts.add(new ReservedProduct(rs1, rs2));
                        }
                    }
                }
                if (reservedProducts.isEmpty()) throw new NoResultsFoundException("Reserved products not found");
            }

            return reservedProducts;
        } catch (SQLException e) {
            throw new DatabaseException("Database exception occurred.", e);
        }
    }

    public boolean returnProduct(RentalProduct rentalProduct, int userID) throws ReturnFailedException, NoResultsFoundException, DatabaseException{
        String sq1 = "UPDATE PRODUCTS SET CURRENT_STOCK  = CURRENT_STOCK + 1 WHERE PRODUCT_ID = ? AND CURRENT_STOCK < TOTAL_STOCK";
        String sq2 = "DELETE FROM RENTAL_PRODUCTS WHERE USER_ID = ? AND RENTAL_PRODUCT_ID = ?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sq1)) {
                ps1.setInt(1, rentalProduct.getProductID());

                int affected = ps1.executeUpdate();
                if (affected != 1) {
                    conn.rollback();
                    throw new NoResultsFoundException("Product not found.");
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sq2)) {
                ps2.setInt(1, userID);
                ps2.setInt(2, rentalProduct.getRentalProductID());

                int affected = ps2.executeUpdate();
                if (affected != 1) {
                    conn.rollback();
                    throw new NoResultsFoundException("Rental not found.");
                }
            }

            String sq3 = "SELECT * FROM PRODUCTS WHERE PRODUCT_ID = ?";
            try (PreparedStatement ps3 = conn.prepareStatement(sq3)) {
                ps3.setInt(1, rentalProduct.getProductID());

                try (ResultSet rs3 = ps3.executeQuery()) {
                    if (rs3.next()) {
                        if (rs3.getInt("CURRENT_STOCK") == 1 && rs3.getInt("NUM_RESERVATION") > 0) {
                            Integer newRentalUserID = null;
                            String sq4 = "SELECT * FROM RESERVED_PRODUCTS WHERE PRODUCT_ID = ? ORDER BY RESERVED_PRODUCT_ID";
                            try (PreparedStatement ps4 = conn.prepareStatement(sq4)) {
                                ps4.setInt(1, rentalProduct.getProductID());
                                try (ResultSet rs4 = ps4.executeQuery()) {
                                    if (rs4.next()) newRentalUserID = rs4.getInt("USER_ID");
                                    else {
                                        conn.rollback();
                                        throw new NoResultsFoundException("Reserved product not found.");
                                    }
                                }
                            }
                            Product product = new Product(rs3);
                            addNewRentalProduct(product, newRentalUserID);
                            cancelProductReserve(product, newRentalUserID);
                        }
                    } else {
                        conn.rollback();
                        throw new NoResultsFoundException("Product not found.");
                    }
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseException("Failed to execute rollback.", rollbackEx);
            }
            throw new ReturnFailedException("Database exception occurred.", e);
        } catch (RentalFailedException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseException("Failed to execute rollback.", rollbackEx);
            }
            throw new ReturnFailedException("Database exception occurred.", e);
        } catch (ReserveCancelFailedException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseException("Failed to execute rollback.", rollbackEx);
            }
            throw new ReturnFailedException("Database exception occurred.", e);
        } finally {
            try {
            conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to set auto commit true.", e);
            }
        }

        return true;
    }

    public boolean cancelProductReserve(Product product, int userID) throws ReserveCancelFailedException, NoResultsFoundException, DatabaseException{
        String sq1 = "UPDATE PRODUCTS SET NUM_RESERVATION = NUM_RESERVATION - 1 WHERE PRODUCT_ID = ? AND NUM_RESERVATION > 0";
        String sq2 = "DELETE FROM RESERVED_PRODUCTS WHERE USER_ID = ? AND PRODUCT_ID = ?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sq1)) {
                ps1.setInt(1, product.getProductID());

                int affected = ps1.executeUpdate();
                if (affected != 1) {
                    conn.rollback();
                    throw new NoResultsFoundException("Product not found.");
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sq2)) {
                ps2.setInt(1, userID);
                ps2.setInt(2, product.getProductID());

                int affected = ps2.executeUpdate();
                if (affected != 1) {
                    conn.rollback();
                    throw new NoResultsFoundException("Reservation not found.");
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseException("Failed to execute rollback.", rollbackEx);
            }
            throw new ReserveCancelFailedException("Database exception occurred.", e);
        } finally {
            try {
            conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DatabaseException("Failed to set auto commit true.", e);
            }
        }

        return true;
    }
}
