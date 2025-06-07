package gay.paradox.utils;
import gay.paradox.mixin.ClientPlayerInteractionManagerAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.SwordItem;

public final class InventoryUtils implements Imports {
    public static void setInvSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).syncSlot();
    }

    public static boolean selectItemFromHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            inv.selectedSlot = i;
            return true;
        }
        return false;
    }

    public static boolean selectItemFromHotbar(Item item) {
        return selectItemFromHotbar(i -> i == item);
    }

    public static boolean hasItemInHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            return true;
        }
        return false;
    }

    public static int countItem(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        int count = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            count += itemStack.getCount();
        }
        return count;
    }

    public static int countItemExceptHotbar(Predicate<Item> item) {
        PlayerInventory inv = mc.player.getInventory();
        int count = 0;
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = inv.getStack(i);
            if (!item.test(itemStack.getItem())) continue;
            count += itemStack.getCount();
        }
        return count;
    }

    public static int getSwordSlot() {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (!(inv.getStack(i).getItem() instanceof SwordItem)) continue;
            return i;
        }
        return -1;
    }

    public static boolean selectSword() {
        int slot = getSwordSlot();
        if (slot != -1) {
            setInvSlot(slot);
            return true;
        }
        return false;
    }

    public static int findTotemSlot() {
        assert mc.player != null;
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 9; i < 36; i++) {
            if (inv.getStack(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
            return i;
        }
        return -1;
    }

    public static boolean selectAxe() {
        int slot = getAxeSlot();
        if (slot != -1) {
            setInvSlot(slot);
            return true;
        }
        return false;
    }

    public static int findRandomTotemSlot() {
        PlayerInventory inv = mc.player.getInventory();
        List<Integer> totemSlots = new ArrayList<>();
        for (int i = 9; i < 36; i++) {
            if (inv.getStack(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
            totemSlots.add(i);
        }
        return totemSlots.isEmpty() ? -1 : totemSlots.get(new Random().nextInt(totemSlots.size()));
    }

    public static int findRandomPot(String potion) {
        PlayerInventory inv = mc.player.getInventory();
        Random random = new Random();
        int startSlot = random.nextInt(27) + 9;
        
        for (int i = 0; i < 27; i++) {
            int index = (startSlot + i) % 36;
            ItemStack stack = inv.getStack(index);
            
            if (!(stack.getItem() instanceof SplashPotionItem)) continue;
            if (index >= 36 && index <= 39) continue;
            
            PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
            if (contents.getEffects().toString().contains(potion)) {
                return index;
            }
        }
        return -1;
    }

    public static List<Integer> getEmptyHotbarSlots() {
        PlayerInventory inv = mc.player.getInventory();
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).isEmpty()) {
                slots.add(i);
            }
        }
        return slots;
    }

    public static int getAxeSlot() {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (!(inv.getStack(i).getItem() instanceof AxeItem)) continue;
            return i;
        }
        return -1;
    }

    public static int countItem(Item item) {
        return countItem(i -> i == item);
    }
}
