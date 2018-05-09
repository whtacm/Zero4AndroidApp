package com.robin.atm.network;

/**
 * Created by robin on 4/18/18.
 */

public class RobinFactory {
    protected static final Object monitor = new Object();
    public static boolean isDebug = true;

    static AtmAdApi atmAdApiSingleton = null;

    public static AtmAdApi getAtmAdApiSingleton() {
        synchronized (monitor) {
            if (atmAdApiSingleton == null) {
                atmAdApiSingleton = new RobinRetrofit().getAtmadService();
            }
            return atmAdApiSingleton;
        }
    }
}
