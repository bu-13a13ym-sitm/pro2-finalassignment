package processor;
import java.util.ArrayList;
import java.util.regex.Pattern;

import processor.exceptions.*;
import sql.*;
import sql.account.*;
import sql.product.*;
import sql.exception.*;

public class Processor {
    private SQL sql;
    private Account currentUser;
    private static final Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Processor() throws ConnectionFailedException {
        try {
            this.sql = new SQL();
            this.currentUser = null;
        } catch (DatabaseException e) {
            throw new ConnectionFailedException("Failed to make instance of Processor class.", e);
        }
    }

    public boolean loginCheck(String email, String password) throws EmptyInputException, EmailPatternException, IncorrectInputException, ConnectionFailedException {
        if(email == null || email.trim().isEmpty()){
            throw new EmptyInputException("Email empty.");
        }
        if(password == null || password.trim().isEmpty()){
            throw new EmptyInputException("Password empty");
        }
        if(!validEmailPattern.matcher(email).find()){
            throw new EmailPatternException("Invalid email.");
        }

        try {
            Account account = sql.getCustomerInfo(email);
            if(account.getPassword().equals(password)){
                this.currentUser = account;
                return true;
            }else{
                throw new IncorrectInputException("Incorrect password");
            }
        } catch (NoResultsFoundException | DatabaseException e) {
            throw new ConnectionFailedException("Failed to login due to database error.", e);
        }
    }

    public void logout() {
        this.currentUser = null;
    }

    public void disconnect() throws ConnectionFailedException {
        try {
            this.sql.disconnect();
        } catch (DatabaseException e) {
            throw new ConnectionFailedException("Failed to close connection due to database error.", e);
        }
    }

    //Exception definition required
    public boolean newCustomer(String userName, String email, String password) throws EmptyInputException, EmailPatternException, ConnectionFailedException {
        if(userName == null || userName.trim().isEmpty()){
            throw new EmptyInputException("UserName empty.");
        }
        if(email == null || email.trim().isEmpty()){
            throw new EmptyInputException("Email empty.");
        }
        if(password == null || password.trim().isEmpty()){
            throw new EmptyInputException("Password empty");
        }
        if(!validEmailPattern.matcher(email).find()){
            throw new EmailPatternException("Invalid email.");
        }
        try {
            return sql.addNewCustomer(userName, email, password);
        } catch (UserAlreadyExistException | DatabaseExceptione e) {
            throw new ConnectionFailedException("Failed to register due to database error.", e);
        }        
    }

    //Exception definition required
    public ArrayList<Product> getProducts(Integer minPrice, Integer maxPrice, Boolean immediate) throws Exception {
        if(minPrice == null){
            throw new Exception("minPrice null.");
        }
        if(maxPrice == null){
            throw new Exception("maxPrice null.");
        }       
        if (minPrice > maxPrice) {
            throw new Exception("Minimum price cannot be greater than maximum price.");
        }
        try {
            return sql.productQuery(minPrice, maxPrice, immediate);
        } catch (NoResultsFoundException e) {
            throw new NoResultsFoundException("Products not found.", e);
        } catch (DatabaseException e){
            throw new DatabaseException("Database exception occurred during searching.", e);
        } 
    }

    //Exception definition required
    public boolean executeRental(Product product) throws Exception {
        if (currentUser == null) {
            throw new Exception("You must be logged in to rent a product.");
        }
        try {
            return sql.addNewRentalProduct(product, this.currentUser.getUserID());
        } catch (RentalFailedException | NoResultsFoundException | DatabaseException e) {
            throw e;
        }
    }

    //Exception definition required
    public boolean executeReserve(Product product) {
        //
    }

    //Exception definition required
    public AllayList<RentalProduct> getRentalProducts(int userID) {
        //
    }

    //Exception definition required
    public AllayList<ReservedProduct> getReservedProduct(int userID) {
        //
    }

    //Exception definition required
    public boolean executeReturn(Product product) {
        //
    }

    //Exception definition required
    public boolean executeReserveCancel(Product product) {
        //
    }
}
