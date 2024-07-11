package iafenvoy.accountswitcher;

import com.mojang.logging.LogUtils;
import iafenvoy.accountswitcher.config.AccountManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

public class AccountSwitcher implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "accountswitcher";
    public static final String MOD_NAME = "Account Switcher";

    @Override
    public void onInitializeClient() {
        LOGGER.info("[" + MOD_NAME + "]Initializing...");
        AccountManager.INSTANCE.load();
        AccountManager.setAccountFromClient();
    }
}
