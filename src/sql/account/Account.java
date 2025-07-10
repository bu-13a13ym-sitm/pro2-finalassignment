package sql.account;

public class Account {
    private int userID;
    private String userName;
    private String email;
    private String password;

    public Account(int userID, String userName, String email, String password) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public int getUserID() {return this.userID;}
    public String getUserName() {return this.userName;}
    public String getEmail() {return this.email;}
    public String getPassword() {return this.password;}
}
