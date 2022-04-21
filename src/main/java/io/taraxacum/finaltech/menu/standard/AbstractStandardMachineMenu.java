package io.taraxacum.finaltech.menu.standard;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import io.taraxacum.finaltech.machine.AbstractMachine;
import io.taraxacum.finaltech.menu.AbstractMachineMenu;
import io.taraxacum.finaltech.util.ItemStackUtil;
import io.taraxacum.finaltech.util.menu.MaxStackHelper;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * @author Final_ROOT
 */
public abstract class AbstractStandardMachineMenu extends AbstractMachineMenu {
    public static final int MACHINE_MAX_STACK_SLOT = 13;

    public AbstractStandardMachineMenu(@Nonnull AbstractMachine abstractMachine) {
        super(abstractMachine);
    }

    @Override
    public void init() {
        super.init();
        this.addItem(MACHINE_MAX_STACK_SLOT, MaxStackHelper.ICON);
        this.addMenuClickHandler(MACHINE_MAX_STACK_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public void newInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block) {
        super.newInstance(blockMenu, block);
        blockMenu.addMenuClickHandler(MACHINE_MAX_STACK_SLOT, (player, i, itemStack, clickAction) -> {
            int quantity = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), MaxStackHelper.KEY));
            if(clickAction.isShiftClicked()) {
                quantity = 0;
            } else {
                if(clickAction.isRightClicked()) {
                    quantity = (quantity - 1) % (this.getInputSlot().length / 2 + 1);
                } else {
                    quantity = (quantity + 1) % (this.getInputSlot().length / 2 + 1);
                }
            }
            MaxStackHelper.setIcon(blockMenu.getItemInSlot(MACHINE_MAX_STACK_SLOT), quantity);
            BlockStorage.addBlockInfo(block, MaxStackHelper.KEY, String.valueOf(quantity));
            return false;
        });
    }

    @Override
    public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        if (ItemTransportFlow.WITHDRAW.equals(flow)) {
            return this.getOutputSlot();
        } else if (flow == null) {
            return new int[0];
        }

        int full = 0;
        if (menu.getItemInSlot(MACHINE_MAX_STACK_SLOT) == null) {
            menu.addItem(MACHINE_MAX_STACK_SLOT, MaxStackHelper.ICON);
        }
        if (menu.getItemInSlot(MACHINE_MAX_STACK_SLOT).getType().equals(Material.CHEST)) {
            return this.getInputSlot();
        }

        ArrayList<Integer> itemList = new ArrayList<>();
        ArrayList<Integer> nullList = new ArrayList<>();
        ItemStackWrapper itemStackWrapper = ItemStackWrapper.wrap(item);
        int inputLimit = menu.getItemInSlot(MACHINE_MAX_STACK_SLOT).getAmount();
        for (int slot : this.getInputSlot()) {
            ItemStack existedItem = menu.getItemInSlot(slot);
            if (ItemStackUtil.isItemNull(existedItem)) {
                nullList.add(slot);
            } else if (ItemStackUtil.isItemSimilar(itemStackWrapper, existedItem)) {
                if (existedItem.getAmount() < existedItem.getMaxStackSize()) {
                    itemList.add(slot);
                } else {
                    full++;
                }
                if(itemList.size() + full >= inputLimit) {
                    break;
                }
            }
        }

        int[] slots = new int[Math.max(inputLimit - full, 0)];
        int i;
        for (i = 0; i < itemList.size() && i < slots.length; i++) {
            slots[i] = itemList.get(i);
        }
        for (int j = 0; j < nullList.size() && j < slots.length - i; j++) {
            slots[i + j] = nullList.get(j);
        }
        return slots;
    }

    @Override
    public void updateMenu(@Nonnull BlockMenu blockMenu, Block block) {
        if (BlockStorage.getLocationInfo(block.getLocation(), MaxStackHelper.KEY) == null) {
            BlockStorage.addBlockInfo(block.getLocation(), MaxStackHelper.KEY, "0");
        }
        int quantity = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), MaxStackHelper.KEY));
        ItemStack item = blockMenu.getItemInSlot(MACHINE_MAX_STACK_SLOT);
        MaxStackHelper.setIcon(item, quantity);
    }
}
