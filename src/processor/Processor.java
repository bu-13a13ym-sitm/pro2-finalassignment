package processor;
import java.util.ArrayList;
import java.util.regex.Pattern;
import processor.exceptions.*;
import sql.*;
import sql.account.*;
import sql.exception.*;
import sql.product.*;

public class Processor {
    private SQL sql;
    private Account currentUser;
    private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public Processor() throws DatabaseErrorException {
        try {
            this.sql = new SQL();
            this.currentUser = null;
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to make instance of Processor class due to database error.", e);
        }
    }

    public boolean loginCheck(String email, String password) throws EmptyInputException, EmailPatternException, IncorrectInputException, NoAccountException, DatabaseErrorException{
        boolean emptyFlag = false;
        StringBuilder emptyExceptionMessage = new StringBuilder();
        if(email == null || email.trim().isEmpty()){
            emptyFlag = true;
            emptyExceptionMessage.append("Email is empty.");
        }
        if(password == null || password.trim().isEmpty()){
            if(emptyFlag) emptyExceptionMessage.append(" ");
            emptyFlag = true;
            emptyExceptionMessage.append("Email is empty.");
        }
        if(emptyFlag){
            throw new EmptyInputException(emptyExceptionMessage.toString());
        }
        if(!VALID_EMAIL_PATTERN.matcher(email).find()){
            throw new EmailPatternException("Invalid email.");
        }
        try {
            Account account = sql.getCustomerInfo(email);
            if(account.getPassword().equals(password)){
                this.currentUser = account;
                return true;
            }else{
                throw new IncorrectInputException("Email address or password is incorrect.");
            }
        } catch (NoResultsFoundException e) {
            throw new NoAccountException("Email address or password is incorrect.", e);
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to login due to database error.", e);
        }
    }

    public void logout() {
        this.currentUser = null;
    }

    public void disconnect() throws DatabaseErrorException {
        try {
            this.sql.disconnect();
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to close the connection due to database error.", e);
        }
    }

    public boolean newCustomer(String userName, String email, String password) throws EmptyInputException, EmailPatternException,  AlreadyRegisteredException, DatabaseErrorException{
        boolean emptyFlag = false;
        StringBuilder emptyExceptionMessage = new StringBuilder();
        if(userName == null || userName.trim().isEmpty()){
            emptyFlag = true;
            emptyExceptionMessage.append("UserName is empty.");
        }
        if(email == null || email.trim().isEmpty()){
            if(emptyFlag) emptyExceptionMessage.append(" ");
            emptyFlag = true;
            emptyExceptionMessage.append("Email is empty.");
        }
        if(password == null || password.trim().isEmpty()){
            if(emptyFlag) emptyExceptionMessage.append(" ");
            emptyFlag = true;
            emptyExceptionMessage.append("Password is empty.");
        }
        if(emptyFlag){
            throw new EmptyInputException(emptyExceptionMessage.toString());
        }
        if(!VALID_EMAIL_PATTERN.matcher(email).find()){
            throw new EmailPatternException("Invalid email.");
        }
        try {
            return sql.addNewCustomer(userName, email, password);
        } catch (UserAlreadyExistException e) {
            throw new AlreadyRegisteredException("Your account is already registered.", e);
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to register due to database error.", e);
        }     
    }

    public ArrayList<Product> getProducts(Integer minPrice, Integer maxPrice, Boolean immediate) throws EmptyInputException, NoProductFoundException, DatabaseErrorException {
        if(minPrice == null){
            minPrice = 0;
        }
        if(maxPrice == null){
            maxPrice = Integer.MAX_VALUE;
        } 
        if (minPrice > maxPrice) {
            throw new EmptyInputException("Minimum price cannot be greater than maximum price.");
        }
        try {
            return sql.productQuery(minPrice, maxPrice, immediate);
        } catch (NoResultsFoundException e) {
            throw new NoProductFoundException("No product is found in the price range.", e);
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to search products due to database error.", e);
        }
    }

    public boolean executeRental(Product product) throws NotYetLoginException, NoProductFoundException, DatabaseErrorException{
        if (this.currentUser == null) {
            throw new NotYetLoginException("You must login to rent a product.");
        }
        try {
            return sql.addNewRentalProduct(product, this.currentUser.getUserID());
        } catch (NoResultsFoundException e) {
            throw new NoProductFoundException("The product is not found in the stock.", e);
        } catch (RentalFailedException | DatabaseException e) {
            e.printStackTrace();
            throw new DatabaseErrorException("Failed to rental the product due to database error.", e);
        }
    }

    public boolean executeReserve(Product product) throws NotYetLoginException, NoProductFoundException, DatabaseErrorException{
        if(this.currentUser == null){
            throw new NotYetLoginException("You must login to reserve product.");
        }
        try{
            return sql.addNewReservedProduct(product, this.currentUser.getUserID());
        } catch (NoResultsFoundException e){
            throw new NoProductFoundException("The product is not found.", e);
        } catch (ReserveFailedException | DatabaseException e){
            throw new DatabaseErrorException("Failed to reserve the product due to database error.", e);
        }
    }

    public ArrayList<RentalProduct> getRentalProducts() throws NoProductFoundException, DatabaseErrorException{
        try {
            return sql.getRentalProducts(this.currentUser.getUserID());
        } catch (NoResultsFoundException e) {
            throw new NoProductFoundException("No rental product is found.", e);
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to search rental products due to database error.", e);
        }
    }

    public ArrayList<ReservedProduct> getReservedProduct() throws NoProductFoundException, DatabaseErrorException{
        try {
            return sql.getReservedProducts(this.currentUser.getUserID());
        } catch (NoResultsFoundException e) {
            throw new NoProductFoundException("No reserved product is found.", e);
        } catch (DatabaseException e) {
            throw new DatabaseErrorException("Failed to search reserved products due to database error.", e);
        }
    }

    public boolean executeReturn(RentalProduct product) throws NotYetLoginException, NoProductFoundException, DatabaseErrorException{
        if (currentUser == null) {
            throw new NotYetLoginException("You must login to return products.");
        }
        try {
            return sql.returnProduct(product, this.currentUser.getUserID());
        } catch (NoResultsFoundException e){
            throw new NoProductFoundException("The product is not your rental product.", e);//////////rentalProductのExceptionはもう少し分けた方が良さそう、特に"preserved product not found"のところ
        } catch (ReturnFailedException | DatabaseException e){
            throw new DatabaseErrorException("Failed to return the product due to database error.", e);
        }
    }

    public boolean executeReserveCancel(Product product) throws NotYetLoginException, NoProductFoundException, DatabaseErrorException{
        if (currentUser == null) {
            throw new NotYetLoginException("You must login to cancel reservation.");
        }
        try {
            return sql.cancelProductReserve(product, this.currentUser.getUserID());
        } catch (NoResultsFoundException e) {
            throw new NoProductFoundException("You do not reserve the product.", e);
        } catch (ReserveCancelFailedException | DatabaseException e) {
            throw new DatabaseErrorException("Failed to cancel reservation due to database error.", e);
        }
    }
}