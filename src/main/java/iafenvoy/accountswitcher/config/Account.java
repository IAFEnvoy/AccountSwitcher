package iafenvoy.accountswitcher.config;

import iafenvoy.accountswitcher.login.MicrosoftLogin;
import iafenvoy.accountswitcher.mixins.MinecraftClientAccessor;
import iafenvoy.accountswitcher.utils.YggdrasilServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Session;

public class Account {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountType type;
    private String accessToken, refreshToken, mcToken;
    private String username, uuid;
    private YggdrasilServer injectorServer;

    public Account(Session session) {
        this(session.getAccessToken().equals("") ? AccountType.Offline : AccountType.Microsoft, null, null, session.getUsername(), session.getUuid());
        this.mcToken = session.getAccessToken();
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

    public String getMcToken() {
        return mcToken;
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

    public void use(MicrosoftLogin login) {
        if (this.type == AccountType.Microsoft)
            new Thread(() -> {
                login.useAccount(this);
                Session session = new Session(this.username, this.uuid, this.mcToken, "mojang");
                ((MinecraftClientAccessor) client).setSession(session);
                AccountManager.CURRENT = this;
            }).start();
        else {
            Session session = new Session(this.username, this.uuid, this.mcToken, "mojang");
            ((MinecraftClientAccessor) client).setSession(session);
            AccountManager.CURRENT = this;
        }
    }

    public void refresh(MicrosoftLogin login) {
        if (this.type == AccountType.Microsoft)
            new Thread(() -> login.refreshAccessToken(this)).start();
    }

    public enum AccountType {
        Offline("as.type.Offline"),//离线模式
        Microsoft("as.type.Microsoft"),//微软账户正版
        Injector("as.type.Injector");//外置登录
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
