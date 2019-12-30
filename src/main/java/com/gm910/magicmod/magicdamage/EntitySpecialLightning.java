package com.gm910.magicmod.magicdamage;

import java.util.List;

import com.gm910.magicmod.handling.util.ReflectUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySpecialLightning extends EntityLightningBolt {
	
	public final DamageSource source;
	
	public final boolean actAsLightning;
	
	public final Block surround;
	
	public boolean effectOnly;
	


    public EntitySpecialLightning(World worldIn, Block surroundEffect, DamageSource damageSource, double x, double y, double z, boolean effectOnlyIn, boolean actAsLightning)
    {
        super(worldIn, x, y, z, effectOnlyIn);
        surround = surroundEffect;
        source = damageSource;
        effectOnly = effectOnlyIn;
        this.actAsLightning = actAsLightning;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.WEATHER;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {

        Integer lightningState = ReflectUtil.getField("lightningState", (EntityLightningBolt)this, Integer.class, 1);
        lightningState = lightningState == null ? 0 : lightningState;
        Integer boltLivingTime = ReflectUtil.getField("boltLivingTime", (EntityLightningBolt)this, Integer.class, 1);
        boltLivingTime = boltLivingTime == null ? 0 : boltLivingTime;
        
        if (lightningState == 2)
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --lightningState;

        if (lightningState < 0)
        {
            if (boltLivingTime == 0)
            {
                this.setDead();
            }
            else if (lightningState < -this.rand.nextInt(10))
            {
                --boltLivingTime;
                lightningState = 1;

                if (!effectOnly && !this.world.isRemote)
                {
                    this.boltVertex = this.rand.nextLong();
                    BlockPos blockpos = new BlockPos(this);

                    if (this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(blockpos, 10) && this.world.getBlockState(blockpos).getMaterial() == Material.AIR && surround.canPlaceBlockAt(this.world, blockpos))
                    {
                        this.world.setBlockState(blockpos, surround.getDefaultState());
                    }
                }
            }
        }

        if (lightningState >= 0)
        {
            if (this.world.isRemote)
            {
                this.world.setLastLightningBolt(2);
            }
            else if (!this.effectOnly)
            {
                double d0 = 3.0D;
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - 3.0D, this.posY - 3.0D, this.posZ - 3.0D, this.posX + 3.0D, this.posY + 6.0D + 3.0D, this.posZ + 3.0D));

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);
                    if (this.actAsLightning) {
	                    if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this.getLightningBolt()))
	                        entity.onStruckByLightning(this.getLightningBolt());
                    } else {
                    	entity.attackEntityFrom(source, 5);
                    }
                }
            }
        }
    }

    
    public EntityLightningBolt getLightningBolt() {
    	return this;
    }

    
    public DamageSource getDamageSource() {
    	return source;
    }
}
