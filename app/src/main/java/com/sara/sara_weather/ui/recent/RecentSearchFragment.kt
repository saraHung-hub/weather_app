package com.sara.sara_weather.ui.recent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sara.sara_weather.R
import com.sara.sara_weather.ui.Adapter.MyListAdapter
import com.sara.sara_weather.ui.Constant
import com.sara.sara_weather.ui.home.HomeFragment


class RecentSearchFragment : Fragment() {

    private lateinit var customList: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recent, container, false)
        customList = root.findViewById(R.id.customList)
        listView()
        return root
    }

    fun listView(){
        val myListAdapter = MyListAdapter(activity!!, Constant.type, Constant.item, Constant.item2, Constant.time)
        customList.adapter = myListAdapter

        customList.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref!!.edit()) {
                putString(getString(R.string.selected_type), Constant.type[itemIdAtPos.toInt()])
                putString(getString(R.string.req_item), Constant.item[itemIdAtPos.toInt()])
                putString(getString(R.string.req_item_2), Constant.item2[itemIdAtPos.toInt()])
                commit()
            }
            val fragmentManager = fragmentManager
            val transaction = fragmentManager!!.beginTransaction()

            val a = HomeFragment()
            transaction.replace(R.id.nav_host_fragment, a)
            transaction.commit()

           val mBottomNavigationView: BottomNavigationView =
                activity!!.findViewById(R.id.nav_view)

            mBottomNavigationView.selectedItemId = R.id.navigation_search
        }
    }
}