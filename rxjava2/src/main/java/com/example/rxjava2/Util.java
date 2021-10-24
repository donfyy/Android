package com.example.rxjava2;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @author linshaoyou
 * 这个文件是 vivo 的相机负责人提供的. vivo 的机型在国内版的低版本 ROM 上, 相机的默认路径是 /scard/相机/,
 * 并且不能识别 /sdcard/DCIM/Camera/ 下面的图片. 因此保存文件的时候需要注意, 在这些特殊的 ROM 上, 保存的位置.
 * 请使用 StorageCompat.getSystemCameraDir() 方法.
 */
public class Util {

    private static final String TAG = "Util";

    private static final float VIVO_DEFAULT_OLD_ROM_VERSION = 2.5f;
    private static final float VIVO_CRITICAL_ROM_VERSION = 3.6f;
    private static DataInstance sDataInstance = new DataInstance();

    public static class DataInstance {
        private boolean sIsPathInited = false;
        private boolean sIsEngpath = false;
    }

    /**
     * just for vivo phone
     * @return  true: save to "DCIM/Camera",
     *           false: save to "相机"
     */
    public static boolean isEngPath() {
        if (!sDataInstance.sIsPathInited) {
            float romVersion = getRomVersion();
            if (isExported() || (romVersion >= VIVO_CRITICAL_ROM_VERSION)) {
                sDataInstance.sIsEngpath = true;
            }
            sDataInstance.sIsPathInited = true;
        }
        return sDataInstance.sIsEngpath;
    }

    /**
     * just for vivo phone
     * @return true: sales oversea, save to "DCIM/Camera"
     * */
    private static boolean isExported() {
        String overseas = "no";
        try {
            overseas = (String) invokeStaticMethod("android.os.SystemProperties", "get", "ro.vivo.product.overseas", "no");
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "isExported class not found!");
        } catch (Exception e) {
            Log.i(TAG, "isExported Exception e = " + e);
        }
        boolean isExported = "yes".equals(overseas);
        return isExported;
    }

    /**
     * just for vivo phone
     * @return >= 3.6f: for domestic, In Funtouch OS Gallery app show images or videos from "DCIM/Camera" and "相机" in one album
     *          < 3.6f: for domestic, In Funtouch OS Gallery app show images or videos just from "相机" in one album
     * */
    public static float getRomVersion() {
        float version = VIVO_DEFAULT_OLD_ROM_VERSION;
        try {
            version = (Float) invokeStaticMethod("android.os.FtBuild", "getRomVersion");
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "getRomVersion class not found!");
        } catch (Exception e) {
            Log.i(TAG, "getRomVersion Exception e = " + e);
        }
        return version;
    }

    private static Object invokeStaticMethod(String className, String methodName, Object... args) throws Exception {
        Class<?> ownerClass = Class.forName(className);
        Class<?>[] argsClass = new Class[args.length];
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        Method method = ownerClass.getMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(null, args);
    }
}
