package com.example.and3_4project.Main

import android.net.Uri
import com.example.and3_4project.Contact.ContactList
import com.example.and3_4project.R

object InfoSingleton {
    var contactList: MutableList<ContactList> = mutableListOf(
        ContactList(
            Uri.parse("@drawable/icon_add_person"),
            "이름1",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름2",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름3",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름4",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름5",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름6",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름7",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름8",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        ), ContactList(

            Uri.parse("@drawable/icon_add_person"),

            "이름9",
            "010-1234-1234",
            "kyhi525@naver.com",
            "dhgn 5시에 알림이 있습니다."
        )
    )
    fun getcontactList(): MutableList<ContactList> {
        return contactList
    }
}