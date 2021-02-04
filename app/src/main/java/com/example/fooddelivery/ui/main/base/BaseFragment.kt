package com.gadgeon.webcardio.operations.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.fooddelivery.MainActivity
import com.example.fooddelivery.ui.main.base.BaseActivity

abstract class BaseFragment : Fragment() {

  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(getLayout(), container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewReady()
  }
  
  protected fun onBackPressed() = (activity as BaseActivity).onBackPressed()
  
  abstract fun viewReady()
  
  abstract fun getLayout(): Int
  
  open fun showError(@StringRes errorMessage: Int, rootView: View) {
    (activity as BaseActivity).showError(errorMessage, rootView)
  }
  
  open fun showError(errorMessage: String?, rootView: View) {
    (activity as BaseActivity).showError(errorMessage, rootView)
  }

  fun dismissProgressBar() {
    val activity: Activity? = activity
    if (null != activity && activity is MainActivity) {
      val activity = getActivity() as MainActivity
      activity.dismissProgressBar()
    }
  }

  fun showProgressBar() {
    val activity: Activity? = activity
    if (null != activity && activity is MainActivity) {
      val activity = getActivity() as MainActivity
      activity.showProgressBar()
    }
  }

}