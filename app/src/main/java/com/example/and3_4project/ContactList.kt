package com.example.and3_4project

data class ContactList(
    val profileImage: Int,
    val contactName: String,
    val heart :Int,
    val phoneNumber:String,
    val email:String,
    val notification:String,
    val isliked:Boolean=false
)