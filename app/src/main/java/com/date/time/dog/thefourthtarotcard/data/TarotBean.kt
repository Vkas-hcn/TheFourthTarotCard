package com.date.time.dog.thefourthtarotcard.data

import androidx.annotation.Keep

@Keep
data class TarotBean(
    var name:String,
    var message:String,
    var isShow:Boolean,
    var selectedToday:Boolean
)

