package iafenvoy.accountswitcher.login;

import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.utils.IllegalMicrosoftAccountException;

public interface ILogin {
    Account doAuth(AuthRequest request) throws IllegalMicrosoftAccountException;
    void useAccount(Account account);
    void refreshAccessToken(Account account);
    String getProcess();
}
