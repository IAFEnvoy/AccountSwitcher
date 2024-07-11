package iafenvoy.accountswitcher.gui;

import iafenvoy.accountswitcher.config.Account;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountListWidget extends AlwaysSelectedEntryListWidget<AccountListWidget.Entry> {
    private final AccountScreen screen;
    private final List<AccountListWidget.AccountEntry> accounts = new ArrayList<>();

    public AccountListWidget(AccountScreen screen, MinecraftClient client, int left, int right, int top, int bottom, int entryHeight) {
        super(client, right - left, bottom - top, top, bottom, entryHeight);
        this.screen = screen;
        this.updateSize(left, right, top, bottom);
    }

    @Override
    public void updateSize(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void setAccount(List<Account> accounts) {
        this.accounts.clear();
        for (Account account : accounts)
            this.accounts.add(new AccountEntry(this.screen, account));
        this.updateEntries();
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 30;
    }

    private void updateEntries() {
        this.clearEntries();
        this.accounts.forEach(this::addEntry);
    }

    @Override
    public void setSelected(@Nullable AccountListWidget.Entry entry) {
        super.setSelected(entry);
        this.client.getNarratorManager().narrate((Text.translatable("narrator.select", ((AccountEntry) Objects.requireNonNull(this.getSelectedOrNull())).account.getUsername())).getString());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        AccountListWidget.Entry entry = this.getSelectedOrNull();
        return entry != null && entry.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<AccountListWidget.Entry> {
    }

    public class AccountEntry extends AccountListWidget.Entry {
        private final AccountScreen screen;
        private final Account account;

        public AccountEntry(AccountScreen screen, Account account) {
            this.screen = screen;
            this.account = account;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(AccountListWidget.this.client.textRenderer, this.account.getUsername(), x + 32 + 3, y + 1, 16777215, true);
            context.drawText(AccountListWidget.this.client.textRenderer, this.account.getUsername(), x + 32 + 3, y + 1 + 9, 16777215, true);
            context.drawText(AccountListWidget.this.client.textRenderer, this.account.getUsername(), x + 32 + 3, y + 1 + 18, 16777215, true);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.screen.select(this);
            return false;
        }

        public Account getAccount() {
            return this.account;
        }

        @Override
        public Text getNarration() {
            return Text.of("");
        }
    }
}
