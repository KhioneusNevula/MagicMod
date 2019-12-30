package com.gm910.magicmod.capabilities.ghosts;

import java.util.UUID;

import com.gm910.magicmod.magicdamage.DamageSourceMystic;
import com.gm910.magicmod.magicdamage.EntitySpecialLightning;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public interface IGhost {

	public IGhost setGhost(boolean isGhost);
	public boolean isGhost();
	public EntityLivingBase getEntity();
	public IGhost setEntity(EntityLivingBase entity);
	public void uniqueID(UUID uuid);
	public void onTick(LivingUpdateEvent event);
	public void onDeath(LivingDeathEvent event);
	public boolean isPlayer();
	public void onDrops(LivingDropsEvent event);
	public void onHurt(LivingAttackEvent event);
	public IGhost setKiller(UUID killer);
	public UUID getKiller();
	
	public static class AIGhostAttack extends EntityAIBase {

		public EntityLiving leaper;
		EntityLivingBase leapTarget;
		float leapMotionY;
		
		public AIGhostAttack(EntityLiving en, float lmy) {
			leaper = en;
			leapMotionY = lmy;
			this.setMutexBits(5);
		}
		
		@Override
		public boolean shouldExecute() {
			this.leapTarget = this.leaper.getAttackTarget();

	        if (this.leapTarget == null)
	        {
	            return false;
	        }
	        else
	        {
	            double d0 = this.leaper.getDistanceSq(this.leapTarget);

	            if (d0 >= 4.0D && d0 <= 16.0D)
	            {
	                if (!this.leaper.onGround)
	                {
	                    return false;
	                }
	                else
	                {
	                    return this.leaper.getRNG().nextInt(5) == 0;
	                }
	            }
	            else
	            {
	                return false;
	            }
	        }
		}
		
		@Override
		public void startExecuting() {
			 double d0 = this.leapTarget.posX - this.leaper.posX;
		        double d1 = this.leapTarget.posZ - this.leaper.posZ;
		        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

		        if ((double)f >= 1.0E-4D)
		        {
		            this.leaper.motionX += d0 / (double)f * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
		            this.leaper.motionZ += d1 / (double)f * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
		        }

		        this.leaper.motionY = (double)this.leapMotionY;
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			// TODO Auto-generated method stub
			return !this.leaper.onGround;
		}
		
		@Override
		public void updateTask() {
			if (leaper.getAttackTarget() != null) {
				if (leaper.getEntityBoundingBox().intersects(leaper.getAttackTarget().getEntityBoundingBox())) {
					leaper.world.addWeatherEffect(new EntitySpecialLightning(leaper.world, Blocks.WATER, DamageSourceMystic.causeGhostDamage(leaper), leaper.getAttackTarget().posX, leaper.getAttackTarget().posY, leaper.getAttackTarget().posZ, false, false));
					leaper.getAttackTarget().attackEntityFrom(DamageSourceMystic.causeGhostDamage(leaper), 3);
				}
			}
		}
		
	}
}
