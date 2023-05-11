package com.example.savings_goal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savings_goal.R
import com.example.savings_goal.adapters.CusAdapter
import com.example.savings_goal.models.CustomerModel
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    private lateinit var saveRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var saveList: ArrayList<CustomerModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        saveRecyclerView = findViewById(R.id.rvCus)
        saveRecyclerView.layoutManager = LinearLayoutManager(this)
        saveRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        saveList = arrayListOf<CustomerModel>()

        getSaveData()


    }

    private fun getSaveData() {

        saveRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("SahanDb")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               saveList.clear()
                if (snapshot.exists()){
                    for (cusSnap in snapshot.children){
                        val cusData = cusSnap.getValue(CustomerModel::class.java)
                        saveList.add(cusData!!)
                    }
                    val mAdapter = CusAdapter(saveList)
                    saveRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : CusAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, SaveGoalsDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("id", saveList[position].id)
                            intent.putExtra("saveGoal", saveList[position].saveGoal)
                            intent.putExtra("moneyGoal", saveList[position].moneyGoal)
                            intent.putExtra("date", saveList[position].date)
                            intent.putExtra("note", saveList[position].note)
                            startActivity(intent)
                        }

                    })

                    saveRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}