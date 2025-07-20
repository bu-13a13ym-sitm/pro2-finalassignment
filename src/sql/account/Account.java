package sql.account;

public class Account {
    private int userID;
    private String userName;
    private String email;
    private String password;
    private String cardNumStr;
    private String securityCodeStr;

    public Account(int userID, String userName, String email, String password, String cardNumStr, String securityCodeStr) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.cardNumStr = cardNumStr;
        this.securityCodeStr = securityCodeStr;
    }

    public int getUserID() {return this.userID;}
    public String getUserName() {return this.userName;}
    public String getEmail() {return this.email;}
    public String getPassword() {return this.password;}
    public String getCardNumStr() {return this.cardNumStr;}
    public String getSecurityCodeStr() {return this.securityCodeStr;}
}
