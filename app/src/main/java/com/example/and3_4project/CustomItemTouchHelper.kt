package com.example.and3_4project

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class CustomItemTouchHelper(private val onSwipeListener: OnSwipeListener) :
    ItemTouchHelper.Callback() {

    private val gradientStartColor = Color.parseColor("#0064CF")
    private val gradientEndColor = Color.parseColor("#5C5C5C")
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

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView

            val gradient = LinearGradient(
                itemView.right.toFloat(),
                itemView.top.toFloat(),
                itemView.right + dX,
                itemView.bottom.toFloat(),
                gradientStartColor,
                gradientEndColor,
                Shader.TileMode.CLAMP
            )
            c.drawRect(
                itemView.right.toFloat(),
                itemView.top.toFloat(),
                itemView.right + dX,
                itemView.bottom.toFloat(),
                Paint().apply { shader = gradient }
            )

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface OnSwipeListener {
        fun onSwipe(position: Int, direction: Int)
    }

}

