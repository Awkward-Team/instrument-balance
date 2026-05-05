package com.awkwardinstrumentbalance;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class AIBPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        AutoConfig.register(AIBConfig.class, JanksonConfigSerializer::new);
    }
}
