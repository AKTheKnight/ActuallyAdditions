package co.uk.aktheknight.actuallyadditions.events;

import co.uk.aktheknight.actuallyadditions.ActuallyAdditions;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.impl.WorldTickEvent;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.gui.component.ComponentSlot;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import de.ellpeck.rockbottom.api.gui.container.ContainerSlot;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;

import static co.uk.aktheknight.actuallyadditions.util.Utils.removeAndThrowOrPacket;

public final class DropItem {

    public static EventResult worldTickEvent(EventResult result, WorldTickEvent event){
        if(RockBottomAPI.getGame().isDedicatedServer()){
            return result;
        }

        if(!ActuallyAdditions.instance.dropItemKeybind.isPressed()){
            return result;
        }

        AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();

        Gui gui = RockBottomAPI.getGame().getGuiManager().getGui();
        if(gui instanceof GuiContainer){
            //Inventory open
            for(GuiComponent component : gui.getComponents()){
                if(component.isMouseOver(RockBottomAPI.getGame())
                        && component instanceof ComponentSlot){
                    ContainerSlot slot = ((ComponentSlot)component).slot;
                    if(slot.get() != null){
                        removeAndThrowOrPacket(player.getContainer(), player, player.getContainer().getIndexForInvSlot(slot.inventory, slot.slot), 1);
                    }
                }
            }
        }
        else{
            //No inventory open. Just pressing Q in game
            Inventory inv = player.getInv();
            ItemInstance item = inv.get(player.getSelectedSlot());
            //No item
            if(item != null){
                removeAndThrowOrPacket(player.getInvContainer(), player, player.getInvContainer().getIndexForInvSlot(inv, player.getSelectedSlot()), 1);
            }
        }

        return result;
    }

}
