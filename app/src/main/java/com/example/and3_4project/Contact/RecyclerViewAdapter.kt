package com.example.and3_4project.Contact

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.and3_4project.Main.InfoSingleton.contactList
import com.example.and3_4project.R
import com.example.and3_4project.databinding.ItemGridRecyclerviewBinding
import com.example.and3_4project.databinding.ItemRecyclerviewBinding

class RecyclerViewAdapter(
                          private val mItems: MutableList<ContactList>)
    : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {



    interface ItemClick {
        fun onClick(view : View, position : Int){

        }
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }
    //viewholder class를 이용해야한다. 수정이 필요함
    override fun onBindViewHolder(holder: Holder, position: Int) {
        //좋아요 버튼 클릭시 반영
        val contact= mItems[position] // contactList[position]
        if (contact.isliked){
            holder.isliked.setImageResource(R.drawable.icon_bookmark_fill)
        }else{
            holder.isliked.setImageResource(R.drawable.icon_bookmark)
        }
        holder.isliked.setOnClickListener {
            contact.isliked = !contact.isliked
            // UI 업데이트 및 어댑터에 알림
            notifyDataSetChanged()

        }
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            itemClick?.onClick(it, position)
        }

        holder.productImg.setImageURI(mItems[position].profileImg)
        holder.name.text = mItems[position].contactName


    }

//    fun addItem(contactList: ContactList){
//
//        mItems.add(contactList)
//        notifyDataSetChanged()
//    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    //각 아이템에 관한 기본 설정
    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        //fun bind(){}// 바인딩이 잘못 되어있다. 수정이 필요함
        val productImg = binding.contactListIcon
        val name = binding.contactListName
        val isliked = binding.contactListHeart
    }
}