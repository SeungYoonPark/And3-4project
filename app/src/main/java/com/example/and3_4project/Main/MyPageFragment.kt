package com.example.and3_4project.Main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.and3_4project.Contact.ContactListFragment
import com.example.and3_4project.R

class MyPageFragment : Fragment() {
    // 프래그먼트의 UI를 생성하고 반환하는 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
        // fragment_todo 레이아웃 파일을 인플레이트하여 UI를 생성하고 반환
    }

    // 새로운 TodoFragment 인스턴스를 생성하는 메서드
//    companion object{
//        private var frag : MyPageFragment? = null
//        fun newInstance(args : Bundle = Bundle()): MyPageFragmentt {
//
//            // 있을때는 생성되지 않게 설정
//            if (frag == null) {
//                frag = MyPageFragment()
//            }
//            frag!!.arguments = args
//            return frag!!
//
//        }
//    }
    fun newInstant(): MyPageFragment {
        val args = Bundle()
        val frag = MyPageFragment()
        frag.arguments = args
        return frag
    }
}
