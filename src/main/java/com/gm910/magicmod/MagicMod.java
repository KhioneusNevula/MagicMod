package com.gm910.magicmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.gm910.magicmod.blocks.BlockStatue;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.handling.ClericTradeAdder;
import com.gm910.magicmod.handling.MagicHandler;
import com.gm910.magicmod.handling.RenderHandler;
import com.gm910.magicmod.init.BiomeInit;
import com.gm910.magicmod.init.BlockInit;
import com.gm910.magicmod.init.CapabilityInit;
import com.gm910.magicmod.init.CommandInit;
import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.init.EntityInit;
import com.gm910.magicmod.init.ItemInit;
import com.gm910.magicmod.init.PotionInit;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

@Mod(modid=MagicMod.MODID, name=MagicMod.MODNAME, version=MagicMod.VERSION, acceptedMinecraftVersions=MagicMod.ACCEPTED_MINECRAFT_VERSIONS)
@EventBusSubscriber(modid = MagicMod.MODID)
public class MagicMod implements OrderedLoadingCallback {
	public static final String MODID = "magicmod";
	public static final String MODNAME = "GM910's Magic Mod";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12]";
	
    public static final String CLIENT = "com.gm910.magicmod.proxy.ClientProxy";
    public static final String COMMON = "com.gm910.magicmod.proxy.CommonProxy";
    
    public static enum EnumKey {
    	GOD_INVENTORY,
    	GOD_TRADE
    }
    
    public static Map<EnumKey, KeyBinding> keybindings = new HashMap<EnumKey, KeyBinding>();
    
    @SidedProxy(clientSide = CLIENT, serverSide = COMMON)
    public static CommonProxy proxy;
	
	public static final int DIMENSION_SHEOL = 13;
	public static final int DIMENSION_TYPHON = 12;
	
	@Instance
	public static MagicMod instance;
	
	public MagicMod() {

		
	}
	
	public static Deities deities() {
		return proxy.deities;
	}
	
	public static MagicHandler magic() {
		return proxy.magic;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println(MODID + ":preInit");
		

		deities().initDeities();
		proxy.getMagic().initSpells();
		PotionInit.initDeityEffectsAndBottles();
		
		for (Deity d : proxy.deities.deities) {
			Block block = (new BlockStatue(d)).setCreativeTab(CreativeTabs.MISC);
			BlockInit.GOD_STATUES.put(d, block);
		}
		
		ItemInit.createHolyBooks();
		
		CapabilityInit.preInit();
		EntityInit.registerEntities();
		if (event.getSide().isClient()) {
			RenderHandler.registerEntRenderers();
		}
		BiomeInit.registerBiomes();
		DimensionInit.registerDimensions();
		
		PotionInit.registerPotions();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println(MODID + ":init");
		VillagerProfession prof = VillagerRegistry.getById(2);// ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("cleric"));
		if (prof != null) {
			VillagerCareer career = prof.getCareer(0);
			if (career != null) {
				career.addTrade(1, new ClericTradeAdder());
			} else {
				System.out.println("no such career");
			}
		} else {
			System.out.println("No such profession");
		}

		if (event.getSide().isClient()) {
			keybindings.put(EnumKey.GOD_INVENTORY, new KeyBinding("key.inventory.desc", Keyboard.KEY_G, "key.divine.category"));
	    	keybindings.put(EnumKey.GOD_TRADE, new KeyBinding("key.trade.desc", Keyboard.KEY_T, "key.divine.category"));
	    	
	    	for (KeyBinding key : keybindings.values()) {
	    		ClientRegistry.registerKeyBinding(key);
	    	}
		}
    	
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		System.out.println(MODID + ":postInit");
		ForgeChunkManager.setForcedChunkLoadingCallback(this, this);
	}
	
	@EventHandler
	public void serverInit(FMLServerStartingEvent event) {
		CommandInit.registerCommands(event);
		Scoreboard scoreboard = event.getServer().getWorld(0).getScoreboard();
		for (Deity deity : deities().deities) {
			if (scoreboard.getTeam(deity.getUnlocName().getResourcePath()) == null) {
				scoreboard.createTeam(deity.getUnlocName().getResourcePath());
			}
		}
	}
	
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		int max = ForgeChunkManager.getMaxTicketLengthFor(this.MODID);
		ArrayList<Ticket> prio = getByPriority(Priority.NECESSARY);
		
		int iters = 0;
		for (Ticket t : prio) {
			
			if (iters >= max) {
				ForgeChunkManager.releaseTicket(t);
			}
			iters++;
		}
		prio = getByPriority(Priority.EH);
		for (Ticket t : prio) {
			
			if (iters >= max) {
				ForgeChunkManager.releaseTicket(t);
			}
			iters++;
		}
		prio = getByPriority(Priority.UNNECESSARY);
		for (Ticket t : prio) {
			
			if (iters >= max) {
				ForgeChunkManager.releaseTicket(t);
			}
			iters++;
		}
		
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		ArrayList<Ticket> listo = getByPriority(Priority.NECESSARY);
		return listo;
	}
	
	public static Ticket requestTicket(Priority priority, World world, Type type) {
		Ticket t = ForgeChunkManager.requestTicket(MagicMod.instance, world, type);
		if (t != null) {
			tickets.add(new TicketEntry(priority, t));
		}
		
		return t;
	}
	
	public static void releaseTicket(Ticket ticket) {
		TicketEntry ent = null;
		if (tickets.isEmpty()) {
			ForgeChunkManager.releaseTicket(ticket);
			return;
		}
		for (TicketEntry t : tickets) {
			if (t.getTicket().equals(ticket)) {
				ent = t;
			}
		}
		if (ent != null) tickets.remove(ent);
		ForgeChunkManager.releaseTicket(ticket);
	}
	
	public static ArrayList<Ticket> getByPriority(Priority p) {
		ArrayList<Ticket> ticks = new ArrayList<Ticket>();
		if (tickets.isEmpty()) return ticks;
		for (TicketEntry t : tickets) {
			if (t.priority == p) {
				ticks.add(t.getTicket());
			}
		}
		return ticks;
	}
	
	public static ArrayList<TicketEntry> tickets = new ArrayList<TicketEntry>();
	
	public static class TicketEntry {
		private Priority priority;
		private Ticket ticket;
		
		public TicketEntry(Priority p, Ticket t) {
			this.priority = p;
			this.ticket = t;
		}
		
		public Priority getPriority() {
			return priority;
		}
		public Ticket getTicket() {
			return ticket;
		}
		
		public void setPriority(Priority priority) {
			this.priority = priority;
		}
		public void setTicket(Ticket ticket) {
			this.ticket = ticket;
		}
		
	}
	
	public static enum Priority {
		NECESSARY,
		EH,
		UNNECESSARY
	}
	
	@SubscribeEvent
	public static void unload(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) return;
		if (tickets.isEmpty()) return;
		for (TicketEntry tick : tickets) {
			if (tick.getTicket().world.equals(event.getWorld())) {
				if (tick.getTicket() != null) ForgeChunkManager.releaseTicket(tick.getTicket());
				tickets.remove(tick);
			}
			
		}
	}
}
