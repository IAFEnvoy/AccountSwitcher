package iafenvoy.accountswitcher.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.text.TranslatableText;

public class ToastUtil {
    public static void showToast(String titleKey, String textKey) {
        Toast toast = new SystemToast(SystemToast.Type.NARRATOR_TOGGLE, new TranslatableText(titleKey), new TranslatableText(textKey));
        MinecraftClient.getInstance().getToastManager().add(toast);
    }
}
