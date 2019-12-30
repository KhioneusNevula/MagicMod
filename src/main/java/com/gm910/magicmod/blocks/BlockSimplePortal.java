package com.gm910.magicmod.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.world.Teleport;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSimplePortal extends BlockBase {

	public BlockSimplePortal(String name) {
		super(name, Material.PORTAL);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityPortal();
	}
	
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityEndGateway)
        {
            int i = ((TileEntityEndGateway)tileentity).getParticleAmount();

            for (int j = 0; j < i; ++j)
            {
                double d0 = (double)((float)pos.getX() + rand.nextFloat());
                double d1 = (double)((float)pos.getY() + rand.nextFloat());
                double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                double d3 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
                double d4 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
                double d5 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
                int k = rand.nextInt(2) * 2 - 1;

                if (rand.nextBoolean())
                {
                    d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
                    d5 = (double)(rand.nextFloat() * 2.0F * (float)k);
                }
                else
                {
                    d0 = (double)pos.getX() + 0.5D + 0.25D * (double)k;
                    d3 = (double)(rand.nextFloat() * 2.0F * (float)k);
                }

                worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.CYAN;
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     * <p>
     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
     * does not fit the other descriptions and will generally cause other things not to connect to the face.
     * 
     * @return an approximation of the form of the given face
     */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	public static class TileEntityPortal extends TileEntity implements ITickable {
		private ServerPos destination;
		private int timeLeft;
		public TileEntityPortal() {
			destination = new ServerPos(this.pos, this.world.provider.getDimension());
			timeLeft = -1;
		}
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound comp = super.serializeNBT();
			comp.setTag("Destination", destination.toNBT());
			comp.setInteger("TimeLeft", timeLeft);
			return comp;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			
			super.deserializeNBT(nbt);
			this.destination = ServerPos.fromNBT(nbt.getCompoundTag("Destination"));
			this.timeLeft = nbt.getInteger("TimeLeft");
		}
		
		public void setDestination(ServerPos destination) {
			this.destination = destination;
		}
		
		public ServerPos getDestination() {
			return destination;
		}
		
		public void setTimeLeft(int timeLeft) {
			this.timeLeft = timeLeft;
		}
		
		public int getTimeLeft() {
			return timeLeft;
		}
		
		@Override
		public void update() {
			if (timeLeft == 0) {
				world.setBlockToAir(pos);
			} else {
				if (timeLeft > 0) {
					timeLeft--;
				}
				AxisAlignedBB box = new AxisAlignedBB(pos);
				List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
				if (!entities.isEmpty()) {
					for (Entity en : entities) {
						Teleport.teleportToDimension(en, destination.getD(), destination.getX(), destination.getY(), destination.getZ());
						
					}
				}
			}
		}
		
	}

}
