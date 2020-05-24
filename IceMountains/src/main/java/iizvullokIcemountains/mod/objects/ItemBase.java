package iizvullokIcemountains.mod.objects;

import iizvullokIcemountains.mod.Main;
import iizvullokIcemountains.mod.init.ItemInit;
import iizvullokIcemountains.mod.proxy.ClientProxy;
import iizvullokIcemountains.mod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel{
	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MISC);
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}