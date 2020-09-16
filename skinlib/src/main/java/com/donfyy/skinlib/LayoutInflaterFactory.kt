package com.donfyy.skinlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.LogUtils
import java.lang.reflect.Constructor
import java.util.*

class LayoutInflaterFactory : LayoutInflater.Factory2, Observer {
    companion object {
        private val sConstructorMap = HashMap<String, Constructor<out View?>>()
        private val sClassPrefixList = arrayOf(
                "android.widget.",
                "android.webkit.",
                "android.app.",
                "android.view."
        )

        private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    }

    var skinViewManager = SkinViewManager()

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return if (-1 == name.indexOf('.')) {
            // 如果创建的是系统内置的view，则为name补齐前缀，然后调用createView
            // createView(name, "android.view.", attrs)
            // onCreateView(String name, AttributeSet attrs) ，
            // 该方法被PhoneLayoutInflater子类覆盖 ，以优先使用兼容包中的View
            createSdkView(context, parent, name, attrs)
        } else {
            createView(context, name, null, attrs)
        }
    }

    private fun createSdkView(context: Context, parent: View?, name: String, attrs: AttributeSet): View? {
        sClassPrefixList.forEach {
            val view = createView(context, name, it, attrs)
            if (view != null) {
                return view
            }
        }
        return null
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    private fun createView(viewContext: Context, name: String, prefix: String?, attr: AttributeSet): View? {
        var constructor: Constructor<out View?>? = sConstructorMap[name]
        LogUtils.d("createView(prefix = $prefix,name = $name)")
        var clazz: Class<out View?>? = null
        return try {
            if (constructor == null) {
                // 通过反射获取View对应的Class字节码
                clazz = Class.forName(if (prefix != null) prefix + name else name, false,
                        viewContext.classLoader).asSubclass(View::class.java)
                // 然后获取View的第二个构造方法 View(Context.class, AttributeSet.class)
                constructor = clazz.getConstructor(*sConstructorSignature)
                constructor.isAccessible = true
                sConstructorMap[name] = constructor
            }
            // 通过反射调用构造方法创建View
            constructor.newInstance(viewContext, attr).apply { this?.let { skinViewManager.look(it, attr) } }
        } catch (e: Exception) {
            LogUtils.d(":Error inflating class ${clazz?.name}")
            null
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        skinViewManager.applySkin()
    }
}