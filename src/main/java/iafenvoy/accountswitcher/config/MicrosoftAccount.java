package iafenvoy.accountswitcher.config;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import iafenvoy.accountswitcher.gui.AccountScreen;
import iafenvoy.accountswitcher.mixins.MinecraftClientAccessor;
import iafenvoy.accountswitcher.mixins.PlayerSkinProviderAccessor;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.Session;

import java.io.File;

public class MicrosoftAccount extends Account {
    public MicrosoftAccount(String username, String uuid) {
        super(AccountType.Microsoft, username, uuid);
    }

    @Override
    public void use(AccountScreen screen) {
        new Thread(() -> {
            screen.microsoftLogin.useAccount(this);
            YggdrasilAuthenticationService services = new YggdrasilAuthenticationService(((MinecraftClientAccessor) client).getNetProxy());
            this.applyServices(services);
            AccountManager.CURRENT = this;
        }).start();
    }

    @Override
    public void applyServices(YggdrasilAuthenticationService services) {
        Session session = new Session(this.username, this.uuid, this.mcToken, "mojang");
        ((MinecraftClientAccessor) client).setSession(session);
        MinecraftSessionService service;
        service = services.createMinecraftSessionService();
        ((MinecraftClientAccessor) client).setServices(service);
        SocialInteractionsService field26902 = this.method_31382(services, this.mcToken);
        ((MinecraftClientAccessor) client).setField26902(field26902);
        ((MinecraftClientAccessor) client).setManager(new SocialInteractionsManager(client, field26902));
        File skinDir = ((PlayerSkinProviderAccessor) client.getSkinProvider()).getSkinCacheDir();
        ((MinecraftClientAccessor) client).setSkinProvider(new PlayerSkinProvider(client.getTextureManager(), skinDir, service));
    }

    @Override
    public void refresh(AccountScreen screen) {
        new Thread(() -> screen.microsoftLogin.refreshAccessToken(this)).start();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof MicrosoftAccount)) return false;
        MicrosoftAccount ac = (MicrosoftAccount) obj;
        return this.uuid.equals(ac.uuid);
    }
}
