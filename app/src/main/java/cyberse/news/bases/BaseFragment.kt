package cyberse.news.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import cyberse.news.extensions.getErrorApiSnackbar
import cyberse.news.interfaces.OnLostConnection
import cyberse.news.ui.LoadingDialog

abstract class BaseFragment<B : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B
) : Fragment(), OnLostConnection {

    var binding: B? = null

    private var _loading: LoadingDialog? = null
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _loading = LoadingDialog(requireContext())
        setUpViews()
        onObserveData()
    }

    override fun onRetry(apiCode: String?) {
        onRetryRequest(apiCode)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
        _loading?.dismiss()
        snackbar?.dismiss()
    }

    open fun setUpViews() {}

    open fun onObserveData() {}

    open fun onRetryRequest(apiCode: String?) {}

    protected fun dismiss() {
        findNavController().popBackStack()
    }

    protected fun showLoading() {
        _loading?.show()
    }

    protected fun hideLoading() {
        _loading?.dismiss()
    }

    protected fun handleErrorApi(error: Pair<String?, Int>) {
        val container = activity?.findViewById<View>(android.R.id.content)

        snackbar = container?.getErrorApiSnackbar(error.second) { onRetry(error.first) }
        snackbar?.show()
    }

}