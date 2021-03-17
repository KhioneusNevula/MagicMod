package com.gm910.magicmod.deity.deities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.MagicMod.EnumKey;
import com.gm910.magicmod.deity.entities.EntityDeity;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.RandomnessUtils;
import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.handling.util.TextUtil;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;
import com.gm910.magicmod.magicdamage.EntityDivineLightning;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSeaLantern;
import net.minecraft.block.BlockSign;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DeityLeviathan extends Deity {
	
	public final ServerPosList rainAltars = new ServerPosList(null);
	//public final ServerPosList teleporters = new ServerPosList(null);
	public final UUIDtoServerPosList teleporters = new UUIDtoServerPosList(null);
	public final Ability rainSummoner = new Ability("RainSummoner", 2);
	public final Ability waterSpirit = new Ability("WaterSpirit", 3);
	public final Ability kraken = new Ability("Kraken", 5);
	public final Ability waterTeleport = new Ability("WaterTeleport", 6);
	public final Ability waterShield = new Ability("WaterShield", 7);
	public final Ability waterKill = new Ability("WaterKill", 8);
	public final Ability waterDimension = new Ability("WaterDimension", 9);
	public final Ability waterSeance = new Ability("WaterSeance");
	public final Ability waterReincarnation = new Ability("WaterReincarnation");
	public final Ability waterBending = new Ability("WaterBending");
	public final Ability accessInventory = new Ability("AccessInventory", 4);
	public final UUIDList waterEyes = new UUIDList(null);
	

	@SuppressWarnings("unchecked")
	public DeityLeviathan(ResourceLocation rl, String displayName, boolean warlike, boolean canVillageFollow,
			ItemStack...required) {
		super(rl, displayName, warlike, canVillageFollow, "ocean", required);
		this.registerAbility(rainSummoner);
		this.registerAbility(waterSpirit);
		this.registerAbility(kraken);
		this.registerAbility(waterTeleport);
		this.registerAbility(waterShield);
		this.registerAbility(waterKill);
		this.registerAbility(waterDimension);
		this.registerAbility(waterSeance);
		this.registerAbility(waterReincarnation);
		this.registerAbility(waterBending);
		this.registerAbility(accessInventory);
		this.addRequiredQuestTypes(QuestRain.class, QuestWaterSpirit.class, QuestInventory.class, QuestKraken.class, QuestTeleport.class, QuestShield.class, QuestKill.class, QuestDimension.class);
		this.addSpecialData("RainAltars", rainAltars);
		this.addSpecialData("WaterVisionEntities", waterEyes);
		this.addSpecialData("teleporters", teleporters);
		this.addQuestItem("rain_horn");
		this.addQuestItem("kraken_tentacle");
		this.addQuestItem("ocean_grave");
		this.addQuestItem("ocean_soul");
		this.addQuestItem("ocean_mind");
		//this.registerDevoutDataType("teleporters", NBTType.LIST);
		
	}
	
	@Override
	public Block getLightningBlock() {
		// TODO Auto-generated method stub
		return Blocks.WATER;
	}

	@Override
	public void initAI(EntityDeity avatar) {
		
		avatar.tasks.addTask(0, avatar.new AIDroppedItemTrade());
	}
	
	@Override
	public void onInventoryChange(DivineInventory inventory, UUID devout) {
		
		String[] possibleRequests = {"water seance", "water reincarnation", "waterbending", "water bending"};
		
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			ArrayList<String> requests = new ArrayList<String>();
			if (stack == ItemStack.EMPTY) continue;
			if (stack.getItem() == Items.WRITTEN_BOOK) {
				NBTTagCompound comp = stack.getTagCompound();
				if (comp != null) {
					NBTTagList list = comp.getTagList("pages", NBT.TAG_STRING);
					for (int j = 0; j < list.tagCount(); j++) {
						ITextComponent cmp = ITextComponent.Serializer.fromJsonLenient(list.getStringTagAt(i));
						String string = TextFormatting.getTextWithoutFormattingCodes(cmp.getFormattedText());
						
						if (!string.isEmpty()) {
							requests.add(string);
						}
					}
				}
			} else {
				NBTTagCompound comp = stack.getSubCompound("display");
				if (comp != null) {
					ITextComponent nameI = ITextComponent.Serializer.fromJsonLenient(comp.getString("Name"));
					ITextComponent loreI = ITextComponent.Serializer.fromJsonLenient(comp.getString("Lore"));
					
					String name = TextFormatting.getTextWithoutFormattingCodes(nameI.getFormattedText());
					String lore = TextFormatting.getTextWithoutFormattingCodes(loreI.getFormattedText());
					if (!name.isEmpty()) requests.add(name);
					if (!lore.isEmpty()) requests.add(lore);
				}
			}
			
			for (String s : requests) {
				String request = "";
				for (String re : possibleRequests) {
					if (re.toLowerCase().contains(re)) {
						request = re;
					}
				}
				ItemStack newStack = stack.copy();
				if (!request.isEmpty()) {
					
					if (request.equals("water seance")) {
						newStack = new ItemStack(this.getQuestItem("ocean_grave"), 1);
					} 
					else if (request.equals("water seance")) {
						newStack = new ItemStack(this.getQuestItem("ocean_soul"), 1);
					} 
					else if (request.equals("water bending") || request.equals("waterbending")) {
						newStack = new ItemStack(this.getQuestItem("ocean_mind"), 1);
					}
				}
				if (inventory.getStackInSlot(i) == stack || inventory.getStackInSlot(i) == null) {
					inventory.setInventorySlotContents(i, newStack);
				} else {
					int k = 0;
					boolean cond = false;
					while (!cond) {
						if (inventory.getStackInSlot(k) == stack || inventory.getStackInSlot(k) == ItemStack.EMPTY) {
							cond = true;
							inventory.setInventorySlotContents(k, newStack);
						} else {
							k++;
						}
					}
					if (!cond) {
						Entity e = CommonProxy.getServer().getEntityFromUuid(devout);
						e.world.spawnEntity(new EntityItem(e.world, e.posX, e.posY, e.posZ, newStack));
						
					}
				}
			}
		}
	}
	
	@Override
	public void onDevoutLivingEvent(LivingEvent event) {
		if (this.isDead()) return;
		/*if (!this.hasAbility(event.getEntityLiving().getPersistentID(), accessInventory.name)) {
			this.giveAbility(event.getEntityLiving().getUniqueID(), "AccessInventory");
		}*/
		if (accessInventory.hasAbility(event.getEntityLiving().getPersistentID()) && !this.hasInventory(event.getEntityLiving().getPersistentID())) {
			this.addInventory(event.getEntityLiving().getPersistentID());
		}
		if (!accessInventory.hasAbility(event.getEntityLiving().getPersistentID())) {
			this.godInventory.remove(event.getEntityLiving().getPersistentID());
		}
		
	}
	
	@Override
	public MerchantRecipeList initRecipes(EntityLivingBase base) {
		MerchantRecipeList list = new MerchantRecipeList();
		MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Items.WATER_BUCKET, 1), new ItemStack(Item.getItemFromBlock(Blocks.SEA_LANTERN)));
		list.add(recipe);
		return list;
	}
	
	@Override
	public void onKeyPressed(KeyBinding key, EntityPlayer devotee) {
		// TODO Auto-generated method stub
		super.onKeyPressed(key, devotee);
		if (key.getKeyCode() == MagicMod.keybindings.get(EnumKey.GOD_TRADE).getKeyCode() && accessInventory.hasAbility(devotee.getPersistentID())) {
			devotee.displayVillagerTradeGui(this);
		}
		
	}
	
	@Override
	public void onClTick(ClientTickEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		//System.out.println("DGfh");
		World world = player.world;
		for (int x = -20; x <= 20; x++) {
			for (int y = -20; y <= 20; y++) {
				for (int z = -20; z <= 20; z++) {
					ServerPos pos = new ServerPos(player.getPosition().add(x, y, z), world.provider.getDimension());
					for (UUID uu : this.getFavorPerPlayer().keySet()) {
						if (this.teleporters.get(uu) != null) {
							ArrayList<ServerPos> arr = teleporters.get(uu);
							for (int i = 0; i < arr.size(); i++) {
								if (arr.get(i).equalsWithoutName(pos)) {
									RandomnessUtils.spawnParticles(EnumParticleTypes.END_ROD, world, pos, 3);
								}
							}
						}
					}
				}
			}
		}
	}
	

	@Override
	public void onDevoutPlaceBlock(EntityPlaceEvent event) {
		if (this.isDead()) return;
		if (rainSummoner.hasAbility(event.getEntity().getPersistentID()) && rainAltarConfiguration(event.getWorld(), event.getPos())) {
			event.getWorld().addWeatherEffect(new EntityDivineLightning(event.getWorld(), this, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), true, false));
			rainAltars.add(new ServerPos(event.getPos(), event.getWorld().provider.getDimension()));
		} else if (waterTeleport.hasAbility(event.getEntity().getPersistentID()) && teleportConfig(event.getWorld(), event.getPos())) {
			System.out.println("SDfgh");
			event.getWorld().addWeatherEffect(new EntityDivineLightning(event.getWorld(), this, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), true, false));
			TileEntitySign sign = (TileEntitySign)event.getWorld().getTileEntity(event.getPos());
			String name = TextFormatting.getTextWithoutFormattingCodes(sign.signText[0].getFormattedText());
			teleporters.addServerPos(event.getEntity().getPersistentID(), new ServerPos(event.getPos(), event.getWorld().provider.getDimension()));
		
		}
	}
	
	@Override
	public void onBreakBlock(BreakEvent event) {
		if (rainAltars.contains(new ServerPos(event.getPos(), event.getWorld().provider.getDimension())) || rainAltars.contains(new ServerPos(event.getPos().up(), event.getWorld().provider.getDimension()))) {
			rainAltars.remove(new ServerPos(event.getPos(), event.getWorld().provider.getDimension()));
		} else if (teleporters.hasServerPos(event.getPlayer().getPersistentID(), new ServerPos(event.getPos(), event.getWorld().provider.getDimension()))) {
			teleporters.removeServerPos(event.getPlayer().getPersistentID(), new ServerPos(event.getPos(), event.getWorld().provider.getDimension()));
		}
		super.onBreakBlock(event);
	}
	
	public boolean teleportConfig(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() instanceof BlockSign && world.getBlockState(pos.down()).getBlock() instanceof BlockSeaLantern) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onInteract(RightClickBlock event) {
		if (rainAltars.contains(new ServerPos(event.getPos(), event.getWorld().provider.getDimension()))) {
			if (event.getEntityPlayer().isSneaking()) {
				event.getWorld().getWorldInfo().setRaining(true);
			} else {
				event.getWorld().getWorldInfo().setRaining(false);
			}
			
		} else {
			boolean bool = false;
			UUID devout = null;
			ArrayList<ServerPos> tps = new ArrayList<ServerPos>();
			if (!this.getFavorPerPlayer().isEmpty()) {
				for (UUID uu : this.getFavorPerPlayer().keySet()) {
					
					ArrayList<ServerPos> teleporters = this.teleporters.get(uu);
					System.out.println(teleporters);
					
					if (teleporters != null) {
						for (int i = 0; i < teleporters.size(); i++) {
							ServerPos pos = teleporters.get(i);
							System.out.println("pos : " + pos);
							
							if (event.getPos().equals(pos.getPos())) {// && event.getEntityPlayer().getUniqueID().equals(uu)) {
								bool = true;
								
								devout = uu;
							} //else {
								tps.add(pos);
							//}
						}
					}
				}
			}
			
			if (bool) {// && !event.getWorld().isRemote) {
				
				ItemStack stack = new ItemStack(Items.WRITTEN_BOOK, 1);
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				NBTTagCompound book = stack.getTagCompound();
				book.setString("title", "Teleport List");
				book.setString("author", TextUtil.nameFromUUID(devout));
				NBTTagList pages = new NBTTagList();
				
				
				for (ServerPos pos : tps) {
					TextComponentString str = new TextComponentString(pos.toString(true));
					/*Entity selector;
					try {
						selector = CommandBase.getEntity(CommonProxy.getServer(), event.getEntityPlayer(), "@p");
					} catch (CommandException e) {
						
						e.printStackTrace();
					}*/
					
					str.setStyle(str.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdim " + pos.getD() + " " +
					pos.getX() + " " + pos.getY() + " " + pos.getZ() + " @p")));
					System.out.println(str.toString());
					pages.appendTag(new NBTTagString(TextUtil.jsonize(str)));
				}
				
				book.setTag("pages", pages);
				System.out.println(pages);
				/*if (this.canAccessInventory(event.getEntityPlayer().getPersistentID())) {
					this.getInventory(event.getEntityPlayer().getPersistentID()).setInventorySlotContents(0, stack);
				}*/
				event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ, stack));
				
				/*if (!event.getWorld().isRemote) {
					resolveContents(stack, event.getEntityPlayer());
				}*/
				
				Items.WRITTEN_BOOK.onItemUse(event.getEntityPlayer(), event.getWorld(), event.getPos(), event.getHand(), event.getFace(), 0, 0, 0);
				
				//event.getEntityPlayer().openBook(stack, event.getHand());
				
			}
		}
	}

	
	
	
	public boolean rainAltarConfiguration(World world, BlockPos pos) {
		if (world.getBlockState(pos.down()).getMaterial() == Material.ROCK && (world.getBlockState(pos).getBlock() == Blocks.TORCH)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onTickDevout(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (world.getBlockState(entity.getPosition()).getMaterial() == Material.WATER || world.getBlockState(entity.getPosition().down()).getMaterial() == Material.WATER) {
			if (this.waterSpirit.hasAbility(event.getEntity().getPersistentID())) {
				leviathanEffectControl(entity, true, false);
			}
		} else {
			leviathanEffectControl(entity, true, true);
		}
		
		if (waterShield.hasAbility(event.getEntity().getPersistentID())) {
			boolean cond = nearWater(world, entity.getPosition(), 8);
			
			if (cond) {
				entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), 5, 3));
				entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("regeneration"), 5, 2));
				List<EntityLivingBase> mobs = world.getEntities(EntityLivingBase.class, en -> {
					boolean b = (en.getLastAttackedEntity() != null ? en.getLastAttackedEntity().isEntityEqual(entity) : false) ||
							(en instanceof EntityLiving ? ( ((EntityLiving)en).getAttackTarget() != null ? ((EntityLiving)en).getAttackTarget().isEntityEqual(entity) : false) : false);
					b = en.getDistanceSq(event.getEntityLiving()) <= 64 && b;
					return b;
				});
				if (!mobs.isEmpty()) {
					for (EntityLivingBase mob : mobs) {
						mob.knockBack(entity, 1, 1, 1);
						if (mob.getRNG().nextInt(5) < 1) {
							mob.attackEntityFrom(DamageSourceMystic.castGodCurse(this), 1);
						}
					}
				}
			}
		}

	}

	@Override
	public void onDevoutAttack(LivingAttackEvent event) {
		
		if (this.waterKill.hasAbility(event.getSource().getTrueSource().getPersistentID())) {
			
			EntityLivingBase en = event.getEntityLiving();
			
			en.attackEntityFrom(DamageSourceMystic.castGodCurse(this), event.getAmount());
			event.setCanceled(true);
			if (event.getEntityLiving().getRNG().nextInt(100) < 1 && !(event.getEntityLiving() instanceof EntityDragon) && !(event.getEntityLiving() instanceof EntityPlayer) && nearWater(event.getSource().getTrueSource().world, event.getEntity().getPosition(), 8)) {
				event.getEntityLiving().attackEntityFrom(DamageSourceMystic.castGodCurse(this), event.getEntityLiving().getHealth());
				event.getEntity().world.addWeatherEffect(new EntityDivineLightning(event.getEntityLiving().world, this, en.posX, en.posY, en.posZ, false, false));
				this.changeWorshipBy(event.getSource().getTrueSource().getPersistentID(), 15);
			}
		}
	}
	
	public boolean nearWater(World world, BlockPos pos, int radius) {
		
		boolean cond = false;
		for (int x = -radius; x <= radius; x++) {
			if (cond) break;
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (pos.add(x, y, z).distanceSq(pos) > radius * radius) {
						continue;
					}
					if (world.getBlockState(pos.add(x,y,z)).getMaterial() == Material.WATER) {
						cond = true;
					}
				}
			}
		}
		return cond;
	}
	
	public void leviathanEffectControl(EntityLivingBase base, boolean includeFlight, boolean remove) {
		if (!remove) {
			if (base.getActivePotionEffect(Potion.getPotionFromResourceLocation("regeneration")) == null) {
				base.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("regeneration"), 3, 2));
			}
			if (base.getActivePotionEffect(Potion.getPotionFromResourceLocation("strength")) == null) {
				base.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("strength"), 3, 2));
			}
			if (base.getActivePotionEffect(Potion.getPotionFromResourceLocation("water_breathing")) == null) {
				base.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("water_breathing"), 3, 2));
			}
			if (base.getActivePotionEffect(Potion.getPotionFromResourceLocation("night_vision")) == null) {
				base.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("night_vision"), 3, 2));
			}
			if (base.getActivePotionEffect(Potion.getPotionFromResourceLocation("resistance")) == null) {
				base.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), 3, 2));
			}
			if (base.getActivePotionEffect(Potion.getPotionFromResourceLocation("haste")) == null) {
				base.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("haste"), 3, 2));
			}
		}
		if (base instanceof EntityPlayer && includeFlight) {
			EntityPlayer player = (EntityPlayer) base;
			if (!player.capabilities.isCreativeMode) {
				player.capabilities.allowFlying = true;
				player.capabilities.isFlying = true;
			}
			if (remove) {
				
				if (!player.capabilities.isCreativeMode) {
					player.capabilities.allowFlying = false;
					player.capabilities.isFlying = false;
				}
			}
		}
	}
	
	public static class QuestRain extends Quest {

		public QuestRain(Deity d) {
			super(d, 1, 1, false, "RainHorn");
			
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof PlayerTickEvent) {
				PlayerTickEvent event = (PlayerTickEvent)e;
				for (int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
					ItemStack item = event.player.inventory.getStackInSlot(i);
					if (item != ItemStack.EMPTY) {
						if (item.getItem() == this.deity.getQuestItem("rain_horn")) {
							this.setCondition("RainHorn", true);
						}
					}
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.rainsummoner"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.rainsummoner"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	public static class QuestWaterSpirit extends Quest {

		public QuestWaterSpirit(Deity d) {
			super(d, 1, 2, false, "Nether", "Wither");
			
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof LivingDeathEvent) {
				LivingDeathEvent event = (LivingDeathEvent)e;
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					UUID uuid = null;
					for (UUID uu : deity.getQuests().keySet()) {
						if (deity.getQuest(uu) == this) {
							uuid = uu;
						}
					}
					if (event.getSource().getTrueSource().getUniqueID().equals(uuid)) {
						if (event.getEntityLiving() instanceof EntityBlaze || 
								event.getEntityLiving() instanceof EntityMagmaCube ||
								event.getEntityLiving() instanceof EntityPigZombie ||
								event.getEntityLiving() instanceof EntityWitherSkeleton) {
							this.setCondition("Nether", true);
							
						} else if (event.getEntityLiving() instanceof EntityWither) {
							this.setCondition("Wither", true);
						}
					}
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.waterspirit"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.waterspirit"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	
	public static class QuestInventory extends Quest {

		public QuestInventory(Deity d) {
			super(d, 1, 3, false, "Evoker", "Witch", "Vindicator");
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof LivingDeathEvent) {
				LivingDeathEvent event = (LivingDeathEvent)e;
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					UUID uuid = null;
					for (UUID uu : deity.getQuests().keySet()) {
						if (deity.getQuest(uu) == this) {
							uuid = uu;
						}
					}
					if (event.getSource().getTrueSource().getUniqueID().equals(uuid)) {
						if (event.getEntityLiving() instanceof EntityEvoker) {
							this.setCondition("Evoker", true);
							
						}
						if (event.getEntityLiving() instanceof EntityWitch) {
							this.setCondition("Witch", true);
							
						}
						
						if (event.getEntityLiving() instanceof EntityVindicator) {
							this.setCondition("Vindicator", true);
							
						}
					}
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.accessinventory"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.accessinventory"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	public static class QuestKraken extends Quest {

		public QuestKraken(Deity d) {
			super(d, 1, 4, false, "KT");
			
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof PlayerTickEvent) {
				PlayerTickEvent event = (PlayerTickEvent)e;
				if (deity.hasInventory(event.player.getPersistentID())) {
					DivineInventory inventory = deity.getInventory(event.player.getPersistentID());
					for (int i = 0; i < inventory.getSizeInventory(); i++) {
						ItemStack item = inventory.getStackInSlot(i);
						if (item != ItemStack.EMPTY) {
							if (item.getItem() == this.deity.getQuestItem("kraken_tentacle")) {
								this.setCondition("KT", true);
							}
						}
					}
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.kraken"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.kraken"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	public static class QuestTeleport extends Quest {

		public int counter;
		
		public QuestTeleport(Deity d) {
			super(d, 1, 5, false, "Endermen");
			counter = 10;
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof LivingDeathEvent) {
				LivingDeathEvent event = (LivingDeathEvent)e;
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					UUID uuid = null;
					for (UUID uu : deity.getQuests().keySet()) {
						if (deity.getQuest(uu) == this) {
							uuid = uu;
						}
					}
					if (event.getSource().getTrueSource().getUniqueID().equals(uuid)) {
						if (event.getEntityLiving() instanceof EntityEnderman) {
							this.counter++;
						}
					}
				}
			}
			if (counter >= 5) {
				this.setCondition("Endermen", true);
			}
		}
		
		@Override
		public void fromNBT(NBTTagCompound comp) {
				// TODO Auto-generated method stub
			super.fromNBT(comp);
			counter = comp.getInteger("Counter");
		}
		
		@Override
		public NBTTagCompound toNBT() {
			NBTTagCompound comp = super.toNBT();
			comp.setInteger("Counter", counter);
			return comp;
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.teleport", counter));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.waterteleport"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	public static class QuestShield extends Quest {

		public QuestShield(Deity d) {
			super(d, 1, 6, false, "Guard");
			
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof LivingDeathEvent) {
				LivingDeathEvent event = (LivingDeathEvent)e;
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					UUID uuid = null;
					for (UUID uu : deity.getQuests().keySet()) {
						if (deity.getQuest(uu) == this) {
							uuid = uu;
						}
					}
					if (event.getSource().getTrueSource().getUniqueID().equals(uuid)) {
						if (event.getEntityLiving() instanceof EntityGuardian) {
							this.setCondition("Guard", true);
						}
					}
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.shield"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.questshield"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	public static class QuestKill extends Quest {

		public QuestKill(Deity d) {
			super(d, 1, 7, false, "Guard");
			
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof LivingDeathEvent) {
				LivingDeathEvent event = (LivingDeathEvent)e;
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					UUID uuid = null;
					for (UUID uu : deity.getQuests().keySet()) {
						if (deity.getQuest(uu) == this) {
							uuid = uu;
						}
					}
					if (event.getSource().getTrueSource().getUniqueID().equals(uuid)) {
						if (event.getEntityLiving() instanceof EntityElderGuardian) {
							this.setCondition("Guard", true);
						}
					}
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.kill"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.questkill"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
	public static class QuestDimension extends Quest {

		public QuestDimension(Deity d) {
			super(d, 1, 8, false, "End");
			
			
		}

		@Override
		public void onEvent(Event e) {
			if (e instanceof PlayerTickEvent) {
				if (((PlayerTickEvent) e).player.dimension == 1) {
					this.setCondition("End", true);
				}
			}
		}

		@Override
		public void giveQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.givequest.dimension"));
		}

		@Override
		public void checkConditions(EntityLivingBase devotee) {
			
		}

		@Override
		public void completeQuest(EntityLivingBase devotee) {
			devotee.sendMessage(new TextComponentTranslation("deity.leviathan.questdimension"));
		}

		@Override
		public boolean canGiveQuest(EntityLivingBase devotee) {
			
			return true;
		}
		
	}
	
}