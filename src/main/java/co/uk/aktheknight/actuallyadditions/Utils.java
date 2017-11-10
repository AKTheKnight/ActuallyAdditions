package co.uk.aktheknight.actuallyadditions;

import co.uk.aktheknight.actuallyadditions.packets.ThrowPacket;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.EntityItem;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.container.ContainerSlot;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;

public final class Utils{

    public static void registerPacket(Class theClass) {
        RockBottomAPI.PACKET_REGISTRY.register(RockBottomAPI.PACKET_REGISTRY.getNextFreeId(), theClass);
    }

    public static void removeAndThrowOrPacket(ItemContainer inv, AbstractEntityPlayer player, int slot, int count) {
        if (player.world.isClient()) {
            RockBottomAPI.getNet().sendToServer(new ThrowPacket(player.getUniqueId(), slot, count, inv == player.getInvContainer()));
        }
        else
            removeAndThrow(inv, player, slot, count);
    }

    public static void removeAndThrow(ItemContainer container, AbstractEntityPlayer player, int slot, int count) {
        //Get the slot
        ContainerSlot containerSlot = container.getSlot(slot);
        //Throw count or number of items player has
        int numToThrow = Math.min(containerSlot.get().getAmount(), count);

        //Spawn the amount
        EntityItem.spawn(player.world, containerSlot.get().copy().setAmount(numToThrow), player.x, player.y + 0.75, player.facing.x * 0.25, 0);
        containerSlot.inventory.remove(containerSlot.slot, numToThrow);
    }
}
