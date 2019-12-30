package com.gm910.magicmod.handling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.blocks.te.TileEntityPentacle;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.entity.classes.demons.EntityDemon;
import com.gm910.magicmod.entity.classes.demons.IDemonBoundPos;
import com.gm910.magicmod.entity.classes.demons.IDemonWithOwner;
import com.gm910.magicmod.handling.util.MagicEvent;
import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.init.ItemInit;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;
import com.gm910.magicmod.magicdamage.EntityDemonLightning;
import com.gm910.magicmod.magicdamage.EntitySpellLightning;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class MagicHandler {

	
	public abstract static class Spell {
		public static final List<Spell> spells = new ArrayList<Spell>();
		protected String incantation;
		protected ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		protected Predicate<? super ItemStack> pred = null;
		protected int power;
		protected String formatted = "";
		protected TextFormatting color = TextFormatting.WHITE;
		
		
		public static void registerSpell(Spell spell) {
			System.out.println(spell + " registered");
			spells.add(spell);
			
		}
		
		public Spell setFormatted(String formatted) {
			this.formatted = formatted;
			return this;
		}
		
		public Spell setColor(TextFormatting form) {
			this.color = form;
			return this;
		}
		
		public String getFormatted() {
			return color + formatted + TextFormatting.RESET;
		}
		
		public String getColorlessFormatted() {
			return formatted;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return formatted;
		}
		
		
		
		@Nullable
		public Predicate<? super ItemStack> getPredicate() {
			return pred;
		}
		
		public Spell setPredicate(Predicate<? super ItemStack> pred) {
			this.pred = pred;
			return this;
		}
		
		public Spell(String incantation, ItemStack...triggerItems) {
			this(incantation, null, triggerItems);
		}
		
		public Spell setPower(int mana) {
			this.power = mana;
			return this;
		}
		
		public int getPower() {
			return power;
		}
		
		public Spell(String incantation, @Nullable Predicate<? super ItemStack> pred, ItemStack...triggerItems) {
			this.incantation = incantation;
			this.pred = pred;
			if (triggerItems.length > 0)
			for (ItemStack e : triggerItems) items.add(e);
			this.formatted = "";
			String[] fs = incantation.split(" ");
			for (int i = 0; i < fs.length; i++) {
				fs[i] = Character.toUpperCase(fs[i].charAt(0)) + fs[i].substring(1);
				this.formatted += fs[i] + " ";
			}
			this.color = TextFormatting.LIGHT_PURPLE;
			this.formatted = this.formatted.trim();
		}
		
		public String getIncantation() {
			return incantation;
		}
		
		public void killItems(ArrayList<EntityItem> items) {
			items.forEach(it -> it.setDead());
		}
		
		public final boolean runSpell(World world, BlockPos pentacle, EntityLivingBase activ, ArrayList<ItemStack> items) {
			EntityLivingBase activator = activ;
			if (activator == null) {
				activator = CommonProxy.getServer().getPlayerList().getPlayers().get(world.rand.nextInt(CommonProxy.getServer().getCurrentPlayerCount()));
				System.out.println(activator);
			}
			if (MinecraftForge.EVENT_BUS.post(new MagicEvent(activator, new ServerPos(pentacle, world.provider.getDimension()), world, this))) {
				if (activator != null) {
					
					activator.sendMessage(new TextComponentTranslation("spell.canceled"));
				}
				System.out.println("cannotRun");
				return false;
			}
			boolean canRun = true;
			if (power > 0) {
				canRun = false;
				System.out.println("Power: " + power);
				if (world.getTileEntity(pentacle) instanceof TileEntityPentacle) {
					System.out.println("te ex");
					TileEntityPentacle penta = (TileEntityPentacle) world.getTileEntity(pentacle);
					if (penta.getEnergyStored() >= power) {
						penta.extractEnergy(power);
						canRun = true;
					} else {
						
						int diff = (int)((power-penta.getEnergyStored())/3.0);
						if (activator != null) activator.sendMessage(new TextComponentTranslation("spell.reboundmessage", penta.getEnergyStored(), power, diff*3));
						
						penta.extractEnergy(power);
						if (activator != null) {
							if (activator.getHealth() < diff) {
								
								canRun = false;
							} else {
								
								canRun = true;
							}
							activator.attackEntityFrom(DamageSourceMystic.causeReboundSpellDamage(activator, pentacle, this), diff);
						}
					}
				}
			}
			if (canRun) {
				System.out.println("canRun");
				boolean orun = onRunSpell(world, pentacle, activator, items);
				if (orun) {
					
					return true;
				}
			}
			System.out.println("adsfjndgjjifgf");
			return false;
		}
		
		protected abstract boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator, ArrayList<ItemStack> items);
		
		public static boolean runByIncantation(String incantation, BlockPos pentacle, EntityLivingBase activator, World world, ArrayList<EntityItem> items, ArrayList<IInventory> inventories) {
			int ENT = 0;
			int INV = 1;
			Map<ItemStack, Integer> stacks = new HashMap<ItemStack, Integer>();
			Map<ItemStack, EntityItem> entities = new HashMap<ItemStack, EntityItem>();
			Map<ItemStack, IInventory> invents = new HashMap<ItemStack, IInventory>();
			Map<ItemStack, Integer> invenSlots = new HashMap<ItemStack, Integer>();
			
			Map<IInventory, ArrayList<Integer>> stacksToErase = new HashMap<IInventory, ArrayList<Integer>>();
			Map<ItemStack, Integer> shrinkBy = new HashMap<ItemStack, Integer>();
			ArrayList<EntityItem> entitiesToErase = new ArrayList<EntityItem>();
			
			items.forEach(d -> {
				stacks.put(d.getItem(), ENT);
				entities.put(d.getItem(), d);
				
			});
			inventories.forEach(inv -> {
				
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					if (inv.getStackInSlot(i) != null) {
						stacks.put(inv.getStackInSlot(i), INV);
						invents.put(inv.getStackInSlot(i), inv);
						invenSlots.put(inv.getStackInSlot(i), i);
					}
				}
			});
			System.out.println(stacks.keySet());
			
			for (Spell m : spells) {
				//System.out.println(m);
				String incanta = TextFormatting.getTextWithoutFormattingCodes(m.incantation.trim());
				incantation = TextFormatting.getTextWithoutFormattingCodes(incantation.trim());
				if (incanta.equalsIgnoreCase(incantation)) {
					System.out.println("Found matching spell");
					boolean cond = true;
					if (!stacks.isEmpty()) {
						if (m.pred != null) {
							for (ItemStack it : stacks.keySet()) {
								if (!m.pred.test(it)) {
									//activator.sendMessage(new TextComponentString(it.getItem().getDisplayName() + " is invalid"));
									cond = false;
								}
							}
						}
						Map<Item, Integer> map = new HashMap<Item, Integer>();
						Map<Item, Boolean> map2 = new HashMap<Item, Boolean>();

						
						m.items.forEach(it -> map.put(it.getItem(), it.getCount()));
						m.items.forEach(it -> map2.put(it.getItem(), false));
						
						for (ItemStack item : stacks.keySet()) {
							System.out.println(item);
							Item e = item.getItem();
							if (map.containsKey(e) && map.get(e) == item.getCount()) {
								map2.put(e, true);
								shrinkBy.put(item, map.get(e));
								if (invents.containsKey(item)) {
									if (invenSlots.containsKey(item)) {
										if (stacksToErase.containsKey(invents.get(item)) ? stacksToErase.get(invents.get(item)).contains(invenSlots.get(item)) : false) {
											stacksToErase.get(invents.get(item)).add(invenSlots.get(item));
										} else {
											ArrayList<Integer> intlist = new ArrayList<Integer>();
											intlist.add(invenSlots.get(item));
											stacksToErase.put(invents.get(item), intlist);
										}
									}
								}
								if (entities.containsKey(item) ? !entitiesToErase.contains(entities.get(item)) : false) {
									entitiesToErase.add(entities.get(item));
								}
							}
						}
						for (boolean b : map2.values()) {
							cond = cond && b;
						}
					} else {
						if (!m.items.isEmpty()) {
							cond = false;
						}
					}
					if (cond) {
						
						if (m.runSpell(world, pentacle, activator, new ArrayList<ItemStack>(stacks.keySet()))) {
							for (IInventory inv : stacksToErase.keySet()) {
								for (Integer in : stacksToErase.get(inv)) {
									ItemStack s = inv.getStackInSlot(in);
									s.shrink(shrinkBy.getOrDefault(inv.getStackInSlot(in), 0));
									if (s.getCount() <= 0) {
										inv.setInventorySlotContents(in, ItemStack.EMPTY);
									} else {
										inv.setInventorySlotContents(in, s);
									}
								}
							}
							for (EntityItem en : entitiesToErase) {
								ItemStack s = en.getItem();
								s.shrink(shrinkBy.getOrDefault(shrinkBy, 0));
								en.setItem(s);
								if (s.getCount() <= 0) {
									en.setDead();
								}
							}
							if (activator != null) {
								if (world.getTileEntity(pentacle) instanceof TileEntityPentacle) {
									((TileEntityPentacle)world.getTileEntity(pentacle)).setIncantation("");
								}
								activator.sendMessage(new TextComponentTranslation("spell.success", TextFormatting.GREEN));
							}
							return true;
						} else {
							if (activator != null) {
								activator.sendMessage(new TextComponentTranslation("spell.conditionnotmet", TextFormatting.YELLOW));
							}
						}
					}
					else {
						if (activator != null) {
							activator.sendMessage(new TextComponentTranslation("spell.wrongitems", TextFormatting.RED));
						}
						return false;
					}
					break;
				}
			}
			return false;
		}
	}
	
	public static class PentacleInvokeDeity extends Spell {

		public Deity deity;
		
		public PentacleInvokeDeity(Deity deity) {
			super("deus " + deity.getUnlocName().getResourcePath(), deity.getRequiredItems().toArray(new ItemStack[] {}));
			this.deity = deity;
			this.setPower(300);
			this.setColor(TextFormatting.GOLD);
		}
		
		public Deity getDeity() {
			return deity;
		}

		@Override
		protected boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator,
				ArrayList<ItemStack> items) {
			ArrayList<EntityItem> enItems = new ArrayList<EntityItem>();
			items.forEach(stack -> enItems.add(new EntityItem(world, pentacle.getX(), pentacle.getY(), pentacle.getZ(), stack)));
			if (deity.canSummon(world, activator, pentacle, enItems)) {
				deity.summon(world, activator, pentacle, enItems);
				
				return true;
			} else {
				
			}
			return false;
		}
		
	}
	
	public static class PentacleSummon extends Spell {

		private String demonDomain;
		private String demonType;
		
		public PentacleSummon(String incantation, String demonType, ItemStack...triggerItems) {
			this("daemon " + incantation, new ResourceLocation(MagicMod.MODID, demonType), triggerItems);
		}
		
		public PentacleSummon(String incantation, ResourceLocation demonType, ItemStack...triggerItems) {
			this(incantation, demonType, null, triggerItems);
			
		}
		
		public PentacleSummon(String incantation, ResourceLocation demonType, @Nullable Predicate<? super ItemStack> pred, ItemStack...triggerItems) {
			super(incantation, pred, triggerItems);
			this.demonDomain = demonType.getResourceDomain();
			this.demonType = demonType.getResourcePath();
			this.setColor(TextFormatting.DARK_RED);
		}
		
		public PentacleSummon(String incantation, String demonType, @Nullable Predicate<? super ItemStack> pred, ItemStack...triggerItems) {
			this(incantation, new ResourceLocation(MagicMod.MODID, demonType), pred, triggerItems);
		}
		
		public ResourceLocation getDemonType() {
			return new ResourceLocation(demonDomain, demonType);
		}
		
		@Override
		public String toString() {
			return "Summon " + demonType.toString().toUpperCase();
		}

		@Override
		protected boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator,
				ArrayList<ItemStack> items) {
			if (world.provider.getDimension() == DimensionInit.DIMENSION_SHEOL) {
				activator.sendMessage(new TextComponentTranslation("spell.notinsheol"));
			}
			
			TileEntityPentacle pe = ((TileEntityPentacle)world.getTileEntity(pentacle));
			if (pe != null) {
				if (pe.demon != null) {
					return false;
				}
			}
			
			System.out.println("Summoning demon " + getDemonType() + " sdfdgf");
			Entity d = EntityList.createEntityByIDFromName(new ResourceLocation(demonDomain, demonType), world);
			if (d != null) {
				if (d instanceof IDemonBoundPos) {
					((IDemonBoundPos) d).setBoundOrigin(pentacle);
					((IDemonBoundPos) d).setBoundWorld(world);
				} if (d instanceof IDemonWithOwner) {
					((IDemonWithOwner) d).setOwner(activator);
				}
				
				world.addWeatherEffect(new EntityDemonLightning(world, (EntityDemon)d, pentacle.getX(), pentacle.getY(), pentacle.getZ(), true, false));
				d.setPosition(pentacle.getX()+0.5, pentacle.getY(), pentacle.getZ()+0.5);
				((EntityDemon)d).setSummoner(activator);
				world.spawnEntity(d);
				return true;
			} else {
				activator.sendMessage(new TextComponentTranslation("spell.dysfunctional"));
				return false;
			}
		}
	}

	public static class SacrificeSpell extends Spell {

		public SacrificeSpell(String incantation, Predicate<? super ItemStack> pred, ItemStack[] triggerItems) {
			super(incantation, pred, triggerItems);
		}
		
		public SacrificeSpell(String incantation, ItemStack[] triggerItems) {
			this(incantation, null, triggerItems);
		}

		@Override
		protected boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator,
				ArrayList<ItemStack> items) {
			
			return false;
		}
		
	}
	
	public static void initSpells() {
		Spell.registerSpell(SUMMON_FURNACE);
		Spell.registerSpell(SUMMON_IMP);
		Spell.registerSpell(SUMMON_CACODEMON);
		Spell.registerSpell(SUMMON_AZRAELITE);
		Spell.registerSpell(SUMMON_GOETURGE);
		Spell.registerSpell(SUMMON_GATEKEEPER);
		Spell.registerSpell(DISMISS_DEMON);
		Spell.registerSpell(DESTROY_PENTACLE);
		Spell.registerSpell(BLOOD_ORB);
		for (Deity deity : Deities.deities) {
			Spell.registerSpell(new PentacleInvokeDeity(deity));
		}
	}

	public static final Spell SUMMON_FURNACE = new PentacleSummon("fornacis", "forge_demon", new ItemStack(Items.MAGMA_CREAM, 1));
	public static final Spell SUMMON_IMP = new PentacleSummon("belli", "imp", new ItemStack(Items.GOLDEN_SWORD, 1));
	public static final Spell SUMMON_CACODEMON = new PentacleSummon("asyli", "cacodemon", new ItemStack(Items.SHIELD, 1));
	public static final Spell SUMMON_GOETURGE = new PentacleSummon("goetiae", "goeturge", new ItemStack(Item.getItemFromBlock(Blocks.ENCHANTING_TABLE), 1));
	public static final Spell SUMMON_AZRAELITE = new PentacleSummon("mortis", "azraelite", new ItemStack(Items.NETHER_STAR, 1));
	public static final Spell SUMMON_GATEKEEPER = new PentacleSummon("ianuae", "gatekeeper", it -> it.hasTagCompound(), new ItemStack(ItemInit.DEMON_ORB, 1));
	
	public static final Spell DESTROY_PENTACLE = new Spell("daemon destruo") {
		@Override
		public boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator,
				ArrayList<ItemStack> items) {
			TileEntityPentacle te = (TileEntityPentacle)world.getTileEntity(pentacle);
			te.demonDismiss();
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					world.setBlockToAir(new BlockPos(te.getPos().getX() + x, te.getPos().getY(), te.getPos().getZ()+z));
				}
			}
			return true;
		}
	};
	
	public static final Spell BLOOD_ORB = new Spell("daemon ego", it -> it.hasTagCompound(), new ItemStack(ItemInit.BLOOD_ORB, 1)) {
		@Override
		public boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator,
				ArrayList<ItemStack> items) {
			System.out.println("You can do it?");
			if (world.isRemote) {
				System.out.println("remote world");
				return false;
			} else {
				System.out.println("Nonremote world");
			}
			TileEntityPentacle te = (TileEntityPentacle)world.getTileEntity(pentacle);
			int index = 0;
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getItem() == ItemInit.BLOOD_ORB) {
					index = i;
				}
			}
			te.setDemon_orb(items.get(index).copy());
			
			world.addWeatherEffect(new EntitySpellLightning(world, DamageSourceMystic.castSpell(activator, pentacle, this), pentacle.getX(), pentacle.getY(), pentacle.getZ(), true, false));
			items.get(index).shrink(1);
			return true;
		}
	};
	
	public static final Spell DISMISS_DEMON = new Spell("daemon amando") {
		@Override
		public boolean onRunSpell(World world, BlockPos pentacle, EntityLivingBase activator,
				ArrayList<ItemStack> items) {
			TileEntityPentacle tileEntity = (TileEntityPentacle)world.getTileEntity(pentacle);
			tileEntity.demonDismiss();
			return true;
		}
	};

	
}
