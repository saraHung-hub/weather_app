package com.sara.sara_weather.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sara.sara_weather.R
import com.sara.sara_weather.ui.Constant
import com.sara.sara_weather.ui.Model.WeatherResponse
import com.sara.sara_weather.ui.Server.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var spinnerSelectedType: String
    private lateinit var input_item: String
    private lateinit var input_item2: String

    private lateinit var type_spinner: Spinner
    private lateinit var weatherImg: ImageView
    private lateinit var input_text: EditText
    private lateinit var gps_layout: LinearLayout
    private lateinit var lat_text: EditText
    private lateinit var lon_text: EditText
    private lateinit var errorTxt: TextView
    //weather view
    private lateinit var weatherView: LinearLayout
    private lateinit var citytxt: TextView
    private lateinit var weatherStatus: TextView
    private lateinit var weatherDescription: TextView
    private lateinit var weatherHumidity: TextView
    private lateinit var temperature: TextView
    private lateinit var tempMin: TextView
    private lateinit var tempMax: TextView

    val typeList: ArrayList<String> = Constant.type as ArrayList<String>
    val itemList: ArrayList<String> = Constant.item as ArrayList<String>
    val itemList2: ArrayList<String> = Constant.item2 as ArrayList<String>
    val timeList: ArrayList<String> = Constant.time as ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        type_spinner = root.findViewById(R.id.type_spinner)
        input_text = root.findViewById(R.id.input_text)
        gps_layout = root.findViewById(R.id.gps_layout)
        lat_text = root.findViewById(R.id.lat_text)
        lon_text = root.findViewById(R.id.lon_text)
        errorTxt = root.findViewById(R.id.errorTxt)
        weatherView = root.findViewById(R.id.weatherView)
        citytxt = root.findViewById(R.id.cityTxt)
        weatherImg = root.findViewById(R.id.weatherImg)
        weatherStatus = root.findViewById(R.id.weatherStatus)
        weatherDescription = root.findViewById(R.id.weatherDescription)
        weatherHumidity = root.findViewById(R.id.weatherHumidity)
        temperature = root.findViewById(R.id.temperature)
        tempMin = root.findViewById(R.id.tempMin)
        tempMax = root.findViewById(R.id.tempMax)
        val btn: Button = root.findViewById(R.id.send_data)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        spinnerSelectedType = sharedPref?.getString(getString(R.string.selected_type), "").toString()
        input_item = sharedPref?.getString(getString(R.string.req_item), "").toString()
        input_item2 = sharedPref?.getString(getString(R.string.req_item_2), "").toString()
        if ((!spinnerSelectedType.isEmpty()) && (!input_item.isEmpty())){
            callApi(spinnerSelectedType, input_item, input_item2)
        }

        btn.setOnClickListener {
            it.hideKeyboard()
            val selectedType = type_spinner.getItemAtPosition(type_spinner.selectedItemPosition).toString()
            val inputText = input_text.text.toString()
            val latText = lat_text.text.toString()
            val lonText = lon_text.text.toString()

            if (selectedType.equals("GPS", ignoreCase = true)){
                callApi(selectedType, latText, lonText)
            }
            else{
                callApi(selectedType, inputText, "")
            }
        }

        type_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (type_spinner.getItemAtPosition(position).toString().equals("GPS", ignoreCase = true)){
                    gps_layout.visibility = View.VISIBLE
                    input_text.visibility = View.GONE
                }
                else{
                    gps_layout.visibility = View.GONE
                    input_text.visibility = View.VISIBLE
                    if (type_spinner.getItemAtPosition(position).toString().equals("City", ignoreCase = true)) {
                        input_text.hint = getString(R.string.hint_city)
                    }
                    else{
                        input_text.hint = getString(R.string.hint_zip)
                    }
                }
            }
        }
        return root
    }

    fun callApi(type: String, inputText: String, inputText2: String){

        spinnerSelectedType = type
        input_item = inputText
        input_item2 = inputText2
        if (type.equals("City", ignoreCase = true)){
            getByCity(inputText)
        }
        else if (type.equals("GPS", ignoreCase = true)){
            getByGeo(inputText, inputText2)
        }
        else{
            getByZip(inputText)
        }
    }

    fun getByGeo(lat: String, lon: String){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiService = retrofit.create<ApiService>(ApiService::class.java)
        val call: Call<WeatherResponse?>? = service.getWeaterByGeo(lat, lon, Constant.APIKEY)
        call?.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(
                call: Call<WeatherResponse?>,
                response: Response<WeatherResponse?>
            ) {
                handleResponse(response)
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                handelError(t.message.toString())
            }
        })
    }

    fun getByZip(zip: String){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiService = retrofit.create<ApiService>(ApiService::class.java)
        val call: Call<WeatherResponse?>? = service.getWeaterByZip(zip.toInt(), Constant.APIKEY)
        call?.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(
                call: Call<WeatherResponse?>,
                response: Response<WeatherResponse?>
            ) {
                handleResponse(response)
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                handelError(t.message.toString())
            }
        })
    }

    fun getByCity(city: String){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiService = retrofit.create<ApiService>(ApiService::class.java)
        val call: Call<WeatherResponse?>? = service.getWeaterByCity(city, Constant.APIKEY)
        call?.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(
                call: Call<WeatherResponse?>,
                response: Response<WeatherResponse?>
            ) {
                handleResponse(response)
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                handelError(t.message.toString())
            }
        })
    }

    fun handelError(error: String){
        errorTxt.visibility = View.VISIBLE
        errorTxt.text = error
        weatherView.visibility = View.GONE
    }

    fun handleResponse(response: Response<WeatherResponse?>){
        print(response)

        if (response.code() === 200) {
            val weatherResponse: WeatherResponse = response.body()!!
            errorTxt.visibility = View.GONE
            weatherView.visibility = View.VISIBLE
            citytxt.text = weatherResponse.name
            val imageUrl =
                Constant.URL + Constant.imageSection + weatherResponse.weather[0].icon + ".png"
            Glide.with(this).load(imageUrl).into(weatherImg)
            weatherStatus.text = weatherResponse.weather[0].main
            weatherDescription.text = weatherResponse.weather[0].description
            weatherHumidity.text = getString(R.string.weather_hum) + " " + weatherResponse.main?.humidity.toString()
            temperature.text = getString(R.string.weather_temp) + " " +  weatherResponse.main!!.temp.toString()
            tempMin.text = getString(R.string.weather_temp_min) + " " +  weatherResponse.main!!.temp_min.toString()
            tempMax.text = getString(R.string.weather_temp_max) + " " +  weatherResponse.main!!.temp_max.toString()

            //store reqest if success
            storeSuccessRequest()
        }
        else{
            handelError(response.message())
        }
    }

    fun storeSuccessRequest(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.selected_type), spinnerSelectedType)
            putString(getString(R.string.req_item), input_item)
            putString(getString(R.string.req_item_2), input_item2)
            commit()
        }
        typeList.add(spinnerSelectedType)

        itemList.add(input_item)
        itemList2.add(input_item2)

        Constant.type = typeList
        Constant.item = itemList
        Constant.item2 = itemList2
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        timeList.add(currentDate)
        Constant.time = timeList
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}