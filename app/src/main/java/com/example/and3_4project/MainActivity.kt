package com.example.and3_4project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.and3_4project.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FAB 클릭 이벤트 핸들링
        binding.fabAddContactDialogFragment.setOnClickListener {
            showAddContactDialog()
        }
    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogLayout = inflater.inflate(R.layout.fragment_add_contact_dialog, null)
        val addUserImg = dialogLayout.findViewById<ImageView>(R.id.addUserImg)
        addUserImg.setImageResource(R.drawable.user)
        addUserImg.setColorFilter(ContextCompat.getColor(this, R.color.white))

        // AlertDialog에 커스텀 레이아웃 설정
        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.show()
    }
}
