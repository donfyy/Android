// IServiceManager.aidl
package com.donfyy.crowds;

// Declare any non-default types here with import statements

interface IServiceManager {

    /* Allows services to dump sections in protobuf format. */
//    const int DUMP_FLAG_PROTO = 1 << 4;

    IBinder getService(@utf8InCpp String name);
    oneway void listService();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}