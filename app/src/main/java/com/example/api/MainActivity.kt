package com.example.api

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_dialog.*
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnadd.setOnClickListener {
            addRecord()
        }
        setupListOfDataIntoRecyclerView()
    }

    fun setupListOfDataIntoRecyclerView() {
        Rvitem.layoutManager = LinearLayoutManager(this)

        //Ambil data CEO dari API
        var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
        apiInterface.getCEOs().enqueue(object : retrofit2.Callback<ArrayList<CEOModel>> {
            override fun onFailure(call : retrofit2.Call<ArrayList<CEOModel>>?, t: Throwable) {
                Toast.makeText(baseContext, "Data Downloading is Failed", Toast.LENGTH_LONG).show()
                Log.d("API Failed", t.message.toString())
            }

            override fun onResponse(call: retrofit2.Call<ArrayList<CEOModel>>?, response: Response<ArrayList<CEOModel>>?) {
                var ceoData = response?.body()!!
                if (ceoData.size > 0) {
                    Rvitem.visibility = View.VISIBLE
                    tvnorecord.visibility = View.GONE
                    Rvitem.adapter = MyAdapter(this@MainActivity, ceoData)
                } else {
                    Rvitem.visibility = View.GONE
                    tvnorecord.visibility = View.VISIBLE
                }
                Toast.makeText(baseContext, "Data Downloading is Success", Toast.LENGTH_LONG).show()
                Log.d("API Response", response.toString())
            }
        })

    }

    fun addRecord() {
        val name = etnama.text.toString()
        val companyname = etcompany.text.toString()

        if(name == "" || companyname == ""){
            Toast.makeText(this , "Masih Ada Field yg kosong , tolong di lengkapi",
                    Toast.LENGTH_LONG).show()
        } else {
            val newCEO : CEOModel = CEOModel(null , name , companyname)

            var apiInterface : ApiInterface =
                    ApiClient().getApiClient()!!.create(ApiInterface::class.java)
            var requestCall : retrofit2.Call<CEOModel> = apiInterface.addCEO(newCEO)

            requestCall.enqueue(object : retrofit2.Callback<CEOModel>{
                override fun onResponse(call: retrofit2.Call<CEOModel>, response: Response<CEOModel>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity,
                                "Berhasil Tersimpan" , Toast.LENGTH_LONG).show()
                        setupListOfDataIntoRecyclerView()
                        etnama.setText("")
                        etcompany.setText("")
                    }else {
                        Toast.makeText(this@MainActivity ,
                                "Gagal tersimpan" , Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<CEOModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity ,
                            "Gagal tersimpan" , Toast.LENGTH_LONG).show()
                }

            })
        }
    }
    fun updateRecordDialog(CEOModel: CEOModel){
        val updateDialog = Dialog(this,R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.update_dialog)

        updateDialog.etUpdateName.setText(CEOModel.name)
        updateDialog.etUpdateComp.setText(CEOModel.company_name)

        updateDialog.tvUpdate.setOnClickListener {
            val name = updateDialog.etUpdateName.text.toString()
            val companyName = updateDialog.etUpdateComp.text.toString()

            if(name.isEmpty() && companyName.isEmpty()){
                Toast.makeText(this,
                    "Masih Ada Field yang kosong, Tolong lengkapi",
                    Toast.LENGTH_LONG).show()
            }else{
                val newCEO : CEOModel = CEOModel(null, name, companyName)

                var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
                var requestCall : retrofit2.Call<CEOModel> = apiInterface.updateCEO(newCEO, CEOModel.id!!)

                requestCall.enqueue(object : retrofit2.Callback<CEOModel>{

                    override fun onResponse(call: retrofit2.Call<CEOModel>, response: Response<CEOModel>) {
                        if(response.isSuccessful){
                            Toast.makeText(this@MainActivity,
                                "Berhasil tersimpan", Toast.LENGTH_LONG).show()
                            setupListOfDataIntoRecyclerView()
                            etnama.setText("")
                            etcompany.setText("")
                        }else{
                            Toast.makeText(this@MainActivity,
                                "Gagal tersimpan", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<CEOModel>, t: Throwable) {
                        Toast.makeText(this@MainActivity,
                            "Gagal tersimpan", Toast.LENGTH_LONG).show()

                    }

                })
            }
        }
        updateDialog.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()

    }



    fun deleteRecordDialog(CEOModel: CEOModel?){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Berhasil Dihapus")

        builder.setMessage("Apa Kamu Yakin?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Ya"){dialog, which: Int ->
            var apiInterface: ApiInterface =
                ApiClient().getApiClient()!!.create(ApiInterface::class.java)

            var requestCall: retrofit2.Call<CEOModel> = apiInterface.deleteCEO(CEOModel?.id!!)
            requestCall.enqueue(object : retrofit2.Callback<CEOModel>{
                override fun onResponse(call: retrofit2.Call<CEOModel>, response: Response<CEOModel>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@MainActivity,
                            "Berhasil terhapus", Toast.LENGTH_LONG).show()
                        setupListOfDataIntoRecyclerView()
                    }
                }

                override fun onFailure(call: retrofit2.Call<CEOModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity,
                        "Gagal Terhapus", Toast.LENGTH_LONG).show()
                }

            })
        }


        builder.setNegativeButton("No"){dialog, which: Int->
            dialog?.dismiss()
        }
        builder.show()
    }

}

