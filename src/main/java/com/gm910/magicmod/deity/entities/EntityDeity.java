package com.gm910.magicmod.deity.entities;

import java.util.ArrayList;
import java.util.List;

import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.entity.classes.demons.IDemonBoundPos;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;
import com.gm910.magicmod.magicdamage.EntityDivineLightning;
import com.gm910.magicmod.magicdamage.EntitySpecialLightning;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityDeity extends EntityMob implements IMerchant, IDemonBoundPos {

	public EntityPlayer combatTarget;
	public Deity deity;
	public int graceTime;
	public BlockPos initialPos;
	public ServerPos boundPos;
	
	public EntityDeity(World world) {
		super(world);
		enablePersistence();
		this.navigator = new PathNavigateFlying(this, world);
		setSize(1, 2);
		setNoGravity(true);
		//noClip = true;
		this.deity = Deities.LEVIATHAN;
		graceTime = 100;
		
	}
	
	public void initAI() {
		if (this.deity == null) return;
		this.deity.initAI(this);
	}
	
	public EntityDeity(World world, Deity deity) {
		this(world);
		setDeity(deity);

	}
	
	public EntityDeity setInitialPos(BlockPos initialPos) {
		this.initialPos = initialPos;
		return this;
	}
	
	public BlockPos getInitialPos() {
		return initialPos;
	}
	

	
	public EntityDeity setDeity(Deity deity) {
		this.deity = deity;
		return this;
	}
	
	public Deity getDeity() {
		return deity;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = super.serializeNBT();
		if (this.deity != null) {
			tag.setString("Deity", this.deity.getUnlocName().toString());
		}

		if (this.combatTarget != null) {
			tag.setUniqueId("CT", combatTarget.getUniqueID());
		}
		
		if (this.initialPos != null) {
			tag.setTag("IPos", ServerPos.BPtoNBT(initialPos));
		}
		if (this.boundPos != null) {
			tag.setTag("BPos", boundPos.toNBT());
		}
		
		return super.serializeNBT();
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		boolean condition = false;
		if (nbt.hasKey("Deity")) {
			this.deity = Deities.fromString(nbt.getString("Deity"));
			condition = true;
		}

		if (nbt.hasKey("CT")) {
			this.combatTarget = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(nbt.getUniqueId("CT"));
		}
		
		if (nbt.hasKey("IPos")) {
			this.initialPos = ServerPos.BPfromNBT(nbt.getCompoundTag("IPos"));
		}
		

		if (nbt.hasKey("BPos")) {
			this.boundPos = ServerPos.fromNBT(nbt.getCompoundTag("BPos"));
		}
		
		if (condition) {
			this.deity.setAvatar(this);
		}
		super.deserializeNBT(nbt);
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		//if (player.getHeldItem(hand).getItem()) {
			
		//}
		if (this.deity == null) return false;
		if (!this.deity.isDevout(player)) return false;
		if (!world.isRemote) {
			player.displayVillagerTradeGui(this.deity);
		}
		
		return true;
	}
	
	@Override
	public Entity changeDimension(int dimensionIn, ITeleporter teleporter) {
		// TODO Auto-generated method stub
		Entity e = super.changeDimension(dimensionIn, teleporter);
		((EntityDeity)e).setInitialPos(this.initialPos);
		((EntityDeity)e).setDeity(this.deity);
		if (this.deity == null) return new EntitySpecialLightning(e.world, Blocks.WATER, DamageSourceMystic.HOLY, e.posX, e.posY, e.posZ, true, false);
		deity.setAvatar((EntityDeity)e);
		return e;
	}
	
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (graceTime <= 0) {
			if (deity == null) {
				System.out.println("No deity...");
				this.isDead = true;
				return;
			} else {
				//System.out.println("Found deity " + this.deity);
			}
		} else {
			graceTime--;
			return;
		}
		
		boolean avnull = false;
		
		if (deity.getAvatar() != null) {
			if (!deity.getAvatar().getUniqueID().equals(this.getUniqueID())) {
				System.out.println("Avatar is not this, it is " + deity.getAvatar() + " as compared to " + this);
				this.isDead = true;
				avnull = true;
			}
		} else {
			deity.setAvatar(this);
		}
		
		if (deity.isDead()) {
			this.isDead = true;
		}
		
		/*if (this.deity.getAltarPos() != null) {
			if (this.dimension != this.deity.getAltarPos().getDimension()) {
				this.isDead= true;
				FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(this.deity.getAltarPos().getD()).setBlockState(this.deity.getAltarPos(), Blocks.STONE.getDefaultState(), 1);
			}
			
			if (!(this.world.getTileEntity(this.deity.getAltarPos()) instanceof TileEntityAltar)) {
				this.isDead = true;
			}
		}*/
		
		if (this.isDead) {
			if (avnull) {
				this.deity.dismiss();
			}
			world.createExplosion(null, posX, posY, posZ, 2, true);
			this.world.addWeatherEffect(new EntityDivineLightning(this.world, this.deity, this.posX, this.posY, this.posZ, true, false));
		} else {
			
			/*TileEntityAltar te = (TileEntityAltar)this.world.getTileEntity(this.deity.getAltarPos());
			if (te.getItemStack() != null) {
				if (te.getPlayerId() != null) {
					this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.deity.getAltarPos().getX(), this.deity.getAltarPos().getY(), this.deity.getAltarPos().getZ(), true));
					this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, true));
					this.deity.getTradeResult(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(te.getPlayerId()), te.getItemStack(), this.deity.getAltarPos());
					
				}
			}*/
			/**if (this.deity.getSummonPos() != null) {
				if (world.getTileEntity(deity.getSummonPos()) instanceof TileEntityGodSummoner && this.summonStructure != null) {
					TileEntityGodSummoner summoner = (TileEntityGodSummoner)world.getTileEntity(deity.getSummonPos());
					if (!summoner.positions.contains(this.getPosition().add(0, deity.getSummonPos().getY() - this.getPosition().getY(), 0))) {
						double x = Math.max(deity.getSummonPos().getX()-2, Math.min(deity.getSummonPos().getX()+2, this.posX));
						double z = Math.max(deity.getSummonPos().getZ()-2, Math.min(deity.getSummonPos().getZ()+2, this.posZ));
						this.setPosition(x, posY, z);
					}
				}
				
			}*/
			if (this.world.getBlockState(this.getPosition()).getMaterial().isReplaceable()) {
				world.setBlockState(this.getPosition(), Blocks.STONE.getDefaultState());
				
			}
			
			if (this.boundPos != null) {
				double x = Math.max(boundPos.getX()-1, Math.min(boundPos.getX()+1, this.posX));
				double z = Math.max(boundPos.getZ()-1, Math.min(boundPos.getZ()+1, this.posZ));
				double y = Math.max(boundPos.getY(), Math.min(boundPos.getY()+1, this.posY));
				this.setPosition(x, y, z);
			}
			
		}

	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		if (this.deity != null) return deity.getAmbientSound();
		return null;
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		/*if (source.getTrueSource() instanceof EntityPlayer) {
			if (getCombatTarget() == null && !source.equals(DamageSource.OUT_OF_WORLD)) {
				return true;
			} else {
				return false;
			}
		} else if (DamageSourceMystic.isDemonic(source)) {
			return false;
		} else {
			return true;
		}*/
		return false;
		
	}
	
	@Override
	public boolean getIsInvulnerable() {
		return getCombatTarget() == null;
	}
	
	@Override
	public void setDead() {
		super.setDead();
		if (deity != null)
		this.deity.dismiss();
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		this.setDead();
		super.onDeath(cause);
	}
	
	@Override
	protected void initEntityAI() {
		/*this.tasks.addTask(0, new EntityAIMoveToBlock(this, 2, 10) {
			
			@Override
			protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
				
				if (pos.equals(deity.getAltarPos().getPos().up(2))) {
					return true;
				}
				return false;
			}
		});*/
		//this.tasks.addTask(1, new EntityAIWanderAvoidWaterFlying(this, 30));
		//this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 30));
		//this.tasks.addTask(0, new AIGodTrade());

		//this.tasks.addTask(1, new EntityAIWanderAvoidWaterFlying(this, 30));
		//this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 30));
		//this.tasks.addTask(0, new AIGodTrade());
		
	}
	
	@Override
	protected boolean canDespawn() {
		
		return false;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(700.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(10.0f);
        //this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.5f);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0f);
	}
	
	public void setCombatTarget(EntityPlayer combatTarget) {
		this.combatTarget = combatTarget;
	}
	
	public EntityPlayer getCombatTarget() {
		return combatTarget;
	}
	
	@Override
	protected void updateAITasks() {
		
		super.updateAITasks();
	}




	@Override
	public void setCustomer(EntityPlayer player) {
		if (deity != null) deity.setCustomer(player);
		
	}

	@Override
	public EntityPlayer getCustomer() {
		if (deity != null) return deity.getCustomer();
		return null;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		if (deity != null) return deity.getRecipes(player);
		return null;
	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList) {
		if (deity != null) deity.setRecipes(recipeList);
		
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		if (deity != null) deity.useRecipe(recipe);
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
		if (deity != null) deity.verifySellingItem(stack);
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public BlockPos getPos() {
		return this.getPosition();
	}

	@Override
	public void setBoundOrigin(BlockPos origin) {
		this.boundPos = new ServerPos(origin, this.world.provider.getDimension());
	}

	@Override
	public BlockPos getBoundOrigin() {
		return boundPos;
	}

	@Override
	public void setBoundWorld(World world) {
		this.boundPos = new ServerPos(boundPos.getPos(), world.provider.getDimension());
	}


	
	public class AIGodBase extends EntityAIBase {

		@Override
		public boolean shouldExecute() {
			return true;
		}
		
	}
	
	public class AIDroppedItemTrade extends EntityAIBase {

		public List<EntityItem> items = new ArrayList<EntityItem>();
		
		@Override
		public boolean shouldExecute() {
			
			items = EntityDeity.this.world.getEntitiesWithinAABB(EntityItem.class, EntityDeity.this.getEntityBoundingBox().expand(2, 2, 2));
			
			if (EntityDeity.this.deity == null) return false;
			if (EntityDeity.this.deity.summoner == null) return false;
			if (EntityDeity.this.deity.getRecipes((EntityLivingBase)CommonProxy.getServer().getEntityFromUuid(EntityDeity.this.deity.summoner.getValue())).isEmpty()) return false;
			
			
			return EntityDeity.this.boundPos != null && !items.isEmpty();
		}
		
		@Override
		public void startExecuting() {
			ItemStack item1 = items.get(0).getItem();
			ItemStack item2 = items.size() > 1 ? items.get(1).getItem() : ItemStack.EMPTY;
			MerchantRecipe recipe = EntityDeity.this.deity.getRecipes((EntityLivingBase)CommonProxy.getServer().getEntityFromUuid(EntityDeity.this.deity.summoner.getValue())).canRecipeBeUsed(item1, item2, -1);
			BlockPos pos = items.get(0).getPosition();
			if (recipe != null ? recipe.isRecipeDisabled() : false) {
				EntityItem result = new EntityItem(EntityDeity.this.world, pos.getX(), pos.getY(), pos.getZ(), recipe.getItemToSell().copy());
				
				world.spawnEntity(result);
			}
			
		}
		
	}
	
}
