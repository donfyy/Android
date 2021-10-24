package com.donfyy.crowds;

import android.content.Context;
import android.net.Uri;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.File;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.donfyy.androidcrowds", appContext.getPackageName());
    }

    @Test
    public void testJson() throws JSONException {
        JSONObject jsonObject = new JSONObject("{text: 1}");
        String text = jsonObject.optString("text");
        System.out.println("text:" + text);
        assertTrue(text, text == null);
    }

    @Test
    public void testUri() {
        Uri userUri = Uri.parse("ttfile://user/xxx.txt");
        assertTrue("test scheme", "ttfile".equals(userUri.getScheme()));
        assertTrue("test authority", "user".equals(userUri.getAuthority()));
        assertTrue(userUri.getPath(), "/xxx.txt".equals(userUri.getPath()));


        Uri uri = Uri.fromFile(new File("/1634542614546_滴滴出行行程报销单[12].pdf"));
        assertTrue(uri.getPath(), "/1634542614546_滴滴出行行程报销单[12].pdf".equals(uri.getPath()));
        Uri uri1 = Uri.parse("ttfile://user/1634542614546_滴滴出行行程报销单[12].pdf");
        assertTrue(uri1.getScheme(), "ttfile".equals(uri1.getScheme()));
        assertTrue(uri1.getAuthority(), "user".equals(uri1.getAuthority()));
        assertTrue(uri1.getPath(),"/1634542614546_滴滴出行行程报销单[12].pdf".equals(uri1.getPath()));
    }
}
