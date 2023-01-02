package iafenvoy.accountswitcher.utils;

import com.mojang.authlib.Environment;

import java.util.StringJoiner;

public class InjectorEnvironment implements Environment {
    private final String url;

    public InjectorEnvironment(String serverUrl) {
        this.url = serverUrl;
    }

    @Override
    public String getAuthHost() {
        return url + "/authserver";
    }

    @Override
    public String getAccountsHost() {
        return url + "/api";
    }

    @Override
    public String getSessionHost() {
        return url + "/sessionserver";
    }

    @Override
    public String getServicesHost() {
        return url + "/minecraftservices";
    }

    @Override
    public String getName() {
        return "Authlib-Injector";
    }

    @Override
    public String asString() {
        return new StringJoiner(", ", "", "")
                .add("authHost='" + getAuthHost() + "'")
                .add("accountsHost='" + getAccountsHost() + "'")
                .add("sessionHost='" + getSessionHost() + "'")
                .add("servicesHost='" + getServicesHost() + "'")
                .add("name='" + getName() + "'")
                .toString();
    }
}
