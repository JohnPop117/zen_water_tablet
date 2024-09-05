package com.example.zen_water_tablet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zen_water_tablet.model.BackgroundColor
import com.example.zen_water_tablet.model.PenColor
import com.example.zen_water_tablet.model.PenSize
import com.example.zen_water_tablet.ui.theme.Zen_water_tabletTheme
import com.example.zen_water_tablet.model.Settings
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var appSettings = Settings(autoErase= true, "5", PenSize.small, PenColor.red, BackgroundColor.white, null)
        setContent {
            Zen_water_tabletTheme {
                AppScreen(appSettings)
            }
        }
    }
}

enum class ScreenState {
    Main_Menu,
    Drawing_Canvas,
    Options_Menu
}



@Composable
fun AppScreen(appSettings: Settings, modifier: Modifier = Modifier) {
    var screenState by remember {
        mutableStateOf(ScreenState.Main_Menu) }

    when(screenState) {
        ScreenState.Main_Menu -> StartMenu(screenState = screenState, onScreenChange = {screenState = it})
        ScreenState.Drawing_Canvas -> {
            if(appSettings.autoErase) {
                DrawingFadingCanvas(
                    screenState = screenState,
                    onScreenChange = { screenState = it },
                    appSettings
                )
            } else {
                DrawingCanvas(
                    screenState = screenState,
                    onScreenChange = { screenState = it },
                    appSettings
                )
            }

        }
        ScreenState.Options_Menu -> OptionsMenu(
            screenState = screenState, onScreenChange = {screenState = it},
            appSettings
        )
    }
}

@Composable
fun StartMenu(screenState: ScreenState, onScreenChange: (ScreenState) -> Unit){

    Surface {
        Image(painter = painterResource(R.drawable.zentitle), contentDescription = "Zen App", modifier=Modifier.fillMaxSize())
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            Button(onClick = { onScreenChange(ScreenState.Drawing_Canvas) }) {
                Text(text = "Local")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Online")
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp))
        }
    }
}

@Composable
fun DrawingFadingCanvas(screenState: ScreenState, onScreenChange: (ScreenState) -> Unit, appSettings: Settings) {
    var points by remember { mutableStateOf(listOf<AnimatablePoint>()) }
    val scope = rememberCoroutineScope()
    var radius = 20f
    when(appSettings.size){
        PenSize.small -> radius = 10f
        PenSize.medium -> radius = 20f
        PenSize.large -> radius = 30f
    }

    var bgColor = Color.White
    when(appSettings.backGroundColor){
        BackgroundColor.red -> bgColor = Color.Red
        BackgroundColor.blue -> bgColor = Color.Blue
        BackgroundColor.white -> bgColor = Color.White
        else -> bgColor = Color.Black
    }

    var color = Color.Red
    when(appSettings.penColor){
        PenColor.red -> color = Color.Red
        PenColor.blue -> color = Color.Blue
        PenColor.white -> color = Color.White
        else -> color = Color.Black
    }

    Surface(color = bgColor) {
        if(appSettings.backGroundImage != null)
        {
            Image(painter= painterResource(id = appSettings.backGroundImage!!), contentDescription = "Pitch", modifier = Modifier.fillMaxSize())
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures() { change, dragAmount ->
                    val animatableAlpha = Animatable(1f)
                    val point =
                        AnimatablePoint(
                            Offset(change.position.x, change.position.y),
                            animatableAlpha
                        )
                    points = points + point

                    scope.launch {
                        if (appSettings.autoErase) {
                            animatableAlpha.animateTo(
                                targetValue = if (appSettings.autoErase) 0f else 1f,
                                animationSpec = tween(durationMillis = appSettings.timeToErase.toInt() * 1000)
                            )
                        }
                        points = points.filter { it != point }
                    }
                }
            }
        ) {
            points.forEach { point ->
                drawCircle(
                    color = color.copy(alpha = point.alpha.value),
                    radius = radius,
                    center = point.offset
                )
            }
//        points.zipWithNext() { start, end ->
//            drawLine(
//                color = Color.Red.copy(alpha = start.alpha.value),
//                start = start.offset,
//                end = end.offset,
//                strokeWidth = 24f,
//            )
//        }
        }
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = Modifier
            .fillMaxHeight()
            .padding(top = 24.dp)) {
            Button( onClick = { onScreenChange(ScreenState.Main_Menu) } ) {
                Text(text="Main Menu")
            }
            Button( onClick = {onScreenChange(ScreenState.Options_Menu) } ){
                Text(text="Options Menu")
            }
        }
    }
}

