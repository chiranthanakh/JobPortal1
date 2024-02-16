package com.chiranths.jobportal1.Activities.LoanActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.chiranths.jobportal1.R

class LoanFormfirst : AppCompatActivity() {

    lateinit var loantyperequired : String
    var tenureYears : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_form)

        val tv_loantype = findViewById<TextView>(R.id.tv_loantype)
        val amount = findViewById<EditText>(R.id.edt_loan_amoutnt)
        val income = findViewById<EditText>(R.id.edt_monthly_income1)
        val emptype = findViewById<Spinner>(R.id.edt_emp_type)
        val pancard = findViewById<EditText>(R.id.edt_pancard)
        val tenure = findViewById<Spinner>(R.id.sp_loan_tenure)
        val ll_tenure = findViewById<LinearLayout>(R.id.ll_tenure)
        val ll_company = findViewById<LinearLayout>(R.id.ll_company_name)
        val nextbtn = findViewById<Button>(R.id.btn_next_form)

        val loantype = resources.getStringArray(R.array.typeofemp)
        val tenurelist = resources.getStringArray(R.array.tenure)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, loantype)
        emptype.setAdapter(adapter)

        val bundle = intent.extras
        val type = bundle?.getString("type", "")
        if(type.equals("3")) {
            tv_loantype.setText("Home Loan")
            ll_company.visibility = View.GONE
        } else {
            ll_tenure.visibility = View.GONE
        }

        emptype.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                loantyperequired = loantype[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        tenure.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                tenureYears = tenurelist[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        nextbtn.setOnClickListener(){

            var amt = amount.text.toString().trim()
            var inc = income.text.toString().trim()
            var pan = pancard.text.toString().trim()

            if(!amt.equals("") ){

                if(!inc.equals("")){

                        if(!pan.equals("")){

                            val intent = Intent(this, LoanForm::class.java)
                            intent.putExtra("type", type)
                            intent.putExtra("amount", amt)
                            intent.putExtra("income", inc)
                            intent.putExtra("emptype", loantyperequired)
                            intent.putExtra("pancard", pan)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this, "Please Enter pan card number", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Please Select employement type", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Please enter Anual income", Toast.LENGTH_SHORT).show()
                }
        }

    }
}