package com.gm910.magicmod.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.blocks.BlockHolyTile;
import com.gm910.magicmod.blocks.BlockSimplePortal;
import com.gm910.magicmod.blocks.BlockSimplePortal.TileEntityPortal;
import com.gm910.magicmod.blocks.BlockStatue.TileEntityStatue;
import com.gm910.magicmod.blocks.machine.BlockChasma;
import com.gm910.magicmod.blocks.machine.BlockChasma.TileEntityChasma;
import com.gm910.magicmod.blocks.machine.BlockPentaExtend;
import com.gm910.magicmod.blocks.machine.BlockPentaExtend.TileEntityPentaEx;
import com.gm910.magicmod.blocks.machine.BlockPentacle;
import com.gm910.magicmod.blocks.machine.BlockSheol;
import com.gm910.magicmod.blocks.machine.BlockSheol.TileEntityTeleSheol;
import com.gm910.magicmod.blocks.machine.BlockWizardifier;
import com.gm910.magicmod.blocks.machine.MagicGenerator;
import com.gm910.magicmod.blocks.te.TileEntityPentacle;
import com.gm910.magicmod.deity.util.Deity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid=MagicMod.MODID)
public class BlockInit {
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block WIZARDIFIER = new BlockWizardifier("wizardifier", Material.ROCK);
	public static final Block ORE_OVERWORLD = new BlockOres("ore_overworld", "overworld");
	public static final Block ORE_END = new BlockOres("ore_end", "end");
	public static final Block ORE_NETHER = new BlockOres("ore_nether", "nether");
	public static final Block PENTACLE = (new BlockPentacle("pentacle")).setCreativeTab(CreativeTabs.MISC);
	public static final Block PENTA_EXTEND = new BlockPentaExtend("pentacle_extension");
	public static final Block CHASMA = new BlockChasma("chasma");
	public static final Block SHEOL =  (new BlockSheol("sheol")).setCreativeTab(CreativeTabs.MISC);
	/*public static final Block ALTAR = (new BlockAltar("altar")).setCreativeTab(CreativeTabs.MISC);
	public static final Block GOD_SUMMONER = (new BlockGodCenter("god_summon_center")).setCreativeTab(CreativeTabs.MISC);
	public static final Block GOD_SIDE_BLOCK = new BlockGodSummonStructure("god_summon_side");*/
	public static final Block HOLY_TILE = (new BlockHolyTile("holy_tile")).setCreativeTab(CreativeTabs.MISC);
	public static final Map<Deity, Block> GOD_STATUES = new HashMap<Deity, Block>();
	public static final Block MAGIC_GENERATOR = (new MagicGenerator("magic_generator")).setCreativeTab(CreativeTabs.MISC);
	public static final Block SIMPLE_PORTAL = new BlockSimplePortal("simple_portal");
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		
		BLOCKS.addAll(GOD_STATUES.values());
		
		for (Block b : BLOCKS) {
			event.getRegistry().registerAll(b);
		}
		
		GameRegistry.registerTileEntity(TileEntityPentacle.class, "pentacle");
		GameRegistry.registerTileEntity(TileEntityChasma.class, "chasma");
		/*GameRegistry.registerTileEntity(TileEntityAltar.class, "altar");
		GameRegistry.registerTileEntity(TileEntityGodSummoner.class, "god_summoner");
		GameRegistry.registerTileEntity(TileEntityGodStructure.class, "god_structure");*/
		GameRegistry.registerTileEntity(TileEntityTeleSheol.class, "sheol_teleporter");
		GameRegistry.registerTileEntity(TileEntityStatue.class, "deity_statue");
		GameRegistry.registerTileEntity(MagicGenerator.TileEntityMagicGenerator.class, "magic_generator");
		GameRegistry.registerTileEntity(TileEntityPentaEx.class, "penta_ex");
		GameRegistry.registerTileEntity(TileEntityPortal.class, "portal");
	}

	public static void registerRender(Item item) {
		  ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}

}
