package com.tomorrowhi.thdemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.NativeExpressAdView
import com.tomorrowhi.thdemo.R
import com.tomorrowhi.thdemo.adapter.ADModAdapter.ViewHolder
import com.tomorrowhi.thdemo.bean.ADModTestBean
import com.tomorrowhi.thdemo.interfaces.RecyclerViewClickListener

class ADModAdapter(mCtx: Context) : RecyclerView.Adapter<ViewHolder>() {


    private lateinit var adModTestList: List<ADModTestBean>
    private var context: Context = mCtx

    private val TYPE_CONTENT = 0
    private val TYPE_AD = 1

    private lateinit var mItemCLickListener: RecyclerViewClickListener


    fun setList(functionList: List<ADModTestBean>) {
        this.adModTestList = functionList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != 0 && (position % 10) == 0) {
            TYPE_AD
        } else {
            TYPE_CONTENT
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (i in 1..(adModTestList.size - 1)) {
            if ((i % 10) == 0) {
                count++
            }
        }
        return adModTestList.size + count
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view_content: View = View.inflate(context, R.layout.ad_mod_content_item, null)
        val view_ad: View = View.inflate(context, R.layout.ad_mod_item, null)
        var holder: ViewHolder
        holder = when (viewType) {
            TYPE_CONTENT -> MyViewHolderContent(view_content)
            TYPE_AD -> MyViewHolderAD(view_ad)
            else -> MyViewHolderContent(view_content)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adModTestBean = adModTestList.get(position)
        var holderView: RecyclerView.ViewHolder
        if (position != 0 && (position % 10) == 0) {
            holderView = holder as MyViewHolderAD
            holderView.ad.loadAd(AdRequest.Builder().build())
        } else {
            holderView = holder as MyViewHolderContent
            holderView.desc.text = adModTestBean.desc
        }
    }


    class MyViewHolderContent(item: View) : ViewHolder(item) {
        var desc: TextView = item.findViewById(R.id.tv_ad_mod_test) as TextView
    }

    class MyViewHolderAD(item: View) : ViewHolder(item) {
        var ad: NativeExpressAdView = item.findViewById(R.id.adView) as NativeExpressAdView
    }

    open class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    }

}