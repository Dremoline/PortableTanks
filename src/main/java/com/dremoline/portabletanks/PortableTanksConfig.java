package com.dremoline.portabletanks;

import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;

import java.util.function.Supplier;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class PortableTanksConfig {

    public static final Supplier<Integer> basicTankCapacity;
    public static final Supplier<Integer> advancedTankCapacity;
    public static final Supplier<Integer> expertTankCapacity;
    public static final Supplier<Integer> ultimateTankCapacity;

    static{
        IConfigBuilder builder = ConfigBuilders.newTomlConfig("portabletanks", null, false);

        basicTankCapacity = builder.comment("How much fluid should the basic tank be able to hold (in millibuckets)?").define("basicTankCapacity", 8000, 1000, 256000);
        advancedTankCapacity = builder.comment("How much fluid should the advanced tank be able to hold (in millibuckets)?").define("advancedTankCapacity", 16000, 1000, 256000);
        expertTankCapacity = builder.comment("How much fluid should the expert tank be able to hold (in millibuckets)?").define("expertTankCapacity", 64000, 1000, 256000);
        ultimateTankCapacity = builder.comment("How much fluid should the ultimate tank be able to hold (in millibuckets)?").define("ultimateTankCapacity", 128000, 1000, 256000);

        builder.build();
    }

    public static void init(){
        // just to cause this class to load
    }

}
