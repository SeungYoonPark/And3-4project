package com.example.and3_4project.Contact

import android.net.Uri

data class ContactList(
    var profileImg: Uri? = null,
    val contactName: String,
    val phoneNumber:String,
    val email:String,
    var notification:String,
    var isliked:Boolean=false
)