package co.uk.aktheknight.actuallyadditions.features;

import co.uk.aktheknight.actuallyadditions.ActuallyAdditions;
import co.uk.aktheknight.actuallyadditions.packets.SprintPacket;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.impl.ResetMovedPlayerEvent;
import de.ellpeck.rockbottom.api.event.impl.WorldTickEvent;

public final class Sprinting{

    static boolean isSprinting;

    public static EventResult worldTickEvent(EventResult result, WorldTickEvent event){
        if(RockBottomAPI.getGame().isDedicatedServer()){
            return result;
        }

        AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();

        if (ActuallyAdditions.instance.sprintKeybind.isDown()) {
            if (!isSprinting) {
                isSprinting = true;
                RockBottomAPI.getNet().sendToServer(new SprintPacket(player.getUniqueId(), true));
            }
            //TODO Limit on time for sprinting
        }
        else {
            if (isSprinting) {
                isSprinting = false;
                RockBottomAPI.getNet().sendToServer(new SprintPacket(player.getUniqueId(), false));
            }
        }

        if (isSprinting)
            player.motionX = player.motionX * 1.15;

        return result;
    }

    //TODO Custom sprint speed in config
    public static EventResult resetMovedPlayerEvent(EventResult result, ResetMovedPlayerEvent event){
        DataSet set = event.player.getOrCreateAdditionalData();
        if(!set.hasKey("actadd.sprint")){
            return result;
        }

        long playerTime = set.getLong("actadd.sprint");

        //0.2 * 80 = 16
        //(0.2 * 1.2) * 80 = 19.2
        //19.2 - 16 = 3.2
        //4 as upper bound
        if (playerTime >= -1){
            event.allowedDefaultDistance += 5;

            if (playerTime > 0) {
                set.addLong("actadd.sprint", -2);
            }
        }

        return result;
    }
}
