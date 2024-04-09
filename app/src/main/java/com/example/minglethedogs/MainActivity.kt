package com.example.minglethedogs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minglethedogs.auth.IntroActivity
import com.example.minglethedogs.databinding.ActivityIntroBinding
import com.example.minglethedogs.databinding.ActivityMainBinding
import com.example.minglethedogs.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val logTag = "SignUpActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        addEventListeners()
    }

    private fun addEventListeners() {
        binding.btnLogOut.setOnClickListener {
            logOut()
        }
    }


    private fun logOut() {
        auth.signOut()

        val intent = Intent(this, IntroActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish() // 메인 액티비티 스택에서 제거하여 뒤로가기를 눌렀을 때 메인 화면으로 돌아오지 않게 함
    }
}