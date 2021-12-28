package com.semyon.cucon

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI

fun requestCryptoRates(cryptoPair: String): JSONObject? {
    return (request("https://min-api.cryptocompare.com/data/pricemulti?fsyms=" + cryptoPair + "&tsyms=USD,EUR,RUB&api_key=" + com.semyon.cucon.BuildConfig.cryptocompare_key))
}

fun requestRate(pair: String): Float? {
    val result =
        request("https://free.currencyconverterapi.com/api/v6/convert?q=" + pair + "&compact=ultra&apiKey=" + com.semyon.cucon.BuildConfig.currencyconverterapi_key)
    try {
        return result!!.get(pair).toString().toFloat()
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        return null
    }
}

fun requestCurrencies(): Iterator<String>? {
    val currencies: Iterator<String>
    val result =
        request("https://free.currencyconverterapi.com/api/v6/currencies?apiKey=" + com.semyon.cucon.BuildConfig.currencyconverterapi_key)
    return try {
        val result2 = result!!.getJSONObject("results")
        currencies = result2.keys()
        currencies
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        null
    }
}

fun requestCryptoCurrencies(): Iterator<String>? {
    val currencies: Iterator<String>?
    val result =
        request("https://min-api.cryptocompare.com/data/v4/all/exchanges?api_key=" + BuildConfig.cryptocompare_key)
    try {
        val result2 =
            result!!.getJSONObject("Data").getJSONObject("exchanges").getJSONObject("Exmo")
                .getJSONObject("pairs")
        currencies = result2.keys()
//        currencies.forEach {
//            println(it)
//        }
        return currencies
    } catch (e: Exception) {
        println(e.message.toString())
    }
    return null
}

fun request(url: String): JSONObject? {
    return try {
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.connect()
        val text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        (JSONObject(text))
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        null
    }
}

fun isInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}