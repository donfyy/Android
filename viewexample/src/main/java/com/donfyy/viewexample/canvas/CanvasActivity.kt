package com.donfyy.viewexample.canvas

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.donfyy.viewexample.R

class CanvasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
    }

    private fun showActivity(clazz: Class<out AppCompatActivity>) {
        startActivity(Intent(this, clazz))
    }

    fun onShowCompose(view: View) {
        showActivity(CanvasComposeActivity::class.java)
    }

    fun onShowDraw(view: View) {
        showActivity(DrawActivity::class.java)
    }

    fun onShowStack(view: View) {
        showActivity(StackActivity::class.java)
    }

    fun onShowTransformation(view: View) {
        showActivity(TransformationActivity::class.java)
    }

    fun onShowMatrix(view: View) {
        showActivity(MatrixActivity::class.java)
    }

    fun onShowRoundImage(view: View) {
        showActivity(BitmapDrawActivity::class.java)
    }

    fun onShowXfermodes(view: View) {
        showActivity(XfermodesActivity::class.java)
    }
    fun onShowTextMultiDraw(view: View) {
        showActivity(TextMultiDrawActivity::class.java)
    }
}