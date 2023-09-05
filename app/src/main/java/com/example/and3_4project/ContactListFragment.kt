package com.example.and3_4project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and3_4project.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {
    private lateinit var binding: FragmentContactListBinding
    // 프래그먼트의 UI를 생성하고 반환하는 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        val dataList = mutableListOf<ContactList>()

        dataList.add(ContactList(R.drawable.icon_add_person,"이름1"))
        dataList.add(ContactList(R.drawable.icon_add_person,"이름2"))
        dataList.add(ContactList(R.drawable.icon_add_person,"이름3"))
        dataList.add(ContactList(R.drawable.icon_add_person,"이름4",))

        val adapter = RecyclerViewAdapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        return binding.root

    }



    // 새로운 TodoFragment 인스턴스를 생성하는 메서드
    fun newInstant(): ContactListFragment {
        val args = Bundle()
        val frag = ContactListFragment()
        frag.arguments = args
        return frag
    }
}