package iafenvoy.accountswitcher.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class ButtonWidget extends net.minecraft.client.gui.widget.ButtonWidget {
    public ButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, textSupplier -> Text.empty());
    }
}
