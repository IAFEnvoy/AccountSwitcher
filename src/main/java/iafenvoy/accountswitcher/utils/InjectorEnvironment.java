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
        return "https://" + this.url + "/api/yggdrasil/authserver";
    }

    @Override
    public String getAccountsHost() {
        return "https://" + this.url + "/api/yggdrasil/api";
    }

    @Override
    public String getSessionHost() {
        return "https://" + this.url + "/api/yggdrasil/sessionserver";
    }

    @Override
    public String getServicesHost() {
        return "https://" + this.url + "/api/yggdrasil/minecraftservices";
    }

    @Override
    public String getName() {
        return "Authlib-Injector";
    }

    @Override
    public String asString() {
        return new StringJoiner(", ", "", "")
                .add("authHost='" + this.getAuthHost() + "'")
                .add("accountsHost='" + this.getAccountsHost() + "'")
                .add("sessionHost='" + this.getSessionHost() + "'")
                .add("servicesHost='" + this.getServicesHost() + "'")
                .add("name='" + this.getName() + "'")
                .toString();
    }
}
