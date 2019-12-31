package com.gm910.magicmod.blocks.machine;

import com.gm910.magicmod.blocks.BlockBase;
import com.gm910.magicmod.init.ItemInit;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class MagicGenerator extends BlockBase {

	public MagicGenerator(String name) {
		super(name, Material.IRON);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityMagicGenerator();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		TileEntityMagicGenerator entity = (TileEntityMagicGenerator) worldIn.getTileEntity(pos);
		worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), entity.handler.getStackInSlot(0)));
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntityMagicGenerator generator = (TileEntityMagicGenerator)worldIn.getTileEntity(pos);
		if (playerIn.isSneaking()) {
			playerIn.sendStatusMessage(new TextComponentTranslation("magicgenerator.statusmessage", generator.getEnergyStored(), generator.range), true);
			return true;
		} else {
			//if (generator.isItemValidForSlot(0, playerIn.getHeldItem(hand))) {
				ItemStack handler = generator.handler.getStackInSlot(0);
				ItemStack pla = playerIn.getHeldItem(hand);
				generator.handler.setStackInSlot(0, pla);
				playerIn.setHeldItem(hand, handler);
			//}
		}
		
		return true;
	}

	
	public static class TileEntityMagicGenerator extends TileEntity implements IInventory, ITickable, IMagicEnergy {
		public ItemStackHandler handler = new ItemStackHandler(1);
		private CustomEnergyStorage storage = new CustomEnergyStorage(100000);
		private String customName;
		public int cookTime;
		public int energy = storage.getEnergyStored();
		public int range = 0;
		@Override
		public void update() {
			if (!handler.getStackInSlot(0).isEmpty() && (isItemFuel(handler.getStackInSlot(0)) || isItemRange(handler.getStackInSlot(0)))) {
				cookTime++;
				if (cookTime >= 25) {
					if (isItemFuel(handler.getStackInSlot(0))) {
						
						storage.receiveEnergy(getFuelValue(handler.getStackInSlot(0)), false);
						
					} else {
						range += getRangeValue(handler.getStackInSlot(0));
					}
					handler.getStackInSlot(0).shrink(1);
					cookTime = 0;
				}
			}
			
			int range = this.range > 30 ? 30 : this.range;
			
			for (int x = -(range-1); x <= (range+1); x++) {
				for (int y = -(range-1); y <= (range+1); y++) {
					for (int z = -(range-1); z <= (range+1); z++) {
						BlockPos checkPos = pos.add(x, y, z);
						if (checkPos.distanceSq(pos) > range*range) continue;
						if (checkPos.distanceSq(pos) > (range-2)*(range-2)) {
							if (world.isRemote && world.rand.nextInt(50) < 2 && this.storage.getEnergyStored() > 0) {
								world.spawnParticle(EnumParticleTypes.REDSTONE, 
										checkPos.getX() + world.rand.nextDouble()*2-1, 
										checkPos.getY() + world.rand.nextDouble()*2-1, 
										checkPos.getZ() + world.rand.nextDouble() * 2-1, 
										world.rand.nextDouble()*2-1, 
										world.rand.nextDouble()*2-1, 
										world.rand.nextDouble()*2-1, 
										world.rand.nextInt(2), 
										world.rand.nextInt(155)+100, 
										world.rand.nextInt(155)+100);
							}
						}
						IBlockState state = world.getBlockState(checkPos);
						
						if (world.isRemote && world.getTileEntity(checkPos) instanceof IMagicEnergy && world.getTileEntity(checkPos) != this && world.rand.nextInt(30) < 2) {
							((IMagicEnergy)world.getTileEntity(checkPos)).receiveEnergy((int)(storage.getEnergyStored() / 10.0));
							this.extractEnergy((int)(storage.getEnergyStored() / 10.0));
							for (int i = 0; i < 20; i++) world.spawnParticle(EnumParticleTypes.REDSTONE, 
									checkPos.getX() + world.rand.nextDouble()*2-1, 
									checkPos.getY() + world.rand.nextDouble()*2, 
									checkPos.getZ() + world.rand.nextDouble() * 2-1, 
									world.rand.nextDouble()*2-1, 
									-world.rand.nextDouble()*2, 
									world.rand.nextDouble()*2-1, 
									world.rand.nextInt(255), 
									world.rand.nextInt(255), 
									world.rand.nextInt(255));
						}
					}
				}
			}
			
			
			
		}
		
		private int getRangeValue(ItemStack stackInSlot) {
			if (stackInSlot.getItem() == Items.WHEAT) return 1;
			return 0;
		}

		private boolean isItemRange(ItemStack stackInSlot) {
			
			return getRangeValue(stackInSlot) > 0;
		}

		private boolean isItemFuel(ItemStack stackInSlot) {
			// TODO Auto-generated method stub
			return getFuelValue(stackInSlot) > 0;
		}

		private int getFuelValue(ItemStack stackInSlot) {
			// TODO Auto-generated method stub
			Item item = stackInSlot.getItem();
			if (item == Items.NETHER_STAR) return 20;
			if (item == ItemInit.SOUL) return 30;
			if (item == Items.GOLDEN_APPLE) return 20;
			
			return 0;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY) return (T)this.storage;
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.handler;
			return super.getCapability(capability, facing);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			// TODO Auto-generated method stub
			if (capability == CapabilityEnergy.ENERGY) return true;
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
			return super.hasCapability(capability, facing);
		}
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			// TODO Auto-generated method stub
			super.writeToNBT(compound);
			compound.setTag("Inventory", this.handler.serializeNBT());
			compound.setInteger("CookTime", cookTime);
			compound.setInteger("GuiEnergy", this.energy);
			compound.setString("Name", getDisplayName().toString());
			this.storage.writeToNBT(compound);
			return compound;
		}
		
		@Override
		public void readFromNBT(NBTTagCompound compound) {
			// TODO Auto-generated method stub
			super.readFromNBT(compound);
			this.handler.deserializeNBT(compound.getCompoundTag("Inventory"));
			this.cookTime = compound.getInteger("CookTime");
			this.energy = compound.getInteger("GuiEnergy");
			this.customName = compound.getString("Name");
			this.storage.readFromNBT(compound);
			
		}
		
		@Override
		public ITextComponent getDisplayName() {
			// TODO Auto-generated method stub
			return new TextComponentTranslation("block.magic_generator");
		}
		
		public int getEnergyStored() {
			return this.storage.getEnergyStored();
		}
		
		public void setEnergyStored(int en) {
			this.storage.setEnergy(en);
		}
		
		public int getMaxEnergyStored() {
			return this.storage.getMaxEnergyStored();
		}
		
		public int receiveEnergy(int energy) {
			return this.storage.receiveEnergy(energy, false);
		}
		
		public int extractEnergy(int energy) {
			return this.storage.extractEnergy(energy, false);
		}
		
		public int getField(int id) {
			switch (id) {
				case 0: return this.energy;
				case 1: return this.cookTime;
				default: return 0;
			}
		}
		
		public void setField(int id, int value) {
			switch (id) {
				case 0: this.energy = value;
				case 1: this.cookTime = value;
			}
		}
		
		public boolean isUsableByPlayer(EntityPlayer player) {
			// TODO Auto-generated method stub
			return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return (new TextComponentTranslation("container.magicgenerator")).getFormattedText();
		}

		@Override
		public boolean hasCustomName() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getSizeInventory() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return handler.getStackInSlot(0) == ItemStack.EMPTY;
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			// TODO Auto-generated method stub
			return handler.getStackInSlot(index);
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			ItemStack stack = handler.getStackInSlot(index);
			if (stack != ItemStack.EMPTY) {
				stack.shrink(count);
			}
			return stack;
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			ItemStack stack = handler.getStackInSlot(index);
			handler.setStackInSlot(index, ItemStack.EMPTY);
			return stack;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			handler.setStackInSlot(index, stack);
		}

		@Override
		public int getInventoryStackLimit() {
			
			return 64;
		}

		@Override
		public void openInventory(EntityPlayer player) {
			
		}

		@Override
		public void closeInventory(EntityPlayer player) {
			
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			
			return this.isItemFuel(stack) || this.isItemRange(stack);
		}

		@Override
		public int getFieldCount() {
			
			return 2;
		}

		@Override
		public void clear() {
			this.handler.setStackInSlot(0, ItemStack.EMPTY);
		}
	}

}
