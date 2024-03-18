package com.date.time.dog.thefourthtarotcard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.lifecycle.lifecycleScope
import com.date.time.dog.thefourthtarotcard.ui.theme.TheFourthTarotCardTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GuideActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheFourthTarotCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingGuide()
                }
            }
        }
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@GuideActivity, MainActivity::class.java))
            finish()
        }
    }
}

@Composable
fun GreetingGuide() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(R.drawable.icon_start_background), contentDescription = "background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
            Spacer(modifier =Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.icon_start_logo),
                contentDescription = "Background",
                modifier = Modifier
                    .padding(start = 22.dp)
                    .width(77.dp)
                    .height(81.dp)
            )
            val customFontFamily = FontFamily(Font(R.font.aclonica, FontWeight.Bold))
            val customFontFamily2 = FontFamily(Font(R.font.abel, FontWeight.Bold))
            Text(
                text = "Modern\nTarot",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = customFontFamily,
                color = Color(0xFFFFFFFF),
                lineHeight = 48.sp,
                modifier = Modifier
                    .padding(start = 22.dp)
            )
            Text(
                text = "Чтение и изучение Таро",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0x80FFFFFF),
                fontFamily = customFontFamily2,
                modifier = Modifier
                    .padding(start = 22.dp,bottom = 103.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TheFourthTarotCardTheme {
        GreetingGuide()
    }
}