package com.chiranths.jobportal1.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chiranths.jobportal1.Activities.BasicActivitys.UserDetailsActivity
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.chiranths.jobportal1.Utilitys.PreferenceManager
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
    lateinit var preferenceManager: PreferenceManager
    private lateinit var mAuth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_login)
        preferenceManager= PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance()
        edtPhone = findViewById(R.id.edt_phone)
        tvSendOtp = findViewById(R.id.tv_send_otp)
        cvOtp = findViewById(R.id.cv_otp)
        btn_continue = findViewById(R.id.btn_continue)
        edt_otp = findViewById(R.id.edt_otp)
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
            }
            R.id.btn_continue ->
                if (edt_otp?.text?.length == 0) {

                Toast.makeText(applicationContext, "Enter the otp", Toast.LENGTH_SHORT).show()
                /*val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                preferenceManager.saveLoginState(true)
                myEdit.putString(AppConstants.number, edtPhone?.text.toString())
                myEdit.commit()
                //fetchProfile(edtPhone?.text.toString())
                startActivity(Intent(this@OtpLoginActivity, UserDetailsActivity::class.java))*/

            } else if (edt_otp?.text?.length == 6) {
                verifyCode(edt_otp?.text.toString())
            }
        }
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
                Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
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
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    /*val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString(AppConstants.number, edtPhone?.text.toString())
                    myEdit.commit()*/
                    fetchProfile(edtPhone?.text.toString())

                } else {
                    Toast.makeText(applicationContext, "Invalid Otp", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchProfile(userNumber: String) {
        if (userNumber !== "") {
            val profile = FirebaseDatabase.getInstance().reference.child(AppConstants.profile).child(userNumber)
            profile.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataMap = snapshot.value as HashMap<String, Any>?
                        for (key in dataMap!!.keys) {
                            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            val myEdit = sharedPreferences.edit()
                            myEdit.putString(AppConstants.number, edtPhone?.text.toString())
                            myEdit.commit()
                            finish()
                        }
                    } else {
                        startActivity(Intent(this@OtpLoginActivity, UserDetailsActivity::class.java))
                        finish()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

}