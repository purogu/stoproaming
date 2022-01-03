package purogu.stoproaming;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoalRemoval {

    private static Set<String> validEntities = new HashSet<>();

    public static void init(List<? extends String> entityList) {
        validEntities = new HashSet<>();
        validEntities.addAll(entityList);
    }

    private static boolean shouldRemoveGoal(WrappedGoal goal) {
        return goal.getGoal() instanceof RandomStrollGoal;
    }

    @SuppressWarnings("unchecked")
    private static void removeRoamingGoals(GoalSelector goalSelector) throws IllegalAccessException {
        Field goalsField = ObfuscationReflectionHelper.findField(GoalSelector.class, "f_25345_");
        Set<WrappedGoal> goals = (Set<WrappedGoal>) goalsField.get(goalSelector);
        goals.stream().filter(GoalRemoval::shouldRemoveGoal).filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
        goals.removeIf(GoalRemoval::shouldRemoveGoal);
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) throws Exception {
        if(!event.getWorld().isClientSide) {
            Entity entity = event.getEntity();
            String name = ForgeRegistries.ENTITIES.getKey(entity.getType()).getPath();
            if(validEntities.contains(name)) {
                if(entity instanceof Mob) {
                    Mob mob = (Mob) entity;
                    removeRoamingGoals(mob.goalSelector);
                }
            }
        }
    }
}
