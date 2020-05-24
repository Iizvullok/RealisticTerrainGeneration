package iizvullokIcemountains.mod;

import iizvullokIcemountains.mod.init.BiomeInit;
import iizvullokIcemountains.mod.proxy.CommonProxy;
import iizvullokIcemountains.mod.util.Reference;
import iizvullokIcemountains.mod.util.handlers.RecipeHandler;
import iizvullokIcemountains.mod.util.handlers.RegistryHandler;
import iizvullokIcemountains.mod.util.handlers.WorldTickHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
	@Instance
	public static Main instance;
	
	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		RegistryHandler.preInitRegistries();
		BiomeInit.registerBiomes();
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new WorldTickHandler());
		RecipeHandler.addRecipes();
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		RegistryHandler.postInitRegistries();
	}
	
	@Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
		
    }
	
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		event.world.setWorldTime(18000);
	}
}
