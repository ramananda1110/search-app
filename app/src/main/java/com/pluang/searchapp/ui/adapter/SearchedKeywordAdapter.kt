package com.pluang.searchapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pluang.searchapp.R
import com.pluang.searchapp.databinding.SearchListItemBinding
import com.pluang.searchapp.ui.adapter.SearchedKeywordAdapter.SearchViewHolder
import com.pluang.searchapp.ui.contact.OnSearchListener
import java.util.*
import kotlin.collections.ArrayList

class SearchedKeywordAdapter(
    private val context: Context,
    mListData: ArrayList<String?>,
    onRequestListener: OnSearchListener
) : RecyclerView.Adapter<SearchViewHolder>() {
    private val mList: ArrayList<String?>
    private var layoutInflater: LayoutInflater? = null
    private val onSearchListener: OnSearchListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: SearchListItemBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.search_list_item, parent, false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchKeywords = mList[position]
        holder.binding.tvSearchTitle.text = searchKeywords
        holder.binding.tvSearchTitle.setOnClickListener { view: View? ->
            onSearchListener.onSearchKeywordPress(
                searchKeywords
            )
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class SearchViewHolder(val binding: SearchListItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    init {
        Collections.reverse(mListData)
        mList = mListData
        onSearchListener = onRequestListener
    }
}