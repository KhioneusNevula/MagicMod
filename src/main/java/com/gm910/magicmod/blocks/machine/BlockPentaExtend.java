package com.gm910.magicmod.blocks.machine;

import java.util.Random;

import javax.annotation.Nullable;

import com.gm910.magicmod.blocks.te.TileEntityPentacle;
import com.gm910.magicmod.init.BlockInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPentaExtend extends Block {
	
	public static final PropertyInteger POWER_TIME = PropertyInteger.create("powertime", 0, 30);
	
	public BlockPentaExtend(String name) {
		super(Material.ROCK);
		setRegistryName(name);
		setUnlocalizedName(name);
		BlockInit.BLOCKS.add(this);
		setLightLevel(1.0f);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		// TODO Auto-generated method stub
		return new BlockStateContainer(this, new IProperty[] {POWER_TIME});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return state.getValue(POWER_TIME) / 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		// TODO Auto-generated method stub
		return this.getDefaultState().withProperty(POWER_TIME, meta * 2);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		boolean condition = false;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z<= 1;z++) {
				if ((worldIn.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z)).getBlock() instanceof BlockPentacle)) {
					condition = true;
					break;
				}
			}
		}
		if (condition) {
			
		} else {
			//worldIn.setBlockToAir(pos);
		}
	}
	

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		// TODO Auto-generated method stub
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		
		
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z<= 1;z++) {
				if ((worldIn.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z)).getBlock() instanceof BlockPentacle)) {
					worldIn.setBlockToAir(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
				}
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub
		return blockState.getValue(POWER_TIME) > 0 ? 15 : 0;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
		if (face == EnumFacing.DOWN) {
			return BlockFaceShape.SOLID;
		}
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return super.getBlockLayer();
    }
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
    */
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Determines if an entity can path through this block
     */
	@Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     */
	@Override
    public boolean canSpawnInBlock()
    {
        return false;
    }
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityPentaEx();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		BlockPos pentapos = null;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z<= 1;z++) {
				if ((worldIn.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z)).getBlock() instanceof BlockPentacle)) {
					
					pentapos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
					break;
				}
			}
		}
		if (pentapos != null) {
			return BlockInit.PENTACLE.onBlockActivated(worldIn, pentapos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	public static class TileEntityPentaEx extends TileEntity implements ITickable {

		@Override
		public void update() {
			if (world.isRemote) return;
			IBlockState state = world.getBlockState(pos);
			if (state.getValue(POWER_TIME) > 0) {
				world.setBlockState(pos, state.withProperty(POWER_TIME, state.getValue(POWER_TIME) - 1));
			}
			
			BlockPos pentapos = null;
			boolean condition = false;
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z<= 1;z++) {
					if ((world.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z)).getBlock() instanceof BlockPentacle)) {
						condition = true;
						pentapos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
						break;
					}
				}
			}
			if (condition) {
				
				if (world.isBlockPowered(pos)) {
					System.out.println("POWERED");
					if (world.isRemote) return;
					TileEntityPentacle penta = (TileEntityPentacle)world.getTileEntity(pentapos);
					for (int x = -2; x <= 2; x++) {
						for (int z = -2; z<= 2;z++) {
							if ((world.getBlockState(pos.add(x, 0, z)).getBlock() instanceof BlockSign)) {
								System.out.println("Found sign");
								TileEntitySign sign = (TileEntitySign)world.getTileEntity(pos.add(x, 0, z));
								for (ITextComponent s : sign.signText) {
									String mes = TextFormatting.getTextWithoutFormattingCodes(s.getFormattedText());
									if (!mes.isEmpty()) {
										penta.setIncantation(mes);
										System.out.println(penta.getIncantation());
										break;
									} else {
										System.out.println("empty");
									}
								}
								//penta.setIncantation(sign.signText[0].getFormattedText());
								
							}
						}
					}
				}
			} else {
				world.setBlockToAir(pos);
			}
		}
		
	}
	
}
