package com.work.sqlServerProject.Helper;

import java.util.Random;

/**
 * Created by a.shcherbakov on 08.07.2019.
 */
public class ColorHelper {
    // Member variables (properties about the object)
    public static String[] mColors = {
            "#e15258", // red
            "#51b46d", // green
            "#637a91", // dark gray
            "#3079ab", // dark blue
            "#f9845b", // orange
            "#39add1", // light blue
            "#c25975", // mauve
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#e0ab18", // mustard
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

    // Method (abilities: things the object can do)
    public static String getColor() {
        String color = "";

        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);

        color = mColors[randomNumber];

        return color;
    }
}
