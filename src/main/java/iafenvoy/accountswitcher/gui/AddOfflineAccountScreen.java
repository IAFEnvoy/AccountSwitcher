package iafenvoy.accountswitcher.gui;

import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.login.OfflineLogin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class AddOfflineAccountScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final AccountScreen parent;
    private TextFieldWidget usernameField;

    public AddOfflineAccountScreen(AccountScreen parent) {
        super(new TranslatableText("as.gui.offline.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.usernameField = this.addButton(new TextFieldWidget(client.textRenderer, this.width / 2 - 100, this.height / 2 - 30, 200, 20, new LiteralText("")));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 10, 100, 20, new TranslatableText("as.gui.Accept"), button -> {
            if (this.usernameField.getText().equals("")) return;
            Account account = OfflineLogin.generateAccount(this.usernameField.getText());
            parent.addAccount(account);
            this.openParent();
        }));
        this.addButton(new ButtonWidget(this.width / 2, this.height / 2 + 10, 100, 20, new TranslatableText("as.gui.Cancel"), button -> this.openParent()));
    }

    public void openParent() {
        client.openScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        drawCenteredText(matrices, textRenderer, this.title, this.width / 2, this.height / 2 - 50, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
