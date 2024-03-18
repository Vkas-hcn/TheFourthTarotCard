package com.date.time.dog.thefourthtarotcard

import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.date.time.dog.thefourthtarotcard.data.DownloadImageUtils
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils.Companion.getImageByName
import com.date.time.dog.thefourthtarotcard.data.TarotBean

class EndActivity : ComponentActivity() {
    var cardTurnedMain by mutableStateOf("")
    var showPermissionDialog by mutableStateOf(false)
    var downloadImage: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardTurnedMain = LocalDataUtils.cardTurnedJump
        setContent {
            TheFourthTarotCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    downloadImage = cardTurnedMain.getImageByName()
                    TarotEnd(this@EndActivity)
                }
            }
        }
    }

    fun getDetailByName(name: String): TarotBean {
        val allData = LocalDataUtils().toTarotBeanList()
        allData.forEach {
            if (it.name == name) {
                return it
            }
        }
        return TarotBean("", "", isShow = false, selectedToday = false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        DownloadImageUtils.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        ) {
            showPermissionDialog = true
        }
    }
}

@Composable
fun GreetingEnd(activity: EndActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = "Back",
                modifier = Modifier
                    .requiredSize(24.dp)
                    .clickable {
                        activity.finish()
                    },
            )
            Text(
                text = "Result",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding()
                    .weight(1f),
            )
        }
        Image(
            painterResource(
                if (activity.cardTurnedMain.isEmpty()) {
                    R.drawable.icon_car
                } else {
                    activity.cardTurnedMain.getImageByName()
                }
            ),
            contentDescription = "car",
            modifier = Modifier
                .padding(17.dp)
                .width(118.dp)
                .height(216.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            DynamicColorTextEnd(
                backgroundColor = Color(0xFFE0E0E0),
                textColor = Color(0xFF000000),
                text = "Download"
            ) {
                DownloadImageUtils.clickImage(activity)
            }
            DynamicColorTextEnd(
                backgroundColor = Color(0xFFE0E0E0),
                textColor = Color(0xFF000000),
                text = "Share"
            ) {
                DownloadImageUtils.clickShare(activity)
            }
            Spacer(modifier = Modifier.weight(1f))

        }
        Text(
            text = "${activity.cardTurnedMain}:",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold)),
            color = Color(0xFFFFFFFF),
            lineHeight = 27.sp,
            modifier = Modifier
                .padding(start = 22.dp),
            textAlign = TextAlign.Start,
        )
        val scrollState = rememberScrollState()
        Box(modifier = Modifier
            .padding(start = 24.dp, bottom = 12.dp)
            .verticalScroll(scrollState)
        ) {
            Text(
                text = activity.getDetailByName(activity.cardTurnedMain).message,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.abel, FontWeight.Bold)),
                color = Color(0xFFFFFFFF),
                lineHeight = 27.sp,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
fun TarotEnd(activity: EndActivity) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(R.drawable.icon_background_main), contentDescription = "background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.weight(1f)) {
                GreetingEnd(activity)
            }
        }
        IpAlertDialog(activity)
    }
}

@Composable
fun DynamicColorTextEnd(
    backgroundColor: Color,
    textColor: Color,
    text: String,
    clickFun: () -> Unit
) {
    val customFontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold))

    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        fontFamily = customFontFamily,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .width(112.dp)
            .background(
                color = backgroundColor, shape = RoundedCornerShape(20.dp)
            )
            .clickable { clickFun() }
            .padding(top = 10.dp, bottom = 10.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun IpAlertDialog(activity: EndActivity) {
    if (activity.showPermissionDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(text = "Tips")
            },
            text = {
                Text("Storage permission is required to save images to the gallery, please go to the settings page to grant permission.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        DownloadImageUtils.openAppSettings(activity)
                        activity.showPermissionDialog = false

                    }
                ) {
                    Text("Go to settings")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        activity.showPermissionDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewEnd() {
    TheFourthTarotCardTheme {
        TarotEnd(activity = EndActivity())
    }
}