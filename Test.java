import sql.*;
import sql.exception.DatabaseException;

public class Test {
    public static SQL sql;
    public static void main(String[] args) {
        try {
            sql = new SQL();
            System.out.println("Connection completed.");
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                sql.disconnect();
            } catch (DatabaseException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Disconnection completed.");
        }
    }
}
