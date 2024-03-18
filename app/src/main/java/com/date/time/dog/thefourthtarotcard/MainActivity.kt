package com.date.time.dog.thefourthtarotcard

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.date.time.dog.thefourthtarotcard.ui.theme.TheFourthTarotCardTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils.Companion.getImageByName
import com.date.time.dog.thefourthtarotcard.data.TarotBean

class MainActivity : ComponentActivity() {
    var cardTurnedMain by mutableStateOf("")
    val cards = LocalDataUtils().getTodayTarotData()
    lateinit var card: TarotBean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = cards.random()
        setContent {
            TheFourthTarotCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TarotMain(this@MainActivity)
                }
            }
        }
    }

    fun jumpToDetail() {
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }

    fun jumpToEnd(cardTurnedMain: String) {
        val intent = Intent(this, EndActivity::class.java)
        LocalDataUtils.cardTurnedJump = cardTurnedMain
        startActivity(intent)
    }

    fun jumpToWeb() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("http://www.baidu.com"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "There is currently no browser installed on your phone",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun GreetingMain(activity: MainActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.drawable.icon_setting), contentDescription = "setting",
                modifier = Modifier
                    .padding(16.dp)
                    .requiredSize(24.dp)
                    .clickable {
                        activity.jumpToWeb()
                    },
                contentScale = ContentScale.Crop
            )
        }
        if (activity.cardTurnedMain.isEmpty()) {
            CarouselCards(activity)
        } else {
            Box(
                modifier = Modifier
                    .width(205.dp)
                    .height(360.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(
                        activity.cardTurnedMain.getImageByName()
                    ),
                    contentDescription = "car",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            activity.jumpToEnd(activity.cardTurnedMain)
                        }
                )
            }
        }
        val customFontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold))
        val customFontFamily2 = FontFamily(Font(R.font.abel, FontWeight.Bold))
        Text(
            text = "Daily Tarot Card",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = customFontFamily,
            color = Color(0xFFFFFFFF),
            lineHeight = 400.sp,
            modifier = Modifier
                .padding(top = 12.dp)
        )
        if (activity.cardTurnedMain.isNotEmpty()) {
            Text(
                text = "Today's Tarot has been opened, Come back tomorrow!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xBDFFFFFF),
                fontFamily = customFontFamily2,
                modifier = Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Cards are ready,let’s begin!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0x80FFFFFF),
                fontFamily = customFontFamily2,
                modifier = Modifier
            )
            Text(
                text = "Enter",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0x80FFFFFF),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable {
                        if (activity.cardTurnedMain.isEmpty()) {
                            LocalDataUtils.cardTurnedUp = activity.card.name
                            activity.cardTurnedMain = LocalDataUtils.cardTurnedUp
                        }
                    }
                    .background(
                        Color(0xFF9095BB), shape = RoundedCornerShape(28.dp)
                    )
                    .padding(top = 13.dp, bottom = 13.dp, start = 122.dp, end = 122.dp),
            )
        }
    }
}

