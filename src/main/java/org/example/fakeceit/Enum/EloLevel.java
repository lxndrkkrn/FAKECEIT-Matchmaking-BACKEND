package org.example.fakeceit.Enum;

import java.util.Arrays;

public enum EloLevel {

    LEVEL_1(0, 500),
    LEVEL_2(501, 750),
    LEVEL_3(751, 900),
    LEVEL_4(901, 1050),
    LEVEL_5(1051, 1200),
    LEVEL_6(1201, 1350),
    LEVEL_7(1351, 1530),
    LEVEL_8(1531, 1750),
    LEVEL_9(1751, 2000),
    LEVEL_10(2001, Integer.MAX_VALUE);

    private final int min;
    private final int max;

    EloLevel(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static EloLevel getLevelById(int elo) {

        return Arrays.stream(values())
                .filter(level -> elo >= level.min && elo <= level.max)
                .findFirst()
                .orElse(LEVEL_1);
    }
}
