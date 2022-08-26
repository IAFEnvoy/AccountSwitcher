package iafenvoy.accountswitcher;

import iafenvoy.accountswitcher.config.AccountManager;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountSwitcher implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "accountswitcher";
    public static final String MOD_NAME = "Account Switcher";

    @Override
    public void onInitializeClient() {
        LOGGER.info("[" + MOD_NAME + "]Initializing...");

        AccountManager.INSTANCE.load();
        AccountManager.setAccountFromClient();
    }
}
