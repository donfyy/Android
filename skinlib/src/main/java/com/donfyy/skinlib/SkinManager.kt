package com.donfyy.skinlib

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.blankj.utilcode.util.LogUtils
import java.util.*

interface ISKinResource {
    fun getBackground(resId: Int): Any
    fun getColor(resId: Int): Int
    fun getDrawable(resId: Int): Drawable
    fun getIdentifier(resId: Int): Int
    fun getColorStateList(resId: Int): ColorStateList
}

object SkinManager : Observable() {
    lateinit var skinResources: SkinResources
    lateinit var context: Context
    lateinit var sp: SkinPreference

    @JvmStatic
    // 初始化插件化换肤框架
    fun init(app: Application) {
        context = app
        // 使用SharedPreference保存当前用户使用的皮肤
        sp = SkinPreference(app)
        // 注册Activity的生命周期回调，实现Activity无入侵性换肤
        app.registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
        // 加载用户选择的皮肤
        loadSkin(sp.skinPath)
    }

    @JvmStatic
    fun loadSkin(skinPath: String?) {
        if (skinPath.isNullOrEmpty()) {
            skinResources = SkinResources(context.resources)
            sp.reset()
        } else {
            try {
                val appResources = context.resources
                val assetManager = AssetManager::class.java.newInstance()
                val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                addAssetPath.invoke(assetManager, skinPath)
                val skinResource = Resources(assetManager, appResources.displayMetrics, appResources.configuration)
                val packageName = context.packageManager?.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)?.packageName
                sp.skinPath = skinPath
                skinResources = SkinResources(appResources, skinResource, packageName)
            } catch (e: Exception) {
                skinResources = SkinResources(context.resources)
                sp.reset()
                LogUtils.d("Exception: ", e)
            }
        }
        setChanged()
        notifyObservers()
    }

    class SkinResources(private val appResources: Resources,
                        private val skinResources: Resources? = null,
                        private val pkgName: String? = null) : ISKinResource {
        private val isUsingDefaultSkin = skinResources == null || pkgName.isNullOrEmpty()
        override fun getBackground(resId: Int): Any {
            val typeName = appResources.getResourceTypeName(resId)
            return if (typeName == "color") getColor(resId)
            else getDrawable(resId)
        }

        override fun getColorStateList(resId: Int): ColorStateList {
            return if (isUsingDefaultSkin) {
                appResources.getColorStateList(resId)
            } else {
                val skinId = getIdentifier(resId)
                if (skinId == 0) appResources.getColorStateList(resId)
                else skinResources!!.getColorStateList(skinId)
            }
        }
        override fun getColor(resId: Int): Int {
            return if (isUsingDefaultSkin) {
                appResources.getColor(resId)
            } else {
                val skinId = getIdentifier(resId)
                if (skinId == 0) appResources.getColor(resId)
                else skinResources!!.getColor(skinId)
            }
        }

        override fun getDrawable(resId: Int): Drawable {
            return if (isUsingDefaultSkin) {
                appResources.getDrawable(resId)
            } else {
                val skinId = getIdentifier(resId)
                if (skinId == 0) appResources.getDrawable(resId)
                else skinResources!!.getDrawable(skinId)
            }
        }

        override fun getIdentifier(resId: Int): Int {
            if (isUsingDefaultSkin) return resId
            val typeName = appResources.getResourceTypeName(resId)
            val entryName = appResources.getResourceEntryName(resId)
            return skinResources?.getIdentifier(entryName, typeName, pkgName) ?: resId
        }
    }
}