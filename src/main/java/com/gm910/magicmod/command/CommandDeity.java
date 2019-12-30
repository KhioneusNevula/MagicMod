package com.gm910.magicmod.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.init.PotionInit;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandDeity extends CommandBase {

	private final List<String> aliases = Lists.newArrayList(MagicMod.MODID, "deity", "god");
	private final List<String> operations = Lists.newArrayList("add", "subtract", "set");
	private final List<String> commands = Lists.newArrayList("level", "worship", "curse", "bless");
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "deity";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "Use 1: deity level <deity name/all> <set/add/subtract> <integer amount of levels> [entity selector]\n"
				+ TextFormatting.RED + "Use 2: deity worship <deity name/all> <set/add/subtract> <integer or decimal amount of points> [entity selector]\n"
				+ TextFormatting.RED + "Use 3: deity <curse/bless> <deity name> <entity selector> [time]";
	}
	
	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return aliases;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		return sender.canUseCommand(server.getOpPermissionLevel(), "xp");
	}
	
	public void sendUsage(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(TextFormatting.RED + getUsage(sender)));
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if(args.length < 3 || args.length > 5) {
			System.out.println("args!");
			sendUsage(sender);
			return;
		}
		
		if (args[0].equalsIgnoreCase("level") || args[0].equalsIgnoreCase("worship")) {
			System.out.println("level/worship");
			if (args.length != 5 && args.length != 4) {
				System.out.println("args!");
				sendUsage(sender);
				return;
			} 
			if (!(sender.getCommandSenderEntity() instanceof EntityLivingBase) && args.length < 5) {
				System.out.println("sender!");
				sendUsage(sender);
				return;
			}
			
			String dString = args[1];
			String operation = args[2];
			Deity deity = Deities.fromString(ResourceLocation.splitObjectName(dString)[0].equalsIgnoreCase("minecraft") ? (new ResourceLocation("magicmod:" + dString)).toString() : dString);
			List<Entity> players = new ArrayList<Entity>();
			
			if (args.length == 5) {
				players = getEntityList(server, sender, args[4]);
				players.removeIf(en -> !(en instanceof EntityLivingBase));
			} else {
				players.add((EntityLivingBase) sender.getCommandSenderEntity());
			}
			
			if (!this.operations.contains(operation)) {
				System.out.println("operation! " + operation);
				sendUsage(sender);
				return;
			}
			
			if (deity == null) {
				if (!dString.equalsIgnoreCase("all")) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED + dString + " is not a deity!"));
					return;
				}
			}
			
			if (players.isEmpty()) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Selector " + args[4] + " found nothing"));
				return;
			}
			
			double points = 0;
			try {
				points = Double.parseDouble(args[3]);
			} catch (NumberFormatException e) {
				sendUsage(sender);
				return;
			}
			if (args[0].equalsIgnoreCase("worship")) {
				if (dString.equalsIgnoreCase("all")) {
					for (Deity d : Deities.deities) {
						for (Entity en : players) {
							if (d.isDevout(en.getUniqueID())) {
								if (operation.equalsIgnoreCase("add")) {
									d.changeWorshipBy((EntityLivingBase)en, points);
								} else if (operation.equalsIgnoreCase("subtract")) {
									d.changeWorshipBy((EntityLivingBase)en, -points);
								} else if (operation.equalsIgnoreCase("set")) {
									d.setWorship((EntityLivingBase)en, points);
								}
								en.sendMessage(new TextComponentString("You now have " + d.getWorship((EntityLivingBase)en) + " worship for " + d));
							}
						}
					}
				} else {
					for (Entity en : players) {
						if (deity.isDevout(en.getUniqueID())) {
							if (operation.equalsIgnoreCase("add")) {
								deity.changeWorshipBy((EntityLivingBase)en, points);
							} else if (operation.equalsIgnoreCase("subtract")) {
								deity.changeWorshipBy((EntityLivingBase)en, -points);
							} else if (operation.equalsIgnoreCase("set")) {
								deity.setWorship((EntityLivingBase)en, points);
							}
							en.sendMessage(new TextComponentString("You now have " + deity.getWorship((EntityLivingBase)en) + " worship for " + deity));
						}
					}
				}
			} else {
				points = (int)points;
				if (dString.equalsIgnoreCase("all")) {
					for (Deity d : Deities.deities) {
						for (Entity en : players) {
							if (d.isDevout(en.getUniqueID())) {
								if (operation.equalsIgnoreCase("add")) {
									d.changePointsBy((EntityLivingBase)en, (int)points);
								} else if (operation.equalsIgnoreCase("subtract")) {
									d.changePointsBy((EntityLivingBase)en, -(int)points);
								} else if (operation.equalsIgnoreCase("set")) {
									d.setPoints((EntityLivingBase)en, (int)points);
								}
								en.sendMessage(new TextComponentString("You now have " + d.getPoints((EntityLivingBase)en) + " levels for " + d));
							}
						}
					}
				} else {
					for (Entity en : players) {
						System.out.println(operation);
						if (deity.isDevout(en.getUniqueID())) {
							if (operation.equalsIgnoreCase("add")) {
								deity.changePointsBy((EntityLivingBase)en, (int)points);
								deity.notifyDevotion((EntityLivingBase)en);
							} else if (operation.equalsIgnoreCase("subtract")) {
								deity.changePointsBy((EntityLivingBase)en, -(int)points);
								deity.notifyDevotion((EntityLivingBase)en);
							} else if (operation.equalsIgnoreCase("set")) {
								deity.setPoints((EntityLivingBase)en, (int)points);
								deity.notifyDevotion((EntityLivingBase)en);
							}
							en.sendMessage(new TextComponentString("You now have " + deity.getPoints((EntityLivingBase)en) + " points for " + deity));
						}
					}
				}
			}
			
		} else if (args[0].equalsIgnoreCase("curse") || args[0].equalsIgnoreCase("bless")) {
			if (args.length != 4 && args.length != 3) {
				sendUsage(sender);
				return;
			} 
			String dString = args[1];
			Deity deity = Deities.fromString(ResourceLocation.splitObjectName(dString)[0].equalsIgnoreCase("minecraft") ? (new ResourceLocation("magicmod:" + dString)).toString() : dString);
			
			if (deity == null) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + " is not a deity!"));
				return;
			}
			int time = 2400;
			String selector = args[2];
			List<Entity> players = getEntityList(server, sender, selector);
			players.removeIf(en -> !(en instanceof EntityLivingBase));
			if (players.isEmpty()) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Selector " + args[4] + " found nothing"));
				return;
			}
			
			if (args.length == 4) {
				try {
					time = Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					sendUsage(sender);
					return;
				}
			}
			
			boolean isCurse = args[0].equalsIgnoreCase("curse");
			for (Entity en : players) {
				((EntityLivingBase)en).addPotionEffect(new PotionEffect(isCurse ? PotionInit.CURSES.get(deity) : PotionInit.BLESSINGS.get(deity), time));
			}
			
		} else {
			sendUsage(sender);
		}

		
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		switch(args.length) {
			case 1: 
				return getListOfStringsMatchingLastWord(args, commands.toArray(new String[4]));
			case 2: 
				if (args[0].equalsIgnoreCase("curse") || args[0].equalsIgnoreCase("bless")) {
					String[] list = new String[Deities.deities.size()];
					for (int i = 0; i < Deities.deities.size(); i++) {
						list[i] = Deities.deities.get(i).getUnlocName().toString();
					}
					
					if (list.length == 0) return Collections.emptyList();
					return getListOfStringsMatchingLastWord(args, list);
				} else if (args[0].equalsIgnoreCase("worship") || args[0].equalsIgnoreCase("level")){
					String[] list = new String[Deities.deities.size() + 1];
					for (int i = 0; i < Deities.deities.size(); i++) {
						list[i] = Deities.deities.get(i).getUnlocName().getResourcePath();
					}
					list[list.length-1] = "all";
					if (list.length == 0) return Collections.emptyList();
					return getListOfStringsMatchingLastWord(args, list);
				} else {
					return Collections.emptyList();
				}
			
			case 3: 
				if (args[0].equalsIgnoreCase("curse") || args[0].equalsIgnoreCase("bless")) {
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				} else if (args[0].equalsIgnoreCase("level") || args[0].equalsIgnoreCase("worship")) {
					return getListOfStringsMatchingLastWord(args, operations.toArray(new String[3]));
					
				}
				else {
					return Collections.emptyList();
				}
			
			case 5: 
				if (args[0].equalsIgnoreCase("worship") || args[0].equalsIgnoreCase("level")) {
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				} else {
					return Collections.emptyList();
				}
			
			default: 
				return Collections.emptyList();
			
		}
	}

}
