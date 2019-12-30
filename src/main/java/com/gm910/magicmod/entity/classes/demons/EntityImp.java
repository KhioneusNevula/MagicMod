package com.gm910.magicmod.entity.classes.demons;

import javax.annotation.Nullable;

import com.gm910.magicmod.magicdamage.DamageSourceMystic;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityImp extends EntityDemon implements IDemonWithOwner, IDemonBoundPos {

	
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.<Byte>createKey(EntityImp.class, DataSerializers.BYTE);
    private EntityLivingBase owner;
    @Nullable
    private BlockPos boundOrigin;
    private World boundWorld;
    private boolean limitedLifespan;
    private int limitedLifeTicks = 50;
    
	public EntityImp(World worldIn) {
		super(worldIn);
        this.isImmuneToFire = true;
        this.moveHelper = new AIMoveControl(this);
        this.setSize(0.4F, 0.8F);
        this.experienceValue = 3;
        boundWorld = worldIn;
        setCanMove(true);
	}

	public void move(MoverType type, double x, double y, double z)
    {
        super.move(type, x, y, z);
        this.doBlockCollisions();
    }
	
	public void onUpdate()
    {
        super.onUpdate();
        this.setNoGravity(true);

        if (this.limitedLifespan && --this.limitedLifeTicks <= 0)
        {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 4.0F);
        } if (getOwner() == null) {
        	this.limitedLifespan = true;
        } else {
        	if (getOwner().isDead) {
        		this.limitedLifespan = true;
        	}
        }
    }
	

	protected void initEntityAI()
    {

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityDemon.AICastingSpell());
        this.tasks.addTask(3, new EntityDemon.AIAttackSpell());
        this.tasks.addTask(2, new AIChargeAttack());
        this.tasks.addTask(4, new AIMoveRandom());
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new AICopyOwnerTarget(this));
        //this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }
    

    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(VEX_FLAGS, Byte.valueOf((byte)0));
    }

    
    
    public static void registerFixesImp(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityImp.class);
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("BoundX"))
        {
            this.boundOrigin = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ"));
        }

        if (compound.hasKey("LifeTicks"))
        {
            this.setLimitedLife(compound.getInteger("LifeTicks"));
        }
        if (compound.hasKey("Owner")) {
        	this.owner = (EntityLivingBase)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(compound.getUniqueId("Owner"));
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (this.boundOrigin != null)
        {
            compound.setInteger("BoundX", this.boundOrigin.getX());
            compound.setInteger("BoundY", this.boundOrigin.getY());
            compound.setInteger("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan)
        {
            compound.setInteger("LifeTicks", this.limitedLifeTicks);
        }
        if (this.owner != null) {
        	compound.setUniqueId("Owner", this.owner.getUniqueID());
        }
    }
    
    public EntityLivingBase getOwner()
    {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin()
    {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn)
    {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getImpFlag(int mask)
    {
        int i = ((Byte)this.dataManager.get(VEX_FLAGS)).byteValue();
        return (i & mask) != 0;
    }

    private void setImpFlag(int mask, boolean value)
    {
        int i = ((Byte)this.dataManager.get(VEX_FLAGS)).byteValue();

        if (value)
        {
            i = i | mask;
        }
        else
        {
            i = i & ~mask;
        }

        this.dataManager.set(VEX_FLAGS, Byte.valueOf((byte)(i & 255)));
    }

    public boolean isCharging()
    {
        return this.getImpFlag(1);
    }

    public void setCharging(boolean charging)
    {
        this.setImpFlag(1, charging);
    }

    public void setOwner(EntityLivingBase ownerIn)
    {
        this.owner = ownerIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn)
    {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    public SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    public SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Nullable
    public ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_VEX;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
    }

    
    class AIChargeAttack extends EntityAIBase
    {
        public AIChargeAttack()
        {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (EntityImp.this.getAttackTarget() != null && !EntityImp.this.getMoveHelper().isUpdating() && EntityImp.this.rand.nextInt(7) == 0)
            {
                return EntityImp.this.getDistanceSq(EntityImp.this.getAttackTarget()) > 4.0D;
            }
            else
            {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return EntityImp.this.getMoveHelper().isUpdating() && EntityImp.this.isCharging() && EntityImp.this.getAttackTarget() != null && EntityImp.this.getAttackTarget().isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            EntityLivingBase entitylivingbase = EntityImp.this.getAttackTarget();
            Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
            EntityImp.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityImp.this.setCharging(true);
            EntityImp.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            EntityImp.this.setCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            EntityLivingBase entitylivingbase = EntityImp.this.getAttackTarget();

            if (EntityImp.this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox()))
            {
            	if (EntityImp.this.owner == null) {
            		EntityImp.this.attackEntityAsMob(entitylivingbase);
            	} else {
            		 float f = (float)EntityImp.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            	        int i = 0;

            	            f += EnchantmentHelper.getModifierForCreature(EntityImp.this.getHeldItemMainhand(), entitylivingbase.getCreatureAttribute());
            	            i += EnchantmentHelper.getKnockbackModifier(EntityImp.this);
            		entitylivingbase.attackEntityFrom(DamageSourceMystic.causeIndirectDemonDamage(EntityImp.this, EntityImp.this.owner), f);
            	}
                EntityImp.this.setCharging(false);
            }
            else
            {
                double d0 = EntityImp.this.getDistanceSq(entitylivingbase);

                if (d0 < 9.0D)
                {
                    Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
                    EntityImp.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
        }
    }

    class AICopyOwnerTarget extends EntityAITarget
    {
        public AICopyOwnerTarget(EntityCreature creature)
        {
            super(creature, false);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
        	if (owner instanceof EntityLiving) {
        		return EntityImp.this.owner != null && ((EntityLiving)EntityImp.this.owner).getAttackTarget() != null && this.isSuitableTarget(((EntityLiving)EntityImp.this.owner).getAttackTarget(), false);
        	}
            return EntityImp.this.owner != null && EntityImp.this.owner.getAttackingEntity() != null && this.isSuitableTarget(EntityImp.this.owner.getAttackingEntity(), false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
        	if (owner instanceof EntityLiving) {
        		EntityImp.this.setAttackTarget(((EntityLiving)EntityImp.this.owner).getAttackTarget());
        	} else {
        		EntityImp.this.setAttackTarget(EntityImp.this.owner.getAttackingEntity());
        	}
            super.startExecuting();
        }
    }

    class AIMoveControl extends EntityMoveHelper
    {
        public AIMoveControl(EntityImp vex)
        {
            super(vex);
        }

        public void onUpdateMoveHelper()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - EntityImp.this.posX;
                double d1 = this.posY - EntityImp.this.posY;
                double d2 = this.posZ - EntityImp.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double)MathHelper.sqrt(d3);

                if (d3 < EntityImp.this.getEntityBoundingBox().getAverageEdgeLength())
                {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityImp.this.motionX *= 0.5D;
                    EntityImp.this.motionY *= 0.5D;
                    EntityImp.this.motionZ *= 0.5D;
                }
                else
                {
                    EntityImp.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityImp.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityImp.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityImp.this.getAttackTarget() == null)
                    {
                        EntityImp.this.rotationYaw = -((float)MathHelper.atan2(EntityImp.this.motionX, EntityImp.this.motionZ)) * (180F / (float)Math.PI);
                        EntityImp.this.renderYawOffset = EntityImp.this.rotationYaw;
                    }
                    else
                    {
                        double d4 = EntityImp.this.getAttackTarget().posX - EntityImp.this.posX;
                        double d5 = EntityImp.this.getAttackTarget().posZ - EntityImp.this.posZ;
                        EntityImp.this.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * (180F / (float)Math.PI);
                        EntityImp.this.renderYawOffset = EntityImp.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase
    {
        public AIMoveRandom()
        {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return !EntityImp.this.getMoveHelper().isUpdating() && EntityImp.this.rand.nextInt(7) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            BlockPos blockpos = EntityImp.this.getBoundOrigin();

            if (blockpos == null)
            {
                blockpos = new BlockPos(EntityImp.this);
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(EntityImp.this.rand.nextInt(15) - 7, EntityImp.this.rand.nextInt(11) - 5, EntityImp.this.rand.nextInt(15) - 7);

                if (EntityImp.this.world.isAirBlock(blockpos1))
                {
                    EntityImp.this.moveHelper.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityImp.this.getAttackTarget() == null)
                    {
                        EntityImp.this.getLookHelper().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return boundWorld;
	}

	@Override
	public void setBoundWorld(World world) {
		boundWorld = world;
	}
    
    

	
}
