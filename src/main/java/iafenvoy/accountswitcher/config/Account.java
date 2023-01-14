package iafenvoy.accountswitcher.config;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import iafenvoy.accountswitcher.AccountSwitcher;
import iafenvoy.accountswitcher.gui.AccountScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;

public abstract class Account {
    protected static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountType type;
    protected String username = "", uuid = "", mcToken = "";

    protected Account(AccountType type, String username, String uuid) {
        this.type = type;
        this.username = username;
        this.uuid = uuid;
    }

    public AccountType getType() {
        return type;
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

    public abstract void use(AccountScreen screen);

    public abstract void applyServices(YggdrasilAuthenticationService services);


    protected SocialInteractionsService method_31382(YggdrasilAuthenticationService yggdrasilAuthenticationService, String token) {
        try {
            return yggdrasilAuthenticationService.createSocialInteractionsService(token);
        } catch (AuthenticationException e) {
            AccountSwitcher.LOGGER.error("Failed to verify authentication", e);
            return new OfflineSocialInteractions();
        }
    }

    public abstract void refresh(AccountScreen screen);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Account)) return false;
        return this == obj;
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
