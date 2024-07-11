package iafenvoy.accountswitcher.gui;

import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.utils.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class AddInjectorAccountScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountScreen parent;
    private final Account account;
    private TextFieldWidget server, username, password;

    public AddInjectorAccountScreen(AccountScreen parent) {
        this(parent, null);
    }

    public AddInjectorAccountScreen(AccountScreen parent, Account account) {
        super(Text.translatable("as.gui.injector.title"));
        this.parent = parent;
        this.account = account;
    }

    public void openParent() {
        client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();
        this.server = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 50, 200, 20, Text.literal(this.account == null ? "" : this.account.getInjectorServer())));
        this.username = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 25, 200, 20, Text.literal(this.account == null ? "" : this.account.getUsername())));
        this.password = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2, 200, 20, Text.literal(this.account == null ? "" : this.account.getAccessToken())));
        this.addField(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 25, 100, 20, Text.translatable("as.gui.Accept"), button -> {
            new Thread(() -> {
                if (this.account == null) {
                    Account a = new Account(Account.AccountType.Injector);
                    if (this.parent.injectorLogin.doLogin(a, this.server.getText(), this.username.getText(), this.password.getText()))
                        this.parent.addAccount(a);
                } else
                    this.parent.injectorLogin.doLogin(this.account, this.server.getText(), this.username.getText(), this.password.getText());
            }).start();
            this.openParent();
        }));
        this.addField(new ButtonWidget(this.width / 2, this.height / 2 + 25, 100, 20, Text.translatable("as.gui.Cancel"), button -> this.openParent()));

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);
        context.drawText(this.textRenderer, Text.translatable("as.gui.injector.label1"), this.width / 2 - 175, this.height / 2 - 45, 16777215, true);
        context.drawText(this.textRenderer, Text.translatable("as.gui.injector.label2"), this.width / 2 - 175, this.height / 2 - 20, 16777215, true);
        context.drawText(this.textRenderer, Text.translatable("as.gui.injector.label3"), this.width / 2 - 175, this.height / 2 + 5, 16777215, true);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 70, 16777215);
        super.render(context, mouseX, mouseY, delta);
    }

    public ClickableWidget addField(ClickableWidget drawable) {
        this.addDrawable(drawable);
        this.addSelectableChild(drawable);
        return drawable;
    }
}
