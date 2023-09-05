package com.example.and3_4project.Contact

data class ContactList(
    var profileImg: Int,
    val contactName: String,
    var heartImg :Int,
    val phoneNumber:String,
    val email:String,
    var notification:String,
    var isliked:Boolean=false
)