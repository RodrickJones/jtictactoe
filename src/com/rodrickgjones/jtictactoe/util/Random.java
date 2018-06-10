package com.rodrickgjones.jtictactoe.util;

import java.util.List;

public class Random {
    private final static java.util.Random RNG = new java.util.Random();
    private Random() {}

    public static int nextInt(int upperBound) {
        return RNG.nextInt(upperBound);
    }

    public static <T> T nextElement(List<T> list) {
        return list.isEmpty() ? null : list.get(nextInt(list.size()));
    }
}
