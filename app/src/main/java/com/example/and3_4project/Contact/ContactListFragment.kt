package com.example.and3_4project.Contact


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
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
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentContactListBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

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
                    it.setIcon(R.drawable.icon_menu_line)
                        binding.recyclerView.layoutManager = GridLayoutManager(activity,2)}


                    else {it.setIcon(R.drawable.icon_menu)
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)



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

    override fun onResume() {
        super.onResume()
        // 데이터 변경 시 어댑터에 알림
        adapter.notifyDataSetChanged()
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



