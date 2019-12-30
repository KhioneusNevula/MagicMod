package com.gm910.magicmod.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.world.Teleport;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class CommandDimensionTeleport extends CommandBase {

	private final List<String> aliases = Lists.newArrayList(MagicMod.MODID, "tpdim", "teleportdim", "tpdimension", "teleportdimension", "tpd", "dtp", "dimtp", "dimensiontp", "dimteleport", "dimensionteleport");
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "teleportdimension";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "teleportdimension <dimensionID/name of dimension lower case> [x] [y] [z] [entityToTeleport]";
	}
	
	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return aliases;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		return sender.canUseCommand(server.getOpPermissionLevel(), "tp");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if(args.length < 1) {
			return;
		}
		String dimension = args[0];
		ICommandSender toTeleport = sender;
		boolean success = false;
		int dimensionID = 0;
		if (this.getDimensionNames().contains(dimension)) {
			try {
				dimensionID = DimensionType.byName(dimension).getId();
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!success) {
			try {
					dimensionID = Integer.parseInt(dimension);
			} catch (NumberFormatException e) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED + "Dimension id " + dimensionID + " is invalid"));
					return;
			}
		}
		if(args.length == 1) {
			tpdim(server, sender, dimensionID, sender.getPosition());
		} else if (args.length == 4 || args.length == 5) {
			int x = sender.getPosition().getX();
			int y = sender.getPosition().getY();
			int z = sender.getPosition().getZ();
			try {
				x = Integer.parseInt(args[1]);
				y = Integer.parseInt(args[2]);
				z = Integer.parseInt(args[3]);
			} catch (NumberFormatException e ) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Position arguments invalid"));
				return;
			}
			if (args.length == 5) {
				List<Entity> entities = EntitySelector.matchEntities(sender, args[4], Entity.class); //getEntityList(server, sender, selector)
				if (entities.isEmpty()) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED + "Selector arguments invalid"));
				} else {
					for (Entity e : entities) {
						tpdim(server, e, dimensionID, new BlockPos(x, y, z));
					}
				}
			} else {
				tpdim(server, sender, dimensionID, new BlockPos(x, y, z));
			}
		} 
	}

	private void tpdim(MinecraftServer server, ICommandSender sender, int dimensionID, BlockPos position) {
		// TODO Auto-generated method stub
		if (sender instanceof EntityPlayer) {
			Teleport.teleportToDimension((EntityPlayer)sender, dimensionID, position.getX(), position.getY(), position.getZ());
		}
	}
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, getDimensionNames().toArray(new String[] {})) : Collections.emptyList();
	}
	
	public List<String> getDimensionNames() {
			ArrayList<String> dimensions = new ArrayList<String>();
			for (DimensionType dimension : DimensionManager.getRegisteredDimensions().keySet()) {
				dimensions.add(dimension.getName());
			}
			return dimensions;
	}

}
