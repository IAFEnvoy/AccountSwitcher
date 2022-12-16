package iafenvoy.accountswitcher.mixins;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.Proxy;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Mutable
    @Accessor("session")
    void setSession(Session session);

    @Mutable
    @Accessor("sessionService")
    void setServices(MinecraftSessionService service);

    @Mutable
    @Accessor("socialInteractionsManager")
    void setManager(SocialInteractionsManager manager);

    @Mutable
    @Accessor("socialInteractionsService")
    void setField26902(SocialInteractionsService service);

    @Mutable
    @Accessor("skinProvider")
    void setSkinProvider(PlayerSkinProvider skinProvider);

    @Accessor("networkProxy")
    Proxy getNetProxy();
}
