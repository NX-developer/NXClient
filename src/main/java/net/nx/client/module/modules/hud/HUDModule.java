package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.SliderSetting;

public abstract class HUDModule extends Module {

    protected double posX;
    protected double posY;
    protected final SliderSetting scale;

    public HUDModule(String name, String description, double defaultX, double defaultY) {
        super(name, description, Category.HUD);
        this.posX = defaultX;
        this.posY = defaultY;
        this.scale = addSetting(new SliderSetting("Scale", "HUD element scale", 1.0, 0.5, 2.0, 0.05));
    }

    public abstract void renderHUD(ScaledResolution res);

    public abstract int getWidth();
    public abstract int getHeight();

    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
    public void setPos(double x, double y) { this.posX = x; this.posY = y; }
    public double getScale() { return scale.getValue(); }
}
