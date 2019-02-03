package com.semyon.cucon

import android.util.Log
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URI

fun requestCryptoRates(): JSONObject?{
    return(request("https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH,DASH,ZEC,LTC,WAVES,EOS&tsyms=USD,EUR,RUB&api_key=$apiCryptoKey"))
}

fun requestRate(pair: String): Float? {
    val result = request("https://free.currencyconverterapi.com/api/v6/convert?q=" + pair + "&compact=ultra")
    try {
        return result!!.get(pair).toString().toFloat()
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        return null
    }
}

fun requestCurrencies(): Iterator<String>? {
    var currencies: Iterator<String>
    val result = request("https://free.currencyconverterapi.com/api/v6/currencies")
    try {
        val result2 = result!!.getJSONObject("results")
        currencies = result2.keys()
        return currencies
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        return null
    }
}

fun request(url: String): JSONObject? {
    try {
        val connection = URI(url).toURL().openConnection() as HttpURLConnection
        connection.connect()
        val text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        return (JSONObject(text))
    } catch (e: Exception) {
        Log.e("Error: ", e.toString())
        return null
    }
}