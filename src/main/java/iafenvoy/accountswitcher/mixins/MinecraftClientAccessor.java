package iafenvoy.accountswitcher.mixins;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
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
    @Accessor("field_26902")
    void setField26902(SocialInteractionsService service);

    @Accessor("netProxy")
    Proxy getNetProxy();

    @Accessor("field_26902")
    SocialInteractionsService getField26902();
}
