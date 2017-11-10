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
        //Check if player has enough items
        //TODO Only throw min (number of items, count)
        ContainerSlot containerSlot = container.getSlot(slot);
        EntityItem.spawn(player.world, containerSlot.get().copy().setAmount(1), player.x, player.y + 0.75, player.facing.x * 0.25, 0);
        containerSlot.inventory.remove(containerSlot.slot, count);
    }
}
