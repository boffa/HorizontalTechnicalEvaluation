package com.horizontal.test.core

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.horizontal.test.databinding.CustomDialogBinding

fun showCustomDialog(context: Context, customText: String? = null, resource: Int? = null) {
    val dialogBinding: CustomDialogBinding =
        CustomDialogBinding.inflate(
            LayoutInflater.from(context)
        )
    val customDialog = AlertDialog.Builder(context, 0).create()
    customDialog.apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setView(dialogBinding.root)
        setCancelable(false)
    }.show()
    if (resource != null) dialogBinding.image.setImageResource(resource)

    if (customText != null) dialogBinding.text.text = customText

    dialogBinding.btnOk.setOnClickListener {
        customDialog.dismiss()
    }
}