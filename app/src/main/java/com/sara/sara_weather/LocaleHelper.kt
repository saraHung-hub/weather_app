package com.sara.sara_weather

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object LocaleHelper {
    private lateinit var mSharedPreferences: SharedPreferences
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    private const val COUNTRY = "country"
    private const val LANG = "lang"

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun onAttach(context: Context?): Context? {
        saveSettingLang(context, Locale.getDefault().language, Locale.getDefault().country)
        return initLocale(context, readSettingLang(), readSettingCountry())
    }

    //進行本地化
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun initLocale(context: Context?, lang: String?, country: String? = ""): Context? {
        saveSettingLang(context, lang!!, country!!)

        if (VersionUtils.isAfter24) {
            return updateResources(context, lang, country)
        }
        return updateResourcesLegacy(context, lang, country)
    }

    //region 因應 API Level 取得相對應context更新方法
    //updateResources為7.0後
    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context?, lang: String, country: String): Context? {
        val locale = Locale(lang, country)
        Locale.setDefault(locale)

        val configuration: Configuration? = context?.resources?.configuration
        if (configuration != null) {
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
        }

        return context?.createConfigurationContext(configuration!!)
    }

    //updateResourcesLegacy為7.0前
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun updateResourcesLegacy(context: Context?, lang: String, country: String): Context? {
        val locale = Locale(lang, country)
        Locale.setDefault(locale)

        val resource = context?.resources

        val configuration: Configuration? = resource?.configuration
        if (configuration != null) {
            configuration.locale = locale
            if (VersionUtils.isAfter11)
                configuration.setLayoutDirection(locale)
            resource.updateConfiguration(configuration, resource.displayMetrics)
        }

        return context
    }
    //endregion

    //region 讀寫設定語言及國家
    private fun readSettingLang(): String? {
        return mSharedPreferences.getString(LANG, "")
    }

    private fun readSettingCountry(): String? {
        return mSharedPreferences.getString(COUNTRY, "")
    }

    private fun saveSettingLang(context: Context?, lang: String, country: String) {
        if (context != null) {
            mSharedPreferences = context.getSharedPreferences(SELECTED_LANGUAGE, 0)
        }
        mSharedPreferences.edit()
            .putString(LANG, lang)
            .putString(COUNTRY, country)
            .apply()
    }
    //endregion
}