package iafenvoy.accountswitcher.mixins;

import iafenvoy.accountswitcher.AccountSwitcher;
import iafenvoy.accountswitcher.config.AccountManager;
import iafenvoy.accountswitcher.gui.AccountScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    private static final Identifier SWITCH_ACCOUNT_ICON_TEXTURE = new Identifier(AccountSwitcher.MOD_ID, "textures/gui/account.png");
    @Final
    @Shadow
    private boolean doBackgroundFade;
    @Shadow
    private long backgroundFadeStart;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    protected void init(CallbackInfo ci) {
        assert this.client != null;
        int j = this.height / 4 + 48;
        this.addField(new TexturedButtonWidget(this.width / 2 + 104, j + 24 * 2, 20, 20, 0, 0, 20, SWITCH_ACCOUNT_ICON_TEXTURE, 32, 64, (buttonWidget) -> this.client.setScreen(new AccountScreen(this)), Text.translatable("as.gui.title")));
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float f = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        drawCenteredText(matrices, this.textRenderer, AccountManager.getAccountInfoText(), this.width / 2, this.height - 50, 16777215 | l);
    }

    public void addField(ClickableWidget drawable) {
        this.addDrawable(drawable);
        this.addSelectableChild(drawable);
    }
}
