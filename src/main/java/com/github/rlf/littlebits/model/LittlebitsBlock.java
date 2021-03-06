package com.github.rlf.littlebits.model;

import dk.lockfuglsang.minecraft.util.ItemStackUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.Directional;

import static dk.lockfuglsang.minecraft.po.I18nUtil.tr;

/**
 * Represents a littlebits device in the minecraft world.
 *
 * Note: the output of a littlebits block is the input of a Device (and vice-versa).
 */
public class LittlebitsBlock {
    public static final Material MATERIAL = Material.REDSTONE_COMPARATOR;
    public static final ItemStack ITEM_STACK = ItemStackUtil
            .builder(new ItemStack(MATERIAL))
            .select()
            .lore(tr("littleBits"))
            .build();

    public static final ShapedRecipe RECIPE = new ShapedRecipe(ITEM_STACK);
    static {
        RECIPE.shape("dre");
        RECIPE.setIngredient('d', Material.DIAMOND);
        RECIPE.setIngredient('r', MATERIAL);
        RECIPE.setIngredient('e', Material.EMERALD);
    }

    private final Block block;
    private final Location output;
    private final Location input;
    private Device device;

    public LittlebitsBlock(Block block) {
        this.block = block;
        output = getOutputLocation(block);
        input = getInputLocation(block);
    }

    public Block getOutputBlock() {
        return output != null ? output.getBlock() : null;
    }

    public Block getInputBlock() {
        return input != null ? input.getBlock() : null;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Location getOutput() {
        return output;
    }

    public Location getInput() {
        return input;
    }

    public Location getLocation() {
        return block.getLocation();
    }

    public Block getBlock() {
        return block;
    }

    public int getInputPower() {
        BlockFace facing = getFacing(block);
        return facing != null ? block.getBlockPower(facing.getOppositeFace()) : 0;
    }

    @Override
    public String toString() {
        return BlockLocation.wrap(block.getLocation()).toString() + " -> " + (device != null ? device.getLabel() : tr("-none-"));
    }

    public static BlockFace getFacing(Block block) {
        BlockState state = block.getState();
        if (state != null && state.getData() instanceof Directional) {
            Directional directional = (Directional) state.getData();
            return directional.getFacing();
        }
        return null;
    }

    public static Location getInputLocation(Block block) {
        BlockState state = block.getState();
        if (state != null && state.getData() instanceof Directional) {
            Directional directional = (Directional) state.getData();
            return block.getRelative(directional.getFacing().getOppositeFace()).getLocation();
        }
        return null;
    }

    public static Location getOutputLocation(Block block) {
        BlockState state = block.getState();
        if (state != null && state.getData() instanceof Directional) {
            Directional directional = (Directional) state.getData();
            return block.getRelative(directional.getFacing()).getLocation();
        }
        return null;
    }

    public static boolean isLittlebitsItemStack(ItemStack itemInHand) {
        return itemInHand != null
                && itemInHand.getType() == MATERIAL
                && itemInHand.getEnchantments() != null
                && itemInHand.getEnchantments().size() > 0
                && itemInHand.getItemMeta() != null
                && itemInHand.getItemMeta().getLore() != null
                && itemInHand.getItemMeta().getLore().size() > 0
                && itemInHand.getItemMeta().getLore().get(0).equalsIgnoreCase(tr("littleBits"));
    }

    public static boolean isLittlebitsBlockType(Block block) {
        return block != null
                && block.getType() == Material.REDSTONE_COMPARATOR_OFF;
    }
}
