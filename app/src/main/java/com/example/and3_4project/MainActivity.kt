package com.example.and3_4project
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.and3_4project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    private var userNameInput: String = ""
    private var userPhoneNumberInput: String = ""
    private var userEmailInput: String = ""

    private lateinit var addUserImg : ImageView
    private var selectTime: String = ""
    private var notificationId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolBar
        setSupportActionBar(toolbar)

        val viewPager2 = binding.viewPager
        val tabLayout = binding.tabLayout

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        //탭설정
        tabLayout.addTab(tabLayout.newTab().setText("Contact"))
        tabLayout.addTab(tabLayout.newTab().setText("Mypage"))


        // TabLayout의 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position // 선택된 탭에 해당하는 페이지로 이동
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 사용하지 않음
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 사용하지 않음
            }
        })

        // ViewPager2의 페이지 변경 콜백 설정
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position)) // 페이지 변경 시 탭도 변경
            }
        })

        binding.fabAdd.setOnClickListener {
            showAddContactDialog()
        }

    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogLayout = inflater.inflate(R.layout.fragment_add_contact_dialog, null)
        val Cancel = dialogLayout.findViewById<Button>(R.id.cancel)
        val Save = dialogLayout.findViewById<Button>(R.id.save)
        addUserImg = dialogLayout.findViewById<ImageView>(R.id.addUserImg)

        val offBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.off)
        val fivePastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.fivePast)
        val quarterPastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.quarterPast)
        val halfPastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.halfPast)

        var userName = dialogLayout.findViewById<EditText>(R.id.addUserName)
        var userPhoneNumber = dialogLayout.findViewById<EditText>(R.id.addUserPhoneNumber)
        userPhoneNumber.filters = arrayOf(InputFilter.LengthFilter(13))

        val userEmailLeft = dialogLayout.findViewById<EditText>(R.id.addUserEmailLeft)
        val userEmailRight = dialogLayout.findViewById<EditText>(R.id.addUserEmailRight)

        addUserImg.setColorFilter(ContextCompat.getColor(this, R.color.white))
        addUserImg.setImageResource(R.drawable.user)

        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.show()

        addUserImg.setOnClickListener{
            //다시 기본 필터로 변경
            addUserImg.setColorFilter(null)
            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }
        offBtn.setOnClickListener{
            selectTime = "OFF"
            notificationId = 1

            fivePastBtn.isChecked = false
            quarterPastBtn.isChecked = false
            halfPastBtn.isChecked = false
        }
        fivePastBtn.setOnClickListener{
            selectTime = "5분 뒤 알림"
            notificationId = 2
            offBtn.isChecked = false

            quarterPastBtn.isChecked = false
            halfPastBtn.isChecked = false
        }
        quarterPastBtn.setOnClickListener{
            selectTime = "15분 뒤 알림"
            notificationId = 3
            offBtn.isChecked = false
            fivePastBtn.isChecked = false

            halfPastBtn.isChecked = false
        }
        halfPastBtn.setOnClickListener{
            selectTime = "30분 뒤 알림"
            notificationId = 4
            offBtn.isChecked = false
            fivePastBtn.isChecked = false
            quarterPastBtn.isChecked = false

        }

        Save.setOnClickListener {
            userNameInput = userName.text.toString()
            userPhoneNumberInput = userPhoneNumber.text.toString()
            val EmailLeft = userEmailLeft.text.toString()
            val EmailRight = userEmailRight.text.toString()
            userEmailInput = "$EmailLeft@$EmailRight"

            Log.d("useong", "userNameInput: $userNameInput")
            Log.d("useong", "userNameInput: $userPhoneNumberInput")

            Log.d("useong", "userNameInput: $userEmailInput")
            if (userNameInput.isEmpty()) {
                Toast.makeText(this, R.string.name_exception, Toast.LENGTH_SHORT).show()
            } else if (userPhoneNumberInput.isEmpty()) {
                Toast.makeText(this, R.string.phone_number_exception, Toast.LENGTH_SHORT).show()
            } else if (!isValidPhoneNumber(userPhoneNumberInput)) {
                Toast.makeText(this, R.string.phone_number_policy_exception, Toast.LENGTH_SHORT)
                    .show()
            }

            else if (EmailLeft.isEmpty()) {
                Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
            } else if (EmailRight.isEmpty()) {
                Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(userEmailInput)) {
                Toast.makeText(this, R.string.email_policy_exception, Toast.LENGTH_SHORT).show()
            } else {
                createScheduleNotification(this, selectTime, notificationId)
                dialog.dismiss()
            }
        }

        Cancel.setOnClickListener {
            dialog.dismiss()
        }

        userPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumberWithoutHyphen = s.toString().replace("-", "")
                if (phoneNumberWithoutHyphen.length <= 11) {
                    val formattedPhoneNumber = buildString {
                        for (i in phoneNumberWithoutHyphen.indices) {
                            if (i == 3 || i == 7) {
                                append("-")
                            }
                            append(phoneNumberWithoutHyphen[i])
                        }
                    }
                    userPhoneNumber.removeTextChangedListener(this)
                    userPhoneNumber.setText(formattedPhoneNumber)
                    userPhoneNumber.setSelection(formattedPhoneNumber.length)
                    userPhoneNumber.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^\\d{3}-\\d{4}-\\d{4}\$")
        return regex.matches(phoneNumber)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    //알림 설정하기
    fun createScheduleNotification(
        context: Context,
        selectTime: String,
        notificationId: Int
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "ChannelId"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId.toString(),
                "Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }


        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val timeInMillis = when (selectTime) {
            "5분 뒤 알림" -> System.currentTimeMillis() + 5 * 1000         // 5분 5 * 60 * 1000
            "15분 뒤 알림" -> System.currentTimeMillis() + 15 * 1000       // 15분 15 * 60 * 1000
            "30분 뒤 알림" -> System.currentTimeMillis() + 30 * 1000       // 30분 30 * 60 * 1000
            else -> System.currentTimeMillis()                                  // 즉시 알림
        }


        runBlocking {
            launch(Dispatchers.Default) {
                val currentTime = System.currentTimeMillis()
                if (timeInMillis > currentTime) {
                    delay(timeInMillis - currentTime)
                }

                val notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.alarm)
                    .setContentTitle("연락처 알림")
                    .setContentText("$userNameInput 에게 연락을 할 시간입니다.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                notificationManager.notify(notificationId, notificationBuilder.build())
            }
        }
    }

    //사진 갖고오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 OK , 결가값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val uri  = it.data!!.data

            //화면에 보여주기
            Glide.with(this)
                .load(uri) //이미지
                .into(addUserImg) //보여줄 위치
        }
    }
}
