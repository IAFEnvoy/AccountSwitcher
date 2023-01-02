package iafenvoy.accountswitcher.login;

import iafenvoy.accountswitcher.config.Account;

import java.util.UUID;

public class OfflineLogin {
    public static Account generateAccount(String username) {
        String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer"+username).getBytes()).toString().replace("-", "");
        return new Account(Account.AccountType.Offline, "", "", username, uuid);
    }
}
