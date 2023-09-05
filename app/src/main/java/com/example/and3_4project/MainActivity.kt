package com.example.and3_4project
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.and3_4project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    private var userNameInput: String = ""
    private var userPhoneNumberInput: String = ""
    private var userEmailInput: String = ""
//    private var userEventInput: String = ""

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

//        val dataList = mutableListOf<ContactList>()
//        dataList.add(ContactList(R.drawable.ic_launcher_foreground, "이승훈", "01000000000"))
//        val adapter = Adapter(dataList)
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogLayout = inflater.inflate(R.layout.fragment_add_contact_dialog, null)
        val Cancel = dialogLayout.findViewById<Button>(R.id.cancel)
        val Save = dialogLayout.findViewById<Button>(R.id.save)
        val addUserImg = dialogLayout.findViewById<ImageView>(R.id.addUserImg)

        var userName = dialogLayout.findViewById<EditText>(R.id.addUserName)
        var userPhoneNumber = dialogLayout.findViewById<EditText>(R.id.addUserPhoneNumber)
        userPhoneNumber.filters = arrayOf(InputFilter.LengthFilter(13))

        val userEmailLeft = dialogLayout.findViewById<EditText>(R.id.addUserEmailLeft)
        val userEmailRight = dialogLayout.findViewById<EditText>(R.id.addUserEmailRight)

//        var userEvent = dialogLayout.findViewById<EditText>(R.id.addUserEvent)

        addUserImg.setColorFilter(ContextCompat.getColor(this, R.color.white))
        addUserImg.setImageResource(R.drawable.user)

        builder.setView(dialogLayout)
        val dialog = builder.create()
        dialog.show()

        addUserImg.setOnClickListener {

        }

        Save.setOnClickListener {
            userNameInput = userName.text.toString()
            userPhoneNumberInput = userPhoneNumber.text.toString()

//            userEventInput = userEvent.text.toString()

            val EmailLeft = userEmailLeft.text.toString()
            val EmailRight = userEmailRight.text.toString()
            userEmailInput = "$EmailLeft@$EmailRight"

            Log.d("useong", "userNameInput: $userNameInput")
            Log.d("useong", "userNameInput: $userPhoneNumberInput")
//            Log.d("useong", "userNameInput: $userEventInput")
            Log.d("useong", "userNameInput: $userEmailInput")
            if (userNameInput.isEmpty()) {
                Toast.makeText(this, R.string.name_exception, Toast.LENGTH_SHORT).show()
            } else if (userPhoneNumberInput.isEmpty()) {
                Toast.makeText(this, R.string.phone_number_exception, Toast.LENGTH_SHORT).show()
            } else if (!isValidPhoneNumber(userPhoneNumberInput)) {
                Toast.makeText(this, R.string.phone_number_policy_exception, Toast.LENGTH_SHORT)
                    .show()
            }
//            else if (userEventInput.isEmpty()){
//                Toast.makeText(this, R.string.event_exception, Toast.LENGTH_SHORT).show()
//            }
            else if (EmailLeft.isEmpty()) {
                Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
            } else if (EmailRight.isEmpty()) {
                Toast.makeText(this, R.string.email_exception, Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(userEmailInput)) {
                Toast.makeText(this, R.string.email_policy_exception, Toast.LENGTH_SHORT).show()
            } else {
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
}
