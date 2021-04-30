package com.example.api

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class MyAdapter (val context: Context , var dataList: ArrayList<CEOModel>?): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain = view.llMain
        val tvId = view.tvId
        val tvName = view.tvnama
        val tvCompanyName = view.tvcompany
        val ivDelete = view.ivdelete
        val ivEdit = view.ivedit
}

override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): MyViewHolder {
    return MyViewHolder(
    (LayoutInflater.from(parent.context).inflate(
        R.layout.item_row , parent , false
    )
            )
    )
}

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList?.get(position)

        holder.tvId.text = (position+1).toString()
        holder.tvName.text = "(id:${item?.id.toString()}) ${item?.name.toString()}"
        holder.tvCompanyName.text = item?.company_name.toString()

        if (position % 2 == 0 ) {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context , R.color.teal_200))
        }else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context , R.color.white))
        }
        holder.ivEdit.setOnClickListener {
            if (context is MainActivity) {
                context.updateRecordDialog(item!!)
            }
        }

        holder.ivDelete.setOnClickListener {
            if (context is MainActivity) {
                context.deleteRecordDialog(item!!)
            }
        }
    }



    override fun getItemCount(): Int {
        return dataList!!.size
    }
}