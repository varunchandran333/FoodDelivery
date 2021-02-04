package com.example.fooddelivery.ui.main.utils

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.fooddelivery.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt


inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline onDataReceived: (T) -> Unit) =
    observe(owner, { onDataReceived(it) })

fun snackbar(@StringRes message: Int, rootView: View) = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()

fun snackbar(message: String, rootView: View) = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()

fun View.visible() {
    visibility = View.VISIBLE
}

fun Activity.finishAndExit() {
    setResult(RESULT_OK)
    finish()
    overridePendingTransition(R.anim.right_left_in, R.anim.right_left_out)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

inline fun View.onClick(crossinline onClick: () -> Unit) {
    setOnClickListener { onClick() }
}

fun FragmentActivity.showFragment(fragment: Fragment, @IdRes container: Int, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().apply {
        if (addToBackStack) {
            addToBackStack(fragment.tag)
        }
    }
        .replace(container, fragment)
        .commitAllowingStateLoss()
}

fun FragmentActivity.goBack() {
    supportFragmentManager.popBackStack()
}

inline fun ViewModel.launch(
    crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Main) { block() }
}
/**
 * To add a delay.
 */
fun withDelay(delay: Long, block: () -> Unit) {
    Handler().postDelayed(Runnable(block), delay)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> Activity.openActivityForResult(
        it: Class<T>,
        resultCode: Int,
        extras: Bundle.() -> Unit = {}
) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivityForResult(intent, resultCode)
}

/**
 * To show snack bar.
 */
fun View.showSnackbar(
        message: String,
        duration: Int = Snackbar.LENGTH_LONG,
        isActionEnabled: Boolean = false
) {

    val snackBar = Snackbar.make(this, message, duration)
    if (isActionEnabled) {
        snackBar.action("OK", listener = {
            snackBar.dismiss()
        })
    }
    snackBar.show()
}

/**
 * Adds action to the Snackbar
 *
 * @param actionRes Action text to be shown inside the Snackbar
 * @param color Color of the action text
 * @param listener Onclick listener for the action
 */

fun Snackbar.action(actionRes: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(actionRes, listener)
    color?.let { setActionTextColor(color) }
}

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    runOnUiThread { Toast.makeText(this, message, duration).show() }
}

fun Activity.showCenterToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    runOnUiThread {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

fun Fragment.showCenterToast(toast: Toast, message: String, duration: Int = Toast.LENGTH_LONG) {
    requireActivity().runOnUiThread {
        toast.setText(message)
        toast.duration = duration
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

fun Activity.makeFullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    // Hide Status Bar
    val decorView = window.decorView
    // Hide Status Bar.
    val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
    decorView.systemUiVisibility = uiOptions

}


/**
 * For response logging purpose only.
 * So in case of issues one can refer the logs.
 */
fun logLargeString(tag: String, str: String) {
    if (str.length > 3000) {
        Log.i(tag, str.substring(0, 3000))
        logLargeString(tag, str.substring(3000))
    } else {
        Log.i(tag, "remote stream stats$str")
    }
}

/**
 * When trying to draw a larger bitmap into canvas there is a high chance of failure
 * It is better to compress the bitmap before feeding into a drawing function
 */
fun getCompressedBitmap(filePath: String): Bitmap? {
    val IMAGE_MAX_SIZE = 1200000 // 1.2MP

    // Decode image size
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filePath, options)
    var scale = 1.0
    while ((options.outWidth * options.outHeight) * (1 / scale.pow(2.0)) > IMAGE_MAX_SIZE) {
        scale++
    }
    Log.d(
            "BITMAP",
            "scale = " + scale + ", orig-width: " + options.outWidth + ", orig -height: " + options.outHeight
    )

    var resultBitmap: Bitmap?
    if (scale > 1) {
        scale--
        // scale to max possible inSampleSize that still yields an image
        // larger than target
        options = BitmapFactory.Options()
        options.inSampleSize = scale.toInt()
        resultBitmap = BitmapFactory.decodeFile(filePath, options)

        // resize to desired dimensions
        val height: Float = resultBitmap.height.toFloat()
        val width: Float = resultBitmap.width.toFloat()

        val y = sqrt((IMAGE_MAX_SIZE / ((width) / height)).toDouble())
        val x = (y / height) * width

        val scaledBitmap = Bitmap.createScaledBitmap(
                resultBitmap,
                x.toInt(), y.toInt(), true
        )
        resultBitmap.recycle()
        resultBitmap = scaledBitmap
        System.gc()
    } else {
        resultBitmap = BitmapFactory.decodeFile(filePath)
    }
    return resultBitmap
}



