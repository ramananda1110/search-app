package com.pluang.searchapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.pluang.searchapp.BuildConfig
import com.pluang.searchapp.R
import com.pluang.searchapp.data.model.ResultsItem
import com.pluang.searchapp.databinding.ActivityMainBinding
import com.pluang.searchapp.ui.adapter.DividerGridItemDecoration
import com.pluang.searchapp.ui.adapter.ImageViewAdapter
import com.pluang.searchapp.ui.contact.OnPressListener
import com.pluang.searchapp.ui.viewModel.SearchDataViewModel
import com.pluang.stockapp.network.NetworkState


class MainActivity : AppCompatActivity(), OnPressListener {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var adapter: ImageViewAdapter
    private var viewModel: SearchDataViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SearchDataViewModel::class.java)



        viewModel!!.updateStatus.observe(this) { status: Boolean ->
            binding.progressBar = status
        }


        binding.etSearchKey.apply {
            afterTextChanged {

                searchKey(binding.etSearchKey.text.toString())

            }


        }

    }


    private fun searchKey(searchKey: String) {

        if (NetworkState.isNetworkAvailable(this)) {
            viewModel?.searchImages(BuildConfig.APP_CLIENT_ID, searchKey)?.observe(this, Observer {
                val dataResponse = it ?: return@Observer
                getImages(dataResponse.results)

            })
        } else {
            Toast.makeText(
                this,
                getString(R.string.internet_check_text),
                Toast.LENGTH_LONG
            ).show()
        }


    }

    private fun getImages(mDataList: List<ResultsItem?>?) {

        if (!mDataList.isNullOrEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.ivNotFound.visibility = View.GONE
            adapter = ImageViewAdapter(mDataList as List<ResultsItem>, this)
            binding.recyclerView.addItemDecoration(DividerGridItemDecoration(this))

            binding.recyclerView.setAdapter(adapter)
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.ivNotFound.visibility = View.VISIBLE
        }
    }

    override fun onImagePressed(url: String?) {
        ImageFullViewActivity.start(this, url)
    }


    override fun onBackPressed() {
        exitApp()

    }


    private fun exitApp() {
        if (doubleBackToExitPressedOnce) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            finishAffinity()
            finish()
            System.exit(0)
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_LONG)
            .show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 5000)
    }

    fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable?.length!! > 3) {
                    afterTextChanged.invoke(editable.toString())

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}


