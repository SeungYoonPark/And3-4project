package com.example.and3_4project.Contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and3_4project.Main.InfoSingleton
import com.example.and3_4project.R
import com.example.and3_4project.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {

    private lateinit var binding: FragmentContactListBinding

    //싱글톤 연결하기
    private var contactList = InfoSingleton.getcontactList()

    private val adapter = RecyclerViewAdapter(contactList)

    fun newInstant(): ContactListFragment {
        val args = Bundle()
        val frag = ContactListFragment()
        frag.arguments = args
        return frag
    }

    // 처음에 그려질때 호출되는 콜백 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactListBinding.inflate(inflater, container, false)


        // adapter 연결하기 및 view 초기화 하기

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        //아이템 클릭시 넘어가기
        adapter.itemClick = object : RecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                startActivity(ContactDetailActivity.newIntentForDetail(context,position))
            }
        }

        return binding.root
    }



    fun addContacntListSetting(newContact: ContactList){
        InfoSingleton.contactList.add(newContact)
        contactList = InfoSingleton.getcontactList()
        adapter.notifyDataSetChanged()//addItems(contactList)

        Log.d("recordUser", newContact.profileImg.toString())
        Log.d("recordUser", newContact.contactName)
        Log.d("recordUser", newContact.phoneNumber)
        Log.d("recordUser", newContact.email)
        Log.d("recordUser", newContact.notification)
        Log.d("recordUser", newContact.isliked.toString())
    }



}