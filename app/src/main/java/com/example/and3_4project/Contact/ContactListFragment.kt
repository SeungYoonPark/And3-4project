package com.example.and3_4project.Contact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and3_4project.Main.InfoSingleton
import com.example.and3_4project.R
import com.example.and3_4project.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {

    private lateinit var binding: FragmentContactListBinding
    private var viewType = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    // 처음에 그려질때 호출되는 콜백 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentContactListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        //싱글톤 연결하기
        val contactList = InfoSingleton.getcontactList()
        val adapter = RecyclerViewAdapter(contactList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter.itemClick = object : RecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                startActivity(ContactDetailActivity.newIntentForDetail(context, position))
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.grid_menu -> {
                    if (viewType){
                    it.setIcon(R.drawable.list)}
                    else{
                        it.setIcon(R.drawable.grid)
                    }
                    viewType = !viewType
                    true
                }
                else -> {
                }
            }
            true
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


