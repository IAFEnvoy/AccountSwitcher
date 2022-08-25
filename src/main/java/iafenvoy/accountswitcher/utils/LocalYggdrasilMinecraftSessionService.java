package iafenvoy.accountswitcher.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

public class LocalYggdrasilMinecraftSessionService extends YggdrasilMinecraftSessionService {
    public LocalYggdrasilMinecraftSessionService(YggdrasilMinecraftSessionService service, String serverUrl) {
        super(service.getAuthenticationService(), new InjectorEnvironment(serverUrl));
    }

    @Override
    public void joinServer(GameProfile profile, String authenticationToken, String serverId) {
    }
}
