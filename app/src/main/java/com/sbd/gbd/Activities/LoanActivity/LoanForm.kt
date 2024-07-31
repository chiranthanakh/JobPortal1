package com.sbd.gbd.Activities.LoanActivity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sbd.gbd.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sbd.gbd.databinding.ActivityLoanForm2Binding

class LoanForm : AppCompatActivity() {
    private lateinit var binding: ActivityLoanForm2Binding
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    private var genderType: String? = "Male"
    var type : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoanForm2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Loans")
        mAuth = FirebaseAuth.getInstance()
        loadingBar = ProgressDialog(this)
        val bundle = intent.extras
        type = bundle?.getString("type", "")
        initilize()
    }

    private fun initilize() {
        binding.ivNavView.setOnClickListener {
            finish()
        }
        if(type.equals("2")) {
            binding.tvLoantype.text = "Business Loan"
        } else if (type.equals("3")) {
            binding.tvLoantype.text = "Home Loan"
        } else if (type.equals("4")) {
            binding.tvLoantype.text = "Martgage Loan"
        }else if (type.equals("5")) {
            binding.tvLoantype.text = "LAP"
        }else if (type.equals("6")) {
            binding.tvLoantype.text = "Vehicle Loan"
        }
        binding.rbGender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { arg0, id ->
            when (id) {
                R.id.rb_male -> {
                    genderType = "Male"
                }
                R.id.rb_female -> {
                    genderType = "Female"
                }
            }
        })


        binding.btnNext.setOnClickListener(View.OnClickListener { view: View? ->
            if (TextUtils.isEmpty(binding.edtName.getText().toString())) {
                toast("Please enter Your Name.")
            } else if(TextUtils.isEmpty(binding.edtNumber.getText().toString()) || binding.edtNumber.getText().toString().length != 10){
                toast("Please enter a valid phone number.")
            }else if(TextUtils.isEmpty(binding.edtDob.getText().toString())){
                toast("Please enter date of birth.")
            }else if(TextUtils.isEmpty(binding.edtAddress.getText().toString())){
                toast("Please enter your address.")
            }else if(TextUtils.isEmpty(binding.edtCity.getText().toString())){
                toast("Please enter your city")
            } else {
                val phone = "+91" + binding.edtNumber.getText().toString()
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
        LoanMap["name"] = binding.edtName.text.toString()
        LoanMap["number"] = binding.edtNumber.text.toString()
        LoanMap["email"] = binding.edtEmail.text.toString()
        LoanMap["dob"] = binding.edtDob.text.toString()
        LoanMap["address"] = binding.edtAddress.text.toString()
        LoanMap["gender"]  = genderType.toString()
        LoanMap["city"]  = binding.edtCity.text.toString()
        LoanMap["reqAmount"] = amount
        LoanMap["monthincome"] = income
        LoanMap["tenure"] = tenure
        LoanMap["vehicleprice"] = vehicleprice
        LoanMap["pancard"] = pancard
        LoanMap["aadhar"] = aadhar

        ProductsRef!!.child(binding.edtNumber.text.toString()).updateChildren(LoanMap)
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
        LoanMap["name"] = binding.edtName.text.toString()
        LoanMap["number"] = binding.edtNumber.text.toString()
        LoanMap["email"] = binding.edtEmail.text.toString()
        LoanMap["dob"] = binding.edtDob.text.toString()
        LoanMap["address"] = binding.edtAddress.text.toString()
        LoanMap["gender"]  = genderType.toString()
        LoanMap["city"]  = binding.edtCity.text.toString()
        LoanMap["reqAmount"] = amount
        LoanMap["monthincome"] = income
        LoanMap["emptype"] = emptype
        LoanMap["pancard"] = pancard
        LoanMap["aadhar"] = aadhar

        ProductsRef!!.child(binding.edtNumber.text.toString()).updateChildren(LoanMap)
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
        LoanMap["name"] = binding.edtName.text.toString()
        LoanMap["number"] = binding.edtNumber.text.toString()
        LoanMap["email"] = binding.edtEmail.text.toString()
        LoanMap["dob"] = binding.edtDob.text.toString()
        LoanMap["address"] = binding.edtAddress.text.toString()
        LoanMap["gender"]  = genderType.toString()
        LoanMap["city"]  = binding.edtCity.text.toString()
        LoanMap["reqAmount"] = amount
        LoanMap["monthlyincome"] = income
        LoanMap["pancard"] = pan
        LoanMap["turnover"] = turnover
        LoanMap["companyName"] = companyName
        LoanMap["aadhar"] = aadhar
        LoanMap["companyType"] = companyType
        LoanMap["nature"] = nature

        ProductsRef!!.child(binding.edtNumber.text.toString()).updateChildren(LoanMap)
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