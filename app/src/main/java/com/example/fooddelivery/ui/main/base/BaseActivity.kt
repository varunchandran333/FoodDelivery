package com.example.fooddelivery.ui.main.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.fooddelivery.ui.main.utils.*
import org.koin.core.logger.KOIN_TAG

abstract class BaseActivity : AppCompatActivity(), LifecycleObserver {
  var isForeground = false
  fun showError(@StringRes errorMessage: Int, rootView: View) = snackbar(errorMessage, rootView)

  fun showError(errorMessage: String?, rootView: View) = snackbar(errorMessage ?: "", rootView)


  fun addFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean = false) {
    showFragment(fragment, containerId, addToBackStack)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    viewReady()
  }


  abstract fun viewReady()
  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount <= 1) finish() else goBack()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onBackGround() {
    isForeground = false
    Log.d(KOIN_TAG, "App in BackGround")
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onForeground() {
    isForeground = true
    Log.d(KOIN_TAG, "App in Foreground")
  }
}