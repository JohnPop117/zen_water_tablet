package com.example.zen_water_tablet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.zen_water_tablet.R
import com.example.zen_water_tablet.data.AppUiState
import com.example.zen_water_tablet.data.BackgroundColor
import com.example.zen_water_tablet.data.PenColor
import com.example.zen_water_tablet.data.PenSize

@Composable
fun DrawingSettingsScreen(uiState: AppUiState, modifier: Modifier = Modifier
){
    Surface(modifier= Modifier)
    {
        Column(modifier = Modifier.padding(top = 48.dp)) {
            Row() {
                var isChecked by remember { mutableStateOf(uiState.drawSettings.autoErase) }
                Text(text="Auto erase:")
                Switch(checked = isChecked, onCheckedChange = {
                    uiState.drawSettings.autoErase = it
                    isChecked = it})
            }
            Row() {
                Text(text="Erase Time:")
                var time by remember{ mutableStateOf(uiState.drawSettings.timeToErase) }
                TextField(value = time, label={ Text("Seconds") }, onValueChange = {
                    time = it ?: "0"
                    uiState.drawSettings.timeToErase = time }
                    , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true, modifier = Modifier.height(56.dp))
            }
            Row() {
                Text(text="Brush Size")
                Button(onClick = { uiState.drawSettings.size = PenSize.small }) {
                    Text("Small")
                }
                Button(onClick = { uiState.drawSettings.size = PenSize.medium }) {
                    Text("Medium")
                }
                Button(onClick = { uiState.drawSettings.size = PenSize.large }) {
                    Text("Large")
                }
            }
            Row() {
                Text(text="Brush Color")
                Button(onClick = { uiState.drawSettings.penColor = PenColor.red }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Red")
                }
                Button(onClick = { uiState.drawSettings.penColor = PenColor.blue }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                    Text("Blue")
                }
                Button(onClick = { uiState.drawSettings.penColor = PenColor.black }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                    Text("Black")
                }
                Button(onClick = {uiState.drawSettings.penColor = PenColor.white }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text("White", color= Color.Black)
                }
            }
            Row() {
                Text(text="Background Color")
                Button(onClick = { uiState.drawSettings.backGroundColor = BackgroundColor.red }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Red")
                }
                Button(onClick = { uiState.drawSettings.backGroundColor = BackgroundColor.blue }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                    Text("Blue")
                }
                Button(onClick = {uiState.drawSettings.backGroundColor = BackgroundColor.black }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                    Text("Black")
                }
                Button(onClick = {uiState.drawSettings.backGroundColor = BackgroundColor.white }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text("White", color= Color.Black)
                }
            }
            Row() {
                Text(text="Background Image")
                Button(onClick = { uiState.drawSettings.backGroundImage = R.drawable.pitch }) {
                    Text("Field")
                }
                Button(onClick = { uiState.drawSettings.backGroundImage = R.drawable.hockeyrink }) {
                    Text("Hockey")
                }
                Button(onClick = { uiState.drawSettings.backGroundImage = null }) {
                    Text("Blank")
                }
            }
        }
    }

}
