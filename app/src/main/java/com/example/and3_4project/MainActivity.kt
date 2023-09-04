package com.example.and3_4project
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and3_4project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FAB 클릭 이벤트 핸들링
        binding.fabAddContactDialogFragment.setOnClickListener {
            showAddContactDialog()
        }

        val dataList = mutableListOf<ContactList>()
        dataList.add(ContactList(R.drawable.ic_launcher_foreground, "이승훈", "01000000000"))
        val adapter = Adapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogLayout = inflater.inflate(R.layout.fragment_add_contact_dialog, null)
        val addUserImg = dialogLayout.findViewById<ImageView>(R.id.addUserImg)
        addUserImg.setImageResource(R.drawable.user)
        addUserImg.setColorFilter(ContextCompat.getColor(this, R.color.white))

        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.show()
    }
}
