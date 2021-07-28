package com.donfyy.crowds;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;


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
}
