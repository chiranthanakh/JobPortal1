package com.chiranths.jobportal1.Activities.LoanActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.chiranths.jobportal1.R

class LoanFormfirst : AppCompatActivity() {

    lateinit var loantyperequired : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_form)

        val amount = findViewById<EditText>(R.id.edt_loan_amoutnt)
        val income = findViewById<EditText>(R.id.edt_monthly_income1)
        val emptype = findViewById<Spinner>(R.id.edt_emp_type)
        val pancard = findViewById<EditText>(R.id.edt_pancard)
        val nextbtn = findViewById<Button>(R.id.btn_next_form)

        val loantype = resources.getStringArray(R.array.typeofemp)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, loantype)
        emptype.setAdapter(adapter)


        emptype.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                loantyperequired = loantype[position]
               // Toast.makeText(this@LoanFormfirst,  loantype[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        nextbtn.setOnClickListener(){

            var amt = amount.text.trim()
            var inc = income.text.trim()
            var pan = pancard.text.trim()

            if(!amt.equals("") ){

                if(!inc.equals("")){

                        if(!pan.equals("")){

                            val intent = Intent(this, LoanForm::class.java)
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