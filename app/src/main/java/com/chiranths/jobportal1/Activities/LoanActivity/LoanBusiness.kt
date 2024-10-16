package com.chiranths.jobportal1.Activities.LoanActivity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.databinding.ActivityLoanBusinessBinding
import com.chiranths.jobportal1.databinding.ActivityStartingBinding
import com.chiranths.jobportal1.databinding.DashboardFragmentBinding

class LoanBusiness : AppCompatActivity() {

    //private lateinit var binding: ActivityLoanBusinessBinding
    var companyType : String? = ""
    var businessNature : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_business)
        val amount = findViewById<EditText>(R.id.edt_loan_amoutnt)
        val income = findViewById<EditText>(R.id.edt_monthly_income1)
        val turnover = findViewById<EditText>(R.id.edt_turnover)
        val companyName = findViewById<EditText>(R.id.edt_company_name)
        val edt_pan = findViewById<EditText>(R.id.edt_pancard)
        val edt_aadhar = findViewById<EditText>(R.id.edt_aadhaar)
        val sp_company_type = findViewById<Spinner>(R.id.Sp_company_type)
        val sp_business_nature = findViewById<Spinner>(R.id.sp_business_nature)
        val nextbtn = findViewById<Button>(R.id.btn_next_form)

       // binding= ActivityLoanBusinessBinding.inflate(layoutInflater)

        val comtype = resources.getStringArray(R.array.companyType)
        val nature = resources.getStringArray(R.array.businessNature)

        sp_company_type.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                companyType = comtype[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        sp_business_nature.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                businessNature = nature[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        nextbtn.setOnClickListener{
            var amt = amount.text.toString().trim()
            var inc = income.text.toString().trim()
            var turnover = turnover.text.toString()
            var companyName = companyName.text.toString()
            var pan = edt_pan.text.toString()
            var aadhar = edt_aadhar.text.toString()


            if(!amt.equals("") ){
                if(!inc.equals("")){
                    if(!pan.equals("")){
                        if(!aadhar.equals("") ){
                            if(companyType.equals("") && businessNature.equals("") ) {
                                Toast.makeText(this, "Please Select company type and nature of business", Toast.LENGTH_SHORT).show()
                            } else {
                                val intent = Intent(this, LoanForm::class.java)
                                intent.putExtra("type","2")
                                intent.putExtra("amount", amt)
                                intent.putExtra("income", inc)
                                intent.putExtra("turnover", turnover)
                                intent.putExtra("companyName", companyName)
                                intent.putExtra("pan", pan)
                                intent.putExtra("aadhar", aadhar)
                                intent.putExtra("companyType", companyType)
                                intent.putExtra("nature", businessNature)
                                startActivity(intent)
                            }

                        }else{
                            Toast.makeText(this, "Please Enter aadhar card number", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Please Enter pan card number", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Please Enter your annual income", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please enter Required Loan Amount", Toast.LENGTH_SHORT).show()
            }
        }

    }
}