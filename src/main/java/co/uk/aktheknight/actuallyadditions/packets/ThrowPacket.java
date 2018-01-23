package co.uk.aktheknight.actuallyadditions.packets;

import co.uk.aktheknight.actuallyadditions.util.Utils;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.net.packet.IPacket;
import de.ellpeck.rockbottom.api.world.IWorld;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.UUID;

public class ThrowPacket implements IPacket{

    public ThrowPacket() {
    }

    public ThrowPacket(UUID playerID, int slot, int count, boolean isInv){
        this.playerID = playerID;
        this.slot = slot;
        this.count = count;
        this.isInv = isInv;
    }

    private UUID playerID;
    private int slot;
    private int count;
    private boolean isInv;

    @Override
    public void toBuffer(ByteBuf buf) throws IOException{
        buf.writeLong(this.playerID.getMostSignificantBits());
        buf.writeLong(this.playerID.getLeastSignificantBits());

        buf.writeInt(this.slot);
        buf.writeInt(this.count);

        buf.writeBoolean(this.isInv);
    }

    @Override
    public void fromBuffer(ByteBuf buf) throws IOException{
        this.playerID = new UUID(buf.readLong(), buf.readLong());

        this.slot = buf.readInt();
        this.count = buf.readInt();

        this.isInv = buf.readBoolean();
    }

    @Override
    public void handle(IGameInstance game, ChannelHandlerContext context){
        IWorld world = game.getWorld();
        if (world != null) {
            AbstractEntityPlayer player = world.getPlayer(this.playerID);
            if (player != null) {
                Utils.removeAndThrow(this.isInv ? player.getInvContainer() : player.getContainer(), player, this.slot, this.count);
            }
        }
    }
}
