package co.uk.aktheknight.actuallyadditions.tiles;

import co.uk.aktheknight.actuallyadditions.ActuallyAdditions;
import co.uk.aktheknight.actuallyadditions.util.Utils;
import co.uk.aktheknight.actuallyadditions.renderers.VineRenderer;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.render.tile.ITileRenderer;
import de.ellpeck.rockbottom.api.tile.TileBasic;
import de.ellpeck.rockbottom.api.util.BoundBox;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

import java.util.Collections;
import java.util.List;

public class TileVine extends TileBasic{

    static int destroyDelay = 5;

    public TileVine(){
        super(Utils.createRes("vine"));
        this.setForceDrop();
    }

    @Override
    public List<ItemInstance> getDrops(IWorld world, int x, int y, TileLayer layer, Entity destroyer){
        if (!(destroyer instanceof AbstractEntityPlayer))
            return Collections.EMPTY_LIST;

        AbstractEntityPlayer player = (AbstractEntityPlayer)destroyer;

        if (player == null)
            return Collections.EMPTY_LIST;

        if (player.getInv().get(player.getSelectedSlot()).getItem() == ActuallyAdditions.instance.itemMachete) {
            return Collections.singletonList(new ItemInstance(this));
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isFullTile(){
        return false;
    }

    @Override
    public boolean canReplace(IWorld world, int x, int y, TileLayer layer){
        return true;
    }

    @Override
    public BoundBox getBoundBox(IWorld world, int x, int y){
        return null;
    }

    @Override
    protected ITileRenderer createRenderer(IResourceName name){
        return new VineRenderer(name);
    }

    @Override
    public void onScheduledUpdate(IWorld world, int x, int y, TileLayer layer){
        world.destroyTile(x, y, layer, null, false);

        if (world.getState(layer, x, y - 1).getTile() == this) {
            world.scheduleUpdate(x, y - 1, layer, destroyDelay);
        }
    }

    @Override
    public void onRemoved(IWorld world, int x, int y, TileLayer layer){
        super.onRemoved(world, x, y, layer);
        if (world.getState(layer, x, y - 1).getTile() == this) {
            world.scheduleUpdate(x, y - 1, layer, destroyDelay);
        }
    }
}
