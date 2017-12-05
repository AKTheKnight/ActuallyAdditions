package co.uk.aktheknight.actuallyadditions.config;

import de.ellpeck.rockbottom.api.data.IDataManager;
import de.ellpeck.rockbottom.api.data.settings.IPropSettings;

import java.io.File;
import java.util.Properties;

public class ModConfig implements IPropSettings{

    public boolean sprintingEnabled;
    public int sprintingSpeed;

    @Override
    public void load(Properties props){
        this.sprintingEnabled = this.getProp(props, "sprint_enabled", true);
        this.sprintingSpeed = this.getProp(props, "sprint_speed", 115);
    }

    @Override
    public void save(Properties props){
        this.setProp(props, "sprint_enabled", this.sprintingEnabled);
        this.setProp(props, "sprint_speed", this.sprintingSpeed);
    }

    @Override
    public File getFile(IDataManager manager){
        return new File(manager.getModsDir(), "actuallyadditions.cfg");
    }

    @Override
    public String getName(){
        return "Actually Additions Config";
    }
}
