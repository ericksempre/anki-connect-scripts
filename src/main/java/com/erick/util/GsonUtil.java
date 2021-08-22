package com.erick.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtil {
    public static Gson buildGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
