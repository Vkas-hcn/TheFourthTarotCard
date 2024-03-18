package com.date.time.dog.thefourthtarotcard

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.date.time.dog.thefourthtarotcard.data.AppUsageTracker
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class TarotApp : Application(), LifecycleObserver {
    companion object {
        lateinit var instance: TarotApp
        fun getVpnInstance(): TarotApp {
            return instance
        }
    }

    private var appBackgroundTimestamp: Long = 0

    override fun onCreate() {
        super.onCreate()
        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        LocalDataUtils.uuid_tarot = UUID.randomUUID().toString()
        getBlackList(this)
        isTurnOverCards()
    }

    private fun getBlackList(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            LocalDataUtils.getMapData(
                LocalDataUtils.blackUrl,
                LocalDataUtils().cloakMapData(context),
                onNext = {
                    Log.e("TAG", "The blacklist request is successful：$it")
                    LocalDataUtils.black_data = it
                },
                onError = {
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(10000)
                        Log.e("TAG", "The blacklist request failed：$it")
                        getBlackList(context)
                    }
                })
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        if ((System.currentTimeMillis() - appBackgroundTimestamp) >= 3000) {
            restartApp()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopState() {
        appBackgroundTimestamp = System.currentTimeMillis()

    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun isTurnOverCards() {
        val appUsageTracker = AppUsageTracker(this)
        val isFirstOpen = appUsageTracker.isFirstOpenToday()
        if (isFirstOpen) {
            LocalDataUtils.cardTurnedUp =""
            LocalDataUtils.dailyDeck = ""
        }
    }
}