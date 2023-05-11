package com.example.savings_goal.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.savings_goal.R
import com.example.savings_goal.models.CustomerModel
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Locale


class SaveGoalsDetailsActivity : AppCompatActivity() {

    private lateinit var tsId: TextView
    private lateinit var tsSaveGoal: TextView
    private lateinit var tsMoneyGoal: TextView
    private lateinit var tsDate: TextView
    private lateinit var tsNote: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_details)


        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("id").toString(),
                intent.getStringExtra("saveGoal").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("id").toString()
            )
        }


//        btnDatePicker = findViewById(R.id.btnDatePicker)
//


    }


    private fun deleteRecord(
        id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("SahanDb").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Saved goal deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun initView() {
        tsId = findViewById(R.id.tsId)
        tsSaveGoal = findViewById(R.id.tsSaveGoal)
        tsMoneyGoal = findViewById(R.id.tsMoneyGoal)
        tsDate = findViewById(R.id.tsDate)
        tsNote = findViewById(R.id.tsNote)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tsId.text = intent.getStringExtra("id")?.substring(0, 5) ?: ""
        tsSaveGoal.text = intent.getStringExtra("saveGoal")
        tsMoneyGoal.text = intent.getStringExtra("moneyGoal")
        tsDate.text = intent.getStringExtra("date")
        tsNote.text = intent.getStringExtra("note")

    }

    private fun openUpdateDialog(
        id: String,
        saveGoal: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val stSaveGoal = mDialogView.findViewById<EditText>(R.id.stSaveGoal)
        val stMoneyGoal = mDialogView.findViewById<EditText>(R.id.stMoneyGoal)
        val stDate = mDialogView.findViewById<EditText>(R.id.stDate)
        val stNote = mDialogView.findViewById<EditText>(R.id.stNote)
        val stDatePicker = mDialogView.findViewById<TextView>(R.id.stDate)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.UK)
            stDatePicker.setText(sdf.format(myCalendar.time))
        }

        stDatePicker.setOnClickListener {
            DatePickerDialog(
                this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //update
        stSaveGoal.setText(intent.getStringExtra("saveGoal").toString())
        stMoneyGoal.setText(intent.getStringExtra("moneyGoal").toString())
        stDate.setText(intent.getStringExtra("date").toString())
        stNote.setText(intent.getStringExtra("note").toString())

        mDialog.setTitle("Updating $saveGoal Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCusData(
                id,
                stSaveGoal.text.toString(),
                stMoneyGoal.text.toString(),
                stDate.text.toString(),
                stNote.text.toString()
            )

            Toast.makeText(applicationContext, "Saved goal Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textview
            tsSaveGoal.text = stSaveGoal.text.toString()
            tsMoneyGoal.text = stMoneyGoal.text.toString()
            tsDate.text = stDate.text.toString()
            tsNote.text = stNote.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateCusData(
        id: String,
        goal: String,
        moneyGoal: String,
        date: String,
        note: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("SahanDb").child(id)
        val cusInfo = CustomerModel(id, goal, moneyGoal, date, note)
        dbRef.setValue(cusInfo)
    }
}