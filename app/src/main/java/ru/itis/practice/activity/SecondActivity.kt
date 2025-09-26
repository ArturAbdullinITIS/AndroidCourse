package ru.itis.practice.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.practice.ui.common.commonFontSize
import ru.itis.practice.ui.common.commonModifier

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extraText = intent.getStringExtra("text_key_2")
            ?.takeIf { it.isNotBlank() }
            ?: "Screen 2"
        setContent {

            Column(
                modifier = Modifier.
                fillMaxSize().
                padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text(text = extraText,
                    modifier = Modifier.padding(8.dp),
                    fontSize = commonFontSize
                )

                Button(onClick = {
                    val intent = Intent(this@SecondActivity, ThirdActivity::class.java)
                    intent.putExtra("text_key_3", extraText)
                    startActivity(intent)
                },
                    modifier = commonModifier
                ) {
                    Text("To third screen",
                        fontSize = commonFontSize)
                }

                Button(onClick = {
                    val intent = Intent(this@SecondActivity, FirstActivity::class.java)
                    startActivity(intent)
                },
                    modifier = commonModifier) {
                    Text("To first screen",
                        fontSize = commonFontSize)

                }
            }

        }


    }
}
