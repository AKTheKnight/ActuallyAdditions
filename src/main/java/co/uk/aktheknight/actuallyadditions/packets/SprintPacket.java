package co.uk.aktheknight.actuallyadditions.packets;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.net.packet.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.UUID;

public class SprintPacket implements IPacket{

    public SprintPacket(){
    }

    public SprintPacket(UUID playerID, boolean start){
        this.playerID = playerID;
        this.endTime = start ? -1 : System.currentTimeMillis();
    }

    private UUID playerID;
    private long endTime;

    @Override
    public void toBuffer(ByteBuf buf) throws IOException{
        buf.writeLong(this.playerID.getMostSignificantBits());
        buf.writeLong(this.playerID.getLeastSignificantBits());
        buf.writeLong(this.endTime);
    }

    @Override
    public void fromBuffer(ByteBuf buf) throws IOException{
        this.playerID = new UUID(buf.readLong(), buf.readLong());
        this.endTime = buf.readLong();
    }

    @Override
    public void handle(IGameInstance game, ChannelHandlerContext context){
        AbstractEntityPlayer player = game.getWorld().getPlayer(this.playerID);
        DataSet set = player.getOrCreateAdditionalData();

        set.addLong("actadd.sprint", this.endTime);
    }
}
