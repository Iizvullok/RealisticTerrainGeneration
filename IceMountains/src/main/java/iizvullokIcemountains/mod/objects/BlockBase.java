package iizvullokIcemountains.mod.objects;

import iizvullokIcemountains.mod.Main;
import iizvullokIcemountains.mod.init.BlockInit;
import iizvullokIcemountains.mod.init.ItemInit;
import iizvullokIcemountains.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel{
	public BlockBase(String name, Material material, SoundType sound) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		this.setSoundType(sound);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
