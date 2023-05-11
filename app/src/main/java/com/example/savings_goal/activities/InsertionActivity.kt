package com.example.savings_goal.activities

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.savings_goal.R
import com.example.savings_goal.models.CustomerModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.maltaisn.calcdialog.CalcDialog
import java.util.Calendar
import java.util.Date
import java.util.Locale



class InsertionActivity : AppCompatActivity() {
    //initializing variables

    private lateinit var stSaveGoal: EditText
    private lateinit var stMoneyGoal: EditText
    private lateinit var stDate: EditText
    private lateinit var stNote: EditText
    private lateinit var btnSaveData: Button
    private lateinit var etDatePicker: TextView
    private lateinit var etMoneyGoal: EditText

//  private lateinit var btnDatePicker: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)


        etDatePicker = findViewById(R.id.etDate)
//        btnDatePicker = findViewById(R.id.btnDatePicker)

//        val editText = findViewById<EditText>(R.id.etMoneyGoal)
//        editText.setOnClickListener {
//            showCalculatorDialog()
//        }
//
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener() {view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        etDatePicker.setOnClickListener{
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }



        stSaveGoal = findViewById(R.id.etMyGoal)
        stMoneyGoal = findViewById(R.id.etMoneyGoal)
        stDate = findViewById(R.id.etDate)
        stNote = findViewById(R.id.etNote)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("SahanDb")

        btnSaveData.setOnClickListener {
            saveCustomerData()
        }

    }

//    private fun showCalculatorDialog() {
//        val currentValue = etMoneyGoal.text.toString().toDoubleOrNull() ?: 0.0
//        val calcDialog = CalcDialog.builder(requireContext())
//            .startValue(currentValue)
//            .showNegativeButton(true)
//            .decimalSeparator('.')
//            .maxDigits(12) // optional
//            .alwaysShowSign(false) // optional
//            .callback { result ->
//                etMoneyGoal.setText(result.toString())
//            }
//            .show()
//    }

    private fun updateLable(myCalendar: Calendar) {
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.UK)
            etDatePicker.setText(sdf.format(myCalendar.time))
    }


    private fun saveCustomerData() {

        //Getting user input
        val saveGoal = stSaveGoal.text.toString()
        val moneyGoal = stMoneyGoal.text.toString()
        val date = stDate.text.toString()
        val note = stNote.text.toString()

        //Validating part
        if (saveGoal.isEmpty()) {
            stSaveGoal.error = "Please enter goal"
        }
        if (moneyGoal.isEmpty()) {
            stMoneyGoal.error = "Please enter money goal"
        }
        if (date.isEmpty()) {
            stDate.error = "Please enter date"
        }
        if (note.isEmpty()) {
            stNote.error = "Please enter note"
        }


        //Unique ID generation
        val id = dbRef.push().key!!

        val customer = CustomerModel(id, saveGoal, moneyGoal, date, note)

        dbRef.child(id).setValue(customer)
            .addOnCompleteListener {
                Toast.makeText(this,"Goal Saved successfully",Toast.LENGTH_SHORT).show()

                //clear data after insert
                stSaveGoal.text.clear()
                stMoneyGoal.text.clear()
                stDate.text.clear()
                stNote.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this,"Try Again! ${err.message}",Toast.LENGTH_SHORT).show()
            }

    }

}