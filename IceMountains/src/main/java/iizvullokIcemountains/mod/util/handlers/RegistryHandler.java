package iizvullokIcemountains.mod.util.handlers;

import iizvullokIcemountains.mod.init.BlockInit;
import iizvullokIcemountains.mod.init.ItemInit;
import iizvullokIcemountains.mod.util.IHasModel;
import iizvullokIcemountains.mod.world.WorldTypeIceIslands;
import iizvullokIcemountains.mod.world.WorldTypeIceMountains;
import iizvullokIcemountains.mod.world.WorldTypeIceMountains2;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class RegistryHandler {
	//public static WorldType ICEISLANDS = new WorldTypeIceIslands("IceIslands");
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : ItemInit.ITEMS) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
		
		for(Block block : BlockInit.BLOCKS) {
			if(block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
	}
	
	public static void preInitRegistries() {
		
	}
	
	public static void postInitRegistries() {
		//TODO
		WorldType ICEMOUNTAINS = new WorldTypeIceMountains("IceMountains");
		WorldType ICEMOUNTAINSERODED = new WorldTypeIceMountains2("IceMountainsE");
		//WorldType ICEMOUNTAINSTALL = new WorldTypeIceMountainsTall("IceMountainsTall");
		//ICEISLANDS = new WorldTypeIceIslands("IceIslands");
	}
}