package com.gm910.magicmod.entity.classes.magical;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.world.Teleport;
import com.google.common.collect.Maps;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityHellPortal extends Entity {
    private static final DataParameter<Float> RADIUS = EntityDataManager.<Float>createKey(EntityHellPortal.class, DataSerializers.FLOAT);
    //private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityHellPortal.class, DataSerializers.VARINT);
    private static final int COLOR = 0x770000;
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.<Boolean>createKey(EntityHellPortal.class, DataSerializers.BOOLEAN);
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;
    

    public EntityHellPortal(World worldIn)
    {
        super(worldIn);
        this.reapplicationDelayMap = Maps.<Entity, Integer>newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(3.0F);
    }
    
    
    
    public EntityHellPortal(World worldIn, double x, double y, double z, EntityLivingBase owner) {
    	this(worldIn, x, y, z);
    	setOwner(owner);
    }

    public EntityHellPortal(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    protected void entityInit()
    {
        //this.getDataManager().register(COLOR, Integer.valueOf(0xAA0000));
        this.getDataManager().register(RADIUS, Float.valueOf(0.5F));
        this.getDataManager().register(IGNORE_RADIUS, Boolean.valueOf(false));
    }

    public void setRadius(float radiusIn)
    {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.setSize(radiusIn * 2.0F, 2F);
        this.setPosition(d0, d1, d2);

        if (!this.world.isRemote)
        {
            this.getDataManager().set(RADIUS, Float.valueOf(radiusIn));
        }
    }

    public float getRadius()
    {
        return ((Float)this.getDataManager().get(RADIUS)).floatValue();
    }


    /*private void updateFixedColor()
    {
            this.getDataManager().set(COLOR, Integer.valueOf(0xAA0000));
    }

    public int getColor()
    {
        return ((Integer)this.getDataManager().get(COLOR)).intValue();
    }

    public void setColor(int colorIn)
    {
        this.colorSet = true;
        this.getDataManager().set(COLOR, Integer.valueOf(colorIn));
    }*/

    /**
     * Sets if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    protected void setIgnoreRadius(boolean ignoreRadius)
    {
        this.getDataManager().set(IGNORE_RADIUS, Boolean.valueOf(ignoreRadius));
    }

    /**
     * Returns true if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    public boolean shouldIgnoreRadius()
    {
        return ((Boolean)this.getDataManager().get(IGNORE_RADIUS)).booleanValue();
    }

    public int getDuration()
    {
        return this.duration;
    }

    public void setDuration(int durationIn)
    {
        this.duration = durationIn;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        boolean flag = this.shouldIgnoreRadius();
        float f = this.getRadius();

        EnumParticleTypes particle = EnumParticleTypes.SPELL_MOB;
        
        if (this.world.isRemote)
        {
            EnumParticleTypes enumparticletypes = particle;

            if (flag)
            {
                if (this.rand.nextBoolean())
                {
                    for (int i = 0; i < 2; ++i)
                    {
                        float f1 = this.rand.nextFloat() * ((float)Math.PI * 2F);
                        float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                        float f3 = MathHelper.cos(f1) * f2;
                        float f4 = MathHelper.sin(f1) * f2;

                        if (enumparticletypes == particle)
                        {
                            int j = this.rand.nextBoolean() ? 16777215 : COLOR;
                            int k = j >> 16 & 255;
                            int l = j >> 8 & 255;
                            int i1 = j & 255;
                            this.world.spawnAlwaysVisibleParticle(particle.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, (double)((float)k / 255.0F)*1, (double)((float)l / 255.0F)*1, (double)((float)i1 / 255.0F)*1);
                        }
                        else
                        {
                            this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }
            else
            {
                float f5 = (float)Math.PI * f * f;

                for (int k1 = 0; (float)k1 < f5; ++k1)
                {
                    float f6 = this.rand.nextFloat() * ((float)Math.PI * 2F);
                    float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;

                    if (enumparticletypes == particle)
                    {
                        int l1 = COLOR;
                        int i2 = l1 >> 16 & 255;
                        int j2 = l1 >> 8 & 255;
                        int j1 = l1 & 255;
                        this.world.spawnAlwaysVisibleParticle(particle.getParticleID(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (double)((float)i2 / 255.0F), (double)((float)j2 / 255.0F), (double)((float)j1 / 255.0F));
                    }
                    else
                    {
                        this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D);
                    }
                }
            }
        }
        else
        {
        	/*
        	for (int x = (int) (-this.getRadius()-1); x <= this.getRadius()+1; x++) {
        		for (int z = (int)(-this.getRadius() -1); z <= this.getRadius()+1;x++) {
        			if (this.getDistanceSq(new BlockPos(posX+x, posY, posZ+z)) < this.getRadius()*this.getRadius()) {
        				this.world.setBlockState(new BlockPos(posX+x, posY, posZ+z), Blocks.FIRE.getDefaultState());
        			}
        		}
        	}*/
            if (this.ticksExisted >= this.waitTime + this.duration)
            {
                this.setDead();
                return;
            }

            boolean flag1 = this.ticksExisted < this.waitTime;

            if (flag != flag1)
            {
                this.setIgnoreRadius(flag1);
            }

            if (flag1)
            {
                return;
            }

            if (this.radiusPerTick != 0.0F)
            {
                f += this.radiusPerTick;

                if (f < 0.5F)
                {
                    this.setDead();
                    return;
                }

                this.setRadius(f);
            }

            if (this.ticksExisted % 5 == 0)
            {
                Iterator<Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Entry<Entity, Integer> entry = (Entry)iterator.next();

                    if (this.ticksExisted >= ((Integer)entry.getValue()).intValue())
                    {
                        iterator.remove();
                    }
                }

                    List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), ent -> ent != this.owner);

                    if (!list.isEmpty())
                    {
                        for (EntityLivingBase entitylivingbase : list)
                        {
                            if (!this.reapplicationDelayMap.containsKey(entitylivingbase) && entitylivingbase.canBeHitWithPotion())
                            {
                                double d0 = entitylivingbase.posX - this.posX;
                                double d1 = entitylivingbase.posZ - this.posZ;
                                double d2 = d0 * d0 + d1 * d1;

                                if (d2 <= (double)(f * f))
                                {
                                    this.reapplicationDelayMap.put(entitylivingbase, Integer.valueOf(this.ticksExisted + this.reapplicationDelay));
                                    if (entitylivingbase instanceof EntityPlayer) {
                                    	Teleport.teleportToDimension(entitylivingbase, dimension != DimensionInit.DIMENSION_SHEOL ? DimensionInit.DIMENSION_SHEOL : 0, posX, 256, posZ);
                                    } else {
                                    	entitylivingbase.changeDimension(dimension != DimensionInit.DIMENSION_SHEOL ? DimensionInit.DIMENSION_SHEOL : 0);//, ((WorldServer)entitylivingbase.world).getDefaultTeleporter());
                                    }
                                    if (this.radiusOnUse != 0.0F)
                                    {
                                        f += this.radiusOnUse;

                                        if (f < 0.5F)
                                        {
                                            this.setDead();
                                            return;
                                        }

                                        this.setRadius(f);
                                    }

                                    if (this.durationOnUse != 0)
                                    {
                                        this.duration += this.durationOnUse;

                                        if (this.duration <= 0)
                                        {
                                            this.setDead();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    public void setRadiusOnUse(float radiusOnUseIn)
    {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn)
    {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn)
    {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable EntityLivingBase ownerIn)
    {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner()
    {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer)
        {
            Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId);

            if (entity instanceof EntityLivingBase)
            {
                this.owner = (EntityLivingBase)entity;
            }
        }

        return this.owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.ticksExisted = compound.getInteger("Age");
        this.duration = compound.getInteger("Duration");
        this.waitTime = compound.getInteger("WaitTime");
        this.reapplicationDelay = compound.getInteger("ReapplicationDelay");
        this.durationOnUse = compound.getInteger("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");



    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Age", this.ticksExisted);
        compound.setInteger("Duration", this.duration);
        compound.setInteger("WaitTime", this.waitTime);
        compound.setInteger("ReapplicationDelay", this.reapplicationDelay);
        compound.setInteger("DurationOnUse", this.durationOnUse);
        compound.setFloat("RadiusOnUse", this.radiusOnUse);
        compound.setFloat("RadiusPerTick", this.radiusPerTick);
        compound.setFloat("Radius", this.getRadius());

        if (this.ownerUniqueId != null)
        {
            compound.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }


    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (RADIUS.equals(key))
        {
            this.setRadius(this.getRadius());
        }

        super.notifyDataManagerChange(key);
    }

    
    @Override
    public EnumPushReaction getPushReaction()
    {
        return EnumPushReaction.IGNORE;
    }

}
