package co.uk.aktheknight.actuallyadditions;

import co.uk.aktheknight.actuallyadditions.features.DropItem;
import co.uk.aktheknight.actuallyadditions.features.Sprinting;
import co.uk.aktheknight.actuallyadditions.packets.SprintPacket;
import co.uk.aktheknight.actuallyadditions.packets.ThrowPacket;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.data.settings.Keybind;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.ResetMovedPlayerEvent;
import de.ellpeck.rockbottom.api.event.impl.WorldTickEvent;
import de.ellpeck.rockbottom.api.mod.IMod;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import org.lwjgl.input.Keyboard;

import java.util.logging.Logger;

public class ActuallyAdditions implements IMod{

    public static ActuallyAdditions instance;

    private Logger modLogger;

    public ActuallyAdditions(){
        instance = this;
    }

    public static Logger getLogger(){
        return instance.modLogger;
    }

    public static IResourceName createRes(String name){
        return RockBottomAPI.createRes(instance, name);
    }

    //Vars
    public Keybind dropItemKeybind;
    public Keybind sprintKeybind;

    @Override
    public String getDisplayName(){
        return "Actually Additions";
    }

    @Override
    public String getId(){
        return "actuallyadditions";
    }

    @Override
    public String getVersion(){
        return "@VERSION@";
    }

    @Override
    public String getResourceLocation(){
        return "/assets/actuallyadditions";
    }

    @Override
    public String getDescription(){
        return "A RockBottom Mod about lots of useful gadgets and things!";
    }

    @Override
    public String[] getAuthors(){
        return new String[]{"AKTheKnight", "Ellpeck"};
    }

    @Override
    public void prePreInit(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler){
        this.modLogger = apiHandler.createLogger(this.getDisplayName());

        //Keybinds
        this.sprintKeybind = new Keybind(createRes("sprintKeybind"), Keyboard.KEY_LCONTROL, false).register();
        this.dropItemKeybind = new Keybind(createRes("dropItemKeybind"), Keyboard.KEY_Q, false).register();
    }

    @Override
    public void preInit(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler){
        this.modLogger.info("Starting Actually Additions for RockBottom");
    }

    @Override
    public void init(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler){
        //Packets
        Utils.registerPacket(ThrowPacket.class);
        Utils.registerPacket(SprintPacket.class);

        //Register events
        RockBottomAPI.getEventHandler().registerListener(WorldTickEvent.class, DropItem:: worldTickEvent);
        RockBottomAPI.getEventHandler().registerListener(WorldTickEvent.class, Sprinting:: worldTickEvent);
        RockBottomAPI.getEventHandler().registerListener(ResetMovedPlayerEvent.class, Sprinting:: resetMovedPlayerEvent);
    }

    @Override
    public void initAssets(IGameInstance game, IAssetManager assetManager, IApiHandler apiHandler){
        this.modLogger.info("Localized text: "+assetManager.localize(RockBottomAPI.createRes(this, "test")));
    }
}
