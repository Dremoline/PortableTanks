package com.dremoline.portabletanks;

import com.supermartijn642.configlib.ModConfigBuilder;

import java.util.function.Supplier;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class PortableTanksConfig {

    public static final Supplier<Integer> basicTankCapacity;
    public static final Supplier<Integer> advancedTankCapacity;

    static{
        ModConfigBuilder builder = new ModConfigBuilder("portabletanks");

        basicTankCapacity = builder.comment("How much fluid should the basic tank be able to hold (in millibuckets)?").define("basicTankCapacity", 8000, 1000, 256000);
        advancedTankCapacity = builder.comment("How much fluid should the advanced tank be able to hold (in millibuckets)?").define("advancedTankCapacity", 16000, 1000, 256000);

        builder.build();
    }

    public static void init(){
        // just to cause this class to load
    }

}
