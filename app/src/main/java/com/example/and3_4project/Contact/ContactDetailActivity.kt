package com.example.and3_4project.Contact

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.and3_4project.Main.InfoSingleton.contactList
import com.example.and3_4project.R
import com.example.and3_4project.databinding.ActivityContactDetailBinding

class ContactDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding

    companion object {
        var contactPosition :Int = 0
        fun newIntentForDetail(context: Context?, position: Int) =
            Intent(context, ContactDetailActivity::class.java).apply {
                contactPosition= position
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //데이터받기

        val contact=contactList[contactPosition]


        val profileImg = contact.profileImg
        val contactName = contact.contactName
        val phoneNumber = contact.phoneNumber
        val email = contact.email
        val notification = contact.notification


        // 받은 데이터를 활용하여 디테일 화면 구성
        binding.apply {
            tvAdress.text = email
            tvNumber.text = phoneNumber
            tvName.text = contactName
            ivProfile.setImageURI(profileImg)
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
            if(contact.isliked){
                Toast.makeText(this,"관심목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                binding.ivHeart.setImageResource(R.drawable.heart_fill)
            }else {
                binding.ivHeart.setImageResource(R.drawable.heart)
            }
            //좋아요 상태를 메인으로 연결하기
            val intent = Intent()
            intent.putExtra("position", contactPosition)
            setResult(Activity.RESULT_OK, intent)
        }
    }
}