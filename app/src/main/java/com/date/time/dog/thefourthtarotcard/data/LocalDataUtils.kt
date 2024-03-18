package com.date.time.dog.thefourthtarotcard.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.date.time.dog.thefourthtarotcard.R
import com.date.time.dog.thefourthtarotcard.TarotApp
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LocalDataUtils {
    companion object {
        const val blackUrl = "https://kickoff.mysticanswer.com/placater/prorate"
        val sharedPreferences =
            TarotApp.getVpnInstance().getSharedPreferences("tarot", Context.MODE_PRIVATE)
        var uuid_tarot = ""
            set(value) {
                sharedPreferences.edit().run {
                    putString("uuid_tarot", value)
                    commit()
                }
                field = value
            }
            get() = sharedPreferences.getString("uuid_tarot", "").toString()
        var black_data = ""
            set(value) {
                sharedPreferences.edit().run {
                    putString("black_data", value)
                    commit()
                }
                field = value
            }
            get() = sharedPreferences.getString("black_data", "").toString()
        var cardTurnedUp = ""
            set(value) {
                sharedPreferences.edit().run {
                    putString("cardTurnedUp", value)
                    commit()
                }
                field = value
            }
            get() = sharedPreferences.getString("cardTurnedUp", "").toString()

        var cardTurnedJump = ""
            set(value) {
                sharedPreferences.edit().run {
                    putString("cardTurnedUp", value)
                    commit()
                }
                field = value
            }
            get() = sharedPreferences.getString("cardTurnedUp", "").toString()
        var dailyDeck = ""
            set(value) {
                sharedPreferences.edit().run {
                    putString("dailyDeck", value)
                    commit()
                }
                field = value
            }
            get() = sharedPreferences.getString("dailyDeck", "").toString()

        fun getMapData(
            url: String,
            map: Map<String, Any>,
            onNext: (response: String) -> Unit,
            onError: (error: String) -> Unit
        ) {
            val queryParameters = StringBuilder()
            for ((key, value) in map) {
                if (queryParameters.isNotEmpty()) {
                    queryParameters.append("&")
                }
                queryParameters.append(URLEncoder.encode(key, "UTF-8"))
                queryParameters.append("=")
                queryParameters.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }

            val urlString = if (url.contains("?")) {
                "$url&$queryParameters"
            } else {
                "$url?$queryParameters"
            }

            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000

            try {
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    inputStream.close()
                    onNext(response.toString())
                } else {
                    onError("HTTP error: $responseCode")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }

        fun String.getImageByName(): Int {
            return when (this) {
                "The Fool" -> {
                    R.drawable.icon_fool
                }

                "The Magician" -> {
                    R.drawable.icon_magician
                }

                "The High Priestess" -> {
                    R.drawable.icon_high_priestess
                }

                "The Empress" -> {
                    R.drawable.icon_empress
                }

                "The Emperor" -> {
                    R.drawable.icon_emperor
                }

                "The Hierophant" -> {
                    R.drawable.icon_hierophant
                }

                "The Lovers" -> {
                    R.drawable.icon_lovers
                }

                "The Chariot" -> {
                    R.drawable.icon_chariot
                }

                "Strength" -> {
                    R.drawable.icon_strength
                }

                "The Hermit" -> {
                    R.drawable.icon_hermit
                }

                "Wheel of Fortune" -> {
                    R.drawable.icon_wheel
                }

                "Justice" -> {
                    R.drawable.icon_justice
                }

                "The Wheel of Fortune" -> {
                    R.drawable.icon_wheel
                }

                "The Hanged Man" -> {
                    R.drawable.icon_hanged
                }

                "Death" -> {
                    R.drawable.icon_death
                }
                "Temperance" ->{
                    R.drawable.icon_temperance
                }
                "The Devil" ->{
                    R.drawable.icon_devil
                }
                "The Tower"->{
                    R.drawable.icon_tower
                }
                "The Star"->{
                    R.drawable.icon_star
                }
                "The Moon"->{
                    R.drawable.icon_moon
                }
                "The Sun"->{
                    R.drawable.icon_sun
                }

                "Judgment"->{
                    R.drawable.icon_judgement
                }
                "The World"->{
                    R.drawable.icon_world
                }
                else -> {
                    R.drawable.icon_car
                }
            }
        }

    }

    fun cloakMapData(context: Context): Map<String, Any> {
        return mapOf<String, Any>(
            //distinct_id
            "alp" to (uuid_tarot),
            //client_ts
            "flatland" to (System.currentTimeMillis()),
            //device_model
            "derate" to Build.MODEL,
            //bundle_id
            "marine" to ("com.mystic.answer.book.wisdom.guide"),
            //os_version
            "labial" to Build.VERSION.RELEASE,
            //gaid
            "alizarin" to "",
            //android_id
            "burrow" to context.getAppId(),
            //os
            "argument" to "wit",
            //app_version
            "wallow" to context.getAppVersion(),
        )
    }

    private fun Context.getAppVersion(): String {
        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    @SuppressLint("HardwareIds")
    private fun Context.getAppId(): String {
        return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    private fun getBlackList(context: Context) {
        if (black_data.isNotEmpty()) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            getMapData(blackUrl, cloakMapData(context), onNext = {
                Log.e("TAG", "The blacklist request is successful：$it")
                black_data = it
            }, onError = {
                retry(it, context)
            })
        }
    }

    private fun retry(it: String, context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(10003)
            Log.e("TAG", "The blacklist request failed：$it")
            getBlackList(context)
        }
    }


    //读取本地assets文件
    fun getJson(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open(fileName)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    fun toTarotBeanList(): MutableList<TarotBean> {
        val type = object : com.google.gson.reflect.TypeToken<MutableList<TarotBean>>() {}.type
        return Gson().fromJson(
            getJson(TarotApp.instance, "localJson.json"),
            type
        )
    }

    fun toAnswerBeanList(): MutableList<TarotBean> {
        val type = object : com.google.gson.reflect.TypeToken<MutableList<TarotBean>>() {}.type
        return Gson().fromJson(
            getJson(TarotApp.instance, "localAnswerJson.json"),
            type
        )
    }

    fun getSavePackName(): MutableList<String>? {
        if (cardTurnedUp.isEmpty()) {
            return null
        }
        return cardTurnedUp.split(",").toMutableList()
    }

    fun setSavePackName(name: String) {
        cardTurnedUp = if (cardTurnedUp.isEmpty()) {
            name
        } else {
            "${cardTurnedUp},${name}"
        }
    }

    fun getAllTarotData(): MutableList<TarotBean> {
        val saveData = getSavePackName()
        val allData = toTarotBeanList()
        allData.forEach { allData ->
            saveData?.forEach { save ->
                if (allData.name == save) {
                    allData.isShow = true
                }
            }
        }
        return allData
    }

    fun getTodayTarotData(): MutableList<TarotBean> {
        if (dailyDeck.isNotEmpty()) {
            return Gson().fromJson(
                dailyDeck,
                object : com.google.gson.reflect.TypeToken<MutableList<TarotBean>>() {}.type
            )
        }
        val allData = toTarotBeanList()
        val newData = allData.shuffled().take(3).toMutableList()
        dailyDeck = Gson().toJson(newData)
        return newData
    }
}