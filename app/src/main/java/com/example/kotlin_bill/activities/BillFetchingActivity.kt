package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_bill.R
import com.example.kotlin_bill.adapters.BillAdapter
import com.example.kotlin_bill.models.BillModel
import com.google.firebase.database.*

class BillFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var billList: ArrayList<BillModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        billList = arrayListOf<BillModel>()

        getEmployeeData()


    }

    private fun getEmployeeData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("BillsDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               billList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val billData = empSnap.getValue(BillModel::class.java)
                        billList.add(billData!!)
                    }
                    val mAdapter = BillAdapter(billList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BillAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@BillFetchingActivity, BillDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("billId", billList[position].billId)
                            intent.putExtra("billType", billList[position].billType)
                            intent.putExtra("billAmount", billList[position].billAmount)
                            intent.putExtra("billNotes", billList[position].billNotes)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}