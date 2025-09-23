package ru.itis.practice.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.practice.ui.common.commonFontSize
import ru.itis.practice.ui.common.commonModifier

class FirstActivity : ComponentActivity() {
    companion object {
        var FirstActivityOld: FirstActivity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirstActivityOld = this
        setContent {
            var text by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                TextField(
                    value = text,
                    onValueChange = { newText -> text = newText },
                    label = { Text("Write smth",
                        fontSize = 15.sp) },
                    textStyle = TextStyle(fontSize = commonFontSize),
                    modifier = commonModifier
                )

                Button(onClick = {
                    val intent = Intent(this@FirstActivity, SecondActivity::class.java)
                    intent.putExtra("text_key_2", text)
                    startActivity(intent)
                },
                    modifier = commonModifier
                ){
                    Text("To second screen",
                        fontSize = commonFontSize
                    )
                }

                Button(onClick = {
                    val intent = Intent(this@FirstActivity, ThirdActivity::class.java)
                    intent.putExtra("text_key_3", text)
                    startActivity(intent)

                },
                    modifier = commonModifier)
                {
                    Text("To third screen",
                        fontSize = commonFontSize)
                }
            }
        }
    }
}
