package com.sbd.gbd.Activities.LoanActivity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class LoanForm : AppCompatActivity() {
    private val ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var et_name: EditText? = null
    private var et_address: EditText? = null
    private var edt_email: EditText? = null
    private var et_number: EditText? = null
    private var et_dob: EditText? = null
    private var btn_next: Button? = null
    private var loadingBar: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    private var genderType: String? = "Male"
    private var gender : RadioGroup? = null
    private var edt_city : EditText ?= null
    private var tv_loantype : TextView ?= null
    var type : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_form2)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Loans")
        mAuth = FirebaseAuth.getInstance()
        loadingBar = ProgressDialog(this)
        val bundle = intent.extras
        type = bundle?.getString("type", "")
        initilize()
    }

    private fun initilize() {
        et_name = findViewById(R.id.edt_name)
        tv_loantype = findViewById(R.id.tv_loantype)
        edt_email = findViewById(R.id.edt_email)
        btn_next = findViewById(R.id.btn_next)
        et_number = findViewById(R.id.edt_number)
        et_dob = findViewById(R.id.edt_dob)
        et_address = findViewById(R.id.edt_address)
         gender = findViewById(R.id.rb_gender)
        edt_city = findViewById(R.id.edt_city)

        if(type.equals("2")) {
            tv_loantype?.setText("Business Loan")
        } else if (type.equals("3")) {
            tv_loantype?.setText("Home Loan")
        } else if (type.equals("4")) {
            tv_loantype?.setText("Martgage Loan")
        }else if (type.equals("5")) {
            tv_loantype?.setText("LAP")
        }else if (type.equals("6")) {
            tv_loantype?.setText("Vehicle Loan")
        }
        gender?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { arg0, id ->
            when (id) {
                R.id.rb_male -> {
                    genderType = "Male"
                }
                R.id.rb_female -> {
                    genderType = "Female"
                }
            }
        })


        btn_next?.setOnClickListener(View.OnClickListener { view: View? ->
            if (TextUtils.isEmpty(et_number?.getText().toString())) {
                toast("Please enter a valid phone number.")
            } else if(TextUtils.isEmpty(et_name?.getText().toString())){
                toast("Please your name.")
            }else if(TextUtils.isEmpty(et_dob?.getText().toString())){
                toast("Please enter date of birth.")
            }else if(TextUtils.isEmpty(et_address?.getText().toString())){
                toast("Please enter your address.")
            }else if(TextUtils.isEmpty(edt_city?.getText().toString())){
                toast("Please enter your city")
            } else {
                val phone = "+91" + et_number?.getText().toString()
                //sendVerificationCode(phone);
                if(type.equals("2")) {
                    SaveBusinessDetails()
                } else if (type.equals("6")) {
                    SavevehicleDetails()
                } else {
                    SaveUserDetails()
                }
            }
        })
    }

    fun toast(msg :String){
        Toast.makeText(
            this@LoanForm,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun SavevehicleDetails() {
        loadingBar!!.setTitle("Submiting loan form")
        loadingBar!!.setMessage("please wait your loan application is submiting")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()
        val bundle = intent.extras

        val amount = bundle!!.getString("amount", "")
        val income = bundle.getString("income", "")
        val vehicleprice = bundle.getString("vehicleprice", "")
        val tenure = bundle.getString("tenure", "")
        val pancard = bundle.getString("pan", "")
        val aadhar = bundle.getString("aadhar", "")
        val LoanMap = HashMap<String, Any>()

        LoanMap["loanType"] = "VehicleLoan"
        LoanMap["name"] = et_name!!.text.toString()
        LoanMap["number"] = et_number!!.text.toString()
        LoanMap["email"] = edt_email!!.text.toString()
        LoanMap["dob"] = et_dob!!.text.toString()
        LoanMap["address"] = et_address!!.text.toString()
        LoanMap["gender"]  = genderType.toString()
        LoanMap["city"]  = edt_city?.text.toString()
        LoanMap["reqAmount"] = amount
        LoanMap["monthincome"] = income
        LoanMap["tenure"] = tenure
        LoanMap["vehicleprice"] = vehicleprice
        LoanMap["pancard"] = pancard
        LoanMap["aadhar"] = aadhar

        ProductsRef!!.child(et_number!!.text.toString()).updateChildren(LoanMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@LoanForm,
                        "Your Loan Application submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    Showsuccess()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@LoanForm, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun SaveUserDetails() {
        loadingBar!!.setTitle("Submiting loan form")
        loadingBar!!.setMessage("please wait your loan application is submiting")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()
        val bundle = intent.extras
        val amount = bundle!!.getString("amount", "")
        val income = bundle.getString("income", "")
        val emptype = bundle.getString("emptype", "")
        val pancard = bundle.getString("pancard", "")
        val aadhar = bundle.getString("aadhar", "")
        val LoanMap = HashMap<String, Any>()
        LoanMap["name"] = et_name!!.text.toString()
        LoanMap["number"] = et_number!!.text.toString()
        LoanMap["email"] = edt_email!!.text.toString()
        LoanMap["dob"] = et_dob!!.text.toString()
        LoanMap["address"] = et_address!!.text.toString()
        LoanMap["gender"]  = genderType.toString()
        LoanMap["city"]  = edt_city?.text.toString()
        LoanMap["reqAmount"] = amount
        LoanMap["monthincome"] = income
        LoanMap["emptype"] = emptype
        LoanMap["pancard"] = pancard
        LoanMap["aadhar"] = aadhar

        ProductsRef!!.child(et_number!!.text.toString()).updateChildren(LoanMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@LoanForm,
                        "Your Loan Application submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    Showsuccess()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@LoanForm, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun SaveBusinessDetails() {
        loadingBar!!.setTitle("Submiting loan form")
        loadingBar!!.setMessage("please wait your loan application is submitting")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()
        val bundle = intent.extras
        val amount = bundle!!.getString("amount", "")
        val income = bundle.getString("income", "")
        val turnover = bundle.getString("turnover", "")
        val companyName = bundle.getString("companyName", "")
        val pan = bundle.getString("pan", "")
        val aadhar = bundle.getString("aadhar", "")
        val companyType = bundle.getString("companyType", "")
        val nature = bundle.getString("nature", "")


        val LoanMap = HashMap<String, Any>()
        LoanMap["loanType"] = "business"
        LoanMap["name"] = et_name!!.text.toString()
        LoanMap["number"] = et_number!!.text.toString()
        LoanMap["email"] = edt_email!!.text.toString()
        LoanMap["dob"] = et_dob!!.text.toString()
        LoanMap["address"] = et_address!!.text.toString()
        LoanMap["gender"]  = genderType.toString()
        LoanMap["city"]  = edt_city?.text.toString()
        LoanMap["reqAmount"] = amount
        LoanMap["monthlyincome"] = income
        LoanMap["pancard"] = pan
        LoanMap["turnover"] = turnover
        LoanMap["companyName"] = companyName
        LoanMap["aadhar"] = aadhar
        LoanMap["companyType"] = companyType
        LoanMap["nature"] = nature

        ProductsRef!!.child(et_number!!.text.toString()).updateChildren(LoanMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(
                        this@LoanForm,
                        "Your Loan Application submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    Showsuccess()
                } else {
                    loadingBar!!.dismiss()
                    val message = task.exception.toString()
                    Toast.makeText(this@LoanForm, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun Showsuccess() {
        val dialogView = Dialog(this)
        dialogView.setContentView(R.layout.dialog_success)
        dialogView.setCancelable(false)
        val submit: LinearLayout = dialogView.findViewById(R.id.ll_btn_submit)

        submit.setOnClickListener {
            val intent = Intent(this, LoanActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            dialogView.dismiss()
        }
        dialogView.show()
    }

}