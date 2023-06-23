package com.chiranths.jobportal1.Activities.LoanActivity

import android.os.Bundle
import android.text.TextUtils
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.chiranths.jobportal1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class LoanFormGeneral : AppCompatActivity() {
    var edt_loan_name: EditText? = null
    var edt_loan_business_type: EditText? = null
    var edt_loan_amoutnt: EditText? = null
    var edt_loan_dob: EditText? = null
    var edt_loan_income: EditText? = null
    var edt_loan_number: EditText? = null
    var edt_loan_alternative: EditText? = null
    var edt_loan_email: EditText? = null
    var edt_pan_number: EditText? = null
    var edt_loan_adhar: EditText? = null
    var edt_loan_address: EditText? = null
    var edt_loan_pin: EditText? = null
    var tv_loan_type: TextView? = null
    var iv_loan_back: ImageView? = null
    var edt_Familyanual_income :EditText? = null
    private var marriedStatus: AutoCompleteTextView? = null
    var btn_loan_submit: AppCompatButton? = null
    private var ProductsRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_form_general)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Loans")
        mAuth = FirebaseAuth.getInstance()
        initilize()
    }

    private fun initilize() {
        edt_loan_name = findViewById(R.id.edt_loan_name)
        edt_loan_business_type = findViewById(R.id.edt_loan_business_type)
        edt_loan_amoutnt = findViewById(R.id.edt_loan_business_type)
        edt_loan_dob = findViewById(R.id.edt_loan_dob)
        edt_loan_income = findViewById(R.id.edt_loan_income)
        edt_loan_number = findViewById(R.id.edt_loan_number)
        edt_loan_alternative = findViewById(R.id.edt_loan_alternative)
        edt_loan_email = findViewById(R.id.edt_loan_email)
        edt_pan_number = findViewById(R.id.edt_pan_number)
        edt_loan_adhar = findViewById(R.id.edt_loan_adhar)
        edt_loan_address = findViewById(R.id.edt_loan_address)
        edt_loan_pin = findViewById(R.id.edt_loan_pin)
        tv_loan_type = findViewById(R.id.tv_loan_type)
        iv_loan_back = findViewById(R.id.iv_loan_back)
        btn_loan_submit = findViewById(R.id.btn_loan_submit)
        marriedStatus =findViewById(R.id.edt_married_status)
        edt_Familyanual_income = findViewById(R.id.edt_Familyanual_income)



        btn_loan_submit?.setOnClickListener {
            if (TextUtils.isEmpty(
                    edt_loan_number?.getText().toString()
                ) && edt_loan_number?.getText().toString().count() != 10
            ) {
                Toast.makeText(
                    this@LoanFormGeneral,
                    "Please enter a valid phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val phone = "+91" + edt_loan_number?.getText().toString()
                //sendVerificationCode(phone);
                validation()
            }
        }

        val list: ArrayList<String> = ArrayList<String>()
        list.add("Boy")
        list.add("Girl")

        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        marriedStatus?.setAdapter<ArrayAdapter<*>>(arrayAdapter)
        marriedStatus?.setInputType(0)

        marriedStatus?.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus -> if (hasFocus) marriedStatus?.showDropDown() })
    }

    private fun validation() {
        if (TextUtils.isEmpty(edt_loan_name?.getText().toString())) {
            edt_loan_number?.setError("Field cannot be empty");
        } else if (TextUtils.isEmpty(edt_loan_business_type?.getText().toString())) {
            edt_loan_business_type?.setError("Field cannot be empty");
        }else if (TextUtils.isEmpty(edt_loan_amoutnt?.getText().toString())) {
            edt_loan_amoutnt?.setError("Field cannot be empty");
        }else if (TextUtils.isEmpty(edt_loan_dob?.getText().toString())) {
            edt_loan_dob?.setError("Field cannot be empty");
        }else if (TextUtils.isEmpty(edt_loan_income?.getText().toString())) {
            edt_loan_income?.setError("Field cannot be empty");
        }else if (TextUtils.isEmpty(edt_pan_number?.getText().toString())) {
            edt_pan_number?.setError("Field cannot be empty");
        }else if (TextUtils.isEmpty(edt_loan_address?.getText().toString())) {
            edt_loan_address?.setError("Field cannot be empty");
        }else{
            SaveUserDetails()
        }
    }

    open fun SaveUserDetails() {
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MM:dd:yyyy")
        currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm")
        currentTime.format(calendar.time)
        val LoanMap = HashMap<String, Any>()
        LoanMap["name"] = edt_loan_name?.getText().toString()
        LoanMap["number"] = edt_loan_number?.getText().toString()
        LoanMap["email"] = edt_loan_email?.getText().toString()
        LoanMap["dob"] = edt_loan_dob?.getText().toString()
        LoanMap["address"] = edt_loan_address?.getText().toString()
        LoanMap["reqAmount"] = edt_loan_amoutnt?.getText().toString()
        LoanMap["monthincome"] = edt_loan_income?.getText().toString()
        LoanMap["emptype"] = edt_loan_business_type?.getText().toString()
        LoanMap["pancard"] = edt_pan_number?.getText().toString()
        LoanMap["alternative"] = edt_loan_alternative?.getText().toString()
        LoanMap["adhar"] = edt_loan_adhar?.getText().toString()
        LoanMap["pincode"] = edt_loan_pin?.getText().toString()
        LoanMap["status"] = "1"
        LoanMap["appliedOn"] = currentDate
        LoanMap["familyIncome"] = edt_Familyanual_income?.getText().toString()

        ProductsRef?.child(edt_loan_number?.getText().toString())?.updateChildren(LoanMap)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@LoanFormGeneral,
                        "Product is added successfully..",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this@LoanFormGeneral, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
    }

}