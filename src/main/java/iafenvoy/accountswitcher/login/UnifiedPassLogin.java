package iafenvoy.accountswitcher.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import iafenvoy.accountswitcher.config.Account;
import iafenvoy.accountswitcher.utils.NetworkUtil;
import iafenvoy.accountswitcher.utils.Profiler;

public class UnifiedPassLogin implements ILogin {
    private final Profiler profiler = new Profiler();
    private static final String BASE_URL = "https://auth.mc-user.com:233/$server_id/";
    private static final String AUTHENTICATE_URL = BASE_URL + "authserver/authenticate";
    private static final String REFRESH_URL = BASE_URL + "authserver/refresh";
    private static final String VALIDATE_URL = BASE_URL + "authserver/validate";
    private static final String INVALIDATE_URL = BASE_URL + "authserver/invalidate";
    private static final String SIGNOUT_URL = BASE_URL + "authserver/signout";
    private static final String PROFILE_URL = BASE_URL + "sessionserver/session/minecraft/profile/";

    private String accessToken;
    private String clientToken;
    private String username;
    private String uuid;

    @Override
    public Account doAuth(AuthRequest request) {
        JsonObject root = new JsonObject();
        root.addProperty("username", request.name);
        root.addProperty("password", request.password);
        root.addProperty("clientToken", clientToken);
        root.addProperty("requestUser", true);

        String data = NetworkUtil.getDataWithJson(AUTHENTICATE_URL.replace("$server_id",request.server), root);
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        this.accessToken = json.get("accessToken").getAsString();
        this.clientToken = json.get("clientToken").getAsString();
        this.username = json.get("selectedProfile").getAsJsonObject().get("name").getAsString();
        this.uuid = json.get("selectedProfile").getAsJsonObject().get("id").getAsString();

        Account a = new Account(Account.AccountType.UnifiedPass,this.username, this.uuid, this.accessToken, this.clientToken);
        a.setUnifiedServer(request.server);
        return a;
    }

    @Override
    public void useAccount(Account account) {
        this.accessToken = account.getAccessToken();
        this.clientToken = account.getClientToken();
        this.username = account.getUsername();
        this.uuid = account.getUuid();
    }

    @Override
    public void refreshAccessToken(Account account) {
        JsonObject root = new JsonObject();
        root.addProperty("accessToken", this.accessToken);
        root.addProperty("clientToken", this.clientToken);
        root.addProperty("requestUser", true);

        String data = NetworkUtil.getDataWithJson(REFRESH_URL.replace("$server_id",account.getUnifiedServer()), root);
        JsonObject json = JsonParser.parseString(data).getAsJsonObject();
        this.accessToken = json.get("accessToken").getAsString();
        this.clientToken = json.get("clientToken").getAsString();
        this.username = json.get("selectedProfile").getAsJsonObject().get("name").getAsString();
        this.uuid = json.get("selectedProfile").getAsJsonObject().get("id").getAsString();

        account.setAccessToken(this.accessToken);
        account.setClientToken(this.clientToken);
    }

    @Override
    public String getProcess() {
        return profiler.getLocation();
    }
}