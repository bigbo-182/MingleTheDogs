package com.example.minglethedogs.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.Toast
import com.example.minglethedogs.R
import com.example.minglethedogs.databinding.ActivityIntroBinding
import com.example.minglethedogs.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private val logTag = "SignUpActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        addEventListeners()
    }

    private fun addEventListeners() {
        binding.btnJoin.setOnClickListener {
            val nickname = binding.edtNickName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val passwordCheck = binding.edtPasswordCheck.text.toString()

            if (validateInput(email, password, passwordCheck, nickname)) {
                signUp(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String, passwordCheck: String, nickname: String): Boolean {
        when {
            email.isEmpty() -> showToast("Please input email address")
            password.isEmpty() -> showToast("Please input password")
            passwordCheck.isEmpty() -> showToast("Please input password check")
            nickname.isEmpty() -> showToast("Please input nickname")
            password != passwordCheck -> showToast("Passwords do not match")
            else -> return true
        }
        return false
    }
    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showToast("SignUp success")
                // TODO: 여기서 Firestore에 닉네임과 다른 정보를 저장하는 로직 추가
                val intent = Intent(this, IntroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP // 뒤로가기 눌렀을떄 회원가입 안나오게
                startActivity(intent)

            } else {
                val errorMessage = when (val exception = task.exception) {
                    is FirebaseAuthWeakPasswordException -> "비밀번호는 최소 6자 이상이어야 합니다."
                    is FirebaseAuthInvalidCredentialsException -> "이메일 형식이 잘못되었습니다."
                    is FirebaseAuthUserCollisionException -> "이미 사용 중인 이메일 주소입니다."
                    else -> "회원가입 실패: ${exception?.localizedMessage}"
                }
                showToast(errorMessage)
                Log.e(logTag, "SignUp failed", task.exception)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}