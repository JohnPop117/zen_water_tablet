package com.example.zen_water_tablet.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zen_water_tablet.R
import com.example.zen_water_tablet.ScreenTitles
import com.example.zen_water_tablet.ui.theme.Zen_water_tabletTheme

@Composable
fun StartScreen(drawAppClick: () -> Unit, navController: NavController, main_image: Int? = null, modifier: Modifier = Modifier){
    val main_image = main_image ?: R.drawable.default_main
    Column(modifier= modifier){
        Image(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .clip(MaterialTheme.shapes.small)
                .weight(8f),
            contentScale = ContentScale.Crop,
            painter = painterResource(main_image),

            contentDescription = null
        )
        Row() {
            Spacer(modifier = Modifier.weight(.5f))
            Button(onClick = drawAppClick ,
                modifier = Modifier.weight(3f)) {
                Text(stringResource(R.string.draw_app))
            }
            Spacer(modifier = Modifier.weight(.5f))
            Button(onClick = { navController.navigate(route = ScreenTitles.Draw_Settings.name)},
                modifier = Modifier.weight(3f)) {
                Text(stringResource(R.string.draw_settings))
            }
            Spacer(modifier = Modifier.weight(.5f))
        }
        Row(){
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { navController.navigate(route= ScreenTitles.Call_App.name) },
                modifier = Modifier.weight(3f)) {
                Text(stringResource(R.string.call_app))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight")
@Composable
fun StartScreenPreview() {
    Zen_water_tabletTheme {
        StartScreen(drawAppClick = {}, rememberNavController())
    }
}