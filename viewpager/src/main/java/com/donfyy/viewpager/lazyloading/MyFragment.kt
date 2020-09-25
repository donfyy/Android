package com.donfyy.viewpager.lazyloading

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.donfyy.viewpager.R

class MyFragment : androidx.fragment.app.Fragment() {
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    var tabIndex = 0
    lateinit var con: CountDownTimer
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "$tabIndex fragment onAttach: ")
        tabIndex = requireArguments().getInt(INTENT_INT_INDEX)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$tabIndex fragment onCreate: ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lazy_loading, null)
        imageView = view.findViewById(R.id.iv_content)
        textView = view.findViewById(R.id.tv_loading)
        con = object : CountDownTimer(1000, 100) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                handler.sendEmptyMessage(0)
            }
        }
        con.start()
        Log.d(TAG, "$tabIndex fragment onCreateView: ")
        return view
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "$tabIndex fragment onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "$tabIndex fragment onPause: ")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //        StackTraceElement element[] = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stackTraceElement : element) {
//            Log.d(TAG, tabIndex + " fragment "+ "setUserVisibleHint: " + stackTraceElement.toString());
//        }
        tabIndex = arguments!!.getInt(INTENT_INT_INDEX)
        Log.d(TAG, "$tabIndex fragment setUserVisibleHint: $isVisibleToUser")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "$tabIndex fragment onDetach: ")
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            textView!!.visibility = View.GONE
            val id: Int
            id = when (tabIndex) {
                1 -> R.drawable.a
                2 -> R.drawable.b
                3 -> R.drawable.c
                4 -> R.drawable.d
                else -> R.drawable.a
            }
            imageView!!.setImageResource(id)
            imageView!!.scaleType = ImageView.ScaleType.FIT_XY
            imageView!!.visibility = View.VISIBLE
            Log.d(TAG, "$tabIndex handleMessage: ")
            //模拟耗时工作
            try {
                Thread.sleep(40)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        con.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    companion object {
        private const val TAG = "MyFragment"
        const val INTENT_INT_INDEX = "index"
        fun newInstance(tabIndex: Int): MyFragment {
            val bundle = Bundle()
            bundle.putInt(INTENT_INT_INDEX, tabIndex)
            val fragment = MyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}