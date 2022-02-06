package com.pluang.searchapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pluang.searchapp.R
import com.pluang.searchapp.data.model.ResultsItem
import com.pluang.searchapp.databinding.ImageViewAdapterBinding
import com.pluang.searchapp.ui.adapter.ImageViewAdapter.FileViewHolder
import com.pluang.searchapp.ui.contact.OnPressListener
import com.squareup.picasso.Picasso

class ImageViewAdapter(
    private val dataArrayList: List<ResultsItem>,
    private val onPressListener: OnPressListener
) : RecyclerView.Adapter<FileViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FileViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.context)
        }
        val binding: ImageViewAdapterBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.image_view_adapter, viewGroup, false
        )
        return FileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val (_, _, _, _, _, _, _, _, urls) = dataArrayList.get(position)
        holder.binding.ivShowImage.visibility = View.VISIBLE
        Picasso.get().load(urls!!.small).placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.ivShowImage)

        holder.binding.ivShowImage.setOnClickListener {
            onPressListener.onImagePressed(urls.full)
        }
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    class FileViewHolder(
        val binding: ImageViewAdapterBinding,

        ) : RecyclerView.ViewHolder(
        binding.root
    )
}