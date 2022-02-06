package com.pluang.searchapp.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.ObjectsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pluang.searchapp.R
import com.pluang.searchapp.databinding.ActivityFullImageShowBinding
import com.pluang.searchapp.ui.view.ImageFullViewActivity

class ImageFullViewActivity : AppCompatActivity() {
    private var binding: ActivityFullImageShowBinding? = null
    var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_image_show)
        setSupportActionBar(binding?.toolbar)
        ObjectsCompat.requireNonNull(supportActionBar).setDisplayHomeAsUpEnabled(true)
        val extras = intent.extras!!
        imageUrl = extras.getString(IMAGE_URL)
        loadPhoto(imageUrl)
    }

    private fun loadPhoto(urlImage: String?) {
        Glide.with(this).load(urlImage).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any,
                target: Target<Drawable?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                binding!!.progressBar.visibility = View.INVISIBLE
                return false
            }
        }).into(binding!!.ivShowImage)
    }

    companion object {
        var IMAGE_URL = "image_url"
        fun start(context: Context, imageUrl: String?) {
            val intent = Intent(context, ImageFullViewActivity::class.java)
            intent.putExtra(IMAGE_URL, imageUrl)
            context.startActivity(intent)
        }
    }
}