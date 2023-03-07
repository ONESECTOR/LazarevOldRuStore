package com.owlylabs.platform.ui.account

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.owlylabs.platform.R
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject


class AccountFragmentNameDialog : DaggerDialogFragment() {

    @Inject
    lateinit var localRepository: AbstractLocalRepository

    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }*/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.fragment_account_dialog_name, null)
        val textInputEditText: TextInputEditText = dialogView.findViewById(R.id.editText)
        textInputEditText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            textInputEditText.post(Runnable {
                val inputMethodManager: InputMethodManager =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(textInputEditText, InputMethodManager.SHOW_IMPLICIT)
            })
        })
        textInputEditText.requestFocus()
        val builder = AlertDialog.Builder(mContext)
            .setTitle(R.string.enter_your_name)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                localRepository.saveAccountName(textInputEditText.text.toString())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setView(dialogView)
        val dialog = builder.create()
        //dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog
    }
}