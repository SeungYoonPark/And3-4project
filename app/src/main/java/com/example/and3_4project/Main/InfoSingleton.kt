package com.example.and3_4project.Main

import android.net.Uri
import com.example.and3_4project.Contact.ContactList
import com.example.and3_4project.R

object InfoSingleton {
    var contactList: MutableList<ContactList> = mutableListOf(
        ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile_white),
            "김영현",
            "010-1111-1234",
            "kyhi525@naver.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile0),
            "나유성",
            "010-2222-1234",
            "yu12@gmail.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile2),
            "이승훈",
            "010-4444-1234",
            "sh62@gmail.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile3),
            "박승윤",
            "010-5555-1234",
            "sy34@gmail.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile4),
            "호랑이",
            "010-6666-1234",
            "hodols@gmail.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile5),
            "강아지",
            "010-7777-1234",
            "bowbow@gmail.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile6),
            "사자",
            "010-8888-1234",
            "lionking@gmail.com",
            "OFF"
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile7),
            "고양이",
            "010-9999-1234",
            "miyao55@gmail.com",
            "OFF"
        ), ContactList(


            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.profile8),
            "곰이올시다",
            "010-1234-1234",
            "bear1@gmail.com",
            "OFF"
        )
    )

    fun getcontactList(): MutableList<ContactList> {
        return contactList
    }
}