package com.example.zen_water_tablet.data

import android.content.Context
import android.util.Log
import com.example.zen_water_tablet.R
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class Contact(
    @DrawableRes val contactImageResourceId: Int,
    @StringRes val nameId: Int,
    @RawRes val recordingId: Int = R.raw.default_recording
)
var contacts = mutableListOf<Contact>()

fun getContacts(context: Context){
    val fields = R.drawable::class.java.fields.filter{ context.resources.getResourceEntryName(it.getInt(null)).endsWith("contact_image") }

    for (field in fields) {
        try {
            val resourceId = field.getInt(null)
            val resourceName = context.resources.getResourceEntryName(resourceId)
            var resource_prefix = resourceName.removeSuffix("_contact_image")
            val stringId = context.resources.getIdentifier(resource_prefix,"string",context.packageName)
            val rawId = context.resources.getIdentifier(resource_prefix+"_recording","raw",context.packageName)
            if(stringId == 0)
                Log.d("Raw", resource_prefix)

            if(rawId > 0)
                contacts.add(Contact(resourceId, stringId, rawId))
            else
                contacts.add(Contact(resourceId, stringId))

        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}
