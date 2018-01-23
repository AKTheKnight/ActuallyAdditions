package co.uk.aktheknight.actuallyadditions;

import co.uk.aktheknight.actuallyadditions.config.ModConfig;
import co.uk.aktheknight.actuallyadditions.events.DropItem;
import co.uk.aktheknight.actuallyadditions.events.Sprinting;
import co.uk.aktheknight.actuallyadditions.guis.GuiConfig;
import co.uk.aktheknight.actuallyadditions.items.ItemMachete;
import co.uk.aktheknight.actuallyadditions.packets.SprintPacket;
import co.uk.aktheknight.actuallyadditions.packets.ThrowPacket;
import co.uk.aktheknight.actuallyadditions.recipes.RecipeTileBreak;
import co.uk.aktheknight.actuallyadditions.tiles.TileLeafLadder;
import co.uk.aktheknight.actuallyadditions.tiles.TileVine;
import co.uk.aktheknight.actuallyadditions.util.Utils;
import co.uk.aktheknight.actuallyadditions.worldgen.CustomWorldGen;
import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.construction.IRecipe;
import de.ellpeck.rockbottom.api.construction.KnowledgeBasedRecipe;
import de.ellpeck.rockbottom.api.construction.resource.ItemUseInfo;
import de.ellpeck.rockbottom.api.data.settings.Keybind;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.BreakEvent;
import de.ellpeck.rockbottom.api.event.impl.ResetMovedPlayerEvent;
import de.ellpeck.rockbottom.api.event.impl.WorldTickEvent;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.mod.IMod;
import de.ellpeck.rockbottom.api.tile.Tile;
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

    @Deprecated
    public static IResourceName createRes(String name){
        return RockBottomAPI.createRes(instance, name);
    }

    private ModConfig config;

    public ModConfig getConfig(){
        return this.config;
    }

    //Vars
    public Keybind dropItemKeybind;
    public Keybind sprintKeybind;

    public Tile tileLeafLadder;
    public Tile tileVine;

    public Item itemMachete;

    public IRecipe recipeMachete;
    public IRecipe recipeLeafLadder;

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
    public Class<? extends Gui> getModGuiClass(){
        return GuiConfig.class;
    }

    @Override
    public void prePreInit(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler){
        this.modLogger = apiHandler.createLogger(this.getDisplayName());

        //Keybinds
        this.sprintKeybind = new Keybind(Utils.createRes("sprintKeybind"), Keyboard.KEY_LCONTROL, false).register();
        this.dropItemKeybind = new Keybind(Utils.createRes("dropItemKeybind"), Keyboard.KEY_Q, false).register();
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
        eventHandler.registerListener(WorldTickEvent.class, DropItem:: worldTickEvent);
        eventHandler.registerListener(WorldTickEvent.class, Sprinting:: worldTickEvent);
        eventHandler.registerListener(ResetMovedPlayerEvent.class, Sprinting:: resetMovedPlayerEvent);
        eventHandler.registerListener(BreakEvent.class, RecipeTileBreak:: tileBreakEvent);

        //Register tiles
        this.tileLeafLadder = new TileLeafLadder().register();
        this.tileVine = new TileVine().register();

        //Register items
        this.itemMachete = new ItemMachete().register();

        //Register world gen
        RockBottomAPI.WORLD_GENERATORS.register(Utils.createRes("worldgen"), CustomWorldGen.class);

        //Config
        this.config = new ModConfig();
        game.getDataManager().loadPropSettings(this.config);

        //Register recipe
        this.recipeMachete = new KnowledgeBasedRecipe(Utils.createRes("machete"), new ItemInstance(this.itemMachete), new ItemUseInfo(GameContent.TILE_PEBBLES, 2), new ItemUseInfo(GameContent.TILE_LOG)).registerManual();
        this.recipeLeafLadder = new KnowledgeBasedRecipe(Utils.createRes("leafladder"), new ItemInstance(this.tileLeafLadder), new ItemUseInfo(GameContent.TILE_LOG, 2), new ItemUseInfo(this.tileVine, 2)).registerManual();
    }

    @Override
    public void initAssets(IGameInstance game, IAssetManager assetManager, IApiHandler apiHandler){
        this.modLogger.info("Localized text: "+assetManager.localize(RockBottomAPI.createRes(this, "test")));
    }
}
