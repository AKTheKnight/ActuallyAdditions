package co.uk.aktheknight.actuallyadditions.features;

import co.uk.aktheknight.actuallyadditions.tiles.TileVine;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.IGraphics;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.render.tile.DefaultTileRenderer;
import de.ellpeck.rockbottom.api.tile.state.TileState;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

public class VineRenderer extends DefaultTileRenderer<TileVine>{

    public VineRenderer(IResourceName texture){
        super(texture);
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, IGraphics g, IWorld world, TileVine tile, TileState state, int x, int y, TileLayer layer, float renderX, float renderY, float scale, int[] light){
        IResourceName vineTexture;
        if (y % 3 == 0) {
            vineTexture = this.texture;
        }
        else if (y % 2 == 0) {
            vineTexture = this.texture.addSuffix(".1");
        }
        else {
            vineTexture = this.texture.addSuffix(".2");
        }

        manager.getTexture(vineTexture).draw(renderX, renderY, scale, scale, light);
    }
}
