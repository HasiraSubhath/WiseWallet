package com.example.kotlin_bill.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlin_bill.models.BankModel
import com.example.kotlin_bill.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BankInsertionActivity : AppCompatActivity() {


    private lateinit var etBankName: EditText
    private lateinit var etBankBranch: EditText
    private lateinit var etBankAmount: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_insertion)

        etBankName = findViewById(R.id.etBankName)
        etBankBranch = findViewById(R.id.etBankBranch)
        etBankAmount = findViewById(R.id.etBankAmount)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("BankDB")

        btnSaveData.setOnClickListener {
            saveBankData()
        }

    }

    private fun saveBankData() {


        val bankName = etBankName.text.toString()
        val bankBranch = etBankBranch.text.toString()
        val bankAmount = etBankAmount.text.toString()

        //validation
        if (bankName.isEmpty()) {
            etBankName.error = "enter bank name"
        }
        if (bankBranch.isEmpty()) {
            etBankBranch.error = " enter branch"
        }
        if (bankAmount.isEmpty()) {
            etBankAmount.error = "enter amount"
        }


        val bankId = dbRef.push().key!!

        val employee = BankModel(bankId, bankName, bankBranch, bankAmount)

        dbRef.child(bankId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this,"data insert successfully",Toast.LENGTH_SHORT).show()


                etBankName.text.clear()
                etBankBranch.text.clear()
                etBankAmount.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
            }

    }

}