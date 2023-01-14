package iafenvoy.accountswitcher.config;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import iafenvoy.accountswitcher.gui.AccountScreen;
import iafenvoy.accountswitcher.mixins.MinecraftClientAccessor;
import net.minecraft.client.util.Session;

public class OfflineAccount extends Account {
    protected OfflineAccount(String username, String uuid) {
        super(AccountType.Offline, username, uuid);
    }

    @Override
    public void use(AccountScreen screen) {
        Session session = new Session(this.username, this.uuid, this.mcToken, "mojang");
        ((MinecraftClientAccessor) client).setSession(session);
        AccountManager.CURRENT = this;
    }

    @Override
    public void applyServices(YggdrasilAuthenticationService services) {

    }

    @Override
    public void refresh(AccountScreen screen) {

    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof OfflineAccount)) return false;
        OfflineAccount ac = (OfflineAccount) obj;
        return this.username.equals(ac.username);
    }
}
