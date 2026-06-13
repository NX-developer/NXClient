package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockHUD extends HUDModule {

    private final BooleanSetting show24h;
    private final BooleanSetting showDate;
    private final ColorSetting color;

    public ClockHUD() {
        super("Clock", "Shows current time and date", 2, 57);
        this.show24h  = addSetting(new BooleanSetting("24h Format", "Use 24-hour time format", true));
        this.showDate = addSetting(new BooleanSetting("Show Date",  "Show current date",        false));
        this.color    = addSetting(new ColorSetting("Color", "Text color", new Color(NXColors.TEXT_SECONDARY, true)));
        setEnabled(false);
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        String fmt = show24h.isEnabled() ? "HH:mm:ss" : "hh:mm:ss a";
        String time = new SimpleDateFormat(fmt).format(new Date());
        mc.fontRendererObj.drawStringWithShadow(time, (float) posX, (float) posY, color.getColor());

        if (showDate.isEnabled()) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            mc.fontRendererObj.drawStringWithShadow(date, (float) posX, (float) posY + 10, color.getColor());
        }
    }

    @Override public int getWidth()  { return 80; }
    @Override public int getHeight() { return showDate.isEnabled() ? 19 : 9; }
}
