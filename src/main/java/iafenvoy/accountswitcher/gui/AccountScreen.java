package iafenvoy.accountswitcher.gui;

import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.config.AccountManager;
import iafenvoy.accountswitcher.login.MicrosoftLogin;
import iafenvoy.accountswitcher.utils.IllegalMicrosoftAccountException;
import iafenvoy.accountswitcher.utils.ToastUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class AccountScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final Screen parent;
    private final MicrosoftLogin login = new MicrosoftLogin();
    private AccountListWidget widget;
    private boolean initialized = false;

    public AccountScreen(Screen parent) {
        super(new TranslatableText("as.gui.title"));
        this.parent = parent;
    }

    public void openParent() {
        client.openScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();
        client.keyboard.setRepeatEvents(true);
        if (this.initialized)
            this.widget.updateSize(100, this.width - 80, 32, this.height - 32);
        else {
            this.initialized = true;
            this.widget = new AccountListWidget(this, client, 100, this.width - 80, 32, this.height - 32, 36);
            this.widget.setAccount(AccountManager.INSTANCE.getAccounts());
        }
        this.children.add(this.widget);

        this.addButton(new ButtonWidget(10, 15, 80, 20, new TranslatableText("as.gui.Close"), button -> this.openParent()));
        this.addButton(new ButtonWidget(10, 35, 80, 20, new TranslatableText("as.gui.AddOffline"), button -> client.openScreen(new AddOfflineAccountScreen(this))));
        this.addButton(new ButtonWidget(10, 55, 80, 20, new TranslatableText("as.gui.AddMicrosoft"), button -> new Thread(() -> {
            try {
                Account account = login.doAuth();
                if (account != null)
                    this.addAccount(account);
            } catch (IllegalMicrosoftAccountException e) {
                ToastUtil.showToast("as.toast.error.InvalidAccount", "as.toast.error.InvalidAccount.text");
            }
        }, "Microsoft Login").start()));
        this.addButton(new ButtonWidget(10, 75, 80, 20, new TranslatableText("as.gui.DeleteAccount"), button -> {
            if (this.widget.getSelected() != null && this.widget.getSelected() instanceof AccountListWidget.AccountEntry)
                AccountManager.INSTANCE.deleteAccountByUuid(((AccountListWidget.AccountEntry) this.widget.getSelected()).getAccount().getUuid());
            this.refreshWidget();
        }));
        this.addButton(new ButtonWidget(10, 95, 80, 20, new TranslatableText("as.gui.UseAccount"), button -> {
            if (this.widget.getSelected() != null && this.widget.getSelected() instanceof AccountListWidget.AccountEntry)
                ((AccountListWidget.AccountEntry) this.widget.getSelected()).getAccount().use(this.login);
        }));
        this.addButton(new ButtonWidget(10, 115, 80, 20, new TranslatableText("as.gui.RefreshAccount"), button -> {
            if (this.widget.getSelected() != null && this.widget.getSelected() instanceof AccountListWidget.AccountEntry)
                ((AccountListWidget.AccountEntry) this.widget.getSelected()).getAccount().refresh(this.login);
        }));
    }

    public void addAccount(Account account) {
        AccountManager.INSTANCE.addAccount(account);
        this.refreshWidget();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        this.widget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, this.title, this.width / 2, 20, 16777215);
        drawCenteredText(matrices, textRenderer, AccountManager.getAccountInfoText(), this.width / 2, this.height - 26, 16777215);
        if (login.getProcess() != null)
            drawCenteredText(matrices, textRenderer, Text.of(login.getProcess()), this.width / 2, this.height - 14, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void refreshWidget() {
        AccountManager.INSTANCE.save();
        this.widget.setAccount(AccountManager.INSTANCE.getAccounts());
    }

    public void select(AccountListWidget.Entry entry) {
        this.widget.setSelected(entry);
    }
}
