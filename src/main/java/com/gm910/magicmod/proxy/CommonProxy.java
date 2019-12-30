package com.gm910.magicmod.proxy;

import java.nio.charset.Charset;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.proxy.CommonProxy.MagicMessage.MagicMessageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MagicMod.MODID);
	
	public CommonProxy() {
		NETWORK.registerMessage(MagicMessageHandler.class, MagicMessage.class, 0, Side.SERVER);
		//NETWORK.registerMessage(MagicMessageHandler.class, MagicMessage.class, 1, Side.CLIENT);
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}
	
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
	
	public static MinecraftServer getServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}
	
	public static class MagicMessage implements IMessage {
		  // A default constructor is always required
		  public MagicMessage(){}

		  private NBTTagCompound toSend = null;
		  public MagicMessage(NBTTagCompound compound) {
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
		  public static class MagicMessageHandler implements IMessageHandler<MagicMessage, IMessage> {
			  // Do note that the default constructor is required, but implicitly defined in this case
			  
			  
			  @Override public IMessage onMessage(MagicMessage message, MessageContext ctx) {
			    // This is the player the packet was sent to the server from
			    EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			    
			    // The value that was sent
			    NBTTagCompound comp = message.toSend;
			    
			    // Execute the action on the main server thread by adding it as a scheduled task
			    serverPlayer.getServerWorld().addScheduledTask(() -> {
			    	Deities.readFromNBT(comp);
			      //serverPlayer.sendMessage(new TextComponentString(comp.toString()));
			    });
			    // No response packet
			    return null;
			  }
		}
		  
	}
	
}
