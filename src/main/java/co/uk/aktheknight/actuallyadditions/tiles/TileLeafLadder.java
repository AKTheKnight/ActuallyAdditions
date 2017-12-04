package co.uk.aktheknight.actuallyadditions.tiles;

import co.uk.aktheknight.actuallyadditions.Utils;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.data.settings.Settings;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.TileBasic;
import de.ellpeck.rockbottom.api.tile.state.BoolProp;
import de.ellpeck.rockbottom.api.tile.state.TileState;
import de.ellpeck.rockbottom.api.util.BoundBox;
import de.ellpeck.rockbottom.api.util.Util;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.gen.IWorldGenerator;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;
import org.newdawn.slick.Input;

import java.util.Collections;
import java.util.List;

public class TileLeafLadder extends TileBasic{

    BoolProp isTop = new BoolProp("isTop", true);

    static int placeDelay = 10;
    static int destroyDelay = 5;

    public TileLeafLadder(){
        super(Utils.createRes("leafladder"));
        this.addProps(this.isTop);
        this.setForceDrop();
    }

    @Override
    public void describeItem(IAssetManager manager, ItemInstance instance, List<String> desc, boolean isAdvanced){
        if(isAdvanced){
            desc.add(FormattingCode.GREEN + manager.localize(Utils.createRes("tooltip.dyk.tooltip"), manager.localize(Utils.createRes("tooltip.dyk.leafLadder"))));
        }
        else{
            desc.add(FormattingCode.GRAY+manager.localize(RockBottomAPI.createInternalRes("info.layer_placement"), manager.localize(TileLayer.MAIN.getName().addPrefix("layer."))));
            desc.add(FormattingCode.DARK_GRAY+manager.localize(RockBottomAPI.createInternalRes("info.advanced_info"), Input.getKeyName(Settings.KEY_ADVANCED_INFO.getKey())));
        }
    }

    @Override
    public boolean canPlaceInLayer(TileLayer layer){
        return layer == TileLayer.MAIN;
    }

    @Override
    public boolean canPlace(IWorld world, int x, int y, TileLayer layer){
        return world.getState(TileLayer.BACKGROUND, x, y).getTile().isFullTile()
                || world.getState(TileLayer.MAIN, x, y + 1).getTile().isFullTile()
                || world.getState(TileLayer.MAIN, x, y + 1).getTile() == this;
    }

    @Override
    public void doPlace(IWorld world, int x, int y, TileLayer layer, ItemInstance instance, AbstractEntityPlayer placer){
        super.doPlace(world, x, y, layer, instance, placer);
        world.scheduleUpdate(x, y, layer, placeDelay);
    }

    @Override
    public BoundBox getBoundBox(IWorld world, int x, int y){
        return null;
    }

    @Override
    public boolean isFullTile(){
        return false;
    }

    @Override
    public boolean canClimb(IWorld world, int x, int y, TileLayer layer, TileState state, BoundBox entityBox, BoundBox entityBoxMotion, List<BoundBox> tileBoxes, Entity entity){
        return Util.floor(entity.y) == y;
    }

    @Override
    public void onScheduledUpdate(IWorld world, int x, int y, TileLayer layer){
        super.onScheduledUpdate(world, x, y, layer);

        TileState state = world.getState(layer, x, y);
        TileState above = world.getState(layer, x, y + 1);

        if (state.get(this.isTop)) {
            //Try placing below as this is the top
            this.placeBelow(world, layer, x, y);
        }
        else {
            //If tile above is rope ladder try placing below
            if (above.getTile() == this) {
                this.placeBelow(world, layer, x, y);
            }
            //Else break it and schedule updates
            else {
                world.destroyTile(x, y, layer, null, false);
                if (world.getState(layer, x, y - 1).getTile() == this)
                    world.scheduleUpdate(x, y - 1, layer, destroyDelay);
            }
        }
    }

    private void placeBelow(IWorld world, TileLayer layer, int x, int y) {
        //If the world below is not a full tile
        if (!world.getState(layer, x, y - 1).getTile().isFullTile()){
            //TODO Uncomment when PR into API is merged
            //If it's not air destroy it
            if (!world.getState(layer, x, y - 1).getTile().isAir())
                world.destroyTile(x, y - 1, layer, null, true);
            //Set the state and schedule the update
            world.setState(layer, x, y-1, this.getDefState().prop(this.isTop, false));
            world.scheduleUpdate(x, y - 1, layer, placeDelay);
        }
    }

    @Override
    public void onRemoved(IWorld world, int x, int y, TileLayer layer){
        //Check the tile is this
        if (world.getState(layer, x, y - 1).getTile() == this) {
            //Schedule update on tile below
            world.scheduleUpdate(x, y - 1, layer, destroyDelay);
        }
    }

    @Override
    public List<ItemInstance> getDrops(IWorld world, int x, int y, TileLayer layer, Entity destroyer){
        return world.getState(layer, x, y).get(this.isTop) ? Collections.singletonList(new ItemInstance(this)) : Collections.EMPTY_LIST;
    }
}