@Composable
fun ReadingListMain(activity: MainActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val allData = LocalDataUtils().getAllTarotData()
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Tarot Card Meanings",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.abrilfatface, FontWeight.Bold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        LazyColumn {
            items(allData) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            activity.jumpToEnd(index.name)
                        }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painterResource(R.drawable.bg_item), contentDescription = "background",
                        modifier = Modifier

                            .fillMaxSize()
                            .alpha(0.4f),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = index.name.getImageByName()),
                            contentDescription = "Country Flag",
                            modifier = Modifier
                                .width(39.dp)
                                .height(72.dp),
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 21.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = index.name,
                                color = Color(0xFFFFFFFF),
                                fontSize = 17.sp,
                                modifier = Modifier,
                            )
                            Text(
                                text = index.message,
                                maxLines = 2,
                                color = Color(0xFFFFFFFF),
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnswersMain(activity: MainActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = "The Book of Answers",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .width(184.dp)
                .height(300.dp), contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.icon_book),
                contentDescription = "book",
                modifier = Modifier
                    .fillMaxSize(),
            )
            Text(
                text = "Open",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .width(139.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(0xE696AAF3), shape = RoundedCornerShape(28.dp)
                    )
                    .clickable {
                        activity.jumpToDetail()
                    }
                    .padding(top = 13.dp, bottom = 13.dp),
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = "Pay attention to your questions, click to start when you're ready, and receive your answers",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xBDFFFFFF),
            fontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold)),
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .padding(start = 64.dp, end = 64.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TarotMain(activity: MainActivity) {
    var bottomSerialNum by remember { mutableIntStateOf(1) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(
                if (activity.cardTurnedMain.isEmpty()) {
                    R.drawable.icon_background_main
                } else {
                    R.drawable.bg_open
                }
            ), contentDescription = "background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.weight(1f)) {
                when (bottomSerialNum) {
                    1 -> {
                        GreetingMain(activity)
                    }

                    2 -> {
                        ReadingListMain(activity)
                    }

                    3 -> {
                        AnswersMain(activity)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth()
                    .background(color = Color(0x33FFFFFF), shape = RoundedCornerShape(28.dp))
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DynamicColorText(
                    backgroundColor = if (bottomSerialNum == 1) {
                        Color(0xFFFFFFFF)
                    } else {
                        Color(0x00FFFFFF)
                    },
                    textColor = if (bottomSerialNum == 1) {
                        Color(0xFF353535)
                    } else {
                        Color(0xFFFFFFFF)
                    },
                    text = "Divination"
                ) {
                    bottomSerialNum = 1
                }
                DynamicColorText(
                    backgroundColor = if (bottomSerialNum == 2) {
                        Color(0xFFFFFFFF)
                    } else {
                        Color(0x00FFFFFF)
                    },
                    textColor = if (bottomSerialNum == 2) {
                        Color(0xFF353535)
                    } else {
                        Color(0xFFFFFFFF)
                    },
                    text = "Reading List"
                ) {
                    bottomSerialNum = 2
                }
                DynamicColorText(
                    backgroundColor = if (bottomSerialNum == 3) {
                        Color(0xFFFFFFFF)
                    } else {
                        Color(0x00FFFFFF)
                    },
                    textColor = if (bottomSerialNum == 3) {
                        Color(0xFF353535)
                    } else {
                        Color(0xFFFFFFFF)
                    },
                    text = "Answers"
                ) {
                    bottomSerialNum = 3
                }
            }
        }

    }

}

@Composable
fun DynamicColorText(backgroundColor: Color, textColor: Color, text: String, clickFun: () -> Unit) {
    Text(
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        fontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold)),
        modifier = Modifier
            .padding(start = 6.dp, end = 6.dp)
            .background(
                color = backgroundColor, shape = RoundedCornerShape(20.dp)
            )
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
            .clickable {
                clickFun()
            }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CarouselCards(activity: MainActivity) {
    val listState = rememberLazyListState()
    activity.cardTurnedMain = LocalDataUtils.cardTurnedUp
    LazyRow(
        state = listState,
        modifier = Modifier
            .padding(top = 30.dp)
            .height(360.dp)
            .fillMaxWidth(),
    ) {
        items(activity.cards) { data ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(
                        if (activity.cardTurnedMain == activity.card.name) {
                            activity.card.name.getImageByName()
                        } else {
                            R.drawable.icon_car
                        }
                    ),
                    contentDescription = "car",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            if (activity.cardTurnedMain.isEmpty()) {
                                LocalDataUtils.cardTurnedUp = activity.card.name
                                activity.cardTurnedMain = LocalDataUtils.cardTurnedUp
                            }
                        }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheFourthTarotCardTheme {
        GreetingMain(activity = MainActivity())
    }
}