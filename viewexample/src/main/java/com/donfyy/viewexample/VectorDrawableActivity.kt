package com.donfyy.viewexample

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VectorDrawableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector_drawble)

        val ll = findViewById<LinearLayout>(R.id.ll)

        for (i in 0..500) {
            val v = layoutInflater.inflate(R.layout.activity_vector_drawble_item, ll, false)

            v.setOnClickListener(View.OnClickListener { Toast.makeText(this, "i$i", Toast.LENGTH_SHORT).show() })

            ll.addView(v)
        }
    }

    fun recreate(view: View) {
        this.recreate();
    }
}