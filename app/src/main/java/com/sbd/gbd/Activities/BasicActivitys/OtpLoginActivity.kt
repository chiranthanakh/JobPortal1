package com.sbd.gbd.Activities.BasicActivitys

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.PreferenceManager
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class OtpLoginActivity : AppCompatActivity(), View.OnClickListener {
    var edtPhone: EditText? = null
    var edt_otp: EditText? = null
    var tvSendOtp: TextView? = null
    var cvOtp: CardView? = null
    var btn_continue: Button? = null
    var mVerificationId: String? = null
    var timertext: TextView? = null
    var progress_layout: RelativeLayout? = null
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String? = null
    lateinit var preferenceManager: PreferenceManager
    private lateinit var countDownTimer: CountDownTimer
    private var timerRunning = false
    private val totalTimeInMillis: Long = 60000 // 1 minute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_login)
        preferenceManager = PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance()
        edtPhone = findViewById(R.id.edt_phone)
        tvSendOtp = findViewById(R.id.tv_send_otp)
        cvOtp = findViewById(R.id.cv_otp)
        timertext = findViewById(R.id.tv_timmer)
        btn_continue = findViewById(R.id.btn_continue)
        edt_otp = findViewById(R.id.edt_otp)
        progress_layout = findViewById(R.id.progress_layout)
        tvSendOtp?.setOnClickListener(this)
        btn_continue?.setOnClickListener(this)

        edtPhone?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 10) {
                    tvSendOtp?.setVisibility(View.VISIBLE)
                } else {
                    tvSendOtp?.setVisibility(View.GONE)
                    cvOtp?.setVisibility(View.GONE)
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
                tvSendOtp?.setText("Resend OTP")
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_send_otp -> {
                cvOtp?.visibility = View.VISIBLE
                val phoneNumber = "+91" + edtPhone?.text.toString().trim { it <= ' ' }
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

                if (edt_otp?.text?.length == 0) {
                    Toast.makeText(applicationContext, "Enter the otp", Toast.LENGTH_SHORT).show()
                } else if (edt_otp?.text?.length == 6) {
                    verifyCode(edt_otp?.text.toString())
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
        timertext?.text = "Resend OTP in : " + timeLeftFormatted + " sec"
    }

    var mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code = credential.smsCode
                if (code != null) {
                    edt_otp!!.setText(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                //fetchProfile(edtPhone?.text.toString()) //romove this
                Toast.makeText(
                    applicationContext,
                    "OTP Request Failed, please try Sometime",
                    Toast.LENGTH_SHORT
                ).show()
                Log.w("TAG", "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                Log.d("TAG", "onCodeSent:$verificationId")
                mVerificationId = verificationId
            }
        }

    // below method is use to verify code from Firebase.
    private fun verifyCode(code: String) {

        progress_layout?.visibility = View.VISIBLE
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fetchProfile(edtPhone?.text.toString())

                } else {
                    progress_layout?.visibility = View.GONE
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
                        val dataMap = snapshot.value as HashMap<String, Any>?
                        for (key in dataMap!!.keys) {
                            preferenceManager.saveLoginState(true)
                            val sharedPreferences =
                                getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            val myEdit = sharedPreferences.edit()
                            myEdit.putString(AppConstants.number, edtPhone?.text.toString())
                            myEdit.commit()
                            finish()
                        }
                    } else {
                        startActivity(
                            Intent(
                                this@OtpLoginActivity,
                                UserDetailsActivity::class.java
                            )
                        )
                        var bundle = Bundle()
                        val intent10 = Intent(this@OtpLoginActivity, UserDetailsActivity::class.java)
                        bundle.putString("usernumber",edtPhone?.text.toString())
                        intent10.putExtras(bundle)
                        startActivity(intent10)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        progress_layout?.visibility = View.GONE
    }
}