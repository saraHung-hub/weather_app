package com.sara.sara_weather.ui.countries

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.sara.sara_weather.LocaleHelper
import com.sara.sara_weather.MainActivity
import com.sara.sara_weather.R


class CountriesFragment : Fragment() {
    var lang = ""
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_countries, container, false)
        val australia_btn: Button = root.findViewById(R.id.australia_btn)
        val canada_btn: Button = root.findViewById(R.id.canada_btn)
        australia_btn.setOnClickListener {
            lateinit var resources: Resources
            lang = "en"
            val context: Context? = LocaleHelper.initLocale(activity, "en", "rAU")
            if (context != null) {
                resources = context.resources

            }
            updateView(resources)
        }

        canada_btn.setOnClickListener {
            lateinit var resources: Resources
            lang = "cr"
            val context: Context? = LocaleHelper.initLocale(activity, "cr", "rCA")
            if (context != null) {
                resources = context.resources

            }
            updateView(resources)
        }
        return root
    }


    private fun updateView(resources: Resources) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.lang), lang)
            commit()
        }
        activity?.runOnUiThread {
            if (lang.equals("en", ignoreCase = true)){
                (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0000ff")))
            }
            else{
                (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#a4c639")))
            }
            (activity as MainActivity).supportActionBar?.title = resources.getText(R.string.app_name)

        }
    }


}