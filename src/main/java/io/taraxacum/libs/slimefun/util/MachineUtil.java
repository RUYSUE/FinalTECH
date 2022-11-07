package io.taraxacum.libs.slimefun.util;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.taraxacum.libs.plugin.dto.ItemAmountWrapper;
import io.taraxacum.libs.plugin.dto.ItemWrapper;
import io.taraxacum.finaltech.core.items.machine.AbstractMachine;
import io.taraxacum.libs.plugin.util.ItemStackUtil;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author Final_ROOT
 * @since 1.0
 */
public final class MachineUtil {
    public static final BlockPlaceHandler BLOCK_PLACE_HANDLER_PLACER_ALLOW = new BlockPlaceHandler(true) {
        @Override
        public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {

        }
    };
    public static final BlockPlaceHandler BLOCK_PLACE_HANDLER_PLACER_DENY = new BlockPlaceHandler(false) {
        @Override
        public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {

        }
    };
    public static final BlockPlaceHandler BLOCK_PLACE_HANDLER_DENY = new BlockPlaceHandler(false) {
        @Override
        public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
            blockPlaceEvent.setCancelled(true);
        }
    };

    public static BlockBreakHandler simpleBlockBreakerHandler() {
        return new BlockBreakHandler(false, true) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent blockBreakEvent, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {

            }
        };
    }
    public static BlockBreakHandler simpleBlockBreakerHandler(@Nonnull AbstractMachine abstractMachine) {
        return new BlockBreakHandler(false, true) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent blockBreakEvent, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                Location location = blockBreakEvent.getBlock().getLocation();
                BlockMenu blockMenu = BlockStorage.getInventory(location);
                blockMenu.dropItems(location, abstractMachine.getInputSlot());
                blockMenu.dropItems(location, abstractMachine.getOutputSlot());
            }
        };
    }
    public static BlockBreakHandler simpleBlockBreakerHandler(@Nonnull int... slot) {
        return new BlockBreakHandler(false, true) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent blockBreakEvent, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                Location location = blockBreakEvent.getBlock().getLocation();
                BlockMenu blockMenu = BlockStorage.getInventory(location);
                blockMenu.dropItems(location, slot);
            }
        };
    }
    public static BlockBreakHandler simpleBlockBreakerHandler(@Nonnull AbstractMachine abstractMachine, int... slot) {
        return new BlockBreakHandler(false, true) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent blockBreakEvent, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                Location location = blockBreakEvent.getBlock().getLocation();
                BlockMenu blockMenu = BlockStorage.getInventory(location);
                blockMenu.dropItems(location, abstractMachine.getInputSlot());
                blockMenu.dropItems(location, abstractMachine.getOutputSlot());
                blockMenu.dropItems(location, slot);
            }
        };
    }

    /**
     * @return Number of slots the machine has and can be interacted.
     */
    public static Integer calMachineSlotSize(@Nonnull AbstractMachine abstractMachine) {
        Set<Integer> slots = new HashSet<>();
        for (int slot : abstractMachine.getInputSlot()) {
            slots.add(slot);
        }
        for (int slot : abstractMachine.getOutputSlot()) {
            slots.add(slot);
        }
        return slots.size();
    }

    /**
     * @return How many slot that has item on it.
     */
    public static int slotCount(@Nonnull Inventory inventory, int[] slots) {
        int count = 0;
        ItemStack itemStack;
        for (int slot : slots) {
            itemStack = inventory.getItem(slot);
            if (!ItemStackUtil.isItemNull(itemStack)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return Whether all item on the specified slots is full.
     */
    public static boolean isFull(@Nonnull Inventory inventory, int[] slots) {
        ItemStack itemStack;
        for (int slot : slots) {
            itemStack = inventory.getItem(slot);
            if (ItemStackUtil.isItemNull(itemStack) || itemStack.getAmount() < itemStack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Whether all item on the specified slots is null.
     */
    public static boolean isEmpty(@Nonnull Inventory inventory, int[] slots) {
        ItemStack itemStack;
        for (int slot : slots) {
            itemStack = inventory.getItem(slot);
            if (!ItemStackUtil.isItemNull(itemStack)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Stock same items in the specified area of slots.
     */
    public static void stockSlots(@Nonnull Inventory inventory, int[] slots) {
        List<ItemWrapper> items = new ArrayList<>(slots.length);
        ItemWrapper itemWrapper = new ItemWrapper();
        for (int slot : slots) {
            ItemStack stockingItem = inventory.getItem(slot);
            if (ItemStackUtil.isItemNull(stockingItem)) {
                continue;
            }
            itemWrapper.newWrap(stockingItem);
            for (ItemWrapper stockedItem : items) {
                ItemStackUtil.stack(itemWrapper, stockedItem);
            }
            if (stockingItem.getAmount() > 0 && stockingItem.getAmount() < stockingItem.getMaxStackSize()) {
                items.add(itemWrapper.shallowClone());
            }
        }
    }

    /**
     * @return Get the List of ItemWrapper by specified slots.
     */
    public static List<ItemWrapper> getItemList(@Nonnull Inventory inventory, int[] slots) {
        List<ItemWrapper> itemWrapperList = new ArrayList<>();
        for (int filterSlot : slots) {
            if (!ItemStackUtil.isItemNull(inventory.getItem(filterSlot))) {
                itemWrapperList.add(new ItemWrapper(inventory.getItem(filterSlot)));
            }
        }
        return itemWrapperList;
    }

    /**
     * @return Get the Map of ItemWrapper by specified slots.
     */
    public static Map<Integer, ItemWrapper> getSlotItemWrapperMap(@Nonnull Inventory inventory, int[] slots) {
        Map<Integer, ItemWrapper> itemMap = new HashMap<>(slots.length);
        for (int slot : slots) {
            ItemStack item = inventory.getItem(slot);
            if (!ItemStackUtil.isItemNull(item)) {
                itemMap.put(slot, new ItemWrapper(item));
            }
        }
        return itemMap;
    }

    /**
     * @return Get the List of ItemWrapper and its amount by specified slots. The ItemStack in return list is not the same of ItemStack in the Inventory.
     */
    public static List<ItemAmountWrapper> calItemListWithAmount(@Nonnull Inventory inventory, int[] slots) {
        List<ItemAmountWrapper> itemAmountWrapperList = new ArrayList<>(slots.length);
        ItemAmountWrapper itemAmountWrapper = new ItemAmountWrapper();
        for (int slot : slots) {
            ItemStack item = inventory.getItem(slot);
            if (ItemStackUtil.isItemNull(item)) {
                continue;
            }
            itemAmountWrapper.newWrap(item);
            boolean find = false;
            for (ItemAmountWrapper existedItemWrapper : itemAmountWrapperList) {
                if (ItemStackUtil.isItemSimilar(itemAmountWrapper, existedItemWrapper)) {
                    existedItemWrapper.addAmount(item.getAmount());
                    find = true;
                    break;
                }
            }
            if (!find) {
                itemAmountWrapperList.add(itemAmountWrapper.shallowClone());
            }
        }
        return itemAmountWrapperList;
    }

    public static int calMaxMatch(@Nonnull Inventory inventory, int[] slots, @Nonnull List<ItemAmountWrapper> itemAmountWrapperList) {
        List<Integer> countList = new ArrayList<>(itemAmountWrapperList.size());
        List<Integer> stackList = new ArrayList<>(itemAmountWrapperList.size());
        int[] counts = new int[itemAmountWrapperList.size()];
        int[] stacks = new int[itemAmountWrapperList.size()];
        for (int i = 0; i < itemAmountWrapperList.size(); i++) {
            countList.add(0);
            stackList.add(0);
        }

        int emptySlot = 0;
        ItemWrapper itemWrapper = new ItemWrapper();
        for (int slot : slots) {
            ItemStack item = inventory.getItem(slot);
            if (ItemStackUtil.isItemNull(item)) {
                emptySlot++;
                continue;
            } else if (item.getAmount() >= item.getMaxStackSize()) {
                continue;
            }
            itemWrapper.newWrap(item);
            for (int i = 0; i < itemAmountWrapperList.size(); i++) {
                if (ItemStackUtil.isItemSimilar(itemWrapper, itemAmountWrapperList.get(i))) {
                    counts[i] = counts[i] + item.getMaxStackSize() - item.getAmount();
                    stacks[i] = counts[i] / itemAmountWrapperList.get(i).getAmount();
                    break;
                }
            }
        }

        while (emptySlot > 0) {
            int minStackP = 0;
            int minStack = stackList.get(0);
            for (int i = 1; i < itemAmountWrapperList.size(); i++) {
                if (minStack > stackList.get(i)) {
                    minStack = stackList.get(i);
                    minStackP = i;
                }
            }
            counts[minStackP] = counts[minStackP] + itemAmountWrapperList.get(minStackP).getItemStack().getMaxStackSize();
            countList.set(minStackP, countList.get(minStackP) + itemAmountWrapperList.get(minStackP).getItemStack().getMaxStackSize());
            stacks[minStackP] = counts[minStackP] / itemAmountWrapperList.get(minStackP).getAmount();
            stackList.set(minStackP, countList.get(minStackP) / itemAmountWrapperList.get(minStackP).getAmount());
            emptySlot--;
        }

        int min = stackList.get(0);
        for (int stack : stackList) {
            min = Math.min(min, stack);
        }
        return min;
    }

    public static int calMaxMatch(@Nonnull Inventory inventory, int[] slots, @Nonnull ItemAmountWrapper[] itemAmountWrapperList) {
        List<Integer> countList = new ArrayList<>(itemAmountWrapperList.length);
        List<Integer> stackList = new ArrayList<>(itemAmountWrapperList.length);
        for (int i = 0; i < itemAmountWrapperList.length; i++) {
            countList.add(0);
            stackList.add(0);
        }

        int emptySlot = 0;
        ItemWrapper itemWrapper = new ItemWrapper();
        for (int slot : slots) {
            ItemStack item = inventory.getItem(slot);
            if (ItemStackUtil.isItemNull(item)) {
                emptySlot++;
                continue;
            } else if (item.getAmount() >= item.getMaxStackSize()) {
                continue;
            }
            itemWrapper.newWrap(item);
            for (int i = 0; i < itemAmountWrapperList.length; i++) {
                if (ItemStackUtil.isItemSimilar(itemWrapper, itemAmountWrapperList[i])) {
                    countList.set(i, countList.get(i) + (item.getMaxStackSize() - item.getAmount()));
                    stackList.set(i, countList.get(i) / itemAmountWrapperList[i].getAmount());
                    break;
                }
            }
        }

        while (emptySlot > 0) {
            int minStackP = 0;
            int minStack = stackList.get(0);
            for (int i = 1; i < itemAmountWrapperList.length; i++) {
                if (minStack > stackList.get(i)) {
                    minStack = stackList.get(i);
                    minStackP = i;
                }
            }
            countList.set(minStackP, countList.get(minStackP) + itemAmountWrapperList[minStackP].getItemStack().getMaxStackSize());
            stackList.set(minStackP, countList.get(minStackP) / itemAmountWrapperList[minStackP].getAmount());
            emptySlot--;
        }

        int min = stackList.get(0);
        for (int stack : stackList) {
            min = Math.min(min, stack);
        }
        return min;
    }
}
