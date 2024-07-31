package com.sbd.gbd.Activities.BasicActivitys

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.PreferenceManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.databinding.ActivityOtpLoginBinding
import java.util.concurrent.TimeUnit

class OtpLoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOtpLoginBinding
    var mVerificationId: String? = null
    private lateinit var mAuth: FirebaseAuth
    lateinit var preferenceManager: PreferenceManager
    private lateinit var countDownTimer: CountDownTimer
    private var timerRunning = false
    private val totalTimeInMillis: Long = 60000
    val Tag = "OtpActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance()
        binding.tvSendOtp.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)

        binding.edtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 10) {
                    binding.tvSendOtp.visibility = View.VISIBLE
                } else {
                    binding.tvSendOtp.visibility = View.GONE
                    binding.cvOtp.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                timerRunning = false
                updateTimerText(0)
                binding.tvSendOtp.text = "Resend OTP"
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_send_otp -> {
                binding.cvOtp.visibility = View.VISIBLE
                val phoneNumber = "+91" + binding.edtPhone.text.toString().trim { it <= ' ' }
                // OnVerificationStateChangedCallbacks
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,  // Phone number to verify
                    60,  // Timeout duration
                    TimeUnit.SECONDS,  // Unit of timeout
                    this,  // Activity (for callback binding)
                    mCallbacks
                )
                startTimer()
            }

            R.id.btn_continue ->
                //fetchProfile(edtPhone?.text.toString())
                /*startActivity(
                    Intent(
                        this@OtpLoginActivity,
                        UserDetailsActivity::class.java
                    )
                )*/

                if (binding.edtOtp.text?.length == 0) {
                    Toast.makeText(applicationContext, "Enter the otp", Toast.LENGTH_SHORT).show()
                } else if (binding.edtOtp.text?.length == 6) {
                    verifyCode(binding.edtOtp.text.toString())
                }
        }
    }

    private fun startTimer() {
        countDownTimer.start()
        timerRunning = true
    }

    private fun updateTimerText(timeInMillis: Long) {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
        binding.tvTimmer.text = "Resend OTP in : " + timeLeftFormatted + " sec"
    }

    var mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code = credential.smsCode
                if (code != null) {
                    binding.edtOtp.setText(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    this@OtpLoginActivity, e.message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.w(Tag, "onVerificationFailed", e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                Log.d(Tag, "onCodeSent:$verificationId")
                mVerificationId = verificationId
            }
        }

    // below method is use to verify code from Firebase.
    private fun verifyCode(code: String) {
        binding.progressLayout.visibility = View.VISIBLE
        if (mVerificationId.isNullOrEmpty()) {
            binding.progressLayout.visibility = View.GONE
            Toast.makeText(applicationContext, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show()
        } else {
            val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fetchProfile(binding.edtPhone.text.toString())

                } else {
                    binding.progressLayout.visibility = View.GONE
                    Toast.makeText(applicationContext, "Invalid Otp", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchProfile(userNumber: String) {
        if (userNumber !== "") {
            val profile = FirebaseDatabase.getInstance().reference.child(AppConstants.profile)
                .child(userNumber)
            profile.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataMap = snapshot.value as HashMap<*, *>?
                        for (key in dataMap!!.keys) {
                            preferenceManager.saveLoginState(true)
                            val sharedPreferences =
                                getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            val myEdit = sharedPreferences.edit()
                            myEdit.putString(AppConstants.number, binding.edtPhone.text.toString())
                            myEdit.apply()
                            finish()
                        }
                    } else {
                        val bundle = Bundle()
                        val intent10 =
                            Intent(this@OtpLoginActivity, UserDetailsActivity::class.java)
                        bundle.putString("usernumber", binding.edtPhone.text.toString())
                        intent10.putExtras(bundle)
                        startActivity(intent10)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        binding.progressLayout.visibility = View.GONE
    }
}