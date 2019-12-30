package com.gm910.magicmod.init;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.world.dimensions.DimensionSheol;
import com.gm910.magicmod.world.dimensions.DimensionTyphon;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class DimensionInit {

	public static final ArrayList<DimensionType> DIMENSIONS = new ArrayList<DimensionType>();
	
	public static final int DIMENSION_SHEOL = MagicMod.DIMENSION_SHEOL;
	public static final int DIMENSION_TYPHON = MagicMod.DIMENSION_TYPHON;
	
	public static DimensionType SHEOL;
	public static DimensionType TYPHON;
	
	
	public static void registerDimensions() {
		register("SHEOL", "Sheol", "_sheol", MagicMod.DIMENSION_SHEOL, DimensionSheol.class, false);
		register("TYPHON", "Typhon", "_typhon", MagicMod.DIMENSION_TYPHON, DimensionTyphon.class, false);
	}
	
	public static void register(String fieldName, String dimensionName, String suffix, int id, Class<? extends WorldProvider> clazz, boolean keepLoaded) {
		Field field = null;
		try {
			field = DimensionInit.class.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		if (field == null) {
			return;
		}
		try {
			field.set(null, DimensionType.register(dimensionName, suffix, id, clazz, keepLoaded));
			DimensionManager.registerDimension(id, (DimensionType) field.get(null));
			DIMENSIONS.add((DimensionType) field.get(null));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
