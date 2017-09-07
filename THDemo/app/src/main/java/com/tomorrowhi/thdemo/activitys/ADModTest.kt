package com.tomorrowhi.thdemo.activitys

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.bindView
import com.tomorrowhi.thdemo.R
import com.tomorrowhi.thdemo.adapter.ADModAdapter
import com.tomorrowhi.thdemo.base.BaseActivity
import com.tomorrowhi.thdemo.bean.ADModTestBean

class ADModTest : BaseActivity() {

    private val mRecyclerView: RecyclerView by bindView(R.id.recycler_view_ad_mod)
    private lateinit var ADModList: MutableList<ADModTestBean>
    private lateinit var mAdapter: ADModAdapter


    override fun getLayoutRes(): Int {
        return R.layout.activity_admod
    }

    override fun initComplete(savedInstanceState: Bundle?) {
    }

    override fun initEvent() {
    }

    override fun initData() {
        ADModList = mutableListOf()
        for (i in 1..1000) {
            ADModList.add(ADModTestBean(i, i.toString()))
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.setList(ADModList)
    }

    override fun initView() {
        mAdapter = ADModAdapter(mContext)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    override fun init(savedInstanceState: Bundle?) {
    }
}