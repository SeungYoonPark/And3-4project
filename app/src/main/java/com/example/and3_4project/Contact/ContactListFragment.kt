package com.example.and3_4project.Contact


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and3_4project.CustomItemTouchHelper
import com.example.and3_4project.CustomItemTouchHelper.OnSwipeListener
import com.example.and3_4project.Main.InfoSingleton
import com.example.and3_4project.R
import com.example.and3_4project.databinding.FragmentContactListBinding


class ContactListFragment : Fragment(), OnSwipeListener {
    companion object {
        var contactPosition: Int = 0
        fun newIntentForDetail(context: Context?, position: Int) =
            Intent(context, ContactDetailActivity::class.java).apply {
                contactPosition = position
            }
    }

    private lateinit var binding: FragmentContactListBinding
    private var viewType = true

    //싱글톤 연결하기
    private var contactList = InfoSingleton.getcontactList()
    private val adapter = RecyclerViewAdapter(contactList)
    private val gridAdapter = GridRecyclerViewAdapter(contactList)

    //여기만 수정 필요합니다
    companion object{
        private var frag : ContactListFragment? = null
        fun newInstance(args : Bundle = Bundle()): ContactListFragment {

            // 있을때는 생성되지 않게 설정
            if (frag == null) {
                frag = ContactListFragment()
                frag!!.arguments = args
            }
            return frag!!

        }
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
        //스와이프
        val SwipeCallback = CustomItemTouchHelper(this)
        ItemTouchHelper(SwipeCallback).attachToRecyclerView(binding.recyclerView)
        //리사이클러뷰 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter.itemClick = object : RecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                //ContactDetailActivity의 함수 newIntentForDetail을 사용한다, 값이 왔다갔다 할 수 없음
                // / 수정 필요
                //register
                startActivity(ContactDetailActivity.newIntentForDetail(context, position))
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                //그리드모양 메뉴 클릭 시 그리드 레이아웃 전환
                R.id.grid_menu -> {
                    if (viewType) {
                        it.setIcon(R.drawable.icon_menu_line)
                        binding.recyclerView.adapter = gridAdapter
                        gridAdapter.itemClick = object : GridRecyclerViewAdapter.ItemClick {
                            override fun onClick(view: View, position: Int) {
                                startActivity(
                                    ContactDetailActivity.newIntentForDetail(
                                        context,
                                        position
                                    )
                                )
                            }
                        }
                        binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
                    } else {
                        //리스트모양 메뉴 클릭 시 리니어레이아웃 전환
                        it.setIcon(R.drawable.icon_menu)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    }
                    viewType = !viewType
                    true
                }
                else -> {}
            }
            true
        }
        return binding.root
    }

//    이거 없으면 fragment에 갱신이 안됌       아래는 수정할 부분 없음

    override fun onResume() {
        super.onResume()
        // 데이터 변경 시 어댑터에 알림
        adapter.notifyDataSetChanged()
    }

    fun addContacntListSetting(newContact: ContactList) {
        InfoSingleton.contactList.add(newContact)
//
//        //contactList = InfoSingleton.getcontactList()
//
        adapter.notifyDataSetChanged()//addItems(contactList)
//        adapter.addContactListSet(newContact)
    }

    fun reviseContactListSetting(newContact: ContactList, position: Int){
        InfoSingleton.contactList[position] = newContact
//        //contactList = InfoSingleton.getcontactList()
        adapter.notifyDataSetChanged()
//        adapter.reviseContactListSet(newContact,position)
    }
    //스와이프시 전화 기능
    override fun onSwipe(position: Int, direction: Int) {
        Log.d("dadadad", "asdadasd")
        contactList = InfoSingleton.getcontactList()
        val swipePhoneNumber = contactList[position].phoneNumber
        val intent = Intent(Intent.ACTION_CALL)
        val uri = Uri.parse("tel:$swipePhoneNumber")
        intent.data = uri
        startActivity(intent)
    }
}




