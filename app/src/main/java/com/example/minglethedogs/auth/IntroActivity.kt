package com.example.minglethedogs.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.minglethedogs.MainActivity
import com.example.minglethedogs.databinding.ActivityIntroBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {
    private val logTag = "IntroActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEventListners()

        auth = Firebase.auth
//        initObject(
    }

    private fun addEventListners() {
        binding.btnLogin.setOnClickListener {
            logIn()
        }

        binding.btnLoginNonMember.setOnClickListener {
            loginNonMember()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(logTag, "signInWithEmail:success")
                    updateUI(auth.currentUser)
                }  else {
                    // 로그인 실패 시, 예외에 따른 적절한 토스트 메시지 띄우기
                    val exception = task.exception
                    Log.w(logTag, "signInWithEmail:failure", exception)

                    val message = when (exception) {
                        is FirebaseAuthInvalidUserException -> "존재하지 않는 사용자입니다."
                        is FirebaseAuthInvalidCredentialsException -> "이메일 또는 비밀번호가 잘못되었습니다."
                        is FirebaseAuthUserCollisionException -> "이미 등록된 이메일입니다."
                        else -> "인증 실패: ${exception?.localizedMessage}"
                    }

                    Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginNonMember() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    // 인증 실패
                    val exception = task.exception
                    when (exception) {
                        is FirebaseAuthException -> {
                            // Firebase Auth 오류 처리
                            Log.e(logTag, "signInAnonymously:failure", exception)
                            Toast.makeText(baseContext, "Authentication failed: ${exception.errorCode}", Toast.LENGTH_SHORT).show()
                        }
                        is FirebaseNetworkException -> {
                            // 네트워크 연결 오류 처리
                            Toast.makeText(baseContext, "Network connection failed", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // 기타 오류 처리
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}