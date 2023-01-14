package iafenvoy.accountswitcher.config;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import iafenvoy.accountswitcher.gui.AccountScreen;
import iafenvoy.accountswitcher.mixins.MinecraftClientAccessor;
import iafenvoy.accountswitcher.mixins.PlayerSkinProviderAccessor;
import iafenvoy.accountswitcher.utils.LocalYggdrasilAuthenticationService;
import iafenvoy.accountswitcher.utils.LocalYggdrasilMinecraftSessionService;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.Session;

import java.io.File;

public class InjectorAccount extends Account {
    private String injectorServer = "";

    public InjectorAccount(String username, String password) {
        super(AccountType.Injector, username, password);
    }

    @Override
    public void use(AccountScreen screen) {
        new Thread(() -> {
            screen.injectorLogin.doLogin(this, injectorServer, this.username, null);
            YggdrasilAuthenticationService services = new LocalYggdrasilAuthenticationService(((MinecraftClientAccessor) client).getNetProxy(), this.injectorServer);
            this.applyServices(services);
            AccountManager.CURRENT = this;
        }).start();
    }

    @Override
    public void applyServices(YggdrasilAuthenticationService services) {
        Session session = new Session(this.username, this.uuid, this.mcToken, "mojang");
        ((MinecraftClientAccessor) client).setSession(session);
        MinecraftSessionService service;
        service = new LocalYggdrasilMinecraftSessionService(services, this.injectorServer);
        ((MinecraftClientAccessor) client).setServices(service);
        SocialInteractionsService field26902 = this.method_31382(services, this.mcToken);
        ((MinecraftClientAccessor) client).setField26902(field26902);
        ((MinecraftClientAccessor) client).setManager(new SocialInteractionsManager(client, field26902));
        File skinDir = ((PlayerSkinProviderAccessor) client.getSkinProvider()).getSkinCacheDir();
        ((MinecraftClientAccessor) client).setSkinProvider(new PlayerSkinProvider(client.getTextureManager(), skinDir, service));
    }

    @Override
    public void refresh(AccountScreen screen) {
        new Thread(() -> screen.injectorLogin.doLogin(this, injectorServer, username, null)).start();
    }

    public String getInjectorServer() {
        return injectorServer;
    }

    public void setInjectorServer(String injectorServer) {
        this.injectorServer = injectorServer;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof InjectorAccount)) return false;
        InjectorAccount ac = (InjectorAccount) obj;
        return this.uuid.equals(ac.uuid) && this.injectorServer.equals(ac.injectorServer);
    }
}
