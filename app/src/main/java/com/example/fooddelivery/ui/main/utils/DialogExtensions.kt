package com.example.fooddelivery.ui.main.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.fooddelivery.R



inline fun Activity.alert(
    title: CharSequence? = null,
    message: CharSequence? = null,
    color: Int = R.color.status_red,
    func: AlertDialogHelper.() -> Unit
): AlertDialog {
    return AlertDialogHelper(this, title, message, color).apply {
        func()
    }.create()
}

inline fun Activity.alert(
    titleResource: Int = 0,
    messageResource: Int = 0,
    func: AlertDialogHelper.() -> Unit
): AlertDialog {
    val title = if (titleResource == 0) null else getString(titleResource)
    val message = if (messageResource == 0) null else getString(messageResource)
    return AlertDialogHelper(this, title, message).apply {
        func()
    }.create()
}

inline fun Fragment.alert(
    title: CharSequence? = null,
    message: CharSequence? = null,
    func: AlertDialogHelper.() -> Unit
): AlertDialog {
    return AlertDialogHelper(context!!, title, message).apply {
        func()
    }.create()
}

inline fun Fragment.alert(
    titleResource: Int = 0,
    messageResource: Int = 0,
    func: AlertDialogHelper.() -> Unit
): AlertDialog {
    val title = if (titleResource == 0) null else getString(titleResource)
    val message = if (messageResource == 0) null else getString(messageResource)
    return AlertDialogHelper(context!!, title, message).apply {
        func()
    }.create()
}

@SuppressLint("InflateParams")
class AlertDialogHelper(
    context: Context,
    title: CharSequence?,
    message: CharSequence?,
    color: Int = R.color.status_red
) {

    private val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.dialog_info, null)
    }

    private val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        .setView(dialogView)

    private val title: TextView by lazy {
        dialogView.findViewById(R.id.dialogInfoTitleTextView)
    }

    private val message: TextView by lazy {
        dialogView.findViewById(R.id.dialogInfoMessageTextView)
    }

    private val positiveButton: Button by lazy {
        dialogView.findViewById(R.id.dialogInfoPositiveButton)
    }

    private val negativeButton: Button by lazy {
        dialogView.findViewById(R.id.dialogInfoNegativeButton)
    }

    private var dialog: AlertDialog? = null

    var cancelable: Boolean = false

    init {
        this.title.text = title
        this.title.setTextColor(context.resources.getColor(color, null))
        this.message.text = message
    }

    fun positiveButton(@StringRes textResource: Int, func: (() -> Unit)? = null) {
        with(positiveButton) {
            text = builder.context.getString(textResource)
            setClickListenerToDialogButton(func)
        }
    }

    fun positiveButton(text: CharSequence, func: (() -> Unit)? = null) {
        with(positiveButton) {
            this.text = text
            setClickListenerToDialogButton(func)
        }
    }

    fun negativeButton(@StringRes textResource: Int, func: (() -> Unit)? = null) {
        with(negativeButton) {
            text = builder.context.getString(textResource)
            setClickListenerToDialogButton(func)
        }
    }

    fun negativeButton(text: CharSequence, func: (() -> Unit)? = null) {
        with(negativeButton) {
            this.text = text
            setClickListenerToDialogButton(func)
        }
    }

    fun onCancel(func: () -> Unit) {
        builder.setOnCancelListener { func() }
    }

    fun create(): AlertDialog {
        title.goneIfTextEmpty()
        message.goneIfTextEmpty()
        positiveButton.goneIfTextEmpty()
        negativeButton.goneIfTextEmpty()

        dialog = builder
            .setCancelable(cancelable)
            .create()
        return dialog!!
    }

    private fun TextView.goneIfTextEmpty() {
        visibility = if (text.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun Button.setClickListenerToDialogButton(func: (() -> Unit)?) {
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }
    }

}