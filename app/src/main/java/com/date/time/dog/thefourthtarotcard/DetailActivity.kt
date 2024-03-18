package com.date.time.dog.thefourthtarotcard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
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
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import com.date.time.dog.thefourthtarotcard.data.DownloadImageUtils
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils
import com.date.time.dog.thefourthtarotcard.data.LocalDataUtils.Companion.getImageByName
import com.date.time.dog.thefourthtarotcard.data.TarotBean
import kotlinx.coroutines.launch
import kotlin.random.Random

class DetailActivity : ComponentActivity() {
    var cardPosition by mutableIntStateOf(0)
    lateinit var allData: MutableList<TarotBean>
    var answerText by mutableStateOf("")
    lateinit var layoutDow: View
    var showPermissionDialog by mutableStateOf(false)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allData = LocalDataUtils().toAnswerBeanList()
        cardPosition = Random.nextInt(allData.size)
        answerText = allData[cardPosition].message
        setContent {
            TheFourthTarotCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TarotDetail(this@DetailActivity)
                }
            }
        }
    }

    fun captureBitmapFromView() {
        layoutDow.post {
            val bitmap =
                Bitmap.createBitmap(layoutDow.width, layoutDow.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layoutDow.draw(canvas)
            DownloadImageUtils.clickImageBitMap(this@DetailActivity, bitmap)
        }
    }

    fun shareBitmapFromView() {
        layoutDow.post {
            val bitmap =
                Bitmap.createBitmap(layoutDow.width, layoutDow.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layoutDow.draw(canvas)
            DownloadImageUtils.clickShare(this, bitmap)
        }
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

@SuppressLint("MissingInflatedId")
@Composable
fun dowView(activity: DetailActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(modifier = Modifier.fillMaxWidth(), factory = {
            activity.layoutDow = LayoutInflater.from(activity).inflate(R.layout.layout_dow, null)
            val tvData = activity.layoutDow.findViewById<TextView>(R.id.tv_message)
            tvData.text = activity.allData[activity.cardPosition].message
            activity.layoutDow
        })
    }
}

@Composable
fun GreetingDetail(activity: DetailActivity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 17.dp)
                .width(312.dp)
                .height(631.dp), contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.bg_detail),
                contentDescription = "detail background",
                modifier = Modifier
                    .fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, bottom = 65.dp),
                contentAlignment = Alignment.Center
            ) {
                dowView(activity)
                Row(
                    modifier = Modifier
                        .padding(bottom = 30.dp, start = 60.dp, end = 60.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(R.drawable.icon_dowload),
                        contentDescription = "download",
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .requiredSize(44.dp)
                            .clickable {
                                activity
                                    .captureBitmapFromView()
                            },
                    )
                    Image(
                        painterResource(R.drawable.icon_share),
                        contentDescription = "download",
                        modifier = Modifier
                            .padding(start = 40.dp)
                            .requiredSize(44.dp)
                            .clickable {
                                activity.shareBitmapFromView()
                            }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(R.drawable.icon_l),
                    contentDescription = "download",
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .requiredSize(44.dp)
                        .clickable {
                            if (activity.cardPosition > 0) {
                                activity.cardPosition--
                                activity.answerText =
                                    activity.allData[activity.cardPosition].message
                                val tvData =
                                    activity.layoutDow.findViewById<TextView>(R.id.tv_message)
                                tvData.text = activity.allData[activity.cardPosition].message
                            } else {
                                Toast
                                    .makeText(
                                        activity,
                                        "It's already up to the first one",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painterResource(R.drawable.icon_r),
                    contentDescription = "download",
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .requiredSize(44.dp)
                        .clickable {
                            if (activity.cardPosition < activity.allData.size - 1) {
                                activity.cardPosition++
                                activity.answerText =
                                    activity.allData[activity.cardPosition].message
                                val tvData =
                                    activity.layoutDow.findViewById<TextView>(R.id.tv_message)
                                tvData.text = activity.allData[activity.cardPosition].message
                            } else {
                                Toast
                                    .makeText(
                                        activity,
                                        "It's already up to the last one",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                )
            }
        }

        Image(
            painterResource(R.drawable.icon_back_de),
            contentDescription = "back",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(207.dp)
                .height(58.dp)
                .clickable {
                    activity.finish()
                }
        )
    }
}

@Composable
fun TarotDetail(activity: DetailActivity) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(R.drawable.icon_background_main), contentDescription = "background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.weight(1f)) {
                GreetingDetail(activity)
            }
        }
        StorageAlertDialog(activity)
    }
}

@Composable
fun StorageAlertDialog(activity: DetailActivity) {
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

@Composable
fun DynamicColorTextDetail(backgroundColor: Color, textColor: Color, text: String) {
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
            .padding(top = 10.dp, bottom = 10.dp)
            .clickable { },
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewDetail() {
    TheFourthTarotCardTheme {
        TarotDetail(activity = DetailActivity())
    }
}