package com.sbd.gbd.Activities.LoanActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.sbd.gbd.R
import com.google.android.material.textfield.TextInputEditText

class LoanVehicle : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_vehicle)
        val amount = findViewById<EditText>(R.id.edt_loan_amoutnt)
        val income = findViewById<EditText>(R.id.edt_monthly_income1)
        val edt_vehicle_module = findViewById<TextInputEditText>(R.id.edt_vehicle_module)
        val edt_vehicle_price = findViewById<EditText>(R.id.edt_vehicle_price)
        val edt_pan = findViewById<EditText>(R.id.edt_pancard)
        val edt_aadhar = findViewById<EditText>(R.id.edt_aadhaar)
        val nextbtn = findViewById<Button>(R.id.btn_next_form)


        nextbtn.setOnClickListener{
            var amt = amount.text.toString().trim()
            var inc = income.text.toString().trim()
            var model = edt_vehicle_module.text.toString()
            var vehiclePrice = edt_vehicle_price.text.toString()
            var pan = edt_pan.text.toString()
            var aadhar = edt_aadhar.text.toString()


            if(!amt.equals("") ){
                if(!inc.equals("")){
                    if(!pan.equals("")){
                        if(!aadhar.equals("") ){
                            if(model.equals("") ) {
                                Toast.makeText(this, "Please enter vehicle module", Toast.LENGTH_SHORT).show()
                            } else if(vehiclePrice.equals("") ) {
                                Toast.makeText(this, "Please enter vehicle price", Toast.LENGTH_SHORT).show()
                            } else {
                                val intent = Intent(this, LoanForm::class.java)
                                intent.putExtra("type","6")
                                intent.putExtra("amount", amt)
                                intent.putExtra("income", inc)
                                intent.putExtra("tunure", model)
                                intent.putExtra("vehicleprice", vehiclePrice)
                                intent.putExtra("pan", pan)
                                intent.putExtra("aadhar", aadhar)
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