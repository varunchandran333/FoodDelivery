package com.example.fooddelivery

import android.os.Bundle
import android.view.View
import com.example.fooddelivery.ui.main.ui.main.MainFragment
import com.example.fooddelivery.ui.main.ui.main.MainViewModel
import com.example.fooddelivery.ui.main.base.BaseActivity
import com.example.fooddelivery.ui.main.utils.finishAndExit
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        viewModel.insertData()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun viewReady() {

    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount == 1 -> {
                finishAndExit()
            }
            supportFragmentManager.backStackEntryCount > 1 -> {
                supportFragmentManager.popBackStack()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
    fun dismissProgressBar() {
        progressBarLayout?.visibility = View.GONE
    }

    fun showProgressBar() {
        progressBarLayout?.visibility = View.VISIBLE
    }
}