package com.pluang.searchapp.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.pluang.searchapp.BuildConfig
import com.pluang.searchapp.R
import com.pluang.searchapp.data.model.ResultsItem
import com.pluang.searchapp.data.prefs_manager.AppSearchPreference
import com.pluang.searchapp.databinding.ActivityMainBinding
import com.pluang.searchapp.ui.adapter.DividerGridItemDecoration
import com.pluang.searchapp.ui.adapter.ImageViewAdapter
import com.pluang.searchapp.ui.adapter.SearchedKeywordAdapter
import com.pluang.searchapp.ui.contact.OnPressListener
import com.pluang.searchapp.ui.contact.OnSearchListener
import com.pluang.searchapp.ui.viewModel.SearchDataViewModel
import com.pluang.stockapp.network.NetworkState


class MainActivity : AppCompatActivity(), OnPressListener, OnSearchListener {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var adapter: ImageViewAdapter
    private lateinit var searchKeyAdapter: SearchedKeywordAdapter
    private var viewModel: SearchDataViewModel? = null
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var searchPreference: AppSearchPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SearchDataViewModel::class.java)
        searchPreference = AppSearchPreference(this)

        layoutManager = GridLayoutManager(this, 2);

        viewModel!!.updateStatus.observe(this) { status: Boolean ->
            binding.progressBar = status
        }

        binding.etSearchKey.apply {
            afterTextChanged {
                // searchKey(binding.etSearchKey.text.toString())
                showSearchKeyList()
            }

        }


        binding.etSearchKey.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                findKeyImages(binding.etSearchKey.text.toString())
                v.hideKeyboard()
            }

            true

        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun findKeyImages(searchKey: String) {
        binding.searchKeyList.visibility = View.GONE
        binding.etSearchKey.hideKeyboard()

        if (searchKey.length <= 3) {
            return
        }

        if (NetworkState.isNetworkAvailable(this)) {
            viewModel?.searchImages(BuildConfig.APP_CLIENT_ID, searchKey)?.observe(this, Observer {
                val dataResponse = it ?: return@Observer
                getImages(dataResponse.results)
                val array = mutableListOf<String>()
                searchPreference.searchHistory?.forEach {
                    array.add(it)
                }
                array.add(searchKey)
                searchPreference.searchHistory = array.toSet()
            })
        } else {
            Toast.makeText(
                this,
                getString(R.string.internet_check_text),
                Toast.LENGTH_LONG
            ).show()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val items = item.itemId
        when (items) {
            R.id.action_2 -> {
                setLayoutGridColumn(2)
            }
            R.id.action_3 -> {
                setLayoutGridColumn(3)
            }
            R.id.action_4 -> {
                setLayoutGridColumn(4)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getImages(mDataList: List<ResultsItem?>?) {

        if (!mDataList.isNullOrEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.ivNotFound.visibility = View.GONE
            adapter = ImageViewAdapter(mDataList as List<ResultsItem>, this)
            binding.recyclerView.addItemDecoration(DividerGridItemDecoration(this))
            setLayoutGridColumn(2)
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.ivNotFound.visibility = View.VISIBLE
        }
    }


    fun setLayoutGridColumn(count: Int) {
        layoutManager = GridLayoutManager(this, count)
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter)
    }


    override fun onImagePressed(url: String?) {
        ImageFullViewActivity.start(this, url)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.view_menu, menu)
        return true
    }

    private fun showSearchKeyList() {
        val searchData = ArrayList<String?>()
        searchPreference.searchHistory?.let { list ->
            for (s in list)
                searchData.add(s)
        }
        if (!searchData.isNullOrEmpty()) {
            searchKeyAdapter = SearchedKeywordAdapter(
                this,
                searchData,
                this
            )
            binding.searchKeyList.visibility = View.VISIBLE
            binding.searchKeyList.setAdapter(searchKeyAdapter)
        }

    }


    override fun onSearchKeywordPress(searchKey: String?) {
        // do something
        findKeyImages(searchKey.toString())

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
                if (editable?.length!! >= 3) {
                    afterTextChanged.invoke(editable.toString())

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}


