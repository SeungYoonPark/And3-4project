package com.example.and3_4project.Main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.and3_4project.Contact.ContactList
import com.example.and3_4project.Contact.ContactListFragment
import com.example.and3_4project.R
import com.example.and3_4project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: FragmentPageAdapter

    // 이 데이터들을 사용할 예정
    private var userNameInput: String = ""
    private var userPhoneNumberInput: String = ""
    private var userEmailInput: String = ""
    private var notificationId: Int = 0

    private lateinit var addUserImg : ImageView
    private lateinit var userName : EditText
    private lateinit var userPhoneNumber : EditText
    private lateinit var userEmailLeft : EditText
    private lateinit var userEmailRight: EditText
    private var selectTime: String = ""
    private var uri: Uri? = null

    // 액티비티 결과를 처리하는데 사용 된다.
    companion object {
        private lateinit var requestLauncher: ActivityResultLauncher<Intent>
    }

    private var contextSet :Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabLayout = binding.tabLayout

        //adapter 연결
        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)


        //탭레이아웃 설정
        tabLayout.addTab(tabLayout.newTab().setText("Contact"))
        tabLayout.addTab(tabLayout.newTab().setText("Mypage"))
        //Tab 아이콘 설정
        tabLayout.getTabAt(0)?.setIcon(R.drawable.profile)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.setting)
        // TabLayout의 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPager.currentItem = tab.position // 선택된 탭에 해당하는 페이지로 이동
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 사용하지 않음
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 사용하지 않음
            }
        })
        binding.viewPager.adapter = adapter
        // ViewPager2의 페이지 변경 콜백 설정
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position)) // 페이지 변경 시 탭도 변경
            }
        })



        //초기화 코드라 onCreate에 설정해야 한다
        // 사용자가 퍼미션 허용했는지 확인
        val status = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else {
            // 퍼미션 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(this, arrayOf<String>("android.permission.READ_CONTACTS"), 100)
            Log.d("test", "permission denied")
        }

        // ActivityResultLauncher 초기화, 결과 콜백 정의
        requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val cursor = contentResolver.query(
                    it.data!!.data!!,
                    arrayOf<String>(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ),
                    null,
                    null,
                    null
                )
                Log.d("test", "cursor size : ${cursor?.count}")

                if (cursor!!.moveToFirst()) {
                    val name = cursor.getString(0)
                    val phone = cursor.getString(1)
                    //수정하기
                    userName.setText(name)
                    userPhoneNumber.setText(phone)
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            showAddContactDialog(
                uri,
                userNameInput,
                userPhoneNumberInput,
                userEmailInput,
                selectTime,
                contextSet,
            )
        }

    }


    ///contactfragment에 저장된것을 알려줘야한다
    fun showAddContactDialog(
        uriSet: Uri?,
        userNameSet: String,
        userPhoneNumberSet: String,
        userEmailSet: String,
        selectTimeSet: String,
        context: Context,
        position: Int = -1
    ) {

        var builder: AlertDialog.Builder
        var inflater: LayoutInflater

        //콘텍스트로 비교하기
        if (userNameSet == "") {
            builder = AlertDialog.Builder(contextSet)
            inflater = LayoutInflater.from(contextSet)
        } else {// ContactDetailActivity 일때   -> 이러면 싱글톤 값을 직접 접근해야함
            // 이러면 전체적인 값이 변경된것을 모든 페이지한테 알려야 하니깐 초기화 해야한다.
            // 싱글톤에서 값을 뿌려주는 형태여서
            builder = AlertDialog.Builder(context)
            inflater = LayoutInflater.from(context)
        }
        val dialogLayout = inflater.inflate(R.layout.fragment_add_contact_dialog, null)
        val Cancel = dialogLayout.findViewById<Button>(R.id.cancel)
        val Save = dialogLayout.findViewById<Button>(R.id.save)
        addUserImg = dialogLayout.findViewById(R.id.addUserImg)

        val offBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.off)
        val fivePastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.fivePast)
        val quarterPastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.quarterPast)
        val halfPastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.halfPast)
        //주소록 버튼 설정

        val addUserContactBookBtn =
            dialogLayout.findViewById<ImageView>(R.id.addUserContactBook)

        userName = dialogLayout.findViewById(R.id.addUserName)
        userPhoneNumber = dialogLayout.findViewById(R.id.addUserPhoneNumber)
        userPhoneNumber.filters = arrayOf(InputFilter.LengthFilter(13))

        userEmailLeft = dialogLayout.findViewById(R.id.addUserEmailLeft)
        userEmailRight = dialogLayout.findViewById(R.id.addUserEmailRight)

        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.show()


        //버튼을 클릭시 생성할지 판단하는 변수
        // 값 수정하기   ->   Context로 변경하기
        if (userNameSet != "") {      //contextSet != context){              //
            addUserImg.setImageURI(uriSet)
            userName.setText(userNameSet)
            userPhoneNumber.setText(userPhoneNumberSet)

            val parts = userEmailSet.split("@")
            userEmailLeft.setText(parts[0])
            userEmailRight.setText(parts[1])

            when (selectTimeSet) {
                "OFF" -> {
                    offBtn.isChecked = true
                    fivePastBtn.isChecked = false
                    quarterPastBtn.isChecked = false
                    halfPastBtn.isChecked = false
                }

                "5분 뒤 알림" -> {
                    offBtn.isChecked = false
                    fivePastBtn.isChecked = true
                    quarterPastBtn.isChecked = false
                    halfPastBtn.isChecked = false
                }

                "15분 뒤 알림" -> {
                    offBtn.isChecked = false
                    fivePastBtn.isChecked = false
                    quarterPastBtn.isChecked = true
                    halfPastBtn.isChecked = false
                }

                "30분 뒤 알림" -> {
                    offBtn.isChecked = false
                    fivePastBtn.isChecked = false
                    quarterPastBtn.isChecked = false
                    halfPastBtn.isChecked = true
                }

                else -> {}

            }

        }

        //주소록 버튼 설정
        addUserContactBookBtn.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            requestLauncher.launch(intent)
        }

        addUserImg.setOnClickListener {
            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        offBtn.setOnClickListener {
            selectTime = "OFF"
            notificationId = 1

            fivePastBtn.isChecked = false
            quarterPastBtn.isChecked = false
            halfPastBtn.isChecked = false
        }

        fivePastBtn.setOnClickListener {
            selectTime = "5분 뒤 알림"
            notificationId = 2
            offBtn.isChecked = false

            quarterPastBtn.isChecked = false
            halfPastBtn.isChecked = false
        }

        quarterPastBtn.setOnClickListener {
            selectTime = "15분 뒤 알림"
            notificationId = 3
            offBtn.isChecked = false
            fivePastBtn.isChecked = false

            halfPastBtn.isChecked = false
        }

        halfPastBtn.setOnClickListener {
            selectTime = "30분 뒤 알림"
            notificationId = 4
            offBtn.isChecked = false
            fivePastBtn.isChecked = false
            quarterPastBtn.isChecked = false

        }

        if (userNameSet == "") {//(contextSet == context){//
            Save.setOnClickListener {
                //값 설정하기
                Log.d("recordUser", 1.toString())
                userNameInput = userName.text.toString()
                userPhoneNumberInput = userPhoneNumber.text.toString()
                val EmailLeftInput = userEmailLeft.text.toString()
                val EmailRightInput = userEmailRight.text.toString()
                userEmailInput = "$EmailLeftInput@$EmailRightInput"


                if (userNameInput.isEmpty()) {
                    Toast.makeText(this, R.string.name_exception, Toast.LENGTH_SHORT).show()
                } else if (userPhoneNumberInput.isEmpty()) {
                    Toast.makeText(this, R.string.phone_number_exception, Toast.LENGTH_SHORT)
                        .show()
                } else if (!isValidPhoneNumber(userPhoneNumberInput)) {
                    Toast.makeText(
                        this,
                        R.string.phone_number_policy_exception,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (EmailLeftInput.isEmpty()) {
                    Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (EmailRightInput.isEmpty()) {
                    Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (!isValidEmail(userEmailInput)) {
                    Toast.makeText(this, R.string.email_policy_exception, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    createScheduleNotification(this, selectTime, notificationId)
                        val newContact = ContactList(
                            uri,
                            userNameInput,
                            userPhoneNumberInput,
                            userEmailInput,
                            selectTime,
                            false
                        )

                        // 아래 작업을 여기서 하지말고 ContactListFragment로 값을 넘겨주자
                        val addContact = adapter.getFragment(0) as ContactListFragment
                        addContact.addContacntListSetting(newContact)
                    //초기화 하기
                    uri = null
                    userNameInput = ""
                    userPhoneNumberInput = ""
                    userEmailInput = ""
                    selectTime = ""
                    dialog.dismiss()
                }
            }

        }
        //ContactDetailActivity
        else {
            Log.d("recordUser", 2.toString())

            val setBtn = dialogLayout.findViewById<Button>(R.id.save)
            setBtn.text = "수정"      //getString(R.string.revise)
            Save.setOnClickListener {
                //값 설정하기
                userNameInput = userName.text.toString()
                userPhoneNumberInput = userPhoneNumber.text.toString()
                val EmailLeftInput = userEmailLeft.text.toString()
                val EmailRightInput = userEmailRight.text.toString()
                userEmailInput = "$EmailLeftInput@$EmailRightInput"


                if (userNameInput.isEmpty()) {
                    Toast.makeText(this, R.string.name_exception, Toast.LENGTH_SHORT).show()
                } else if (userPhoneNumberInput.isEmpty()) {
                    Toast.makeText(this, R.string.phone_number_exception, Toast.LENGTH_SHORT)
                        .show()
                } else if (!isValidPhoneNumber(userPhoneNumberInput)) {
                    Toast.makeText(
                        this,
                        R.string.phone_number_policy_exception,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (EmailLeftInput.isEmpty()) {
                    Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (EmailRightInput.isEmpty()) {
                    Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (!isValidEmail(userEmailInput)) {
                    Toast.makeText(this, R.string.email_policy_exception, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    createScheduleNotification(this, selectTime, notificationId)
                    val newContact = ContactList(
                        uri,
                        userNameInput,
                        userPhoneNumberInput,
                        userEmailInput,
                        selectTime,
                        false
                    )
                    val addContact = adapter.getFragment(0) as ContactListFragment
                    addContact.reviseContactListSetting(newContact, position)

                    dialog.dismiss()
                }

            }


        }

        Cancel.setOnClickListener {
            userName.text.clear()
            userPhoneNumber.text.clear()
            userEmailLeft.text.clear()
            userEmailRight.text.clear()
            dialog.dismiss()
        }

        userPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
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
        //알림을 생성하고 예약하는데 사용된다
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "ChannelId"

        //알림 채널에 연결한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId.toString(),
                "Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }


        val intent = Intent(context, ContactListFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val timeInMillis = when (selectTime) {
            "5분 뒤 알림" -> System.currentTimeMillis() + 5 * 1000         // 5분 5 * 60 * 1000
            "15분 뒤 알림" -> System.currentTimeMillis() + 15 * 1000       // 15분 15 * 60 * 1000
            "30분 뒤 알림" -> System.currentTimeMillis() + 30 * 1000       // 30분 30 * 60 * 1000
            else -> System.currentTimeMillis()                                  // 즉시 알림
        }


        CoroutineScope(Dispatchers.Default).launch {
            val currentTime = System.currentTimeMillis()
            if (timeInMillis > currentTime) {
                delay(timeInMillis - currentTime)
            }

            // 알림을 만들고 표시하는 부분
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("연락처 알림")
                .setContentText("$userName 에게 연락을 할 시간입니다.")
                .setContentIntent(pendingIntent) // 클릭 시 실행할 Intent 설정
                .setAutoCancel(true)

            notificationManager.notify(notificationId, notificationBuilder.build())

            // 알림 예약 작업이 완료되면 다른 작업을 수행 가능
        }
    }

    //사진 갖고오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        //결과 코드 OK , 결가값 null 아니면
        if (it.resultCode == RESULT_OK && it.data != null) {
            //값 담기
            uri = it.data!!.data

            //화면에 보여주기
            Glide.with(this)
                .load(uri)         //이미지 uri
                .fitCenter()
                .into(addUserImg)             //보여줄 위치  ImageView
        }
    }

    // 주소록에서 이름이랑 전화번호 갖고오기
    // 다이얼로그에서 퍼미션 허용했는지 확인
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else {
            Log.d("test", "permission denied")
        }
    }

}


