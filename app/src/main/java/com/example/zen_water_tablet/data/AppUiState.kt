package com.example.zen_water_tablet.data

import com.example.zen_water_tablet.R
import com.example.zen_water_tablet.ScreenTitles

data class AppUiState(
    var screen: ScreenTitles = ScreenTitles.Start,
    var drawSettings: Settings = Settings(
        autoErase = true,
        timeToErase= "5",
        size= PenSize.small,
        penColor = PenColor.red,
        backGroundColor=BackgroundColor.white,
        backGroundImage= null),
    var appBarSecondaryButton: (() -> Unit) = {},
    var appBarSecondaryButtonText: Int = -1,
    var main_image: Int = R.drawable.default_main,
    var user_name: Int = R.string.default_user_name
)
