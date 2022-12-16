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

public class AddCustomAccountScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountScreen parent;
    private TextFieldWidget username, uuid, token;

    public AddCustomAccountScreen(AccountScreen parent) {
        super(new TranslatableText("as.gui.custom.title"));
        this.parent = parent;
    }

    public void openParent() {
        client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();
        this.username = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 50, 200, 20, new LiteralText("")));
        this.uuid = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 25, 200, 20, new LiteralText("")));
        this.token = (TextFieldWidget) this.addField(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2, 200, 20, new LiteralText("")));
        this.token.setMaxLength(1000);
        this.addField(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 25, 100, 20, new TranslatableText("as.gui.Accept"), button -> {
            new Thread(() -> {
                Account account = new Account(Account.AccountType.Custom);
                account.setUsername(this.username.getText());
                account.setUuid(this.uuid.getText());
                account.setMcToken(this.token.getText());
                this.parent.addAccount(account);
            }).start();
            this.openParent();
        }));
        this.addField(new ButtonWidget(this.width / 2, this.height / 2 + 25, 100, 20, new TranslatableText("as.gui.Cancel"), button -> this.openParent()));

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        client.textRenderer.drawWithShadow(matrices, new TranslatableText("as.gui.custom.label1"), this.width / 2.0F - 175, this.height / 2.0F - 45, 16777215);
        client.textRenderer.drawWithShadow(matrices, new TranslatableText("as.gui.custom.label2"), this.width / 2.0F - 175, this.height / 2.0F - 20, 16777215);
        client.textRenderer.drawWithShadow(matrices, new TranslatableText("as.gui.custom.label3"), this.width / 2.0F - 175, this.height / 2.0F + 5, 16777215);
        drawCenteredText(matrices, textRenderer, this.title, this.width / 2, this.height / 2 - 70, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public ClickableWidget addField(ClickableWidget drawable) {
        this.addDrawable(drawable);
        this.addSelectableChild(drawable);
        return drawable;
    }
}
