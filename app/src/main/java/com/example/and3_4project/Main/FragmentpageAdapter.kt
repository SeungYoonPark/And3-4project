package com.example.and3_4project.Main
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.and3_4project.Contact.ContactListFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,// Fragment를 관리하기 위한 FragmentManager 인스턴스
    lifecycle: Lifecycle //라이프사이클 이벤트를 처리하기 위한 Lifecycle 인스턴스


) : FragmentStateAdapter(fragmentManager, lifecycle){
    // 페이지의 개수를 반환하는 메서드    직접 생성하는 형식
    // 수정이 필요
    fun getFragment(position: Int): Fragment{
        return if (position==0)// 첫 번째 페이지일 경우
            ContactListFragment.newInstance()
        else// 그 외의 경우 (두 번째 페이지일 경우)
            MyPageFragment()//.newInstance()
    }
    override fun getItemCount(): Int {
        return 2//2개의 페이지 생성 하겠다고 지정
    }

    // 지정된 위치(position)에 따라 해당하는 Fragment를 생성하는 메서드
    // 수정이 필요
    override fun createFragment(position: Int): Fragment {
        return if (position==0)// 첫 번째 페이지일 경우
            ContactListFragment.newInstance()
        else// 그 외의 경우 (두 번째 페이지일 경우)
            MyPageFragment()//.newInstance()
    }
}
