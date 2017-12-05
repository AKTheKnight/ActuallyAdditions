package co.uk.aktheknight.actuallyadditions.guis;

import co.uk.aktheknight.actuallyadditions.ActuallyAdditions;
import co.uk.aktheknight.actuallyadditions.Utils;
import co.uk.aktheknight.actuallyadditions.config.ModConfig;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.IGraphics;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.IFont;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.ComponentButton;
import de.ellpeck.rockbottom.api.gui.component.ComponentScrollMenu;
import de.ellpeck.rockbottom.api.gui.component.ComponentSlider;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import de.ellpeck.rockbottom.api.util.BoundBox;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;

public class GuiConfig extends Gui{

    public GuiConfig(Gui parent){
        super(parent);

        this.config = ActuallyAdditions.instance.getConfig();
    }

    ModConfig config;

    @Override
    public void render(IGameInstance game, IAssetManager manager, IGraphics g){
        super.render(game, manager, g);

        IFont font = manager.getFont();
        font.drawCenteredString(this.width / 2, 5, Utils.localizeGui("settingsTitle"), 0.7f, false);
    }

    @Override
    public void init(IGameInstance game){
        super.init(game);

        ComponentScrollMenu scrollMenu = new ComponentScrollMenu(this, 25, 25, this.height - 50, 1, 5, new BoundBox(0, 0, 150, this.height - 50).add(25, 25).add(7, 0));

        this.setupComponents(game, scrollMenu);

        this.components.add(scrollMenu);
    }

    @Override
    public void onClosed(IGameInstance game){
        super.onClosed(game);

        game.getDataManager().savePropSettings(ActuallyAdditions.instance.getConfig());
    }

    private void setupComponents(IGameInstance game, ComponentScrollMenu scrollMenu) {
        scrollMenu.clear();

        GuiComponent spacer = new GuiComponent(this, 0, 0, 150, 3){
            @Override
            public IResourceName getName(){
                return Utils.createRes("spacergui");
            }
        };

        ComponentSlider sprintSlider = new ComponentSlider(this, 0, 0, 150, 20, this.config.sprintingSpeed, 100, 120, (integer, aBoolean) -> this.config.sprintingSpeed = integer, Utils.localizeGui("sprintSpeedText"), Utils.localizeGui("sprintSpeedTooltip"));

        ComponentButton sprintToggle = new ComponentButton(this, 0, 0, 150, 20, () -> {
            this.config.sprintingEnabled = !this.config.sprintingEnabled;
            this.setupComponents(game, scrollMenu);
            return true;
        }, this.config.sprintingEnabled ? Utils.localizeGui("sprintDisable") : Utils.localizeGui("sprintEnable"), Utils.localizeGui("sprintEnableText"), Utils.localizeGui("sprintServerNotice"));

        scrollMenu.add(sprintToggle);
        if (this.config.sprintingEnabled)
            scrollMenu.add(sprintSlider);

        scrollMenu.add(spacer);


        scrollMenu.organize();
    }

    @Override
    public IResourceName getName(){
        return Utils.createRes("settingsgui");
    }
}
