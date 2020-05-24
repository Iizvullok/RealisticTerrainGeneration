package iizvullokIcemountains.mod.util.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

@EventBusSubscriber
public class WorldTickHandler {

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer) {
			if(event.getEntity().world.getWorldInfo().getTerrainType().getName() == "IceIslands") {
				WorldInfo worldinfo = event.getEntity().world.getWorldInfo();
				worldinfo.setCleanWeatherTime(0);
                worldinfo.setRainTime(1000000);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
			}
			if(event.getEntity().world.getWorldInfo().getTerrainType().getName() == "IceMountainsE" && Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER && Math.random() < 0.0333){
				int x = (int) (event.getEntity().posX + Math.random() * 128D - 64D);
				int z = (int) (event.getEntity().posZ + Math.random() * 128D - 64D);
				World world = event.getEntity().world;
				int y = world.getHeight(x, z);
				if(event.getEntity().getPositionVector().distanceTo(new Vec3d(x, y, z)) > 25) {
			        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(event.getEntity(), new AxisAlignedBB(x - 32, y - 32, z - 32, x + 32, y + 32, z + 32));
			        if(list != null) {
			        	int counter = 0;
			        	for (Entity entity : list) {
			        		if(entity != null) {
				        		if(entity instanceof EntityCow || entity instanceof EntityChicken || entity instanceof EntitySheep || entity instanceof EntityPig) {
				        			counter++;
				        		}
			        		}
			        	}
			        	if(counter < 8) {
							if(world.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.GRASS.getDefaultState() || world.getBlockState(new BlockPos(x, y - 1, z)) == Blocks.DIRT.getDefaultState()) {
								if(!world.getBlockState(new BlockPos(x - 1, y, z)).isFullCube() && !world.getBlockState(new BlockPos(x + 1, y, z)).isFullCube() && !world.getBlockState(new BlockPos(x, y, z - 1)).isFullCube() && !world.getBlockState(new BlockPos(x, y, z + 1)).isFullCube()) {
									int i = (int)(Math.random() * 4);
									if(i == 0) {
										EntityCow e = new EntityCow(world);
										e.setLocationAndAngles(x, y, z, (float) (Math.random() * 360.0F), 0.0F);
										AnvilChunkLoader.spawnEntity(e, world);
									}
									if(i == 1) {
										EntitySheep e = new EntitySheep(world);
										e.setLocationAndAngles(x, y, z, (float) (Math.random() * 360.0F), 0.0F);
										AnvilChunkLoader.spawnEntity(e, world);
									}
									if(i == 2) {
										EntityChicken e = new EntityChicken(world);
										e.setLocationAndAngles(x, y, z, (float) (Math.random() * 360.0F), 0.0F);
										AnvilChunkLoader.spawnEntity(e, world);
									}
									if(i == 3) {
										EntityPig e = new EntityPig(world);
										e.setLocationAndAngles(x, y, z, (float) (Math.random() * 360.0F), 0.0F);
										AnvilChunkLoader.spawnEntity(e, world);
									}
								}
							}
			        	}
			        }
				}
			}
		}
	}
}
