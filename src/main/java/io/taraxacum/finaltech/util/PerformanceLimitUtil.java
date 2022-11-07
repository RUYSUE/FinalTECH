package io.taraxacum.finaltech.util;

import io.taraxacum.finaltech.FinalTech;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

import javax.annotation.Nonnull;

public class PerformanceLimitUtil {
    public static String KEY = "tps-charge";

    public static boolean charge(@Nonnull Config config) {
        int charge = config.contains(KEY) ? Integer.parseInt(config.getString(KEY)) : 0;
        charge += FinalTech.getTps();
        if (charge >= 20) {
            if (charge >= 40) {
                charge -= 20;
            }
            config.setValue(KEY, String.valueOf(charge - 1000));
            return true;
        } else {
            config.setValue(KEY, String.valueOf(charge));
            return false;
        }
    }
}
