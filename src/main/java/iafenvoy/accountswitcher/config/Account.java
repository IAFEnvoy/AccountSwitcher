package iafenvoy.accountswitcher.config;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import iafenvoy.accountswitcher.AccountSwitcher;
import iafenvoy.accountswitcher.gui.AccountScreen;
import iafenvoy.accountswitcher.mixins.MinecraftClientAccessor;
import iafenvoy.accountswitcher.mixins.PlayerSkinProviderAccessor;
import iafenvoy.accountswitcher.utils.LocalYggdrasilAuthenticationService;
import iafenvoy.accountswitcher.utils.LocalYggdrasilMinecraftSessionService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.Session;

import java.io.File;
import java.util.Optional;

public class Account {
    public static final Account EMPTY = new Account();
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountType type;
    private String accessToken = "", refreshToken = "", mcToken = "";
    private String username = "", uuid = "";
    private String injectorServer = "";

    public Account() {
        this.type = null;
    }

    public Account(AccountType type) {
        this.type = type;
    }

    public Account(AccountType type, String accessToken, String refreshToken, String username, String uuid) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.uuid = uuid;
    }

    public AccountType getType() {
        return type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getInjectorServer() {
        return injectorServer;
    }

    public void setMcToken(String mcToken) {
        this.mcToken = mcToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setInjectorServer(String injectorServer) {
        this.injectorServer = injectorServer;
    }

    public void use(AccountScreen screen) {
        if (this.type == AccountType.Microsoft)
            new Thread(() -> {
                screen.microsoftLogin.useAccount(this);
                YggdrasilAuthenticationService services = new YggdrasilAuthenticationService(((MinecraftClientAccessor) client).getNetProxy());
                this.applyServices(services, false);
                AccountManager.CURRENT = this;
            }).start();
        else if (this.type == AccountType.Injector)
            new Thread(() -> {
                screen.injectorLogin.doLogin(this, injectorServer, username, accessToken);
                YggdrasilAuthenticationService services = new LocalYggdrasilAuthenticationService(((MinecraftClientAccessor) client).getNetProxy(), this.injectorServer);
                this.applyServices(services, true);
                AccountManager.CURRENT = this;
            }).start();
        else {
            Session session = new Session(this.username, this.uuid, this.mcToken, Optional.empty(), Optional.empty(), Session.AccountType.MOJANG);
            ((MinecraftClientAccessor) client).setSession(session);
            AccountManager.CURRENT = this;
        }
    }

    private void applyServices(YggdrasilAuthenticationService services, boolean isInjector) {
        Session session = new Session(this.username, this.uuid, this.mcToken, Optional.empty(), Optional.empty(), Session.AccountType.MOJANG);
        ((MinecraftClientAccessor) client).setSession(session);
        MinecraftSessionService service;
        if (isInjector)
            service = new LocalYggdrasilMinecraftSessionService(services, this.injectorServer);
        else
            service = services.createMinecraftSessionService();
        ((MinecraftClientAccessor) client).setServices(service);
        UserApiService field26902 = this.method_31382(services, this.mcToken);
        ((MinecraftClientAccessor) client).setField26902(field26902);
        ((MinecraftClientAccessor) client).setManager(new SocialInteractionsManager(client, field26902));
        File skinDir = ((PlayerSkinProviderAccessor) client.getSkinProvider()).getSkinCacheDir();
        ((MinecraftClientAccessor) client).setSkinProvider(new PlayerSkinProvider(client.getTextureManager(), skinDir, service));
    }


    private UserApiService method_31382(YggdrasilAuthenticationService yggdrasilAuthenticationService, String token) {
        try {
            return yggdrasilAuthenticationService.createUserApiService(token);
        } catch (AuthenticationException e) {
            AccountSwitcher.LOGGER.error("Failed to verify authentication", e);
            return UserApiService.OFFLINE;
        }
    }

    public void refresh(AccountScreen screen) {
        if (this.type == AccountType.Microsoft)
            new Thread(() -> screen.microsoftLogin.refreshAccessToken(this)).start();
        if (this.type == AccountType.Injector)
            new Thread(() -> screen.injectorLogin.doLogin(this, injectorServer, username, accessToken)).start();
    }

    public enum AccountType {
        Offline("as.type.Offline"),//离线模式
        Microsoft("as.type.Microsoft"),//微软账户正版
        Injector("as.type.Injector"),//外置登录
        Custom("as.type.Custom");//自定义登录
        //不支持Mojang账户和统一通行证哦~

        private final String key;

        AccountType(String key) {
            this.key = key;
        }

        public static AccountType getByName(String name) {
            for (AccountType type : AccountType.values())
                if (type.key.equals(name))
                    return type;
            return null;
        }

        public String getName() {
            return I18n.translate(key);
        }

        public String getKey() {
            return this.key;
        }
    }
}
