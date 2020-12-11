package com.donfyy.crowds;

import org.junit.Test;


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
}