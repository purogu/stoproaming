package purogu.stoproaming;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(StopRoaming.ID)
public class StopRoaming
{
    public static final String ID = "stoproaming";

    public StopRoaming() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }

    @SubscribeEvent
    public static void onLoad(ModConfig.Loading event) {
        if(event.getConfig().getModId().equals(StopRoaming.ID)) {
            GoalRemoval.init(Config.SERVER.entitiesToStop.get());
        }
    }

//    @SubscribeEvent
//    public static void onReload(ModConfig.Reloading event) {
//        System.out.println("RELOAD! id=" + event.getConfig().getModId() + " type=" + event.getConfig().getType().name());
//        GoalRemoval.init(Config.SERVER.entitiesToStop.get());
//    }
}
