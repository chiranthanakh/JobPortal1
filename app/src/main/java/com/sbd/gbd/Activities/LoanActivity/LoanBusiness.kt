package com.sbd.gbd.Activities.LoanActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.sbd.gbd.R
import com.sbd.gbd.databinding.ActivityLoanBinding
import com.sbd.gbd.databinding.ActivityLoanBusinessBinding

class LoanBusiness : AppCompatActivity() {
    private lateinit var binding: ActivityLoanBusinessBinding
    var companyType : String? = ""
    var businessNature : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoanBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val comtype = resources.getStringArray(R.array.companyType)
        val nature = resources.getStringArray(R.array.businessNature)

        binding.ivNavView.setOnClickListener {
            finish()
        }

        binding.SpCompanyType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                companyType = comtype[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spBusinessNature.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                businessNature = nature[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.btnNextForm.setOnClickListener{
            val amt = binding.edtLoanAmoutnt.text.toString().trim()
            val inc = binding.edtMonthlyIncome1.text.toString().trim()
            val turnover = binding.edtTurnover.text.toString()
            val companyName = binding.edtCompanyName.text.toString()
            val pan = binding.edtPancard.text.toString()
            val aadhar = binding.edtAadhaar.text.toString()


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