package purogu.stoproaming;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ServerConfig {
    public ForgeConfigSpec.ConfigValue<List<? extends String>> entitiesToStop;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        List<String> defaultEntityList = Arrays.asList("cow", "chicken", "sheep", "pig");
        entitiesToStop = builder.worldRestart()
                .comment("Names for entities to prevent from moving")
                .defineList("entity-list", defaultEntityList, s -> s instanceof String);
    }
}
