package iafenvoy.accountswitcher.login;

public class AuthRequest {
    public String server, name, password;

    public AuthRequest(String server, String name, String password) {
        this.server = server;
        this.name = name;
        this.password = password;
    }

    public AuthRequest() {

    }
}
