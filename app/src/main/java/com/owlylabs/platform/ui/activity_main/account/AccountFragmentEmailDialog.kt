package com.owlylabs.platform.ui.account

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.owlylabs.platform.R
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.constants.AppLogicConstants
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class AccountFragmentEmailDialog : DaggerDialogFragment() {

    @Inject
    lateinit var localRepository: AbstractLocalRepository

    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.fragment_account_dialog_email, null)
        val textInputEditText: TextInputEditText = dialogView.findViewById(R.id.editText)
        textInputEditText.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            textInputEditText.post(Runnable {
                val inputMethodManager: InputMethodManager =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(
                    textInputEditText,
                    InputMethodManager.SHOW_IMPLICIT
                )
            })
        })
        textInputEditText.requestFocus()
        val builder = AlertDialog.Builder(mContext)
            .setTitle(R.string.enter_your_email)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                localRepository.saveAccountEmail(textInputEditText.text.toString())
                findNavController().previousBackStackEntry?.savedStateHandle?.set(AppLogicConstants.TAG_IS_EMAIL_DIALOG_SUCCESS, true)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set(AppLogicConstants.TAG_IS_EMAIL_DIALOG_SUCCESS, false)
            }
            .setView(dialogView)
        val dialog = builder.create()
        //dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog
    }
}