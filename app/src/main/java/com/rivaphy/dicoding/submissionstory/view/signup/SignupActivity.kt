package com.rivaphy.dicoding.submissionstory.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rivaphy.dicoding.submissionstory.R
import com.rivaphy.dicoding.submissionstory.databinding.ActivitySignupBinding
import com.rivaphy.dicoding.submissionstory.view.ViewModelFactory
import com.rivaphy.dicoding.submissionstory.view.login.LoginActivity
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun errorMessage(it: String) {
        AlertDialog.Builder(this).apply {
            setMessage(it)
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            showLoading(true)

            if (email.isEmpty() && password.isEmpty() && name.isEmpty()) {
                errorMessage(getString(R.string.empty))
            } else {
                lifecycleScope.launch {
                    try {
                        val message = viewModel.userSignUp(name, email, password).message
                        Log.d("SignUp Success", "$message")
                        showLoading(false)
                        errorMessage(getString(R.string.error_201_register))
                        Handler(Looper.getMainLooper()).postDelayed({
                            intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)
                    } catch (e: Exception) {
                        val error = parseError(e.message)
                        Log.e("SignUp Failed", "${e.message}")
                        showLoading(false)
                        errorMessage(getString(R.string.error_json_sign_up))
                    }
                }
            }
        }
    }

    private fun parseError(message: String?): String {
        return try {
            val jsonObject = JSONObject(message.toString())
            jsonObject.getString("error")
        } catch (e: JSONException) {
            getString(R.string.error_json)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pgSignup.visibility = View.VISIBLE
        } else {
            binding.pgSignup.visibility = View.GONE
        }
    }
}