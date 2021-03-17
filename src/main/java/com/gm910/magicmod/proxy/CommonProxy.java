package com.gm910.magicmod.proxy;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.handling.MagicHandler;

import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class CommonProxy {
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MagicMod.MODID);
	
	public final Deities deities;
	public final MagicHandler magic;
	
	public CommonProxy() {
		//NETWORK.registerMessage(MagicMessageHandler.class, DeityMessage.class, 0, Side.CLIENT);
		//NETWORK.registerMessage(MagicMessageHandler.class, MagicMessage.class, 1, Side.CLIENT);

		deities = new Deities();
		
		magic = new MagicHandler();
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}
	
	public Deities getDeities() {
		return deities;
	}
	
	public MagicHandler getMagic() {
		 return magic;
	}
	
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
	
	public static MinecraftServer getServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public static WorldServer getWorld(int d) {
		return getServer().getWorld(d);
	}
	
	/*public static class DeityMessage implements IMessage {
		  // A default constructor is always required
		  public DeityMessage(){}

		  private NBTTagCompound toSend = null;
		  public DeityMessage(NBTTagCompound compound) {
		    this.toSend = compound;
		  }

		  @Override public void toBytes(ByteBuf buf) {
		    // Writes the int into the buf
		    buf.writeInt(toSend.toString().length());

		    buf.writeCharSequence(toSend.toString(), Charset.defaultCharset());
		  }

		  @Override public void fromBytes(ByteBuf buf) {
		    // Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		    int ch = buf.readInt();
		    
		    String chars = "" + buf.readCharSequence(ch, Charset.defaultCharset());
		    try {
				toSend = JsonToNBT.getTagFromJson(chars);
			} catch (NBTException e) {
				e.printStackTrace();
			}
		  }

		  public static class MagicMessageHandler implements IMessageHandler<DeityMessage, IMessage> {
			  // Do note that the default constructor is required, but implicitly defined in this case
			  
			  
			  @Override public IMessage onMessage(DeityMessage message, MessageContext ctx) {
			    // This is the player the packet was sent to the server from
				
			    //EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			    
			    // The value that was sent
			    NBTTagCompound comp = message.toSend;
			    MagicMod.proxy.deities.readFromNBT(comp);
			    // Execute the action on the main server thread by adding it as a scheduled task
			    serverPlayer.getServerWorld().addScheduledTask(() -> {
			    	MagicMod.proxy.deities.readFromNBT(comp);
			      //serverPlayer.sendMessage(new TextComponentString(comp.toString()));
			    });
			    // No response packet
			    return null;
			  }
		}
		  
	}*/
	
}
