package com.mindbuffer.foodfork.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.fragment.app.Fragment
import com.mindbuffer.foodfork.utils.AppConstants.STATUS_CODE_TOKEN_EXPIRED
import com.google.android.material.snackbar.Snackbar
import com.mindbuffer.foodfork.R


fun View.snackBar(message: String, action: (() -> Unit)? = null) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snack.setAction("Retry"){
            it()
        }
    }
    snack.show()
}

fun Fragment.handleApiError(
    failure: NetworkResult.Failure,
    retry: (() -> Unit)? = null
) {
    when{
        failure.isNetworkError -> requireView().snackBar("Please check your internet connection", retry)
        failure.errorCode == STATUS_CODE_TOKEN_EXPIRED -> {
            //perform logout expression
        }
        else -> {
            failure.errorMsg?.let { requireView().snackBar(it) }
        }
    }
}

private var dialog: Dialog? = null
fun Fragment.showLoader() {
    try {
        if (dialog != null) dialog?.dismiss()
        dialog = Dialog(requireContext())
        dialog?.setContentView(R.layout.loading_dialog)
        dialog?.dismiss()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    } catch (E: java.lang.Exception) {
    }
}


fun dismissLoader() {
    try {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    } catch (E: Exception) {
        println(E.message)
    }
}