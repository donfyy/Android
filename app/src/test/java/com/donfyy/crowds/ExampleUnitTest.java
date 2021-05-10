package com.donfyy.crowds;

import android.text.TextUtils;
import android.webkit.CookieManager;

import org.junit.Test;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    interface ICallBack<T> {
        void onResult(T t);
    }

    static class MyResponse {
    }

    static class MyManager {
        public void send(Object cmd, ICallBack<MyResponse> callBack) {
            callBack.onResult(new MyResponse());
            CookieManager.getInstance().flush();
        }
    }

    @Test
    public void addition_isCorrect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MyManager myManager = new MyManager();
        Method send = myManager.getClass().getMethod("send", Object.class, ICallBack.class);
        send.invoke(myManager, null, (ICallBack<Object>) o -> {
            System.out.println(o.getClass());
        });
    }

    @Test
    public void test1() {
        System.out.println(System.currentTimeMillis() + "");
        System.out.println(String.valueOf(System.currentTimeMillis()));
    }

    @Test
    public void testFileWriter() throws Exception {
        File file = new File("build", "text");
        System.out.println("hhhh" + file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
        writeStringToFile(file.getAbsolutePath(), "88888888", "");
    }

    public static boolean writeStringToFile(String filePath, String data, String encoding) throws Exception {

        File file = new File(filePath);// 指定要写入的文件
        if (!file.exists()) { // 如果文件不存在则创建
            file.createNewFile();
        }
        // 获取该文件的缓冲输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        // 写入信息
        bufferedWriter.write(data);
        bufferedWriter.flush();// 清空缓冲区
        bufferedWriter.close();// 关闭输出流
        return true;
    }
}