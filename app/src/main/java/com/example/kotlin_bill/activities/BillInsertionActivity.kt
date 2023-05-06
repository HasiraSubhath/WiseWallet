package com.example.kotlin_bill.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlin_bill.models.BillModel
import com.example.kotlin_bill.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BillInsertionActivity : AppCompatActivity() {
    //initializing variables

    private lateinit var etBillType: EditText
    private lateinit var etBillAmount: EditText
    private lateinit var etBillNotes: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etBillType = findViewById(R.id.etBillType)
        etBillAmount = findViewById(R.id.etBillAmount)
        etBillNotes = findViewById(R.id.etBillNotes)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("BillsDB")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }

    }

    private fun saveEmployeeData() {

        //Geting Values
        val billType = etBillType.text.toString()
        val billAmount = etBillAmount.text.toString()
        val billNotes = etBillNotes.text.toString()

        //validation
        if (billType.isEmpty() || billAmount.isEmpty() || billNotes.isEmpty()) {

            if (billType.isEmpty()) {
                etBillType.error = "Please enter Bill Type"
            }
            if (billAmount.isEmpty()) {
                etBillAmount.error = "Please Bill Amount"
            }
            if (billNotes.isEmpty()) {
                etBillNotes.error = "Please Bill Note"
            }
            Toast.makeText(this, "Some areas are not filled", Toast.LENGTH_LONG).show()
        } else {

            //genrate unique ID
            val billId = dbRef.push().key!!

            val bill = BillModel(billId, billType, billAmount, billNotes)

            dbRef.child(billId).setValue(bill)
                .addOnCompleteListener {
                    Toast.makeText(this, "data insert successfully", Toast.LENGTH_SHORT).show()

                    //clear data after insert
                    etBillType.text.clear()
                    etBillAmount.text.clear()
                    etBillNotes.text.clear()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}