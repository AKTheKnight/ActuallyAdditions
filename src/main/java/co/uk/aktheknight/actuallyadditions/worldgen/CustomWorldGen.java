package co.uk.aktheknight.actuallyadditions.worldgen;

import co.uk.aktheknight.actuallyadditions.ActuallyAdditions;
import de.ellpeck.rockbottom.api.Constants;
import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.tile.state.TileState;
import de.ellpeck.rockbottom.api.util.Util;
import de.ellpeck.rockbottom.api.world.IChunk;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.gen.IWorldGenerator;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

import java.util.Random;

public class CustomWorldGen implements IWorldGenerator{

    private final Random vineRandom = new Random();

    @Override
    public boolean shouldGenerate(IWorld world, IChunk chunk){
        return chunk.getGridY() == 0;
    }

    @Override
    public void generate(IWorld world, IChunk chunk){
        this.vineRandom.setSeed(Util.scrambleSeed(chunk.getX(), chunk.getY(), world.getSeed()));

        for(int x = 0; x < 32; x++){
            for(int y = 1; y < 32; y++){
                TileState state = chunk.getStateInner(TileLayer.MAIN, x, y);
                if (state.getTile() == GameContent.TILE_LEAVES
                        && chunk.getStateInner(TileLayer.MAIN, x, y - 1).getTile().isAir()
                        && this.vineRandom.nextInt(8) == 0) {
                    int length = this.vineRandom.nextInt(4) + 1;

                    for(int i = 1; i <= length; i++){
                        if (world.getState(TileLayer.MAIN, chunk.getX() + x, chunk.getY() + y - i).getTile().isAir())
                            world.setState(TileLayer.MAIN, chunk.getX() + x, chunk.getY() + y - i, ActuallyAdditions.instance.tileVine.getDefState());
                        else
                            break;
                    }
                }
            }
        }
    }

    @Override
    public int getPriority(){
        return -100;
    }
}
