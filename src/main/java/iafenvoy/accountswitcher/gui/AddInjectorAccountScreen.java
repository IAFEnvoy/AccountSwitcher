package iafenvoy.accountswitcher.gui;

import iafenvoy.accountswitcher.config.Account;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class AddInjectorAccountScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountScreen parent;
    private TextFieldWidget server, username, password;
    private final Account account;

    public AddInjectorAccountScreen(AccountScreen parent) {
        this(parent, null);
    }

    public AddInjectorAccountScreen(AccountScreen parent, Account account) {
        super(new TranslatableText("as.gui.injector.title"));
        this.parent = parent;
        this.account = account;
    }

    public void openParent() {
        client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();
        this.server = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 50, 200, 20, new LiteralText(this.account == null ? "" : this.account.getInjectorServer())));
        this.username = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 25, 200, 20, new LiteralText(this.account == null ? "" : this.account.getUsername())));
        this.password = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2, 200, 20, new LiteralText(this.account == null ? "" : this.account.getAccessToken())));
        this.addField(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 25, 100, 20, new TranslatableText("as.gui.Accept"), button -> {
            new Thread(() -> {
                if (account == null) {
                    Account a = new Account(Account.AccountType.Injector);
                    if (parent.injectorLogin.doLogin(a, this.server.getText(), this.username.getText(), this.password.getText()))
                        parent.addAccount(a);
                } else
                    parent.injectorLogin.doLogin(account, this.server.getText(), this.username.getText(), this.password.getText());
            }).start();
            this.openParent();
        }));
        this.addField(new ButtonWidget(this.width / 2, this.height / 2 + 25, 100, 20, new TranslatableText("as.gui.Cancel"), button -> this.openParent()));

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        client.textRenderer.drawWithShadow(matrices, new TranslatableText("as.gui.injector.label1"), this.width / 2.0F - 175, this.height / 2.0F - 45, 16777215);
        client.textRenderer.drawWithShadow(matrices, new TranslatableText("as.gui.injector.label2"), this.width / 2.0F - 175, this.height / 2.0F - 20, 16777215);
        client.textRenderer.drawWithShadow(matrices, new TranslatableText("as.gui.injector.label3"), this.width / 2.0F - 175, this.height / 2.0F + 5, 16777215);
        drawCenteredText(matrices, textRenderer, this.title, this.width / 2, this.height / 2 - 70, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public ClickableWidget addField(ClickableWidget drawable){
        this.addDrawable(drawable);
        this.addSelectableChild(drawable);
        return drawable;
    }
}
