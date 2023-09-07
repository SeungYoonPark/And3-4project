package com.example.and3_4project.Main

import android.net.Uri
import com.example.and3_4project.Contact.ContactList
import com.example.and3_4project.R

object InfoSingleton {
    var contactList: MutableList<ContactList> = mutableListOf(
        ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.dog1),
            "김영현",
            "010-6688-3596",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.cat),
            "나유성",
            "010-1234-1234",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.cat2),
            "박승윤",
            "010-1111-2222",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.cat10),
            "이승훈",
            "010-3333-4444",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.cat14),
            "김뽀삐",
            "010-0987-7789",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.bbang),
            "빵빵이",
            "010-0000-0000",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.oksun),
            "옥지얌",
            "010-5757-2734",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(

            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.poketmon),
            "이상해씨",
            "010-2424-9865",
            "kyhi525@naver.com",
            "알림이 없습니다."
        ), ContactList(


            Uri.parse("android.resource://" + R::class.java.`package`?.name + "/" + R.drawable.poketmon2),
            "피카츄",
            "010-9999-7654",
            "kyhi525@naver.com",
            "알림이 없습니다."
        )
    )

    fun getcontactList(): MutableList<ContactList> {
        return contactList
    }
}