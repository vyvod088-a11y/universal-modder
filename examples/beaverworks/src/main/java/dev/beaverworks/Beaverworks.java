package dev.beaverworks;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

@Mod(Beaverworks.ID)
public final class Beaverworks {
    public static final String ID = "beaverworks";
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, ID);
    public static final RegistryObject<Item> FIBER = item("beaver_fiber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HELMET = armor("beaver_helmet", ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> CHESTPLATE = armor("beaver_chestplate", ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> LEGGINGS = armor("beaver_leggings", ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> BOOTS = armor("beaver_boots", ArmorItem.Type.BOOTS);
    public static final RegistryObject<Item> AXE = item("beaver_axe", () -> new AxeItem(Tiers.IRON, 6.0F, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> PICKAXE = item("beaver_pickaxe", () -> new PickaxeItem(Tiers.IRON, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> SHOVEL = item("beaver_shovel", () -> new ShovelItem(Tiers.IRON, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> HOE = item("beaver_hoe", () -> new HoeItem(Tiers.IRON, -2, -1.0F, new Item.Properties()));
    public static final RegistryObject<Item> SWORD = item("beaver_sword", () -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()));

    public Beaverworks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        TABS.register(bus);
        MinecraftForge.EVENT_BUS.addListener(Beaverworks::livingTick);
        MinecraftForge.EVENT_BUS.addListener(Beaverworks::breakSpeed);
    }
    static {
        TABS.register("beaverworks", () -> CreativeModeTab.builder()
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.beaverworks"))
            .icon(() -> new ItemStack(AXE.get()))
            .displayItems((parameters, output) -> ITEMS.getEntries().forEach(i -> output.accept(i.get())))
            .build());
    }
    private static RegistryObject<Item> item(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }
    private static RegistryObject<Item> armor(String name, ArmorItem.Type type) {
        return item(name, () -> new ArmorItem(BeaverMaterial.INSTANCE, type, new Item.Properties()));
    }
    private static boolean fullSet(Player p) {
        return p.getItemBySlot(EquipmentSlot.HEAD).is(HELMET.get())
            && p.getItemBySlot(EquipmentSlot.CHEST).is(CHESTPLATE.get())
            && p.getItemBySlot(EquipmentSlot.LEGS).is(LEGGINGS.get())
            && p.getItemBySlot(EquipmentSlot.FEET).is(BOOTS.get());
    }
    private static void livingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player p && !p.level().isClientSide
            && p.tickCount % 20 == 0 && p.isInWater() && fullSet(p)) {
            p.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, true, false, true));
        }
    }
    private static void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getEntity().getMainHandItem().is(AXE.get()) && event.getState().is(BlockTags.LOGS)) {
            event.setNewSpeed(event.getNewSpeed() * 1.5F);
        }
    }
    private enum BeaverMaterial implements ArmorMaterial {
        INSTANCE;
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) { case HELMET -> 195; case CHESTPLATE -> 285; case LEGGINGS -> 270; case BOOTS -> 225; };
        }
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) { case HELMET, BOOTS -> 2; case CHESTPLATE -> 6; case LEGGINGS -> 5; };
        }
        public int getEnchantmentValue() { return 15; }
        public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_LEATHER; }
        public Ingredient getRepairIngredient() { return Ingredient.of(FIBER.get()); }
        public String getName() { return ID + ":beaver"; }
        public float getToughness() { return 0.0F; }
        public float getKnockbackResistance() { return 0.0F; }
    }
}
