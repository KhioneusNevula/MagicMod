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
import net.minecraft.entity.monster.EntityMob;
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

public class EntityCacodemon extends EntityDemon implements IDemonWithOwner, IDemonBoundPos {

	
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.<Byte>createKey(EntityCacodemon.class, DataSerializers.BYTE);
    private EntityPlayer owner;
    @Nullable
    private BlockPos boundOrigin;
    private World boundWorld;

    
	public EntityCacodemon(World worldIn) {
		super(worldIn);
        this.isImmuneToFire = true;
        this.moveHelper = new AIMoveControl(this);
        this.setSize(1F, 3F);
        this.experienceValue = 3;
        setCanMove(true);
        boundWorld = worldIn;
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
        //this.targetTasks.addTask(1, new AICopyOwnerTarget(this));
        //this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityMob.class, false));
    }
    

    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(VEX_FLAGS, Byte.valueOf((byte)0));
    }
    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
    	// TODO Auto-generated method stub
    	if (cls == EntityDemon.class || cls.getGenericSuperclass() == EntityDemon.class) {
    		return false;
    	}
    	return super.canAttackClass(cls);
    }
    
    
    
    public static void registerFixesImp(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityCacodemon.class);
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

        if (compound.hasKey("Owner")) {
        	this.owner = (EntityPlayer)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(compound.getUniqueId("Owner"));
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

        if (this.owner != null) {
        	compound.setUniqueId("Owner", this.owner.getUniqueID());
        }
    }
    
    public EntityLivingBase getOwner()
    {
        return this.owner;
    }

    public BlockPos getBoundOrigin()
    {
        return this.boundOrigin;
    }

    public void setBoundOrigin(BlockPos boundOriginIn)
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

    public void setOwner(EntityPlayer ownerIn)
    {
        this.owner = ownerIn;
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
            if (EntityCacodemon.this.getAttackTarget() != null && !EntityCacodemon.this.getMoveHelper().isUpdating() && EntityCacodemon.this.rand.nextInt(7) == 0)
            {
                return EntityCacodemon.this.getDistanceSq(EntityCacodemon.this.getAttackTarget()) > 4.0D;
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
            return EntityCacodemon.this.getMoveHelper().isUpdating() && EntityCacodemon.this.isCharging() && EntityCacodemon.this.getAttackTarget() != null && EntityCacodemon.this.getAttackTarget().isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            EntityLivingBase entitylivingbase = EntityCacodemon.this.getAttackTarget();
            Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
            EntityCacodemon.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityCacodemon.this.setCharging(true);
            EntityCacodemon.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            EntityCacodemon.this.setCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            EntityLivingBase entitylivingbase = EntityCacodemon.this.getAttackTarget();

            if (EntityCacodemon.this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox()))
            {
            	if (EntityCacodemon.this.owner == null) {
            		EntityCacodemon.this.attackEntityAsMob(entitylivingbase);
            	} else {
            		 float f = (float)EntityCacodemon.this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            	        int i = 0;

            	            f += EnchantmentHelper.getModifierForCreature(EntityCacodemon.this.getHeldItemMainhand(), entitylivingbase.getCreatureAttribute());
            	            i += EnchantmentHelper.getKnockbackModifier(EntityCacodemon.this);
            		entitylivingbase.attackEntityFrom(DamageSourceMystic.causeIndirectDemonDamage(EntityCacodemon.this, EntityCacodemon.this.owner), f);
            	}
                EntityCacodemon.this.setCharging(false);
            }
            else
            {
                double d0 = EntityCacodemon.this.getDistanceSq(entitylivingbase);

                if (d0 < 9.0D)
                {
                    Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
                    EntityCacodemon.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
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

            return EntityCacodemon.this.owner != null && EntityCacodemon.this.owner.getAttackingEntity() != null && this.isSuitableTarget(EntityCacodemon.this.owner.getAttackingEntity(), false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
        		EntityCacodemon.this.setAttackTarget(EntityCacodemon.this.owner.getAttackingEntity());
            super.startExecuting();
        }
    }

    class AIMoveControl extends EntityMoveHelper
    {
        public AIMoveControl(EntityCacodemon vex)
        {
            super(vex);
        }

        public void onUpdateMoveHelper()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - EntityCacodemon.this.posX;
                double d1 = this.posY - EntityCacodemon.this.posY;
                double d2 = this.posZ - EntityCacodemon.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double)MathHelper.sqrt(d3);

                if (d3 < EntityCacodemon.this.getEntityBoundingBox().getAverageEdgeLength())
                {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityCacodemon.this.motionX *= 0.5D;
                    EntityCacodemon.this.motionY *= 0.5D;
                    EntityCacodemon.this.motionZ *= 0.5D;
                }
                else
                {
                    EntityCacodemon.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityCacodemon.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityCacodemon.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityCacodemon.this.getAttackTarget() == null)
                    {
                        EntityCacodemon.this.rotationYaw = -((float)MathHelper.atan2(EntityCacodemon.this.motionX, EntityCacodemon.this.motionZ)) * (180F / (float)Math.PI);
                        EntityCacodemon.this.renderYawOffset = EntityCacodemon.this.rotationYaw;
                    }
                    else
                    {
                        double d4 = EntityCacodemon.this.getAttackTarget().posX - EntityCacodemon.this.posX;
                        double d5 = EntityCacodemon.this.getAttackTarget().posZ - EntityCacodemon.this.posZ;
                        EntityCacodemon.this.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * (180F / (float)Math.PI);
                        EntityCacodemon.this.renderYawOffset = EntityCacodemon.this.rotationYaw;
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
            return !EntityCacodemon.this.getMoveHelper().isUpdating() && EntityCacodemon.this.rand.nextInt(7) == 0;
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
            BlockPos blockpos = EntityCacodemon.this.getBoundOrigin();

            if (blockpos == null)
            {
                blockpos = new BlockPos(EntityCacodemon.this);
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(EntityCacodemon.this.rand.nextInt(15) - 7, EntityCacodemon.this.rand.nextInt(11) - 5, EntityCacodemon.this.rand.nextInt(15) - 7);

                if (EntityCacodemon.this.world.isAirBlock(blockpos1))
                {
                    EntityCacodemon.this.moveHelper.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityCacodemon.this.getAttackTarget() == null)
                    {
                        EntityCacodemon.this.getLookHelper().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

	@Override
	public void setOwner(EntityLivingBase owner) {
		if (!(owner instanceof EntityPlayer)) return;
		this.owner = (EntityPlayer)owner;
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
