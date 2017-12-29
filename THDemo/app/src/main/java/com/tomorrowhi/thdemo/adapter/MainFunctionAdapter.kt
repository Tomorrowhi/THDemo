package com.tomorrowhi.thdemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.tomorrowhi.thdemo.R
import com.tomorrowhi.thdemo.bean.MainFunctionBean
import com.tomorrowhi.thdemo.interfaces.RecyclerViewClickListener

class MainFunctionAdapter(mCtx: Context) : RecyclerView.Adapter<MainFunctionAdapter.MyViewHolder>() {

    private lateinit var functionList: List<MainFunctionBean>
    private var context: Context = mCtx

    private lateinit var mItemCLickListener: RecyclerViewClickListener


    fun setList(functionList: List<MainFunctionBean>) {
        this.functionList = functionList
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: RecyclerViewClickListener) {
        this.mItemCLickListener = listener
    }


    override fun getItemCount(): Int {
        return functionList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.main_function_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder?.functionName?.text = functionList.get(position).name
        holder?.functionLl?.setOnClickListener {
            mItemCLickListener.itemClick(position)

        }
    }


    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var functionName: TextView = item.findViewById(R.id.function_name)
        var functionLl: LinearLayout = item.findViewById(R.id.function_ll)
    }

}