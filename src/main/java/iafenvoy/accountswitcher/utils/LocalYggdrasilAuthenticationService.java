package iafenvoy.accountswitcher.utils;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.net.Proxy;

public class LocalYggdrasilAuthenticationService extends YggdrasilAuthenticationService {
    public LocalYggdrasilAuthenticationService(Proxy proxy, String serverUrl) {
        super(proxy, new InjectorEnvironment(serverUrl));
    }
}
