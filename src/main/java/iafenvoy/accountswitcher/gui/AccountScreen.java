package iafenvoy.accountswitcher.gui;

import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.config.AccountManager;
import iafenvoy.accountswitcher.login.InjectorLogin;
import iafenvoy.accountswitcher.login.MicrosoftLogin;
import iafenvoy.accountswitcher.utils.IllegalMicrosoftAccountException;
import iafenvoy.accountswitcher.utils.ToastUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import iafenvoy.accountswitcher.utils.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class AccountScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public final MicrosoftLogin microsoftLogin = new MicrosoftLogin();
    public final InjectorLogin injectorLogin = new InjectorLogin();
    private final Screen parent;
    private AccountListWidget widget;
    private boolean initialized = false;

    public AccountScreen(Screen parent) {
        super(Text.translatable("as.gui.title"));
        this.parent = parent;
    }

    public void openParent() {
        client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();
        if (this.initialized)
            this.widget.updateSize(100, this.width - 80, 32, this.height - 32);
        else {
            this.initialized = true;
            this.widget = new AccountListWidget(this, client, 100, this.width - 80, 32, this.height - 32, 36);
            this.widget.setAccount(AccountManager.INSTANCE.getAccounts());
        }
        this.addSelectableChild(this.widget);

        this.addField(new ButtonWidget(10, 15, 80, 20, Text.translatable("as.gui.Close"), button -> this.openParent()));
        this.addField(new ButtonWidget(10, 35, 80, 20, Text.translatable("as.gui.AddOffline"), button -> client.setScreen(new AddOfflineAccountScreen(this))));
        this.addField(new ButtonWidget(10, 55, 80, 20, Text.translatable("as.gui.AddMicrosoft"), button -> new Thread(() -> {
            try {
                Account account = microsoftLogin.doAuth();
                if (account != Account.EMPTY)
                    this.addAccount(account);
            } catch (IllegalMicrosoftAccountException e) {
                ToastUtil.showToast("as.toast.error.InvalidAccount", "as.toast.error.InvalidAccount.text");
            } catch (Exception e) {
                ToastUtil.showToast("ERROR", e.getLocalizedMessage());
            }
        }, "Microsoft Login").start()));
        this.addField(new ButtonWidget(10, 75, 80, 20, Text.translatable("as.gui.AddInjector"), button -> client.setScreen(new AddInjectorAccountScreen(this))));
        this.addField(new ButtonWidget(10, 95, 80, 20, Text.translatable("as.gui.AddCustom"), button -> client.setScreen(new AddCustomAccountScreen(this))));
        this.addField(new ButtonWidget(10, 115, 80, 20, Text.translatable("as.gui.UseAccount"), button -> {
            if (this.widget.getSelectedOrNull() != null && this.widget.getSelectedOrNull() instanceof AccountListWidget.AccountEntry)
                ((AccountListWidget.AccountEntry) this.widget.getSelectedOrNull()).getAccount().use(this);
        }));
        this.addField(new ButtonWidget(10, 135, 80, 20, Text.translatable("as.gui.RefreshAccount"), button -> {
            if (this.widget.getSelectedOrNull() != null && this.widget.getSelectedOrNull() instanceof AccountListWidget.AccountEntry)
                ((AccountListWidget.AccountEntry) this.widget.getSelectedOrNull()).getAccount().refresh(this);
        }));
        this.addField(new ButtonWidget(10, 180, 80, 20, Text.translatable("as.gui.DeleteAccount"), button -> {
            if (this.widget.getSelectedOrNull() != null && this.widget.getSelectedOrNull() instanceof AccountListWidget.AccountEntry)
                AccountManager.INSTANCE.deleteAccountByUuid(((AccountListWidget.AccountEntry) this.widget.getSelectedOrNull()).getAccount().getUuid());
            this.refreshWidget();
        }));
    }

    public void addAccount(Account account) {
        AccountManager.INSTANCE.addAccount(account);
        AccountManager.INSTANCE.save();
        this.refreshWidget();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrices);
        this.widget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, this.title, this.width / 2, 20, 16777215);
        drawCenteredText(matrices, textRenderer, AccountManager.getAccountInfoText(), this.width / 2, this.height - 26, 16777215);
        if (microsoftLogin.getProcess() != null)
            drawCenteredText(matrices, textRenderer, Text.of(microsoftLogin.getProcess()), this.width / 2, this.height - 14, 16777215);
        if (injectorLogin.getProcess() != null)
            drawCenteredText(matrices, textRenderer, Text.of(microsoftLogin.getProcess()), this.width / 2, this.height - 14, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void refreshWidget() {
        this.widget.setAccount(AccountManager.INSTANCE.getAccounts());
    }

    public void select(AccountListWidget.Entry entry) {
        this.widget.setSelected(entry);
    }

    public void addField(ClickableWidget drawable) {
        this.addDrawable(drawable);
        this.addSelectableChild(drawable);
    }
}
