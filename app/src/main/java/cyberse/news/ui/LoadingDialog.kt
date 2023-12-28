package cyberse.news.ui

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ProgressBar
import cyberse.news.R

class LoadingDialog(context: Context) {

    private var dialog: Dialog = Dialog(context, R.style.Theme_CyberseNews_Loading_Dialog)

    fun show() {
        if (!dialog.isShowing) dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) dialog.dismiss()
    }

    init {
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true

        with(dialog) {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(progressBar)
            setCancelable(false)
        }
    }

}