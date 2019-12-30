package com.gm910.magicmod.world.biome;

import java.util.Random;

import com.gm910.magicmod.entity.classes.demons.EntityAzraelite;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class BiomeTyphon extends Biome {

	public BiomeTyphon() {
		super(new BiomeProperties("Typhon").setBaseHeight(0.4F).setHeightVariation(3F).setTemperature(0.01F).setRainfall(0.0F).setRainDisabled().setWaterColor(0x00AACC));
		
		topBlock = Blocks.SAND.getDefaultState();
		fillerBlock = Blocks.GRASS.getDefaultState();
		
		
		this.decorator.coalGen = new WorldGenMinable(Blocks.PRISMARINE.getDefaultState(), 10);
		this.decorator.andesiteGen = new WorldGenMinable(Blocks.SEA_LANTERN.getDefaultState(), 10);
		this.decorator.sandGen = new WorldGenMinable(Blocks.PACKED_ICE.getDefaultState(), 10);
		this.decorator.treesPerChunk = 0;
		
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		
		this.flowers.clear();
		this.addFlower(Blocks.END_ROD.getDefaultState(), 10);
		
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 80, 1, 5));
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityGuardian.class, 80, 1, 5));
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntityElderGuardian.class, 80, 1, 5));
		//this.spawnableCreatureList.add(new SpawnListEntry(EntityAzraelite.class, 80, 1, 5));
	}
	
	@Override
	public boolean canRain() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		
		return 0x000055;
	}
	
	
	
	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		// TODO Auto-generated method stub
		return (new Random()).nextInt(0x000077);
	}
	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		
		return 0x000077;//(new Random()).nextInt(0xFFFFFF);
	}

	@Override
	public int getWaterColorMultiplier() {
		// TODO Auto-generated method stub
		return 0x00AACC;
	}
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        /*for (int x2 = 0; x < 16; x2++) {
        	for (int y = 0; y < 255; y++) {
        		for (int z2 = 0; z < 16; z2++) {
        			chunkPrimerIn.setBlockState(x2, y, z2, Blocks.WATER.getDefaultState());
        		}
        	}
        }*/
        
        
        
        for (int j1 = 255; j1 >= 0; --j1)
        {
            if (j1 <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(i1, j1, l, WATER);
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR)
                {
                    j = -1;
                }
                else if (iblockstate2.getBlock() == Blocks.STONE)
                {
                	
                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            iblockstate = Blocks.CLAY.getDefaultState();
                            iblockstate1 = Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(EnumDyeColor.CYAN.getMetadata());
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                        {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 1.2F)
                            {
                                iblockstate = Blocks.CONCRETE_POWDER.getStateFromMeta(EnumDyeColor.CYAN.getMetadata());
                            }
                            else
                            {
                                iblockstate = Blocks.PURPUR_BLOCK.getDefaultState();
                            }
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            iblockstate = WATER;
                            iblockstate1 = WATER;
                            chunkPrimerIn.setBlockState(i1, j1, l, Blocks.WATER.getDefaultState());
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.STONE && k > 1)
                        {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.getDefaultState();
                        }
                    }
                    else chunkPrimerIn.setBlockState(i1, j1, l, Blocks.WOOL.getDefaultState());
                }
            }
        }
        
	}
	
}
