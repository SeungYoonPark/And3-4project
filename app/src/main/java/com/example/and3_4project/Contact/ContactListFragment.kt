package com.example.and3_4project.Contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and3_4project.Main.InfoSingleton
import com.example.and3_4project.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {

    private lateinit var binding: FragmentContactListBinding

    // 처음에 그려질때 호출되는 콜백 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactListBinding.inflate(inflater, container, false)

        //싱글톤 연결하기
        val contactList=InfoSingleton.getcontactList()
        val adapter = RecyclerViewAdapter(contactList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)



        adapter.itemClick = object : RecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                startActivity(ContactDetailActivity.newIntentForDetail(context,position))
            }
        }

        return binding.root
    }


    fun newInstant(): ContactListFragment {
        val args = Bundle()
        val frag = ContactListFragment()
        frag.arguments = args
        return frag
    }


}