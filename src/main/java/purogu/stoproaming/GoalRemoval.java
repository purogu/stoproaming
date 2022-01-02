package purogu.stoproaming;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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

    private static boolean shouldRemoveGoal(PrioritizedGoal goal) {
        return goal.getGoal() instanceof RandomWalkingGoal;
    }

    @SuppressWarnings("unchecked")
    private static void removeRoamingGoals(GoalSelector goalSelector) throws IllegalAccessException {
        Field goalsField = ObfuscationReflectionHelper.findField(GoalSelector.class, "field_220892_d");
        Set<PrioritizedGoal> goals = (Set<PrioritizedGoal>) goalsField.get(goalSelector);
        goals.stream().filter(GoalRemoval::shouldRemoveGoal).filter(PrioritizedGoal::isRunning).forEach(PrioritizedGoal::stop);
        goals.removeIf(GoalRemoval::shouldRemoveGoal);
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) throws Exception {
        if(!event.getWorld().isClientSide) {
            Entity entity = event.getEntity();
            String name = ForgeRegistries.ENTITIES.getKey(entity.getType()).getPath();
            if(validEntities.contains(name)) {
                if(entity instanceof MobEntity) {
                    MobEntity mob = (MobEntity) entity;
                    removeRoamingGoals(mob.goalSelector);
                }
            }
        }
    }
}
