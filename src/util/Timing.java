package util;

public class Timing {

    public static void waitFor(long nanoseconds) {
        long elapsedTime = 0;
        final long startTime = System.nanoTime();
        while(elapsedTime < nanoseconds) {
            elapsedTime = System.nanoTime() - startTime;
        }
    }

    public static long secondsToNano(long seconds) { return seconds * ((long) Math.pow(10, 9)); }

    public static long millisecondsToNano(long milliseconds) { return milliseconds * ((long) Math.pow(10, 6)); }

    public static double nanoToSeconds(long nano) { return nano / ((double) Math.pow(10, 9)); }

}
