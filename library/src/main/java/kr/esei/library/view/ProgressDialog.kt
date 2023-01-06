package kr.esei.library.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import kr.esei.library.databinding.EsboilerDialogProgressBinding

public class ProgressDialog(private val statusBarColor: Int, private val isLightStatusBar: Boolean, private val progressTint: Int?): DialogFragment() {

    private companion object {
        private const val TAG = "ProgressDialog"
    }

    private var _binding: EsboilerDialogProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EsboilerDialogProgressBinding.inflate(inflater, container, false)
        isCancelable = false

        progressTint?.let {
            binding.progressCircular.indeterminateTintList = resources.getColorStateList(it, null)
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = this@ProgressDialog.statusBarColor
            setBackgroundDrawableResource(android.R.color.transparent)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            val controllerCompat = WindowInsetsControllerCompat(this, decorView)
            controllerCompat.isAppearanceLightStatusBars = isLightStatusBar
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}