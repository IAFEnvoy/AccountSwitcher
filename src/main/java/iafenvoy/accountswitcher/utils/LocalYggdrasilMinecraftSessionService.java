package iafenvoy.accountswitcher.utils;

import com.google.common.collect.Iterables;
import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.util.UUIDTypeAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalYggdrasilMinecraftSessionService extends YggdrasilMinecraftSessionService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final PublicKey publicKey;
    private final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();

    public LocalYggdrasilMinecraftSessionService(YggdrasilAuthenticationService service, String serverUrl) {
        super(service, new InjectorEnvironment(serverUrl));
        String data = NetworkUtil.getData(serverUrl);
        JsonObject json = new JsonParser().parse(data).getAsJsonObject();
        this.publicKey = getPublicKey(json.get("signaturePublickey").getAsString());
    }

    private static PublicKey getPublicKey(String key) {
        key = key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        try {
            byte[] bykeKey = Base64.getDecoder().decode(key.replace("\n", ""));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bykeKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(final GameProfile profile, final boolean requireSecure) {
        final Property textureProperty = Iterables.getFirst(profile.getProperties().get("textures"), null);

        if (textureProperty == null)
            return new HashMap<>();

        if (requireSecure) {
            if (!textureProperty.hasSignature()) {
                LOGGER.error("Signature is missing from textures payload");
                throw new InsecureTextureException("Signature is missing from textures payload");
            }
            if (!textureProperty.isSignatureValid(publicKey)) {
                LOGGER.error("Textures payload has been tampered with (signature invalid)");
                throw new InsecureTextureException("Textures payload has been tampered with (signature invalid)");
            }
        }

        final MinecraftTexturesPayload result;
        try {
            final String json = new String(org.apache.commons.codec.binary.Base64.decodeBase64(textureProperty.getValue()), StandardCharsets.UTF_8);
            result = gson.fromJson(json, MinecraftTexturesPayload.class);
        } catch (final JsonParseException e) {
            LOGGER.error("Could not decode textures payload", e);
            return new HashMap<>();
        }

        if (result == null || result.getTextures() == null)
            return new HashMap<>();

        return result.getTextures();
    }
}