@Composable
fun DrawingCanvas(screenState: ScreenState, onScreenChange: (ScreenState) -> Unit, appSettings: Settings) {
    var points by remember { mutableStateOf(listOf<Offset>()) }
    var radius = 20f
    when(appSettings.size){
        PenSize.small -> radius = 10f
        PenSize.medium -> radius = 20f
        PenSize.large -> radius = 30f
    }

    var bgColor = Color.White
    when(appSettings.backGroundColor){
        BackgroundColor.red -> bgColor = Color.Red
        BackgroundColor.blue -> bgColor = Color.Blue
        BackgroundColor.white -> bgColor = Color.White
        else -> bgColor = Color.Black
    }

    var color = Color.Red
    when(appSettings.penColor){
        PenColor.red -> color = Color.Red
        PenColor.blue -> color = Color.Blue
        PenColor.white -> color = Color.White
        else -> color = Color.Black
    }

    Surface(color = bgColor) {
        if(appSettings.backGroundImage != null)
        {
            Image(painter= painterResource(id = appSettings.backGroundImage!!), contentDescription = "Pitch", modifier = Modifier.fillMaxSize())
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures() { change, dragAmount ->
                    val point = Offset(change.position.x, change.position.y)
                    points = points + point

                }
            }
        ) {
            points.forEach { point ->
                drawCircle(
                    color = color.copy(),
                    radius = radius,
                    center = point
                )
            }
//        points.zipWithNext() { start, end ->
//            drawLine(
//                color = Color.Red.copy(alpha = start.alpha.value),
//                start = start.offset,
//                end = end.offset,
//                strokeWidth = 24f,
//            )
//        }
        }
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top, modifier = Modifier
            .fillMaxHeight()
            .padding(top = 24.dp)) {
            Button( onClick = { onScreenChange(ScreenState.Main_Menu) } ) {
                Text(text="Main Menu")
            }
            Button( onClick = {onScreenChange(ScreenState.Options_Menu) } ){
                Text(text="Options Menu")
            }
        }
    }
}


@Composable
fun OptionsMenu(            screenState: ScreenState, onScreenChange : (ScreenState) -> Unit,
                            appSettings: Settings, modifier: Modifier = Modifier){
    Surface(modifier= Modifier)
    {
        Column(modifier = Modifier.padding(top = 48.dp)) {
            Row() {
                var isChecked by remember { mutableStateOf(appSettings.autoErase)}
                Text(text="Auto erase:")
                Switch(checked = isChecked, onCheckedChange = {
                    appSettings.autoErase = it
                    isChecked = it})
            }
            Row() {
                Text(text="Erase Time:")
                var time by remember{mutableStateOf(appSettings.timeToErase)}
                TextField(value = time, label={Text("Seconds")}, onValueChange = {
                    time = it ?: "0"
                    appSettings.timeToErase = time }
                    , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true, modifier = Modifier.height(48.dp))
            }
            Row() {
                Text(text="Brush Size")
                Button(onClick = { appSettings.size = PenSize.small }) {
                    Text("Small")
                }
                Button(onClick = { appSettings.size = PenSize.medium }) {
                    Text("Medium")
                }
                Button(onClick = { appSettings.size = PenSize.large }) {
                    Text("Large")
                }
            }
            Row() {
                Text(text="Brush Color")
                Button(onClick = { appSettings.penColor = PenColor.red }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Red")
                }
                Button(onClick = { appSettings.penColor = PenColor.blue }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                    Text("Blue")
                }
                Button(onClick = { appSettings.penColor = PenColor.black }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                    Text("Black")
                }
                Button(onClick = {appSettings.penColor = PenColor.white }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text("White", color=Color.Black)
                }
            }
            Row() {
                Text(text="Background Color")
                Button(onClick = { appSettings.backGroundColor = BackgroundColor.red }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Red")
                }
                Button(onClick = { appSettings.backGroundColor = BackgroundColor.blue }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                    Text("Blue")
                }
                Button(onClick = {appSettings.backGroundColor = BackgroundColor.black }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                    Text("Black")
                }
                Button(onClick = {appSettings.backGroundColor = BackgroundColor.white }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text("White", color=Color.Black)
                }
            }
            Row() {
                Text(text="Background Image")
                Button(onClick = { appSettings.backGroundImage = R.drawable.pitch }) {
                    Text("Choose Image")
                }
            }
            Button( onClick = { onScreenChange(ScreenState.Main_Menu) } ) {
                Text(text="Main Menu")
            }
            Button( onClick = {onScreenChange(ScreenState.Drawing_Canvas) } ){
                Text(text="Drawing Screen")
            }

        }
    }
    
}

data class AnimatablePoint(
    val offset: Offset,
    val alpha: Animatable<Float, *>
)