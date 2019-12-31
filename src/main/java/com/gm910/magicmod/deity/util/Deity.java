package com.gm910.magicmod.deity.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.MagicMod.EnumKey;
import com.gm910.magicmod.blocks.BlockStatue;
import com.gm910.magicmod.blocks.BlockStatue.EnumTypeStatue;
import com.gm910.magicmod.blocks.BlockStatue.TileEntityStatue;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.entities.EntityDeity;
import com.gm910.magicmod.deity.entities.EntityDeityProjection;
import com.gm910.magicmod.deity.entities.RenderDeityBase;
import com.gm910.magicmod.handling.util.DeityQuestEvent;
import com.gm910.magicmod.init.BlockInit;
import com.gm910.magicmod.items.IDivineItem;
import com.gm910.magicmod.items.ItemGodQuest;
import com.gm910.magicmod.items.ItemStatue;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;
import com.gm910.magicmod.proxy.CommonProxy;
import com.gm910.magicmod.world.Teleport;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Deity implements INBTSerializable<NBTTagCompound>, IInventoryChangedListener, IMerchant {
	private ResourceLocation unlocName;
	private String displayName;
	public final String domain;
	private ArrayList<ItemStack> requiredItems = new ArrayList<ItemStack>();
	private EntityLivingBase avatar;
	private boolean warlike;
	//private ServerPos altarPos;
	private EntityPlayer opposition;
	private EntityPlayer bester;
	private com.gm910.magicmod.deity.util.ServerPos prevPos = null;
	public MinecraftServer server;
	private Map<UUID, Integer> favorPerPlayer = new HashMap<UUID, Integer>();
	private Map<UUID, Double> worship = new HashMap<UUID, Double>();
	private int timeActive = 800;
	private Status status = Status.ACTIVE;
	private UUID supplanter;
	protected Random random = new Random();
	private ArrayList<Class<? extends Quest>> requiredQuests = new ArrayList<Class<? extends Quest>>();
	private Map<UUID, Quest> quests = new HashMap<UUID, Quest>();
	private ArrayList<Class<? extends EntityLivingBase>> naturallyDevout = new ArrayList<Class<? extends EntityLivingBase>>();
	private com.gm910.magicmod.deity.entities.RenderDeityBase overrideRender = null;
	protected ResourceLocation textureOverride = new ResourceLocation(MagicMod.MODID + ":textures/entity/goeturge.png");
	private ArrayList<EntityLivingBase> projections = new ArrayList<EntityLivingBase>();
	private com.gm910.magicmod.blocks.BlockStatue statue;
	private ArrayList<ServerPos> statues = new ArrayList<ServerPos>();
	private ArrayList<ServerPos> villageStatues = new ArrayList<ServerPos>();
	public Pronoun pro = Pronoun.THEY;
	private ArrayList<UUID> priests = new ArrayList<UUID>();
	private ArrayList<ServerPos> villReligion = new ArrayList<ServerPos>();
	private Map<UUID, Double> worshipSubtraction = new HashMap<UUID, Double>();
	public final ArrayList<NBTTagCompound> deadDevouts = new ArrayList<NBTTagCompound>();
	public final Map<UUID, Integer> deadPlayerFavor = new HashMap<UUID, Integer>();
	public final Map<UUID, NBTTagCompound> devoutData = new HashMap<UUID, NBTTagCompound>();
	public final Map<String, INBTSerializable<NBTTagCompound>> otherData = new HashMap<String, INBTSerializable<NBTTagCompound>>();
	//protected final ArrayList<Class<? extends INBTSerializable<NBTTagCompound>>> otherDataTypes = new ArrayList<Class<? extends INBTSerializable<NBTTagCompound>>>();
	public SpecialInteger ticksTillSunrise = new SpecialInteger(0);
	public UUIDList devoutsInHolyLand = new UUIDList(null);
	public AbilityList abilities = new AbilityList();
	public UUIDtoInventory godInventory = new UUIDtoInventory(null);
	protected int INVENTORY_SIZE = 54;
	public SpecialUUID customer = new SpecialUUID(null);
	public MerchantRecipeList trades;
	public final boolean canVillageFollow;
	public SpecialUUID summoner = new SpecialUUID(null);
	public Map<String, Item> questItems = new HashMap<String, Item>();
	public Map<String, NBTType> devoutTags = new HashMap<String, NBTType>();
	
	public static enum NBTType {
		COMPOUND(NBTTagCompound.class),
		LIST(NBTTagList.class);
		
		private Class<? extends NBTBase> clazz;
		private NBTType(Class<? extends NBTBase> clazz) {
			this.clazz = clazz;
			
		}
		
		public Class<? extends NBTBase> getClazz() {
			return clazz;
		}
		
		public NBTBase createTag() {
			Constructor<? extends NBTBase> con = null;
			try {
				con = clazz.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
			} catch (SecurityException e) {
				
				e.printStackTrace();
			}
			
			if (con != null) {
				try {
					return con.newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					
					e.printStackTrace();
				}
			}
			return null;
			
		}
		
	}
	
	@Override
	public String toString() {
		return this.displayName;
	}
	
	/**
	 * 
	 * @param rl the location to store this deity's data
	 * @param displayName this deity's name shown in texts
	 * @param warlike whether this deity will fight
	 * @param required preferrably 4 items to use to summon the deity
	 */
	public Deity(ResourceLocation rl, String displayName, boolean warlike, boolean canVillageFollow, String domain, ItemStack...required) {
		this(rl, displayName, warlike, canVillageFollow, domain);
		ArrayList<ItemStack> requiredItems = new ArrayList<ItemStack>();
		for (ItemStack item : required) {
			requiredItems.add(item);
		}
		this.requiredItems = requiredItems;
		status = Status.ACTIVE;
	
		addSpecialData("ticksTillSunrise", ticksTillSunrise);
		addSpecialData("devoutsInHolyLand", devoutsInHolyLand);
		addSpecialData("abilities", abilities);
		addSpecialData("godInventory", godInventory);
		addSpecialData("customer", customer);
		addSpecialData("summoner", summoner);
	}
	
	/**
	 * 
	 * @param rl the location to store this deity's data
	 * @param displayName this deity's name shown in texts
	 * @param warlike whether this deity fights
	 */
	public Deity(ResourceLocation rl, String displayName, boolean warlike, boolean canVillageFollow, String domain) {
		unlocName = rl;
		this.displayName = displayName;
		this.warlike = warlike;
		status = Status.ACTIVE;
		this.domain = (new TextComponentTranslation("deity.domain." + domain)).getFormattedText();
		this.canVillageFollow = canVillageFollow;
	}
	
	@Override
	public void onInventoryChanged(IInventory invBasic) {
		System.out.println("Divine inventory changed " + invBasic.getStackInSlot(0));
		
		for (Entry<UUID, DivineInventory> bas : godInventory.map.entrySet()) {
			
			if (bas.getValue().equals(invBasic)) {
				
				DivineInventory basic = (DivineInventory) invBasic;
				onInventoryChange(basic, bas.getKey());
			}
		}
	}
	
	public void onInventoryChange(DivineInventory inventory, UUID devout) {
	}
	
	public Item getQuestItem(String name) {
		return questItems.get(name);
	}
	
	
	public Deity addInventory(UUID devout) {
		addInventory(devout, true);
		return this;
	}
	
	public Deity addInventory(UUID devout, boolean accessibility) {
		DivineInventory basic = new DivineInventory((new TextComponentTranslation("deity.chestof", domain)).getFormattedText(), devout, this, true, INVENTORY_SIZE);
		basic.addInventoryChangeListener(this);
		this.godInventory.put(devout, basic);
		this.godInventory.setAccessibility(devout, accessibility);
		return this;
	}
	
	public boolean hasInventory(UUID devout) {
		return this.godInventory.getMap().containsKey(devout);
	}
	
	public Deity addQuestItem(String name) {
		this.questItems.put(name, new ItemGodQuest(name, this));
		return this;
	}
	
	public Deity addQuestItem(ItemGodQuest item) {
		this.questItems.put(item.getRegistryName().getResourcePath(), item);
		return this;
	}
	
	public boolean canAccessInventory(UUID devout) {
		return this.godInventory.canAccess(devout);
	}
	
	public DivineInventory getInventory(UUID devout) {
		DivineInventory inv = this.godInventory.get(devout);
		if (inv == null && this.godInventory.canAccess(devout)) {
			DivineInventory basic = new DivineInventory((new TextComponentTranslation("deity.chestof", domain)).getFormattedText(), devout, this, true, INVENTORY_SIZE);
			basic.addInventoryChangeListener(this);
			this.godInventory.put(devout, basic);
			return this.godInventory.get(devout);
		}
		
		return this.godInventory.canAccess(devout) ? inv : null;
	}
	
	public Deity setInventoryAccessible(UUID devout, boolean accessible) {
		godInventory.setAccessibility(devout, accessible);
		return this;
	}
	
	public void openInventory(EntityPlayer player, UUID invOwner) {
		if (hasInventory(invOwner) && this.canAccessInventory(invOwner)) {
			System.out.println(this.godInventory.get(invOwner).getName());
			player.displayGUIChest(this.godInventory.get(invOwner));
		}
	}
	
	/**
	 * Gets extra data stored for a devout
	 * @param uuid the devout uuid
	 * @return the data as a compound
	 */
	public NBTTagCompound getDevoutData(UUID uuid) {
		if (!this.isDevout(uuid)) return null;
		if (this.devoutData.get(uuid) == null) {
			this.resetDevoutData(uuid);
		}
		return this.devoutData.get(uuid);
	}
	
	public Deity registerDevoutDataType(String key, NBTType type) {
		this.devoutTags.put(key, type);
		if (!this.devoutData.isEmpty()) {
			for (UUID uu : this.devoutData.keySet()) {
				this.getDevoutData(uu).setTag(key, type.createTag());
			}
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NBTBase> T getDevoutDataTag(UUID uu, String key) {
		return this.getDevoutData(uu) != null ? (T) this.getDevoutData(uu).getTag(key) : null;
	}
	
	public <T extends NBTBase> T getDevoutDataTag(UUID uu, String key, Class<? extends T> clazz) {
		return this.<T>getDevoutDataTag(uu, key);
	}
	
	/**
	 * resets the devout's personal data
	 * @param uuid the devout uuid
	 * @return
	 */
	public Deity resetDevoutData(UUID uuid) {
		NBTTagCompound comp = new NBTTagCompound();
		comp.setTag("Abilities", new NBTTagList());
		comp.setTag("FinishedQuests", new NBTTagList());
		if (!devoutTags.isEmpty()) {
			for (Entry<String, NBTType> entry : devoutTags.entrySet()) {
				comp.setTag(entry.getKey(), entry.getValue().createTag());
			}
		}
		this.devoutData.put(uuid, new NBTTagCompound());
		return this;
	}
	
	public UUIDList getDevoutsInHolyLand() {
		return devoutsInHolyLand;
	}
	
	public Deity putDevoutInHolyLand(EntityLivingBase devout) {
		this.devoutsInHolyLand.add(devout.getUniqueID());
		return this;
	}
	
	public Deity removeDevoutFromHolyLand(EntityLivingBase devout) {
		this.devoutsInHolyLand.remove(devout.getUniqueID());
		return this;
	}
	
	public boolean isInHolyLand(UUID devout) {
		return this.devoutsInHolyLand.contains(devout);
	}
	
	public void devoutInHolyLand(EntityLivingBase base,LivingEvent event) {
		
	}
	
	public void enemyInHolyLand(EntityLivingBase base, LivingEvent event) {
		
	}
	
	public void normalInHolyLand(EntityLivingBase base, LivingEvent event) {
		
	}
	
	public void inHolyLand(EntityLivingBase base, LivingEvent event) {
		
	}
	
	public int getBlessingColor() {
		return 0xFFFF88;
	}
	
	public int getCurseColor() {
		return 0x440000;
	}
	
	public void bless(EntityLivingBase base, PotionEffect blessing, LivingEvent event) {
		
	}
	
	public void curse(EntityLivingBase base, PotionEffect curse, LivingEvent event) {
		if (base != null && !(event instanceof LivingAttackEvent) && !(event instanceof LivingDamageEvent) && !(event instanceof LivingHurtEvent)) {
			base.attackEntityFrom(DamageSourceMystic.castGodCurse(this), 1);
		}
	}
	
	/**
	 * returns whether the devout has that ability
	 * @param devout the devout's uuid
	 * @param ability the ability string name
	 * @return
	 */
	public boolean doesDevoutHaveAbility(UUID devout, String ability) {
		if (!this.isDevout(devout)) return false;
		NBTTagCompound comp = devoutData.get(devout);
		if (comp == null) {
			this.resetDevoutData(devout);
			return false;
		}
		NBTTagList abilities = comp.getTagList("Abilities", NBT.TAG_STRING);
		for (int i = 0; i < abilities.tagCount(); i++) {
			if (abilities.getStringTagAt(i).equals(ability)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDevoutFinishedWithQuest(UUID devout, Class<? extends Quest> quest) {
		if (!this.isDevout(devout)) return false;
		NBTTagCompound comp = devoutData.get(devout);
		if (comp == null) {
			this.resetDevoutData(devout);
			
			return false;
		}
		NBTTagList abilities = comp.getTagList("FinishedQuests", NBT.TAG_COMPOUND);
		for (int i = 0; i < abilities.tagCount(); i++) {
			//System.out.println(abilities.getCompoundTagAt(i).getString("String"));
			//System.out.println(quest.toGenericString());
			if (abilities.getCompoundTagAt(i).getString("String").trim().equalsIgnoreCase(quest.toGenericString().trim())) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public Deity putQuestStatus(UUID devout, Class<? extends Quest> quest) {
		
		if (!this.isDevout(devout)) return this;
		
		NBTTagCompound comp = devoutData.get(devout);
		if (comp == null) {
			this.resetDevoutData(devout);
			comp = devoutData.get(devout);
		}
		NBTTagList abilities = comp.getTagList("FinishedQuests", NBT.TAG_COMPOUND);
		NBTTagCompound cmsdoof = new NBTTagCompound();
		cmsdoof.setString("String", quest.toGenericString());
		abilities.appendTag(cmsdoof);
		comp.setTag("FinishedQuests", abilities);
		devoutData.put(devout, comp);
		return this;
	}
	
	/**
	 * resets all the data of the deity
	 * @return
	 */
	public Deity reset() {
		avatar = null;
		opposition = null;
		bester = null;
		server = null;
		favorPerPlayer.clear();
		worship.clear();
		timeActive = 800;
		status = Status.ACTIVE;
		supplanter = null;
		quests.clear();
		projections.clear();
		statues.clear();
		villageStatues.clear();
		priests.clear();
		villReligion.clear();
		worshipSubtraction.clear();
		deadDevouts.clear();
		deadPlayerFavor.clear();
		this.devoutData.clear();
		
		return this;
	}
	
	public Block getLightningBlock() {
		return Blocks.GLOWSTONE;
	}

	
	/**
	 * Pronouns to refer to deities
	 * @author borah
	 *
	 */
	public static enum Pronoun {
		THEY("they", "them", "their", "theirs", "deity", true),
		THEY_PLURAL("they_plural", "them_plural", "their_plural", "theirs_plural", "we", "us", "our", "ours", "collective", true),
		HE("he", "him", "his", "his_adj", "god", false),
		SHE("she", "her_ob", "her", "hers", "goddess", false);
		
		private final String sub;
		private final String ob;
		private final String pos;
		private final String posAdj;
		private final String pers;
		private final String persOb;
		private final String persPos;
		private final String persPosAdj;
		private final String reflex;
		public final boolean conjPlural;
		private final String noun;
		//public final boolean isPlural;
		//TODO
		
		
		private Pronoun(String sub, String ob, String pos, String posAdj, String pers, String persOb, String persPos, String persPosAdj, String noun, boolean conjPlural) {
			this.sub = sub;
			this.ob = ob;
			this.pos = pos;
			this.posAdj = posAdj;
			this.pers = pers;
			this.persOb = persOb;
			this.persPos = persPos;
			this.persPosAdj = persPosAdj;
			this.conjPlural = conjPlural;
			this.noun = noun;
			this.reflex = ( conjPlural ? "selves" : "self");
		}
		
		public String getSub() {
			return  (new TextComponentTranslation("deity." + sub)).getFormattedText();
		}
		public String getOb() {
			return (new TextComponentTranslation("deity." + ob)).getFormattedText();
		}
		public String getPos() {
			return (new TextComponentTranslation("deity." + pos)).getFormattedText();
		}
		public String getPosAdj() {
			return (new TextComponentTranslation("deity." + posAdj)).getFormattedText();
		}
		public String getPers() {
			return (new TextComponentTranslation("deity." + pers)).getFormattedText();
		}
		public String getPersOb() {
			return (new TextComponentTranslation("deity." + persOb)).getFormattedText();
		}
		public String getPersPos() {
			return (new TextComponentTranslation("deity." + persPos)).getFormattedText();
		}
		public String getPersPosAdj() {
			return (new TextComponentTranslation("deity." + persPosAdj)).getFormattedText();
		}
		public String getReflex() {
			return (new TextComponentTranslation("deity." + ob + reflex)).getFormattedText();
		}
		public boolean isConjPlural() {
			return conjPlural;
		}
		public String getNoun() {
			return (new TextComponentTranslation("deity." + noun)).getFormattedText();
		}
		
		private Pronoun(String sub, String ob, String pos, String posAdj, String noun, boolean conjPlural) {
			this(sub, ob, pos, posAdj, "i", "me", "my", "mine", noun, conjPlural);
		}
		
		/**
		 * whether verb conjugations of pronoun are plural
		 * @return
		 */
		public boolean conjPlural() {
			return conjPlural;
		}

	}
	
	/**
	 * The deity's activity status
	 * @author borah
	 *
	 */
	public static enum Status {
		/**
		 * The deity is alive and well
		 */
		ACTIVE,
		/**
		 * The deity is dead and certain aspects of the world may be dysfunctional as a result
		 */
		DECEASED,
		/**
		 * The deity was replaced by a player who is now a god
		 */
		SUPPLANTED
	}
	
	/**
	 * return deity pronoun
	 * @return
	 */
	public Pronoun getPronoun() {
		return pro;
	}
	
	/**
	 * sets pronoun of deity
	 * @param pr pronoun
	 */
	public void setPronoun(Pronoun pr) {
		pro = pr;
	}
	
	/**
	 * adds a special form of data to the deity to serialize
	 * @param s the key to store the data under
	 * @param dataClass the object that holds the data
	 * @return the deity instance for easy construction
	 */
	@SuppressWarnings("unchecked")
	public <clazz extends INBTSerializable<NBTTagCompound>> Deity addSpecialData(String s, clazz dataClass) {
		this.otherData.put(s, dataClass);
		//this.otherDataTypes.add((Class<? extends INBTSerializable<NBTTagCompound>>) dataClass.getClass());
		return this;
	}
	
	/**
	 * returns the special form of data under that key cast to the class you give it
	 * @param s the key
	 * @param type the class to cast to
	 * @return the data object
	 */
	@SuppressWarnings("unchecked")
	public <clazz extends INBTSerializable<NBTTagCompound>> clazz getSpecialData(String s, Class<? extends clazz> type) {
		return (clazz) this.otherData.get(s);
	}
	
	/**
	 * returns the special form of data under that key
	 * @param s the key
	 * @return the data object
	 */
	@SuppressWarnings("unchecked")
	public INBTSerializable<NBTTagCompound> getSpecialData(String s) {
		return this.otherData.get(s);
	}
	
	public int getTicksTillSunrise() {
		return ticksTillSunrise.getValue();
	}
	public void setTicksTillSunrise(int ticksTillSunrise) {
		this.ticksTillSunrise.setValue(ticksTillSunrise);;
	}
	
	public NBTTagList getQuestStatuses(UUID devout) {
		if (!this.isDevout(devout)) return null;
		NBTTagCompound data = this.getDevoutData(devout);
		NBTTagList list = data.getTagList("FinishedQuests", NBT.TAG_STRING);
		return list;
	}
	
	/**
	 * returns the types of data that are being stored in this deity
	 * @return
	 */
	/*public ArrayList<Class<? extends INBTSerializable<NBTTagCompound>>> getOtherDataTypes() {
		return otherDataTypes;
	}*/
	
	/** returns an array of priests
	 * 
	 * @return
	 */
	public ArrayList<UUID> getPriests() {
		return priests;
	}
	
	/**
	 * adds a priest to the array
	 * @param priest
	 * @return
	 */
	public Deity addPriest(UUID priest) {
		this.priests.add(priest);
		return this;
	}

	
	/**
	 * returns how much worship is subtracted per tick
	 * @return
	 */
	public Map<UUID, Double> getWorshipSubtraction() {
		return worshipSubtraction;
	}
	
	/**
	 * puts a value of worship to subtract per tick
	 * @param u the uuid of the devout
	 * @param i the value (positive, please)
	 * @return
	 */
	public Deity putWorshipSubtraction(UUID u, Double i) {
		worshipSubtraction.put(u, i);
		return this;
	}
	
	/**
	 * returns the value of worship to subtract
	 * @param u uuid of the devout
	 * @return
	 */
	public double getWorshipSubtraction(UUID u) {
		return worshipSubtraction.get(u) == null ? 0 : worshipSubtraction.get(u);
	}
	
	/** returns array of villages with a religion of this deity
	 * 
	 * @return
	 */
	public ArrayList<ServerPos> getVillReligion() {
		return villReligion;
	}
	
	/**
	 * returns whether a statue was placed by a villager
	 * @param pos position of statue
	 * @return
	 */
	public boolean isVillageStatue(ServerPos pos) {
		return villageStatues.contains(pos);
	}
	
	/**
	 * adds an entity as a priest
	 * @param priest
	 * @return
	 */
	public Deity addPriest(EntityLivingBase priest) {
		if (!priests.contains(priest.getUniqueID())) {
			priest.sendMessage(new TextComponentTranslation("deity.nowpriestof", displayName));
		}
		addPriest(priest.getUniqueID());
		return this;
	}
	
	/**
	 * removes a priest
	 * @param priest
	 * @return
	 */
	public Deity removePriest(UUID priest) {
		this.priests.remove(priest);
		return this;
	}
	
	/**
	 * adds a player to a list of dead players so that the player's status under the deity can be stored but things relying on their worship stop working
	 * @param player
	 * @return
	 */
	public Deity setPlayerDead(UUID player) {
		this.deadPlayerFavor.put(player, this.getPoints(player));
		
		return this;
	}
	
	/**
	 * adds player to list of dead players so that player's status under deity is stored but things relying on their worship stop working
	 * @param player
	 * @return
	 */
	public Deity setPlayerDead(EntityPlayer player) {
		return setPlayerDead(player.getPersistentID());
	}
	
	/**
	 * returns the favor under deity that a dead player has
	 * @param player
	 * @return
	 */
	public int getDeadPlayerFavor(UUID player) {
		return this.deadPlayerFavor.get(player) == null ? 0 : this.deadPlayerFavor.get(player);
	}
	
	/**
	 * returns the favor under a deity that a dead player has
	 * @param player
	 * @return
	 */
	public int getDeadPlayerFavor(EntityPlayer player) {
		return getDeadPlayerFavor(player.getPersistentID());
	}
	
	/**
	 * "resurrects" a player by restoring them to the deity
	 * @param player
	 * @return
	 */
	public Deity resurrectPlayer(UUID player) {
		this.setPoints(player, this.getDeadPlayerFavor(player));
		this.deadPlayerFavor.remove(player);
		return this;
		
	}

	/**
	 * removes priest
	 * @param priest
	 * @return
	 */
	public Deity removePriest(EntityLivingBase priest) {
		if (priests.contains(priest.getUniqueID())) {
			priest.sendMessage(new TextComponentTranslation("deity.nolongerpriest", displayName));
		}
		removePriest(priest.getUniqueID());
		return this;
	}
	
	/**
	 * is player priest?
	 * @param priest
	 * @return
	 */
	public boolean isPriest(UUID priest) {
		return this.priests.contains(priest);
	}
	
	/**
	 * adds a dead devout non-player entity to the afterlifeeeee
	 * @param entity
	 * @return
	 */
	public Deity addDeadDevout(EntityLivingBase entity) {
		if (!this.isDevout(entity)) return this;
		if (entity instanceof EntityPlayer) {
			setPlayerDead((EntityPlayer)entity);
			return this;
		}
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Points", this.getPoints(entity));
		//tag.setDouble("Worship", this.getWorship(entity));
		tag.setTag("Entity", entity.serializeNBT());
		tag.setTag("Data", this.getDevoutData(entity.getPersistentID()));
		this.deadDevouts.add(tag);
		
		return this;
	}
	
	/**
	 * adds a dead non-player entity to the afterlifeeee using their serialized NBT
	 * @param tag
	 * @return
	 */
	public Deity addDeadDevout(NBTTagCompound tag) {
		UUID uu = tag.getUniqueId("UUID");
		if (uu == null) return this;
		if (!this.isDevout(uu)) return this;
		NBTTagCompound th = new NBTTagCompound();
		tag.setInteger("Points", this.getPoints(uu));
		//tag.setDouble("Worship", this.getWorship(uu));
		tag.setTag("Entity", tag);
		tag.setTag("Data", this.getDevoutData(uu));
		this.deadDevouts.add(tag);
		return this;
	}
	
	/**
	 * removes dead devout from the afterlifeeee and erases it from EXISTENCE
	 * @param uuid
	 * @return
	 */
	public Deity removeDeadDevout(UUID uuid) {
		deadDevouts.removeIf(tag -> tag.getCompoundTag("Entity").getUniqueId("UUID").equals(uuid));
		return this;
	}
	
	/**
	 * returns the serialized nbt tag data of a devout who is dead with that uuid
	 * @param uuid
	 * @return
	 */
	public NBTTagCompound getDeadDevout(UUID uuid) {
		for (NBTTagCompound tag : deadDevouts) {
			if (tag.getCompoundTag("Entity").getUniqueId("UUID").equals(uuid)) {
				return tag.getCompoundTag("Entity");
			}
		}
		return null;
	}
	
	/**
	 * returns the serialized entity data, the point data, the worship data, etc of a devout dead with that uuid
	 * @param uuid
	 * @return
	 */
	public NBTTagCompound getUnfilteredDeadDevout(UUID uuid) {
		for (NBTTagCompound tag : deadDevouts) {
			if (tag.getCompoundTag("Entity").getUniqueId("UUID").equals(uuid)) {
				return tag;
			}
		}
		return null;
	}
	
	/** 
	 * creates an entity of that dead devout in the afterlifeee
	 * @param world the world to spawn in
	 * @param uuid the devout
	 * @param shouldResurrect whether the devout should be considered resurrected and added to all necessary lists/maps
	 * @return
	 */
	public EntityLivingBase resurrectDeadDevout(World world, UUID uuid, boolean shouldResurrect) {
		NBTTagCompound possible = getDeadDevout(uuid);
		EntityLivingBase entity = null;
		if (possible != null) {
			entity = (EntityLivingBase) EntityList.createEntityFromNBT(possible.getCompoundTag("Entity"), world);
		}
		if (entity == null) {
			deadDevouts.remove(possible);
		}
		
		if (shouldResurrect && possible != null && entity != null) {
			this.deadDevouts.remove(possible);
			this.setPoints(uuid, possible.getInteger("Points"));
			this.devoutData.put(uuid, possible.getCompoundTag("Data"));
			//this.setWorship(uuid, possible.getInteger("Worship"));
		}
		return entity;
	}
	
	/**
	 * creats entities of all devouts in the afterlifeee
	 * @param world the world to spawn in
	 * @param shouldResurrect whether the devouts should be considered resurrected and added to all necessary lists/maps
	 * @return
	 */
	public ArrayList<EntityLivingBase> resurrectDeadDevouts(World world, boolean shouldResurrect) {
		ArrayList<NBTTagCompound> dvs = new ArrayList<NBTTagCompound>(deadDevouts);
		ArrayList<EntityLivingBase> ens = new ArrayList<EntityLivingBase>();
		for (NBTTagCompound tag : dvs) {
			ens.add(resurrectDeadDevout(world, tag.getCompoundTag("Entity").getUniqueId("UUID"), shouldResurrect));
		}
		ens.removeIf(e -> e == null);
		return ens;
	}
	
	/**
	 * whether this god is dead (has a {@link Status} of Deceased)
	 * @return
	 */
	public boolean isDead() {
		return status == Status.DECEASED;
	}
	
	/**
	 * kills the deity, and sets its status to DECEASED, notifying all players in a server
	 * @return
	 */
	public Deity killDeity() {
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentTranslation("deity.isdead", this.pro.getNoun(), this.domain, this.displayName));
		this.status = Status.DECEASED;
		return this;
	}
	
	public Deity registerAbility(Ability ability) {
		this.abilities.registerAbility(ability);
		return this;
	}
	
	public Deity registerAbility(String name) {
		this.abilities.registerAbility(name);
		return this;
	}
	
	public boolean hasAbility(UUID player, String ability) {
		return this.abilities.hasAbility(player, ability);
	}
	
	public Deity giveAbility(UUID player, String ability) {
		this.abilities.giveAbility(player, ability);
		return this;
	}
	
	public Deity removeAbility(UUID player, String ability) {
		this.abilities.removeAbility(player, ability);
		return this;
	}

	/**
	 * returns the Classes of quests that are required
	 * @return
	 */
	public ArrayList<Class<? extends Quest>> getRequiredQuests() {
		return requiredQuests;
	}
	
	/**
	 * returns the Block that represents the statue
	 * @return
	 */
	public BlockStatue getStatue() {
		return statue;
	}
	
	/**
	 * sets the block that represents the statue
	 * @param statue
	 */
	public void setStatue(BlockStatue statue) {
		this.statue = statue;
	}
	
	/**
	 * returns all entities that function as projections of the deity
	 * @return
	 */
	public ArrayList<EntityLivingBase> getProjections() {
		return projections;
	}
	
	/**
	 * dismiss the projection entity
	 * @param projection
	 */
	public void dismissProjection(EntityLivingBase projection) {
		projections.remove(projection);
		System.out.println("removed " + projection);
		projection.isDead = true;
	}
	
	/**
	 * returns all quests
	 * @return
	 */
	public Map<UUID, Quest> getQuests() {
		return quests;
	}
	
	/**
	 * returns all naturally devout entity types
	 * @return
	 */
	public ArrayList<Class<? extends EntityLivingBase>> getNaturallyDevout() {
		return naturallyDevout;
	}
	
	/**
	 * returns all positions of statues in the server
	 * @return
	 */
	public ArrayList<ServerPos> getStatues() {
		return statues;
	}
	
	/**
	 * returns all positions of villager-placed statues in the server
	 * @return
	 */
	public ArrayList<ServerPos> getVillageStatues() {
		return villageStatues;
	}
	
	/**
	 * adds a type of entity to be considered naturally devout to this deity
	 * @param clasz
	 * @return
	 */
	public Deity addNaturallyDevout(Class<? extends EntityLivingBase> clasz) {
		if (!this.naturallyDevout.contains(clasz) && !MagicMod.deities().naturallyDevout.contains(clasz)) {
			this.naturallyDevout.add(clasz);
			MagicMod.deities().registerNaturallyDevout(clasz);
		}
		return this;
	}
	
	/**
	 * sets the override texture for this deity's entity
	 * @param textureOverride
	 */
	public void setTextureOverride(ResourceLocation textureOverride) {
		this.textureOverride = textureOverride;
	}
	
	/**
	 * returns override texture for deity entity
	 * @return
	 */
	public ResourceLocation getTextureOverride() {
		return textureOverride;
	}
	
	/**
	 * returns the override Render for this deity
	 * @return
	 */
	public RenderDeityBase getOverrideRender() {
		return overrideRender;
	}
	
	/**
	 * sets the override render for this deity
	 * @param overrideRender
	 */
	public void setOverrideRender(RenderDeityBase overrideRender) {
		this.overrideRender = overrideRender;
	}
	
	/**
	 * returns the quest this player is on
	 * @param player
	 * @return
	 */
	public Quest getQuest(EntityLivingBase player) {
		return quests.get(player.getUniqueID());
	}
	/**
	 * returns the quest this player is on
	 * @param player
	 * @return
	 */
	public Quest getQuest(UUID player) {
		return quests.get(player);
	}
	
	/**
	 * gives a quest to a player
	 * @param player
	 * @param quest
	 * @return
	 */
	public Deity giveQuest(EntityLivingBase player, Quest quest) {
		for (Quest q: this.quests.values()) {
			if (q.getClass().equals(quest.getClass())) {
				return this;
			}
		}
		if (quest.canGiveQuest(player)) {
			this.quests.put(player.getUniqueID(), quest);
			quest.giveQuest(player);
		}
		
		return this;
	}
	
	/**
	 * gives a quest to this player but constructs a quest from the bare class rather than requiring a quest Object to be made
	 * @param player
	 * @param clazz
	 * @return
	 */
	public Deity giveQuest(EntityLivingBase player, Class<? extends Quest> clazz) {
		Quest q = null;
		try {
			q = clazz.getConstructor(Deity.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {

			e.printStackTrace();
		}
		if (q != null) {
			giveQuest(player, q);
		}
		return this;
	}

	/**
	 * adds a required type of quest
	 * @param types
	 */
	public void addRequiredQuestTypes(Class<? extends Quest>...types) {
		
		for (Class<? extends Quest> s : types) {
			if (!requiredQuests.contains(s)) {
				requiredQuests.add(s);
			}
			if (!MagicMod.deities().questTypes.contains(s)) {
				MagicMod.deities().questTypes.add(s);
			}
		}
	}
	

	
	/**
	 * returns the {@link Status}
	 * @return
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * sets the {@link Status}
	 * @param status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * if the deity has been supplanted, returns the ID of the player who supplanted it
	 * @return
	 */
	public UUID getSupplanter() {
		return supplanter;
	}
	
	/**
	 * sets the supplanter of the deity
	 * @param supplanter
	 */
	public void setSupplanter(UUID supplanter) {
		this.supplanter = supplanter;
		this.dismiss();
		this.status = Status.SUPPLANTED;
		this.avatar = (EntityLivingBase)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(supplanter);
		if (avatar != null) {
			this.avatar.sendMessage(new TextComponentTranslation("deity.youarenow", this.pro.getNoun(), this.domain, this.displayName));
		}
	}
	
	/**
	 * writes the deity to NBT
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound comp = new NBTTagCompound();
		if (prevPos != null) {
			comp.setTag("PrevPos", prevPos.toNBT());
		}
		/*
		if (summonPos != null) {
			comp.setTag("SummonPos", summonPos.toNBT());
		}*/
		
		if (supplanter != null) {
			comp.setUniqueId("Supplanter", supplanter);
		}
		
		if (avatar != null) {
			comp.setUniqueId("Avatar", avatar.getPersistentID());
		}
		
		if (!projections.isEmpty()) {
			NBTTagList ls = new NBTTagList();
			for (EntityLivingBase proj : projections) {
				NBTTagCompound t = new NBTTagCompound();
				t.setUniqueId("ID", proj.getPersistentID());
				ls.appendTag(t);
			}
			comp.setTag("Projections", ls);
		}
		
		if (!statues.isEmpty()) {
			NBTTagList ls = new NBTTagList();
			for (ServerPos proj : statues) {
				NBTTagCompound t = new NBTTagCompound();
				t.setTag("Pos", proj.toNBT());
				t.setBoolean("Village", villageStatues.contains(t));
				ls.appendTag(t);
			}
			comp.setTag("Statues", ls);
		}
		
		if (!deadDevouts.isEmpty()) {
			NBTTagList ls = new NBTTagList();
			for (NBTTagCompound proj : deadDevouts) {
				NBTTagCompound t = proj;
				
				ls.appendTag(t);
			}
			comp.setTag("DeadDevouts", ls);
		}
		
		if (opposition != null) {
			comp.setUniqueId("Opposition", opposition.getUniqueID());
		}
		if (bester != null) {
			comp.setUniqueId("Bester", bester.getUniqueID());
		}
		
		if (!favorPerPlayer.isEmpty()) {
			NBTTagList list = new NBTTagList();
			for (Entry<UUID, Integer> key : favorPerPlayer.entrySet()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("Player", key.getKey());
				tag.setInteger("Favor", getPoints(key.getKey()));
				tag.setDouble("Worship", getWorship(key.getKey()));
				tag.setTag("Data", this.getDevoutData(key.getKey()));
				list.appendTag(tag);
			}
			comp.setTag("Devotion", list);
			//System.out.println(list);
		}
		
		if (!deadPlayerFavor.entrySet().isEmpty()) {
			NBTTagList list = new NBTTagList();
			for (Entry<UUID, Integer> key : deadPlayerFavor.entrySet()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("Player", key.getKey());
				tag.setInteger("Favor", getPoints(key.getKey()));
				list.appendTag(tag);
			}
			comp.setTag("DeadPlayers", list);
		}
		
		if (!quests.entrySet().isEmpty()) {
			NBTTagList list = new NBTTagList();
			for (Entry<UUID, Quest> key : quests.entrySet()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("Player", key.getKey());
				tag.setTag("Quest", key.getValue().toNBT());
				list.appendTag(tag);
			}
			comp.setTag("Quests", list);
		}
		
		if (!priests.isEmpty()) {
			NBTTagList list = new NBTTagList();
			for (UUID e : priests) {
				NBTTagCompound c = new NBTTagCompound();
				c.setUniqueId("ID", e);
				list.appendTag(c);
			}
			comp.setTag("Priests", list);
		}
		
		if (!otherData.isEmpty()) {
			NBTTagList list = new NBTTagList();
			for (String string : otherData.keySet()) {
				NBTTagCompound c = new NBTTagCompound();
				c.setString("Key", string);
				//System.out.println(otherData.get(string).getClass().toGenericString());
				//c.setString("Class", otherData.get(string).getClass().toGenericString());
				//System.out.println(otherData.get(string));
				c.setTag("Value", otherData.get(string).serializeNBT());
				list.appendTag(c);
			}
			comp.setTag("OtherData", list);
		}
		
		if (trades != null) {
			comp.setTag("Trades", trades.getRecipiesAsTags());
		}
		
		comp.setString("Status", status.toString());
		
		comp.setString("Pronoun", pro.toString());
		return comp;
	}
	
	/** 
	 * sets a tag compound in a devout's devoutData
	 * @param u
	 * @param tag
	 */
	protected void setDevoutData(UUID u, NBTTagCompound tag) {
		this.devoutData.put(u, tag);
	}
	
	/**
	 * reads deity from NBT
	 */
	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		
		
		
		if (compound.hasKey("Trades")) {
			trades = new MerchantRecipeList();
			trades.readRecipiesFromTags(compound.getCompoundTag("Trades"));
		}
		
		if (compound.hasKey("PrevPos")) {
			prevPos = ServerPos.fromNBT(compound.getCompoundTag("PrevPos"));
		}
		
		/*if (compound.hasKey("SummonPos")) {
			summonPos = ServerPos.fromNBT(compound.getCompoundTag("SummonPos"));
		}*/
		if (compound.hasKey("Avatar")) {
			avatar = (EntityDeity)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(compound.getUniqueId("Avatar"));
		}
		if (compound.hasKey("Opposition")) {
			opposition = (EntityPlayer)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(compound.getUniqueId("Opposition"));
		}
		if (compound.hasKey("Bester")) {
			bester = (EntityPlayer)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(compound.getUniqueId("Bester"));
		}
		if (compound.hasKey("Devotion")) {
			this.favorPerPlayer.clear();
			this.worship.clear();
			this.devoutData.clear();
			NBTTagList list = compound.getTagList("Devotion", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				this.setPoints(tag.getUniqueId("Player"), tag.getInteger("Favor"));
				this.setWorship(tag.getUniqueId("Player"), tag.getDouble("Worship"));
				this.setDevoutData(tag.getUniqueId("Player"), tag.getCompoundTag("Data"));
			}
			//System.out.println(favorPerPlayer);
			//System.out.println(worship);
			//System.out.println(devoutData);
			
		}
		
		if (compound.hasKey("OtherData")) {
			
			NBTTagList list = compound.getTagList("OtherData", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				//for (Class<? extends INBTSerializable<NBTTagCompound>> cl : this.otherDataTypes) {
					//if (cl.toGenericString().equals(tag.getString("Class"))) {
						//System.out.println(cl.toGenericString());
						//this.getSpecialData(tag.getString("Key"), cl).deserializeNBT(tag.getCompoundTag("Value"));
				
				this.getSpecialData(tag.getString("Key")).deserializeNBT(tag.getCompoundTag("Value"));
				//System.out.println(this.getSpecialData(tag.getString("Key")));
					//}
				//}
				
			}
			
		}
		
		if (compound.hasKey("DeadPlayers")) {
			this.deadPlayerFavor.clear();
			NBTTagList list = compound.getTagList("DeadPlayers", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				this.setPoints(tag.getUniqueId("Player"), tag.getInteger("Favor"));
			}
			
		}
		
		if (compound.hasKey("Projections")) {
			NBTTagList list = compound.getTagList("Projections", NBT.TAG_COMPOUND);
			projections.clear();
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				EntityLivingBase l = (EntityLivingBase)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(tag.getUniqueId("Projection"));
				if (l != null) this.projections.add(l);
			}
			
		}
		
		if (compound.hasKey("DeadDevouts")) {
			NBTTagList list = compound.getTagList("DeadDevouts", NBT.TAG_COMPOUND);
			deadDevouts.clear();
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				this.deadDevouts.add(tag);
			}
			
		}
		
		if (compound.hasKey("Statues")) {
			NBTTagList list = compound.getTagList("Statues", NBT.TAG_COMPOUND);
			statues.clear();
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				ServerPos l = ServerPos.fromNBT(tag.getCompoundTag("Pos"));
				
				if (l != null) {
					this.statues.add(l);
					if (tag.getBoolean("Village")) {
						villageStatues.add(l);
					}
				}
			}
			
		}
		
		if (compound.hasKey("Quests")) {
			this.quests.clear();
			NBTTagList list = compound.getTagList("Quests", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				if (Quest.fromNBT(tag, this) != null) this.quests.put(tag.getUniqueId("Player"), Quest.fromNBT(tag, this));
			}
			
		}
		
		if (compound.hasKey("Priests")) {
			this.quests.clear();
			NBTTagList list = compound.getTagList("Priests", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				addPriest(tag.getUniqueId("ID"));
			}
			
		}
		
		if (compound.hasKey("Supplanter")) {
			this.supplanter = compound.getUniqueId("Supplanter");
		}
		for (Status s : Status.values()) {
			if (compound.getString("Status").equalsIgnoreCase(s.toString())) {
				status = s;
			}
		}
		
		for (Pronoun s : Pronoun.values()) {
			if (compound.getString("Pronoun").equalsIgnoreCase(s.toString())) {
				pro = s;
			}
		}
	}
	
	/**
	 * sets a position to be the deity's altar
	 * @param altarPos
	 * @return
	 */
	/*public Deity setAltarPos(ServerPos altarPos) {
		this.altarPos = altarPos;
		return this;
	}*/
	
	/**
	 * sets a player who defeated the deity
	 * @param bester
	 * @return
	 */
	public Deity setBester(EntityPlayer bester) {
		this.bester = bester;
		return this;
	}
	
	/**
	 * sets a player to be the EntityDeity's battle target
	 * @param opposition
	 * @return
	 */
	public Deity setOpposition(EntityPlayer opposition) {
		this.opposition = opposition;
		return this;
	}
	
	/**
	 * returns the position of the altar
	 * @return
	 */
	/*public ServerPos getAltarPos() {
		return altarPos;
	}*/
	
	/**
	 * sets the position the deity was summoned at
	 * @param summonPos
	 * @return
	 */
	/*public Deity setSummonPos(ServerPos summonPos) {
		this.summonPos = summonPos;
		return this;
	}*/
	
	/**
	 * returns the position the deity was summoned at
	 * @return
	 */
	/*public ServerPos getSummonPos() {
		return summonPos;
	}*/
	
	/**
	 * returns the player who defeated the deity
	 * @return
	 */
	public EntityPlayer getBester() {
		return bester;
	}
	
	/**
	 * returns the display name of the deity
	 * @return
	 */
	public String getNameToDisplay() {
		return displayName;
	}
	
	/**
	 * returns the player whom the EntityDeity is fighting
	 * @return
	 */
	public EntityPlayer getOpposition() {
		return opposition;
	}
	
	/**
	 * returns the items required to summont he deity
	 * @return
	 */
	public ArrayList<ItemStack> getRequiredItems() {
		return new ArrayList<ItemStack>(requiredItems);
	}
	
	/**
	 * returns the location the deity is stored
	 * @return
	 */
	public ResourceLocation getUnlocName() {
		return unlocName;
	}
	
	/**
	 * creates a projection of the deity at a position
	 * @param world
	 * @param summoner
	 * @param pos
	 */
	public void summonProjection(WorldServer world, EntityLivingBase summoner, BlockPos pos) {
		EntityDeityProjection d = new EntityDeityProjection(world, this);
		d.setSummoner(summoner);
		d.setDeity(this);
		d.setBound(pos);
		this.projections.add(d);
		d.setPosition(pos.getX(), pos.getY(), pos.getZ());
		world.spawnEntity(d);
	}

	/**
	 * sets an entity as the deity's avatar
	 * @param avatar
	 * @return
	 */
	public Deity setAvatar(EntityDeity avatar) {
		this.avatar = avatar;
		return this;
	}
	
	/**
	 * returns the deity's avatar
	 * @return
	 */
	public EntityLivingBase getAvatar() {
		return avatar;
	}
	
	/**
	 * sets the items required to summon the deity
	 * @param requiredItems
	 * @return
	 */
	public Deity setRequiredItems(ItemStack...requiredItems) {
		for (ItemStack stac : requiredItems) {
			this.requiredItems.add(stac);
		}
		return this;
	}
	
	/**
	 * dismisses the avatar of the deity
	 */
	public void dismiss() {
		if (this.status != Status.SUPPLANTED) {
			if (avatar != null) avatar.isDead = true;
			avatar = null;
			this.summoner.setValue(null);
			//altarPos = null;
			//summonPos = null;
		}
	}
	

	
	

	
	/**
	 * returns all required entity items for easy summons unspawned at 0,0,0
	 * @param world
	 * @return
	 */
	public ArrayList<EntityItem> getRequiredEntityItems(World world) {
		ArrayList<EntityItem> entities = new ArrayList<EntityItem>();
		for (ItemStack st : getRequiredItems()) {
			entities.add(new EntityItem(world, 0,0,0, st));
		}
		return entities;
	}
	
	/**
	 * returns all required entity items for easy summons unspawned at the position specified
	 * @param world
	 * @param pos
	 * @return
	 */
	public ArrayList<EntityItem> getRequiredEntityItems(World world, BlockPos pos) {
		ArrayList<EntityItem> entities = new ArrayList<EntityItem>();
		for (ItemStack st : getRequiredItems()) {
			entities.add(new EntityItem(world, pos.getX(),pos.getY(),pos.getZ(), st));
		}
		return entities;
	}
	
	/**
	 * whether the deity can be summoned
	 * @param world the world
	 * @param summoner the summoner
	 * @param pos position to summon at
	 * @param itemEntities the items being used to summon
	 * @param bound whether the deity can move from the summon pos
	 * @param hasAltar whether the deity should check for an altar
	 * @return
	 */
	public boolean canSummon(World world, EntityLivingBase summoner, BlockPos pos, ArrayList<EntityItem> itemEntities) {
		if (this.avatar != null ? !this.avatar.isDead : false) {
			return false;
		}
		int count = 0;
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		if (!itemEntities.isEmpty()) {
			for (EntityItem it : itemEntities) {
				items.add(it.getItem());
			}
		}
		if (!requiredItems.isEmpty() && !items.isEmpty())
		for (ItemStack item : requiredItems) {
			for (ItemStack item2 : items) {
				if (item.getItem().equals(item2.getItem()) && item.getCount() == item2.getCount()) {
					
						count++;
						break;
				}
			}
		}
		if (count == requiredItems.size()) return true;
		return false;
	}
	
	/**
	 * summon a deity's avatar
	 * @param world the world
	 * @param summoner the summoner
	 * @param pos position to summon at
	 * @param itemEntities the items to use for summoning
	 * @param bound whether the deity can move from its position
	 * @param hasAltar whether the deity should look for an altar
	 */
	public void summon(World world, EntityLivingBase summoner, BlockPos pos, ArrayList<EntityItem> itemEntities) {
		
		if (canSummon(world, summoner, pos, itemEntities)) {
			if (status == Status.ACTIVE) {
				EntityDeity e = new EntityDeity(world, this);
				e.setInitialPos(pos.up(2));
				
				e.setPosition(pos.getX(), pos.getY()+2, pos.getZ());
				e.initAI();
				world.spawnEntity(e);
				
				this.summoner.setValue(summoner.getPersistentID());
				//this.timeActive = 800;
				
				avatar = e;

			} else if (status == Status.SUPPLANTED) {
				EntityLivingBase b = (EntityLivingBase)CommonProxy.getServer().getEntityFromUuid(supplanter);
				if (b != null) {
					Teleport.teleportToDimension(b, summoner.dimension, pos.getX(), pos.getY()+2, pos.getZ());
				}
			}
			
			String worldName = DimensionManager.getProviderType(world.provider.getDimension()).getName().replace('_', ' ').trim();
			
			StringBuilder builder = new StringBuilder(worldName);
			for (int index = worldName.indexOf('_'); index != -1; index = worldName.indexOf(index, ' ')) {
				builder.setCharAt(index + 1, Character.toUpperCase(builder.charAt(index+1)));
			}
			if (worldName.length() >= 3 ? worldName.substring(0,3).equalsIgnoreCase("the") : false) {
				builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
			}
			
			worldName = builder.toString();
			
			CommonProxy.getServer().getPlayerList().sendMessage(new TextComponentTranslation("deity.hasbeensummoned", pro.getNoun(), this.domain, TextFormatting.DARK_AQUA + displayName + TextFormatting.RESET, worldName));
		} else {
			summoner.sendMessage(new TextComponentTranslation("deity.cannotsummon", pro.getNoun().toLowerCase(), this.displayName));
		}
	}
	
	public void setCustomer(@Nullable EntityPlayer player) {
		//TODO
		if (player != null) this.customer.setValue(player.getUniqueID());
	}

    @Nullable
    public EntityPlayer getCustomer() {
    	//TODO
    	if (customer != null) {
    		return CommonProxy.getServer().getPlayerList().getPlayerByUUID(customer.value);
    	}
    	return null;
    }

    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer player) {
    	return getRecipes((EntityLivingBase)player);
    }
    
    @Nullable
    public MerchantRecipeList getRecipes(EntityLivingBase player) {
    	//TODO
    	if (trades != null) return trades;
    	trades = initRecipes(player);
    	return trades;
    }
    
    public MerchantRecipeList initRecipes(EntityLivingBase base) {
    	
    	return new MerchantRecipeList();
    }

    @SideOnly(Side.CLIENT)
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {
    	//TODO
    	this.trades = recipeList;
    }

    public void useRecipe(MerchantRecipe recipe) {
    	//TODO
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack) {
    	//TODO
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public ITextComponent getDisplayName() {
    	//TODO
    	return new TextComponentString(this.displayName);
    }

    public World getWorld() {
    	if (avatar != null) return avatar.world;
    	else return null;
    }

    public BlockPos getPos() {
    	if (avatar != null) return avatar.getPosition();
    	else return new BlockPos(0,0,0);
    }
	/**
	 * the AI's for the deity entity
	 * @return
	 */
	public abstract void initAI(EntityDeity avatar);
	
	public SoundEvent getAmbientSound() {
		return SoundEvents.BLOCK_PORTAL_AMBIENT;
	}
	
	public void entityConstructed(EntityDeity entity) {
	}
	
	public void challengedToFight(EntityPlayer player, WorldServer worldIn) {
		if (!warlike) return;
		if (avatar == null) return;
		if (opposition != null) return;
		opposition = player;
		if (avatar instanceof EntityDeity ) {
			((EntityDeity)avatar).setCombatTarget(player);
		}
	}
	
	public void defeat(EntityPlayer player) {
		opposition = null;
		bester = player;
	}
	
	public Map<UUID, Integer> getFavorPerPlayer() {
		return favorPerPlayer;
	}
	
	public Deity setPoints(UUID player, int points) {
		boolean createData = false;
		if (this.getPoints(player) == 0 && points != 0) {
			createData = true;
		}
		this.favorPerPlayer.put(player, points);
		if (createData) {
			this.resetDevoutData(player);
		}
		return this;
	}
	
	public Deity setPoints(EntityLivingBase player, int points) {
		this.setPoints(player.getPersistentID(), points);
		this.notifyDevotion(player);
		return this;
	}
	
	public Deity changePointsBy(UUID player, int points) {
		this.setPoints(player, this.getPoints(player)+points);
		//this.favorPerPlayer.put(player, (favorPerPlayer.get(player) == null ? 0 : favorPerPlayer.get(player))+points);
		return this;
	}
	
	public Deity changePointsBy(EntityLivingBase player, int points) {
		this.changePointsBy(player.getUniqueID(), points);
		//this.favorPerPlayer.put(player.getPersistentID(), (favorPerPlayer.get(player.getPersistentID()) == null ? 0 : favorPerPlayer.get(player.getPersistentID()))+points);
		this.notifyDevotion(player);
		return this;
	}
	
	public Deity removeDevout(UUID player) {
		this.favorPerPlayer.remove(player);
		this.worship.remove(player);
		return this;
	}
	
	public Deity removeDevout(EntityLivingBase p) {
		this.removeDevout(p.getUniqueID());
		this.notifyDevotion(p);
		return this;
	}
	
	public int getPoints(UUID player) {
		return (int)(favorPerPlayer.get(player) == null ? 0 : favorPerPlayer.get(player));
	}
	
	public int getPoints(EntityLivingBase player) {
		return getPoints(player.getPersistentID());
	}
	
	public Map<UUID, Double> getWorship() {
		return worship;
	}
	
	public Deity setWorship(UUID player, double points) {
		if (this.getPoints(player) > 0) {
			this.worship.put(player, points);
		}
		return this;
	}
	
	public Deity setWorship(EntityLivingBase player, double points) {

		this.setWorship(player.getPersistentID(), points);
		//this.notifyDevotion(player);
		return this;
	}
	
	public Deity changeWorshipBy(UUID player, double points) {
		if (this.getPoints(player) > 0) {
			this.worship.put(player, (worship.get(player) == null ? 0 : worship.get(player))+points);
		}
		return this;
	}
	
	public Deity changeWorshipBy(EntityLivingBase player, double points) {
		this.changeWorshipBy(player.getUniqueID(), points);
		return this;
	}
	
	public double getWorship(UUID player) {
		return (double)(worship.get(player) == null ? 0 : worship.get(player));
	}
	
	public double getWorship(EntityLivingBase player) {
		return getWorship(player.getPersistentID());
	}
	
	public boolean isDevout(EntityLivingBase player) {
		return getPoints(player.getPersistentID()) > 0;
	}
	
	public boolean isEnemy(EntityLivingBase player) {
		return getPoints(player.getPersistentID()) < 0;
	}
	
	public boolean isDevout(UUID player) {
		return getPoints(player) > 0;
	}
	
	public boolean isEnemy(UUID player) {
		return getPoints(player) < 0;
	}

	public void onAnyEvent(Event event) {
	}
	
	public void onDevoutLivingEvent(LivingEvent event) {
	}
	
	public void onEnemyLivingEvent(LivingEvent event) {
	}
	
	public void onNormalLivingEvent(LivingEvent event) {
	}
	
	public void onLivingEvent(LivingEvent event) {
	}

	
	public void onTickDevout(LivingUpdateEvent event) {
	}
	
	public void onTickEnemy(LivingUpdateEvent event) {
	}
	
	public void onTickNormal(LivingUpdateEvent event) {
	}
	
	public void onDeathOfDevout(LivingDeathEvent event) {
	}
	
	public void __on__death__devout(LivingDeathEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer)) {
			
			this.addDeadDevout(event.getEntityLiving());
			this.favorPerPlayer.remove(event.getEntityLiving().getUniqueID());
			this.worship.remove(event.getEntityLiving().getUniqueID());
			if (this.quests.get(event.getEntityLiving().getUniqueID()) != null) {
				if (!this.quests.get(event.getEntityLiving().getUniqueID()).postMortem) {
					this.quests.remove(event.getEntityLiving().getUniqueID());
				}
			}
			this.worshipSubtraction.remove(event.getEntityLiving().getUniqueID());
		} else {
			this.setPlayerDead((EntityPlayer)event.getEntityLiving());
			this.favorPerPlayer.remove(event.getEntityLiving().getUniqueID());
			this.worship.remove(event.getEntityLiving().getUniqueID());
			if (this.quests.get(event.getEntityLiving().getUniqueID()) != null ? !this.quests.get(event.getEntityLiving().getUniqueID()).postMortem : false) {
				this.quests.remove(event.getEntityLiving().getUniqueID());
			}
			this.worshipSubtraction.remove(event.getEntityLiving().getUniqueID());
		}
	}
	
	public void __on__death__enemy(LivingDeathEvent event) {
		this.favorPerPlayer.remove(event.getEntityLiving().getUniqueID());
	}
	
	public void onNormalDeath(LivingDeathEvent event) {
	}
	
	public void onDeath(LivingDeathEvent event) {
	}
	
	public void onDevoutKill(LivingDeathEvent event) {
	}
	
	public void onDeathOfEnemy(LivingDeathEvent event) {
	}
	
	public void onEnemyKill(LivingDeathEvent event) {
	}
	
	public void onDevoutInteract(PlayerInteractEvent.RightClickBlock event) {
	}
	
	public void onEnemyInteract(PlayerInteractEvent.RightClickBlock event) {
	}
	
	public void onNormalInteract(PlayerInteractEvent.RightClickBlock event) {
	}
	
	public void onInteract(PlayerInteractEvent.RightClickBlock event) {
	}
	
	public void onDevoutRightClick(PlayerInteractEvent.RightClickEmpty event) {
	}
	
	public void onEnemyRightClick(PlayerInteractEvent.RightClickEmpty event) {
	}
	
	public void onNormalRightClick(PlayerInteractEvent.RightClickEmpty event) {
	}
	
	public void onRightClick(PlayerInteractEvent.RightClickEmpty event) {
	}
	
	public void onBecomeDevout(EntityLivingBase devotee) {
	}
	
	public void onDevoutUseItem(PlayerInteractEvent.RightClickItem event) {
	}
	
	public void onEnemyUseItem(PlayerInteractEvent.RightClickItem event) {
	}
	
	public void onNormalUseItem(PlayerInteractEvent.RightClickItem event) {
	}
	
	public void onUseItem(PlayerInteractEvent.RightClickItem event) {
	}
	
	public void onDevoutUseQuestItem(PlayerInteractEvent.RightClickItem event, Item item) {
	}
	
	public void onEnemyUseQuestItem(PlayerInteractEvent.RightClickItem event, Item item) {
	}
	
	public void onNormalUseQuestItem(PlayerInteractEvent.RightClickItem event, Item item) {
	}
	
	public void onUseQuestItem(PlayerInteractEvent.RightClickItem event, Item item) {
	}
	
	public void onDevoutInteractEntity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onEnemyInteractEntity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onNormalInteractEntity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onInteractDeity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onDevoutInteractDeity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onDevoutInteractDeityProjection(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onEnemyInteractDeity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onNormalInteractDeity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
	}
	
	public void onDevoutDestroyItem(PlayerDestroyItemEvent event) {
	}
	
	public void onEnemyDestroyItem(PlayerDestroyItemEvent event) {
	}
	
	public void onNormalDestroyItem(PlayerDestroyItemEvent event) {
	}
	
	public void onDestroyItem(PlayerDestroyItemEvent event) {
	}
	
	public void onDevoutPlaceBlock(BlockEvent.EntityPlaceEvent event) {
	}
	
	public void onEnemyPlaceBlock(BlockEvent.EntityPlaceEvent event) {
	}

	
	public void onDevoutDrop(LivingDropsEvent event) {
	}
	
	public void onEnemyDrop(LivingDropsEvent event) {
	}
	
	public void onNormalDrop(LivingDropsEvent event) {
	}
	
	public void onDrop(LivingDropsEvent event) {
	}
	
	public void onDevoutAttacked(LivingAttackEvent event) {
	}
	
	public void onEnemyAttacked(LivingAttackEvent event) {
	}
	
	public void onNormalAttacked(LivingAttackEvent event) {
	}
	
	public void onAttacked(LivingAttackEvent event) {
	}
	
	public void onDevoutAttack(LivingAttackEvent event) {
	}
	
	public void onEnemyAttack(LivingAttackEvent event) {
	}
	
	public void onDevoutContainer(PlayerContainerEvent event) {
	}
	
	public void onEnemyContainer(PlayerContainerEvent event) {
	}
	
	public void onNormalContainer(PlayerContainerEvent event) {
	}
	
	public void onContainer(PlayerContainerEvent event) {
	}
	
	public void onDevoutWake(PlayerWakeUpEvent event) {
	}
	
	public void onEnemyWake(PlayerWakeUpEvent event) {
	}
	
	public void onNormalWake(PlayerWakeUpEvent event) {
	}
	
	public void onWake(PlayerWakeUpEvent event) {
	}
	
	public void onDevoutChat(ServerChatEvent event) {
	}
	
	public void onEnemyChat(ServerChatEvent event) {
	}
	
	public void onNormalChat(ServerChatEvent event) {
	}
	
	public void onChat(ServerChatEvent event) {
	}
	
	public void onGeneralTick(ServerTickEvent event) {
	}
	
	public void onAnyLivingTick(LivingUpdateEvent event) {
	}
	
	public void onBlockPlaced(EntityPlaceEvent event) {
	}
	
	public void onNonDevoutBlockPlace(EntityPlaceEvent event) {
	}
	
	public void onDevoutBreakBlock(BlockEvent.BreakEvent event) {
	}
	
	public void onEnemyBreakBlock(BlockEvent.BreakEvent event) {
	}
	
	public void onNormalBreakBlock(BlockEvent.BreakEvent event) {
	}
	
	public void onBreakBlock(BlockEvent.BreakEvent event) {
	}
	
	public void onDevoutToss(ItemTossEvent event) {
	}
	
	public void onEnemyToss(ItemTossEvent event) {
	}
	
	public void onNormalToss(ItemTossEvent event) {
	}
	
	public void onToss(ItemTossEvent event) {
	}
	
	public void onDevoutCraft(ItemCraftedEvent event) {
	}
	
	public void onEnemyCraft(ItemCraftedEvent event) {
	}
	
	public void onNormalCraft(ItemCraftedEvent event) {
	}
	
	public void onCraft(ItemCraftedEvent event) {
	}
	
	public void onDevoutPickup(ItemPickupEvent event) {
		if (event.getStack().getItem() instanceof ItemStatue) {
			
		}
	}
	
	public void onEnemyPickup(ItemPickupEvent event) {
	}
	
	public void onNormalPickup(ItemPickupEvent event) {
	}
	
	public void onPickup(ItemPickupEvent event) {
	}
	
	public void onStatueAdded(World worldIn, BlockPos pos, IBlockState state) {
	}
	
	public boolean onStatueActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return true;
	}
	
	public void onBreakStatuePre(World worldIn, BlockPos pos, IBlockState state) {
	}
	
	public void onBreakStatuePost(World worldIn, BlockPos pos, IBlockState state) {
	}
	
	public void playerBreakStatuePost(World worldIn, BlockPos pos, IBlockState state) {
	}
	
	public void playerBreakStatuePre(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
	}
	
	public void onPlayerHitStatue(World worldIn, BlockPos pos, EntityPlayer playerIn) {
	}
	
	public void statueExplodePre(World world, BlockPos pos, Explosion explosion) {
	}
	
	public void statueExplodePost(World world, BlockPos pos, Explosion explosion) {
	}
	
	public void afterPlayerPlaceStatue(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
	}
	
	public void entityCollideWithStatue(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
	}
	
	public boolean isStatueRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	public void onStatueTick(World world, BlockPos pos, IBlockState state, TileEntityStatue te) {
	}
	
	public float statueEnchantPower(World world, BlockPos pos) {
		return 0f;
	}
	
	public void onStatueNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
	}
	
	public void onKeyPressed(KeyBinding key, EntityPlayer devotee) {
		if (key == MagicMod.keybindings.get(EnumKey.GOD_INVENTORY)) {
			EntityPlayer realPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(devotee.getPersistentID());
			this.openInventory(realPlayer, realPlayer.getPersistentID());
		}
	}

	public String getTitle() {
		return (new TextComponentTranslation("deity.title", this.pro.getNoun(), domain)).getFormattedText();
	}
	
	public void notifyDevotion(EntityLivingBase base) {
		if (isDevout(base)) {
			onBecomeDevout(base);
			base.sendMessage(new TextComponentTranslation("deity.nowdevotedto", this.displayName));
		} else if (isEnemy(base)) {
			base.sendMessage(new TextComponentTranslation("deity.nowenemyto", this.displayName));
		} else {
			base.sendMessage(new TextComponentTranslation("deity.notdevoutto", this.displayName));
		}
	}

	
	public Deity setTimeActive(int timeActive) {
		this.timeActive = timeActive;
		return this;
	}
	
	public int getTimeActive() {
		return timeActive;
	}
	
	public static abstract class Quest {
		public int levelsUp;
		public Deity deity;
		public boolean complete;
		public Map<String, Boolean> conditions = new HashMap<String, Boolean>();
		protected int prerequisiteLevels = 0;
		public final boolean postMortem;
		
		
		/**
		 * Constructor MUST BE IN THE FORMAT <Quest name>(Deity d), not <Quest name>(Deity d, int levelsUp, String...conditions)! These must be filled out in the constructor itself!
		 * @param d
		 * @param levelsUp
		 * @param conditions
		 */
		public Quest(Deity d, int levelsUp, String...conditions) {
			this(d, levelsUp, 0, conditions);
		}
		
		public Quest(Deity d, int levelsUp, int prerequisiteLevels, String...conditions) {
			this(d, levelsUp, prerequisiteLevels, false, conditions);
		}
		
		public Quest(Deity d, int levelsUp, int prerequisiteLevels, boolean postMortem, String...conditions) {
			this.levelsUp = levelsUp;
			this.prerequisiteLevels = prerequisiteLevels;
			for (String s : conditions) {
				this.conditions.put(s, false);
			}
			this.postMortem = postMortem;
			deity = d;
		}
		
		public Quest(Deity d) {
			this(d, 1);
		}
		
		public void setCompletion() {
			boolean comp = true;
			for (String s : conditions.keySet()) {
				comp = comp && (conditions.get(s) != null ? conditions.get(s) : false);
			}
			complete = comp;
		}
		
		public void setCondition(String cond, boolean val) {
			conditions.put(cond, val);
		}
		
		public boolean isComplete() {
			return complete;
		}
		
		public abstract void onEvent(Event e);
		
		public void checkCompletion(EntityLivingBase devotee) {
			if (deity.getQuest(devotee) != this) return;
			this.setCompletion();
			this.checkConditions(devotee);
			if (complete) {
				onCompleteQuest(devotee);
			}
		}
		
		public int levelsUp() {
			return levelsUp;
		}
		public abstract void giveQuest(EntityLivingBase devotee);
		
		public abstract void checkConditions(EntityLivingBase devotee);
		
		public abstract void completeQuest(EntityLivingBase devotee);
		
		public abstract boolean canGiveQuest(EntityLivingBase devotee);
		
		public NBTTagCompound toNBT() {
			NBTTagCompound comp = new NBTTagCompound();
			comp.setString("Type", this.getClass().toString());
			NBTTagList list = new NBTTagList();
			for (String s : conditions.keySet()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("Condition", s);
				tag.setBoolean("Status", conditions.get(s));
				list.appendTag(tag);
			}
			comp.setTag("Conditions", list);
			return comp;
		}
		
		public static Quest newQuest(Class<? extends Quest> clazz, Deity d) {
			Quest q = null;
			try {
				q = clazz.getConstructor(Deity.class).newInstance(d);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {

				e.printStackTrace();
			} catch (SecurityException e) {

				e.printStackTrace();
			}
			return q;
		}
		
		public static Quest fromNBT(NBTTagCompound comp, Deity d) {
			String clas = comp.getString("Type");
			Class<? extends Quest> clazz = null;
			for (Class<? extends Quest> q : MagicMod.deities().questTypes) {
				if (clas.equalsIgnoreCase(q.toString())) {
					clazz = q;
				}
			}
			
			Quest q = null;
			
			if (clazz == null) return null;
			try {
				q = clazz.getConstructor(Deity.class).newInstance(d);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			if (q != null) {
				q.fromNBT(comp);
				return q;
			}
			return null;
		}
		
		public void fromNBT(NBTTagCompound comp) {
			NBTTagList conds = comp.getTagList("Conditions", NBT.TAG_COMPOUND);
			Iterator<NBTBase> iter = conds.iterator();
			while (iter.hasNext()) {
				NBTTagCompound tag = (NBTTagCompound)iter.next();
				this.conditions.put(tag.getString("Condition"), tag.getBoolean("Status"));
			}
		}
		
		public void onCompleteQuest(EntityLivingBase devotee) {
				if (!MinecraftForge.EVENT_BUS.post(new DeityQuestEvent(devotee, this, this.deity))) {

					this.deity.changePointsBy(devotee, this.levelsUp);
					completeQuest(devotee);
					System.out.println("Completed");
					this.deity.quests.remove(devotee.getUniqueID(), this);
					this.deity.putQuestStatus(devotee.getPersistentID(), this.getClass());
				}
		}
	}
	
	public static class AIWorshipDeity extends EntityAIBase {

		private EntityLivingBase entity;
		private Deity deity;
		private BlockPos foundStatue = null;
		
		public AIWorshipDeity(EntityLivingBase entity, Deity d) {
			if (!d.isDevout(entity)) {
				d.setPoints(entity, 1);
			}
			this.entity = entity;
			this.deity = d;
			foundStatue = null;
			this.setMutexBits(8);
		}
		
		@Override
		public boolean shouldExecute() {
			if (entity instanceof EntityVillager) {
				
				if (entity.world.getVillageCollection() != null) {
					for (Village v : entity.world.getVillageCollection().getVillageList()) {
						if (v.getCenter().distanceSq(entity.getPosition()) < v.getVillageRadius()*v.getVillageRadius()) {
							
							return deity.getWorship(entity) < v.getVillageRadius() * 3.0 - 20 && !entity.world.isDaytime();
						}
					}
				}
			}
			if (deity.getWorship(entity) < 25 && !entity.world.isDaytime()) {//Math.abs(entity.world.getWorldTime()) < 1000) {
				return true;
			}
			return false;
		}
		
		@Override
		public void startExecuting() {
			foundStatue = null;
			
		}
		
		
		@Override
		public void updateTask() {
			System.out.println("Worship stuff in progress...");
			if (foundStatue == null) {
				
				List<ServerPos> statues = deity.getStatues();
				for (ServerPos spos : statues) {
					if (spos.getDimension() != entity.dimension) return;
					BlockPos pos = spos.getPos();
					if (entity.world.getVillageCollection().getNearestVillage(pos, 1000) != null) {
						if (pos.distanceSq(entity.world.getVillageCollection().getNearestVillage(pos, 1000).getCenter()) < Math.pow(entity.world.getVillageCollection().getNearestVillage(pos, 1000).getVillageRadius(), 2)) {
							if (entity instanceof EntityLiving) {
								if (((EntityLiving)entity).getNavigator().tryMoveToXYZ(pos.getX(), pos.getY() +1, pos.getZ(), 2+entity.getAIMoveSpeed())) {
									
									System.out.println(deity.statues);
									System.out.println("Successful pathing");
								} else {
									continue;
								}
							} else {
								entity.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
							}
							
							foundStatue = pos;
						
						}
					} else {
						continue;
						//IBlockState state = BlockInit.GOD_STATUES.get(deity).getStateForPlacement(entity.world, ent, facing, hitX, hitY, hitZ, meta, placer)
					}
				}
				
				if (foundStatue == null) {
					if (entity.world.isAirBlock(entity.getPosition().offset(entity.getAdjustedHorizontalFacing()))) {
						IBlockState state = BlockInit.GOD_STATUES.get(deity).getStateForPlacement(entity.world, entity.getPosition().offset(entity.getAdjustedHorizontalFacing()), entity.getAdjustedHorizontalFacing(), 0, 0, 0, 0, entity);
						state = state.withProperty(BlockStatue.TYPE, EnumTypeStatue.VILLAGE);
						entity.world.setBlockState(entity.getPosition().offset(entity.getAdjustedHorizontalFacing()), state);
					} else {
						entity.turn(90, 0);
					}
				}
			} else {
				if (entity instanceof EntityLiving) {
					EntityLiving en = (EntityLiving)entity;
					en.getLookHelper().setLookPosition(this.foundStatue.getX(), this.foundStatue.getY(), this.foundStatue.getZ(), (float)en.getHorizontalFaceSpeed(), (float)en.getVerticalFaceSpeed());
					en.spawnExplosionParticle();
				}
				
				if (entity.getRNG().nextInt(50) < 4) {
					deity.changeWorshipBy(entity, 8);
					
				}
			}
		}
		
		@Override
		public void resetTask() {
			foundStatue = null;
		}
		
	}
	
	public static class Ability implements INBTSerializable<NBTTagCompound> {
		
		public Map<UUID, Boolean> abilities = new HashMap<UUID, Boolean>();
		public String name;
		public int levels;
		
		public Ability(String name) {
			this(name, -1);
		}
		
		/**
		 * 
		 * @param name
		 * @param levels set levels to -1 if the ability must be given in a way other than leveling up
		 */
		public Ability(String name, int levels) {
			this.name = name;
			this.levels = levels;
		}
		
		public Map<UUID, Boolean> getMap() {
			return abilities;
		}
		
		public int getLevels() {
			return levels;
		}
		
		public boolean hasAbility(UUID player) {
			return abilities.getOrDefault(player, false);
		}
		
		public Ability giveAbility(UUID player) {
			abilities.put(player, true);
			return this;
		}
		
		public Ability removeAbility(UUID player) {
			abilities.remove(player);
			return this;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagList ab = new NBTTagList();
			if (!abilities.isEmpty()) {
				for (UUID uu : abilities.keySet()) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setUniqueId("ID", uu);
					tag.setBoolean("has", abilities.getOrDefault(uu, false));
					ab.appendTag(tag);
				}
			}
			compound.setTag("List", ab);
			return compound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			NBTTagList ab = nbt.getTagList("List", NBT.TAG_COMPOUND);
			abilities.clear();
			for (int i = 0; i < ab.tagCount(); i++) {
				NBTTagCompound tag = new NBTTagCompound();
				abilities.put(tag.getUniqueId("ID"), tag.getBoolean("has"));
			}
		}
		
		@Override
		public String toString() {
			return "Ability: " + name;
		}
	}
	
	public static abstract class SerializableValue<T> implements INBTSerializable<NBTTagCompound> {
		
		protected T value;
		
		public SerializableValue(T value) {
			this.value = value;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			value = fromNBT(nbt);
		}
		@Override
		public NBTTagCompound serializeNBT() {
			return toNBT(value, new NBTTagCompound());
		}
		
		protected abstract NBTTagCompound toNBT(T value, NBTTagCompound nbt);
		protected abstract T fromNBT(NBTTagCompound nbt);
		
		public T getValue() {
			return value;
		}
		
		public void setValue(T value) {
			this.value = value;
		}
		
		@Override
		public boolean equals(Object obj) {
			return this.value.equals(obj);
		}
		
		@Override
		public String toString() {
			
			return this.value.toString();
		}
	}

	
	public static abstract class SerializableMap<Key, Value> implements INBTSerializable<NBTTagCompound> {
		protected final Map<Key, Value> map = new HashMap<Key, Value>();
		
		public SerializableMap(@Nullable Map<Key, Value> initial) {
			if (initial == null) return;
			if (initial.isEmpty()) return;
			for (Entry<Key, Value> en : initial.entrySet()) {
				map.put(en.getKey(), en.getValue());
			}
		}
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound comp = new NBTTagCompound();
			NBTTagList l = new NBTTagList();
			if (!map.isEmpty()) {
				for (Entry<Key, Value> t : map.entrySet()) {
					NBTTagCompound c = new NBTTagCompound();
					c.setTag("Key", keyToNBT(t.getKey(), t.getValue(), new NBTTagCompound()));
					c.setTag("Value", valueToNBT(t.getValue(), t.getKey(), new NBTTagCompound()));
					l.appendTag(c);
					
				}
			}
			comp.setTag("Map", l);
			return comp;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			map.clear();
			NBTTagList l = nbt.getTagList("Map", NBT.TAG_COMPOUND);
			for (int i = 0; i < l.tagCount(); i++) {
				NBTTagCompound tg = l.getCompoundTagAt(i);
				NBTTagCompound key = tg.getCompoundTag("Key");
				NBTTagCompound val = tg.getCompoundTag("Value");
				map.put(keyFromNBT(tg.getCompoundTag("Key"), val), valueFromNBT(tg.getCompoundTag("Value"), key));
			}
		}
		
		
		
		public abstract NBTTagCompound keyToNBT(Key key, Value val, NBTTagCompound nbt);
		
		public abstract Key keyFromNBT(NBTTagCompound nbt, NBTTagCompound val);
		
		public abstract NBTTagCompound valueToNBT(Value value, Key key, NBTTagCompound nbt);
		
		public abstract Value valueFromNBT(NBTTagCompound nbt, NBTTagCompound key);
		
		public Map<Key, Value> getMap() {
			return map;
		}
		
		public void put(Key key, Value value) {
			this.map.put(key, value);
		}
		
		public Value remove(Key key) {
			return this.map.remove(key);
		}
		
		public boolean remove(Key key, Value value) {
			return this.map.remove(key, value);
		}
		public Value get(Key key) {
			return this.map.get(key);
		}
		
		public Key getKey(Value value) {
			if (map.keySet().isEmpty()) return null;
			for (Key key : map.keySet()) {
				if (map.get(key).equals(value)) {
					return key;
				}
			}
			return null;
		}
		
		@Override
		public String toString() {
			return map.toString();
		}
		
	}
	
	
	public static class UUIDtoInventory extends SerializableMap<UUID, DivineInventory> {

		public Map<UUID, Boolean> canAccess = new HashMap<UUID, Boolean>();
		
		public UUIDtoInventory(Map<UUID, DivineInventory> initial) {
			super(initial);
			
		}

		@Override
		public NBTTagCompound keyToNBT(UUID key, DivineInventory val, NBTTagCompound nbt) {
			nbt.setUniqueId("ID", key);
			return nbt;
		}

		@Override
		public UUID keyFromNBT(NBTTagCompound nbt, NBTTagCompound val) {
			
			return nbt.getUniqueId("ID");
		}
		
		@Override
		public void put(UUID key, DivineInventory value) {
			if (!value.devotee.equals(key)) value.devotee = key;
			super.put(key, value);
			
		}

		@Override
		public NBTTagCompound valueToNBT(DivineInventory value, UUID key, NBTTagCompound nbt) {
			NBTTagList inventory = new NBTTagList();
			for (int i = 0; i < value.getSizeInventory(); i++) {
				inventory.appendTag(value.getStackInSlot(i).serializeNBT());
			}
			nbt.setTag("Handler", inventory);
			nbt.setInteger("Size", value.getSizeInventory());
			nbt.setString("CustomName", value.getName());
			nbt.setBoolean("CustomNameTrue", value.hasCustomName());
			nbt.setBoolean("canAccess", canAccess.getOrDefault(key, false));
			nbt.setString("Deity", value.deity.unlocName.toString());
			nbt.setUniqueId("Owner", key);
			return nbt;
		}

		@Override
		public DivineInventory valueFromNBT(NBTTagCompound nbt, NBTTagCompound key) {
			DivineInventory handler = new DivineInventory(nbt.getString("CustomName"), nbt.getUniqueId("Owner"), MagicMod.deities().fromString(nbt.getString("Deity")),nbt.getBoolean("CustomNameTrue"), nbt.getInteger("Size"));
			NBTTagList list = nbt.getTagList("Handler", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++) {
				ItemStack stack = new ItemStack(list.getCompoundTagAt(i));
				handler.setInventorySlotContents(i, stack);
			}
			
			canAccess.put(key.getUniqueId("ID"), nbt.getBoolean("canAccess"));
			return handler;
		}
		
		public Map<UUID, Boolean> getCanAccessMap() {
			return canAccess;
		}
		
		public boolean canAccess(UUID uuid) {
			return canAccess.getOrDefault(uuid, false);
		}
		
		public UUIDtoInventory setAccessibility(UUID uuid, boolean bool) {
			canAccess.put(uuid, bool);
			return this;
		}
		
	}

	
	/**
	 * creates a list of NBT data that can be serialized
	 *
	 * @param the type of data to store
	 */
	public static abstract class SerializableList<T> implements INBTSerializable<NBTTagCompound> {
		/**
		 * the list storing everything
		 */
		protected final ArrayList<T> list = new ArrayList<T>();
		/**
		 * 
		 * @param args a list of arguments to add initially, if you want to
		 */
		public SerializableList(@Nullable T[] args) {
			if (args != null) {
				if (args.length > 0) {
					for (T t : args) {
						list.add(t);
					}
				}
			}
		}
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound comp = new NBTTagCompound();
			NBTTagList l = new NBTTagList();
			if (!list.isEmpty()) {
				for (T t : list) {
					l.appendTag(toNBT(t, new NBTTagCompound()));
					
				}
			}
			comp.setTag("List", l);
			return comp;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			list.clear();
			NBTTagList l = nbt.getTagList("List", NBT.TAG_COMPOUND);
			for (int i = 0; i < l.tagCount(); i++) {
				NBTTagCompound tg = l.getCompoundTagAt(i);
				list.add(fromNBT(tg));
			}
		}
		
		public ArrayList<T> getList() {
			return list;
		}
		
		/**
		 * how to convert each element of the list into an nbt tag
		 * @param t the element of the list to convert
		 * @return
		 */
		protected abstract NBTTagCompound toNBT(T t, NBTTagCompound comp);
		/**
		 * how to convert each element of the list from an NBT tag to an object of type {@code <T>}
		 * @param comp
		 * @return
		 */
		protected abstract T fromNBT(NBTTagCompound comp);
		
		public void add(T e) {
			this.list.add(e);
		}
		
		public boolean contains(Object o) {
			return this.list.contains(o);
		}
		
		public void set(int index, T e) {
			this.list.set(index, e);
		}
		
		public T get(int index) {
			return this.list.get(index);
		}
		
		public T remove(int index) {
			return this.list.remove(index);
		}
		
		public boolean remove(Object o) {
			return this.list.remove(o);
		}
		
		@Override
		public boolean equals(Object obj) {
			
			return this.list.equals(obj);
		}
		
		@Override
		public String toString() {
			
			return this.getClass().getSimpleName() + ": " + this.list.toString();
		}
	}
	
	
	
	public static class AbilityList implements INBTSerializable<NBTTagCompound> {

		public final Map<String, Ability> map = new HashMap<String, Ability>();
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound comp = new NBTTagCompound();
			NBTTagList l = new NBTTagList();
			if (!map.isEmpty()) {
				for (String t : map.keySet()) {
					NBTTagCompound ct = new NBTTagCompound();
					ct.setString("ID", t);
					ct.setTag("Val", map.get(t).serializeNBT());
					
				}
			}
			comp.setTag("List", l);
			return comp;
		}
		
		public ArrayList<String> getRegisteredAbilities() {
			ArrayList<String> list = new ArrayList<String>();
			if (map.isEmpty()) return list;
			for (String s : map.keySet()) {
				list.add(s);
			}
			return list;
		}
		
		public void forEach(Consumer<? super Ability> con) {
			map.values().forEach(con);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			NBTTagList list = new NBTTagList();
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound tg = new NBTTagCompound();
				if (map.get(tg.getString("ID")) != null) {
					map.get(tg.getString("ID")).deserializeNBT(tg.getCompoundTag("Val"));
				}
			}
		}
		
		public Ability get(String name) {
			return this.map.get(name);
		}
		
		public AbilityList giveAbility(UUID player, String ability) {
			if (map.get(ability) != null) {
				map.get(ability).giveAbility(player);
			} else {
				throwException(ability);
			}
			return this;
		}
		
		public void throwException(String name) {
			try {
				throw new IllegalArgumentException(name + " is not an existing ability");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public boolean hasAbility(UUID player, String ability) {
			if (map.get(ability) != null) {
				return map.get(ability).hasAbility(player);
			} else {
				throwException(ability);
			}
			return false;
		}
		
		public AbilityList removeAbility(UUID player, String ability) {
			if (map.get(ability) != null) {
				map.get(ability).removeAbility(player);
				
			} else {
				throwException(ability);
			}
			return this;
		}
		
		public AbilityList registerAbility(Ability ability) {
			map.put(ability.name, ability);
			return this;
		}
		
		public AbilityList registerAbility(String name) {
			map.put(name, new Ability(name));
			return this;
		}
		
	}
	
	public static class SpecialInteger extends SerializableValue<Integer> {

		public SpecialInteger(Integer value) {
			super(value);
		}

		@Override
		protected NBTTagCompound toNBT(Integer value, NBTTagCompound nbt) {
			nbt.setInteger("ID", value);
			return nbt;
		}

		@Override
		protected Integer fromNBT(NBTTagCompound nbt) {
			
			return nbt.getInteger("ID");
		}
		
	}
	
	public static class SpecialDouble extends SerializableValue<Double> {

		public SpecialDouble(Double value) {
			super(value);
		}
		
		@Override
		protected Double fromNBT(NBTTagCompound nbt) {
			
			return nbt.getDouble("ID");
		}
		@Override
		protected NBTTagCompound toNBT(Double value, NBTTagCompound nbt) {
			nbt.setDouble("ID", value);
			return nbt;
		}
		
		
	}
	
	public static class SpecialBoolean extends SerializableValue<Boolean> {
		public SpecialBoolean(Boolean value) {
			super(value);
		}
		@Override
		protected Boolean fromNBT(NBTTagCompound nbt) {
			
			return nbt.getBoolean("ID");
		}
		@Override
		protected NBTTagCompound toNBT(Boolean value, NBTTagCompound nbt) {
			nbt.setBoolean("ID", value);
			return nbt;
		}
	}
	
	public static class SpecialUUID extends SerializableValue<UUID> {

		public SpecialUUID(UUID value) {
			super(value);
		}

		@Override
		protected NBTTagCompound toNBT(UUID value, NBTTagCompound nbt) {
			if (this.value != null) {
				nbt.setUniqueId("ID", value);
			}
			return nbt;
		}

		@Override
		protected UUID fromNBT(NBTTagCompound nbt) {
			return nbt.getUniqueId("ID");
		}
		
	}
	
	public static class ServerPosList extends SerializableList<ServerPos> {
		
		
		
		public ServerPosList(ServerPos[] args) {
			super(args);
		}

		protected NBTTagCompound toNBT(ServerPos pos, NBTTagCompound comp) {
			
			return pos.toNBT();
		}
		
		protected ServerPos fromNBT(NBTTagCompound comp) {
			return ServerPos.fromNBT(comp);
		}
		
		
	}
	
	
	
	public static class UUIDList extends SerializableList<UUID>{
		public UUIDList(UUID[] args) {
			super(args);
		}

		
		@Override
		protected NBTTagCompound toNBT(UUID t, NBTTagCompound comp) {
			comp.setUniqueId("ID", t);
			
			return comp;
		}
		@Override
		protected UUID fromNBT(NBTTagCompound comp) {
			return comp.getUniqueId("ID");
		}
		
	}
	
	public static class IntegerList extends SerializableList<Integer>{
		public IntegerList(Integer[] args) {
			super(args);
		}

		
		@Override
		protected NBTTagCompound toNBT(Integer t, NBTTagCompound comp) {
			comp.setInteger("ID", t);
			
			return comp;
		}
		@Override
		protected Integer fromNBT(NBTTagCompound comp) {
			return comp.getInteger("ID");
		}
		
	}
	
	public static class DoubleList extends SerializableList<Double> {

		public DoubleList(Double[] args) {
			super(args);
		}

		@Override
		protected NBTTagCompound toNBT(Double t, NBTTagCompound comp) {
			comp.setDouble("ID", t);
			return comp;
		}

		@Override
		protected Double fromNBT(NBTTagCompound comp) {
			
			return comp.getDouble("ID");
		}
		
	}
	
	public static class BooleanList extends SerializableList<Boolean> {

		public BooleanList(Boolean[] args) {
			super(args);
		}

		@Override
		protected NBTTagCompound toNBT(Boolean t, NBTTagCompound comp) {
			comp.setBoolean("ID", t);
			return comp;
		}

		@Override
		protected Boolean fromNBT(NBTTagCompound comp) {
			return comp.getBoolean("ID");
		}
		
	}

	
	public static class DivineInventory extends InventoryBasic {

		public final Deity deity;
		public UUID devotee;
		
		public DivineInventory(String title, UUID devotee, Deity deity, boolean customName, int slotCount) {
			super(title, customName, slotCount);
			this.deity = deity;
			this.devotee = devotee;
		}
		
		@Override
		public ITextComponent getDisplayName() {
			// TODO Auto-generated method stub
			return new TextComponentTranslation(super.getName(), deity);
		}
		
		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			
			return (stack.getItem() instanceof IDivineItem ? ((IDivineItem)stack.getItem()).getDeity() == this.deity : false) ||
					(stack.hasTagCompound() ? stack.getTagCompound().getString("Deity").equals(this.deity.unlocName.toString()) : false);
		}
		
	}
	
}
