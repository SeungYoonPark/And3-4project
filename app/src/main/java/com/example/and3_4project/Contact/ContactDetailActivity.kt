package com.example.and3_4project.Contact

import android.Manifest
import android.app.Activity
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
import android.view.Menu
import android.view.MenuItem
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
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.and3_4project.Main.FragmentPageAdapter
import com.example.and3_4project.Main.InfoSingleton.contactList
import com.example.and3_4project.R
import com.example.and3_4project.databinding.ActivityContactDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var addUserImg: ImageView

    //사진 설정
    private lateinit var profileUri: Uri

    private lateinit var contactName: String
    private lateinit var phoneNumber: String
    private lateinit var email: String
    private lateinit var notification: String
    private lateinit var userName: EditText
    private lateinit var userPhoneNumber: EditText
    private lateinit var requestLauncher: ActivityResultLauncher<Intent>
    private lateinit var contact: ContactList


    companion object {
        var contactPosition: Int = 0
        fun newIntentForDetail(context: Context?, position: Int) =
            Intent(context, ContactDetailActivity::class.java).apply {
                contactPosition = position
            }
    }//contactPosition 값을 전달하기 위해 있는 것


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //데이터받기

        contact = contactList[contactPosition]

        profileUri = contact.profileImg!!
        contactName = contact.contactName
        phoneNumber = contact.phoneNumber
        email = contact.email
        notification = contact.notification


        //툴바 뒤로가기 버튼 설정
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val customBackIcon = ResourcesCompat.getDrawable(resources, R.drawable.icon_back_2, null)
        supportActionBar?.setHomeAsUpIndicator(customBackIcon)


        // 받은 데이터를 활용하여 디테일 화면 구성
        binding.apply {
            tvAdress.text = email
            tvNumber.text = phoneNumber
            tvName.text = contactName
            Glide.with(root.context) // 또는 Glide.with(ivProfile.context)
                .load(profileUri)
                .fitCenter()
                .into(ivProfile)
            //ivProfile.setImageURI(profileUri)
            tvNotification.text = notification
            if (contact.isliked) {
                ivHeart.setImageResource(R.drawable.icon_bookmark_fill)
            } else {
                ivHeart.setImageResource(R.drawable.icon_bookmark)
            }

        }

        //좋아요버튼 클릭처리
        binding.ivHeart.setOnClickListener {
            val contact = contactList[contactPosition]
            contact.isliked = !contact.isliked

            //버튼 클릭시 이미지 변경되도록 구현
            if (contact.isliked) {
                Toast.makeText(this, "관심목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                binding.ivHeart.setImageResource(R.drawable.icon_bookmark_fill)
            } else {
                binding.ivHeart.setImageResource(R.drawable.icon_bookmark)
            }
            //좋아요 상태를 메인으로 연결하기
            val intent = Intent()
            intent.putExtra("position", contactPosition)
            setResult(Activity.RESULT_OK, intent)
        }

        var number = binding.tvNumber.text.toString()

        //전화걸기 버튼
        binding.btnCall.setOnClickListener {

            val PERMISSIONS_CALL_PHONE = 1
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    PERMISSIONS_CALL_PHONE
                )
            } else {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + number)
                startActivity(callIntent)
            }

        }

        //메세지 보내기
        binding.btnMassage.setOnClickListener {
            val smsUri = Uri.parse("smsto:$number") //phonNumber에는 01012345678과 같은 구성.
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = smsUri
            intent.putExtra("sms_body", "") //해당 값에 전달하고자 하는 문자메시지 전달
            startActivity(intent)
        }

        //알림 설정하기
        binding.btnNotifycation.setOnClickListener {
            showAddContactDialog(
                profileUri,
                contactName,
                phoneNumber,
                email,
                notification,
                contact.isliked,
                this,
                contactPosition
            )

        }
        //초기화 코드라 onCreate에 설정해야 한다
        // 사용자가 퍼미션 허용했는지 확인
        val status = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "permission granted")
        } else {
            // 퍼미션 요청 다이얼로그 표시
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>("android.permission.READ_CONTACTS"),
                100
            )
            Log.d("test", "permission denied")
        }

        // ActivityResultLauncher 초기화, 결과 콜백 정의
        requestLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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

            R.id.edit -> {
                showAddContactDialog(
                    profileUri,
                    contactName,
                    phoneNumber,
                    email,
                    notification,
                    contact.isliked,
                    this,
                    contactPosition
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showAddContactDialog(
        uriSet: Uri?,
        userNameSet: String,
        userPhoneNumberSet: String,
        userEmailSet: String,
        notificationSet: String,
        isliked: Boolean,

        context: Context,
        position: Int = -1
    ) {


        var builder = AlertDialog.Builder(context)
        var inflater = LayoutInflater.from(context)

        val dialogLayout = inflater.inflate(R.layout.fragment_add_contact_dialog, null)
        val Cancel = dialogLayout.findViewById<Button>(R.id.cancel)
        val Save = dialogLayout.findViewById<Button>(R.id.save)

        val offBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.off)
        val fivePastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.fivePast)
        val quarterPastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.quarterPast)
        val halfPastBtn = dialogLayout.findViewById<AppCompatToggleButton>(R.id.halfPast)
        //주소록 버튼 설정
        val addUserContactBookBtn = dialogLayout.findViewById<ImageView>(R.id.addUserContactBook)

        //
        addUserImg = dialogLayout.findViewById<ImageView>(R.id.addUserImg)
        userName = dialogLayout.findViewById<EditText>(R.id.addUserName)
        userPhoneNumber = dialogLayout.findViewById<EditText>(R.id.addUserPhoneNumber)
        userPhoneNumber.filters = arrayOf(InputFilter.LengthFilter(13))

        val userEmailLeft = dialogLayout.findViewById<EditText>(R.id.addUserEmailLeft)
        val userEmailRight = dialogLayout.findViewById<EditText>(R.id.addUserEmailRight)
        //
        lateinit var adapter: FragmentPageAdapter

        //변수 설정     (전달할 값을 설정하기)
        var setUri: Uri? = uriSet
        var setContactName: String
        var setPhoneNumber: String
        var setEmail: String
        var setNotification: String = notificationSet

        var setNotificationId: Int = 0


        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.show()

        // 값 기입하기 알림에서 들어갔을때
        if (userNameSet != "") {      //contextSet != context){              //
            Glide.with(context) // 또는 Glide.with(ivProfile.context)
                .load(uriSet)
                .fitCenter()
                .into(addUserImg)
            //addUserImg.setImageURI(uriSet)
            userName.setText(userNameSet)
            userPhoneNumber.setText(userPhoneNumberSet)

            val parts = userEmailSet.split("@")
            userEmailLeft.setText(parts[0])
            userEmailRight.setText(parts[1])

            when (notificationSet) {
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
            requestLauncher!!.launch(intent)
        }

        addUserImg.setOnClickListener {
            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        offBtn.setOnClickListener {
            setNotification = "OFF"
            setNotificationId = 1
            fivePastBtn.isChecked = false
            quarterPastBtn.isChecked = false
            halfPastBtn.isChecked = false
        }

        fivePastBtn.setOnClickListener {
            setNotification = "5분 뒤 알림"
            setNotificationId = 2
            offBtn.isChecked = false

            quarterPastBtn.isChecked = false
            halfPastBtn.isChecked = false
        }

        quarterPastBtn.setOnClickListener {
            setNotification = "15분 뒤 알림"
            setNotificationId = 3
            offBtn.isChecked = false
            fivePastBtn.isChecked = false

            halfPastBtn.isChecked = false
        }

        halfPastBtn.setOnClickListener {
            setNotification = "30분 뒤 알림"
            setNotificationId = 4
            offBtn.isChecked = false
            fivePastBtn.isChecked = false
            quarterPastBtn.isChecked = false

        }

        if (userNameSet == "") {             //(contextSet == context){//
            Save.setOnClickListener {
                //값 설정하기
                Log.d("recordUser", 1.toString())
                setContactName = userName.text.toString()
                setPhoneNumber = userPhoneNumber.text.toString()
                var EmailLeftInput = userEmailLeft.text.toString()
                var EmailRightInput = userEmailRight.text.toString()
                setEmail = "$EmailLeftInput@$EmailRightInput"


                if (setContactName.isEmpty()) {
                    Toast.makeText(context, R.string.name_exception, Toast.LENGTH_SHORT).show()
                } else if (setPhoneNumber.isEmpty()) {
                    Toast.makeText(context, R.string.phone_number_exception, Toast.LENGTH_SHORT)
                        .show()
                } else if (!isValidPhoneNumber(setPhoneNumber)) {
                    Toast.makeText(
                        context,
                        R.string.phone_number_policy_exception,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (EmailLeftInput.isEmpty()) {
                    Toast.makeText(context, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (EmailRightInput.isEmpty()) {
                    Toast.makeText(context, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (!isValidEmail(setEmail)) {
                    Toast.makeText(context, R.string.email_policy_exception, Toast.LENGTH_SHORT)
                        .show()
                } else {



                    createScheduleNotification(
                        context,
                        notificationSet,
                        setNotificationId,
                        setContactName
                    )

                    val newContact = ContactList(
                        setUri,
                        setContactName,
                        setPhoneNumber,
                        setEmail,
                        setNotification,
                        false
                    )

                    (adapter.getFragment(0) as ContactListFragment).addContacntListSetting(
                        newContact!!
                    )

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
                setContactName = userName.text.toString()
                setPhoneNumber = userPhoneNumber.text.toString()
                var EmailLeftInput = userEmailLeft.text.toString()
                var EmailRightInput = userEmailRight.text.toString()
                setEmail = "$EmailLeftInput@$EmailRightInput"


                if (setContactName.isEmpty()) {
                    Toast.makeText(context, R.string.name_exception, Toast.LENGTH_SHORT).show()
                } else if (setPhoneNumber.isEmpty()) {
                    Toast.makeText(context, R.string.phone_number_exception, Toast.LENGTH_SHORT)
                        .show()
                } else if (!isValidPhoneNumber(setPhoneNumber)) {
                    Toast.makeText(
                        context,
                        R.string.phone_number_policy_exception,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (EmailLeftInput.isEmpty()) {
                    Toast.makeText(context, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (EmailRightInput.isEmpty()) {
                    Toast.makeText(context, R.string.email_exception, Toast.LENGTH_SHORT).show()
                } else if (!isValidEmail(setEmail)) {
                    Toast.makeText(context, R.string.email_policy_exception, Toast.LENGTH_SHORT)
                        .show()
                } else {

                    createScheduleNotification(this, setNotification, setNotificationId, setContactName)

                    val newContact = ContactList(
                        profileUri,
                        setContactName,
                        setPhoneNumber,
                        setEmail,
                        setNotification,
                        isliked
                    )
                    binding.apply {
                        tvAdress.text = setEmail
                        tvNumber.text = setPhoneNumber
                        tvName.text = setContactName
                        Glide.with(root.context) // 또는 Glide.with(ivProfile.context)
                            .load(contact.profileImg)
                            .fitCenter()
                            .into(ivProfile)
                        //ivProfile.setImageURI(contact.profileImg)
                        tvNotification.text = setNotification
                    }
                    Log.d("recordUser", 3.toString())
                    // 값수정하기
                    adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
                    (adapter.getFragment(0) as ContactListFragment).reviseContactListSetting(
                        newContact!!, position
                    )

                    dialog.dismiss()
                }

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
        notificationId: Int,
        userName: String
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
            profileUri = it.data!!.data!!

            //화면에 보여주기
            Glide.with(this)
                .load(profileUri)         //이미지 uri
                .fitCenter()
                .into(addUserImg)
            Glide.with(this)
                .load(profileUri)
                .fitCenter()
                .into(binding.ivProfile)//보여줄 위치  ImageView
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