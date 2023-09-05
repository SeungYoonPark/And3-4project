package com.example.and3_4project.Contact

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.and3_4project.databinding.ItemRecyclerviewBinding

class RecyclerViewAdapter(val mItems: MutableList<ContactList>)
    : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {

    //아이템 클릭시 반응    -> 디테일 페이지로 넘어가기?
    interface ItemClick {
        fun onClick(view : View, position : Int){

        }
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    //뷰를 생성해서 보여주는 것
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            itemClick?.onClick(it, position)
        }
        //아이콘 이미지뷰를 초기화하고 이름 text를 초기화한다.
        holder.iconImageView.setImageURI(Uri.parse(mItems[position].profileImg))
        holder.name.text = mItems[position].contactName

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    //각 아이템에 관한 기본 설정
    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val iconImageView = binding.contactListIcon
        val name = binding.contactListName
        val heart = binding.contactListHeart
    }
}