package com.example.and3_4project.Contact

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.and3_4project.Main.InfoSingleton.contactList
import com.example.and3_4project.R
import com.example.and3_4project.databinding.ActivityContactDetailBinding

class ContactDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding

    companion object {
        var contactPosition: Int = 0
        fun newIntentForDetail(context: Context?, position: Int) =
            Intent(context, ContactDetailActivity::class.java).apply {
                contactPosition = position
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //데이터받기

        val contact = contactList[contactPosition]


        val profileImg = contact.profileImg
        val contactName = contact.contactName
        val phoneNumber = contact.phoneNumber
        val email = contact.email
        val notification = contact.notification


        //툴바 뒤로가기 버튼 설정
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar2.title="상세화면"
        // 뒤로가기 버튼에 클릭 리스너 추가



        // 받은 데이터를 활용하여 디테일 화면 구성
        binding.apply {
            tvAdress.text = email
            tvNumber.text = phoneNumber
            tvName.text = contactName
            ivProfile.setImageResource(profileImg)
            tvNotification.text = notification
            if (contact.isliked) {
                ivHeart.setImageResource(R.drawable.heart_fill)
            } else {
                ivHeart.setImageResource(R.drawable.heart)
            }

        }
        //좋아요버튼 클릭처리
        binding.ivHeart.setOnClickListener {
            val contact = contactList[contactPosition]
            contact.isliked = !contact.isliked

            //버튼 클릭시 이미지 변경되도록 구현
            if (contact.isliked) {
                Toast.makeText(this, "관심목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                binding.ivHeart.setImageResource(R.drawable.heart_fill)
            } else {
                binding.ivHeart.setImageResource(R.drawable.heart)
            }
            //좋아요 상태를 메인으로 연결하기
            val intent = Intent()
            intent.putExtra("position", contactPosition)
            setResult(Activity.RESULT_OK, intent)
        }
    }
    // 툴바 메뉴 버튼을 설정- menu에 있는 item을 연결하는 부분
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.toolbar_menu,
            menu
        )       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }
    //Toolbar 메뉴 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { //뒤로 가기 버튼
                finish()

            }
//            R.id.toolbar_info -> {// 툴팁
//                val view = findViewById<View>(R.id.toolbar_info) //툴팁을 띄우기 위해서는 view가 필요함
//                balloon.showAlignBottom(view)
//            }
        }
        return super.onOptionsItemSelected(item)
    }

}