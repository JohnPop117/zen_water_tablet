package com.example.zen_water_tablet.ui

import android.media.MediaPlayer
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.zen_water_tablet.R
import com.example.zen_water_tablet.data.AppUiState
import com.example.zen_water_tablet.data.Contact
import com.example.zen_water_tablet.data.contacts
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.zen_water_tablet.data.getContacts
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun CallScreen(uiState: AppUiState, modifier: Modifier = Modifier) {
    LazyColumn {
        items(contacts) {
            IndividualContact(
                contact = it,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

/**
 * Composable that displays the Contact's Photo.
 *
 * @param contactIcon is the resource ID for the contact's image
 * @param modifier modifiers to set to this composable
 */
@Composable
fun ContactIcon(
    @DrawableRes contactIcon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        painter = painterResource(contactIcon),
        contentDescription = null
    )
}

@Composable
fun IndividualContact(contact: Contact, modifier: Modifier = Modifier){

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
//    val ringTone = MediaPlayer.create(LocalContext.current, R.raw.outoing_phone_call)
//    val message = MediaPlayer.create(LocalContext.current, contact.recordingId)
//    ringTone.setOnCompletionListener {
//        message.start()
//    }
    Card(modifier = modifier){
        Column(
            modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ){
                ContactIcon(contactIcon = contact.contactImageResourceId)
                Button(onClick = { coroutineScope.launch { callAudio(context, contact) } }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green), modifier= Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)) {
                    Text("Call "+ stringResource(id = contact.nameId), color = Color.Black)
                }
//                Button(onClick = { ringTone.start() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green), modifier= Modifier
//                    .fillMaxSize()
//                    .align(Alignment.CenterVertically)) {
//                    Text("Call "+ stringResource(id = contact.nameId), color = Color.Black)
//                }

            }
        }
    }


}

suspend fun callAudio(context: android.content.Context, contact: Contact) = coroutineScope {
    val ringTone = MediaPlayer.create(context, R.raw.outoing_phone_call)
    val message = MediaPlayer.create(context, contact.recordingId)

    ringTone.setOnCompletionListener {
        message.start()
    }
    ringTone.start()
}
