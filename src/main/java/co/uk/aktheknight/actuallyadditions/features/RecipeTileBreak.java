package co.uk.aktheknight.actuallyadditions.features;

import co.uk.aktheknight.actuallyadditions.ActuallyAdditions;
import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.impl.BreakEvent;
import de.ellpeck.rockbottom.api.tile.state.TileState;

public final class RecipeTileBreak{

    public static EventResult tileBreakEvent(EventResult result, BreakEvent event){
        TileState state = event.player.world.getState(event.layer, event.x, event.y);
        //Pebbles broken
        if (state.getTile() == GameContent.TILE_PEBBLES) {
            //Machete
            event.player.getKnowledge().teachRecipe(ActuallyAdditions.instance.recipeMachete, true);
        }
        else if (state.getTile() == ActuallyAdditions.instance.tileVine) {
            //Leaf ladder
            event.player.getKnowledge().teachRecipe(ActuallyAdditions.instance.recipeLeafLadder, true);
        }
        return result;
    }

}
