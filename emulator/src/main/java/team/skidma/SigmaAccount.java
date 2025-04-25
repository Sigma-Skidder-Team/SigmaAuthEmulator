package team.skidma;

public class SigmaAccount {
    public String username, email, password;
    public boolean premium = false;

    public SigmaAccount(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
