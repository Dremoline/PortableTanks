package com.dremoline.portabletanks;

import com.supermartijn642.configlib.ModConfigBuilder;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class PortableTanksConfig {

    static{
        ModConfigBuilder builder = new ModConfigBuilder("modid");

        builder.build();
    }

    public static void init(){
        // just to cause this class to load
    }

}
