package iafenvoy.accountswitcher.utils;

public class YggdrasilServer {
    private final String name;
    private final String authHost;
    private final String accountsHost;
    private final String sessionHost;
    private final String servicesHost;

    private YggdrasilServer(String name, String authHost, String accountsHost, String sessionHost, String servicesHost) {
        this.name = name;
        this.authHost = authHost;
        this.accountsHost = accountsHost;
        this.sessionHost = sessionHost;
        this.servicesHost = servicesHost;
    }

    public String getAuthHost() {
        return authHost;
    }

    public String getAccountsHost() {
        return accountsHost;
    }

    public String getSessionHost() {
        return sessionHost;
    }

    public String getServicesHost() {
        return servicesHost;
    }

    public String getName() {
        return this.name;
    }
}
