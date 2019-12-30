package com.gm910.magicmod.init;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.entities.EntityDeity;
import com.gm910.magicmod.deity.entities.EntityDeityProjection;
import com.gm910.magicmod.entity.classes.demons.EntityAzraelite;
import com.gm910.magicmod.entity.classes.demons.EntityCacodemon;
import com.gm910.magicmod.entity.classes.demons.EntityForgeDemon;
import com.gm910.magicmod.entity.classes.demons.EntityGatekeeper;
import com.gm910.magicmod.entity.classes.demons.EntityGoeturge;
import com.gm910.magicmod.entity.classes.demons.EntityImp;
import com.gm910.magicmod.entity.classes.demons.EntityKraken;
import com.gm910.magicmod.entity.classes.magical.EntityHellPortal;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
	public static final int BASIC_DEMON = 120;
	public static final int IMP = 121;
	public static final int CACODEMON = 122;
	public static final int GOETURGE = 123;
	public static final int AZRAELITE = 124;
	public static final int GATEKEEPER = 125;
	public static final int HELL_PORTAL = 126;
	public static final int DEITY = 127;
	public static final int DEITY_PROJECTION = 128;
	public static final int KRAKEN = 129;
	
	public static void registerEntities() {
		registerEntity("forge_demon", EntityForgeDemon.class, BASIC_DEMON, 50, 0x110000, 0xFF0000);
		registerEntity("imp", EntityImp.class, IMP, 50, 0xAA0000, 0xFF0000);
		registerEntity("cacodemon", EntityCacodemon.class, CACODEMON, 50, 0x8800AA, 0xCC0000);
		registerEntity("goeturge", EntityGoeturge.class, GOETURGE, 50, 0x8800AA, 0x00FFFF);
		registerEntity("azraelite", EntityAzraelite.class, AZRAELITE, 50, 0x111111, 0);
		registerEntity("gatekeeper", EntityGatekeeper.class, GATEKEEPER, 50, 0x110000, 0xFF0000);
		registerEntity("hell_portal", EntityHellPortal.class, HELL_PORTAL, 50);
		registerEntity("deity", EntityDeity.class, DEITY, 50);
		registerEntity("deity_projection", EntityDeityProjection.class, DEITY_PROJECTION, 50);
		registerEntity("kraken", EntityKraken.class, KRAKEN, 50, 0x000088, 0x880000);
	}
	
	public static void registerEntity(String name, Class<? extends Entity> ent, int id, int range, int color1, int color2) {
		EntityRegistry.registerModEntity(new ResourceLocation(MagicMod.MODID + ":" + name), ent, name, id, MagicMod.instance, range, 1, true, color1, color2);
	}
	
	public static void registerEntity(String name, Class<? extends Entity> ent, int id, int range) {
		EntityRegistry.registerModEntity(new ResourceLocation(MagicMod.MODID + ":" + name), ent, name, id, MagicMod.instance, range, 1, true);
	}
	

}
