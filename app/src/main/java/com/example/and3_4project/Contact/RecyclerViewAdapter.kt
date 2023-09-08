package com.example.and3_4project.Contact

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.and3_4project.Main.InfoSingleton
import com.example.and3_4project.Main.InfoSingleton.contactList
import com.example.and3_4project.R
import com.example.and3_4project.databinding.ItemGridRecyclerviewBinding
import com.example.and3_4project.databinding.ItemRecyclerviewBinding

class RecyclerViewAdapter(
    private val mItems: MutableList<ContactList>,
) : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {



//    fun addContactListSet(newContact: ContactList){
//        InfoSingleton.contactList.add(newContact)
//        //contactList = InfoSingleton.getcontactList()
//        mItems.add(newContact)
//        notifyItemChanged(mItems.size)
//    }
//
//    fun reviseContactListSet(newContact: ContactList, position: Int){
//        InfoSingleton.contactList[position] = newContact
//        //contactList = InfoSingleton.getcontactList()
//        mItems[position] = newContact
//        notifyItemChanged(position)
//    }

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return Holder(binding)
    }

    //viewholder class를 이용해야한다. 수정이 필요함
    override fun onBindViewHolder(holder: Holder, position: Int) {
        //좋아요 버튼 클릭시 반영
        val contact= mItems[position]
        holder.bind(contact, position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    //각 아이템에 관한 기본 설정
    //외부 클래스를 쉽게 접근하기 위해서 inner class를 사용하였다
    inner class Holder(
        private val binding: ItemRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item :ContactList, position: Int) = with(binding){
             if(item.isliked){
                contactListHeart.setImageResource(R.drawable.icon_bookmark_fill)
            }else{
                contactListHeart.setImageResource(R.drawable.icon_bookmark)
            }
            contactListHeart.setOnClickListener{
                item.isliked = !item.isliked
                // 갱신된 거 알리기
                notifyItemChanged(position)
            }
            itemView.setOnClickListener{
                itemClick?.onClick(it, position)
            }

            Glide.with(root.context) // 또는 Glide.with(ivProfile.context)
                .load(item.profileImg)
                .fitCenter()
                .into(binding.contactListIcon)
            contactListName.text=item.contactName
        }

    }
}