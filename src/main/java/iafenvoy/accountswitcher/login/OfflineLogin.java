package iafenvoy.accountswitcher.login;

import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.utils.IllegalMicrosoftAccountException;

import java.util.UUID;

public class OfflineLogin implements ILogin {
    @Override
    public Account doAuth(AuthRequest request) throws IllegalMicrosoftAccountException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return new Account(Account.AccountType.Offline, "", "", request.name, uuid);
    }

    @Override
    public void useAccount(Account account) {

    }

    @Override
    public void refreshAccessToken(Account account) {

    }

    @Override
    public String getProcess() {
        return null;
    }
}
