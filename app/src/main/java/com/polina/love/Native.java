package com.polina.love;

final class Native {
    static { System.loadLibrary("polinalove"); }
    static native boolean checkPassword(String input);
    static native float[] heartTags(float cx, float cy, float scale, int count, long seed);
}
