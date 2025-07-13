package processor;
import java.util.ArrayList;

import processor.exceptions.*;
import sql.*;
import sql.account.*;
import sql.product.*;
import sql.exception.*;

public class Processor {
    private SQL sql;
    private Account currentUser;

    public Processor() throws ConnectionFailedException {
        try {
            this.sql = new SQL();
        } catch (DatabaseException e) {
            throw new ConnectionFailedException("Failed to make instance of Processor class.", e);
        }
    }

    public boolean loginCheck(String email, String password) {
        //
    }

    public void logout() {
        this.currentUser = null;
    }

    public void disconnect()  {
        this.sql.disconnect();
    }

    public boolean newCustomer(String userName, String email, String password) {
        //
    }

    public ArrayList<Product> getProducts(Integer minPrice, Integer maxPrice, Boolean immediate) {
        //
    }

    public boolean executeRental(Product product) {
        //
    }

    public boolean executeReserve(Product product) {
        //
    }

    public AllayList<RentalProduct> getRentalProducts(int userID) {
        //
    }

    public AllayList<ReservedProduct> getReservedProduct(int userID) {
        //
    }

    public boolean executeReturn(Product product) {
        //
    }

    public boolean executeReserveCancel(Product product) {
        //
    }
}
