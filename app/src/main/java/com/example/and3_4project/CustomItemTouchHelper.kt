package com.example.and3_4project


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


 class CustomItemTouchHelper(private val onSwipeListener: OnSwipeListener) : ItemTouchHelper.Callback(){
     override fun getMovementFlags(
         recyclerView: RecyclerView,
         viewHolder: RecyclerView.ViewHolder,
     ): Int {
         val swipeFlags = ItemTouchHelper.RIGHT
         return makeMovementFlags(0, swipeFlags)
     }

     override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeListener.onSwipe(viewHolder.adapterPosition, direction)

    }
     interface OnSwipeListener {
         fun onSwipe(position: Int, direction: Int)
     }

}

