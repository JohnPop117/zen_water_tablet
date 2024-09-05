package com.example.zen_water_tablet.model
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class PenSize{
    small,
    medium,
    large
}

enum class PenColor{
    blue,
    red,
    black,
    green,
    yellow,
    white
}

enum class BackgroundColor{
    blue,
    red,
    black,
    green,
    yellow,
    white
}
data class Settings(var autoErase: Boolean,
var timeToErase: String,
var size: PenSize,
var penColor: PenColor,
var backGroundColor: BackgroundColor,
var backGroundImage: Int?
)
