package cyberse.news.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cyberse.news.bases.BaseFragment
import cyberse.news.databinding.FragmentWebViewBinding
import cyberse.news.extensions.observeData
import cyberse.news.extensions.visibleOrGone
import cyberse.news.ui.main.MainActionClick
import cyberse.news.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(FragmentWebViewBinding::inflate) {

    private val args by navArgs<WebViewFragmentArgs>()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun setUpViews() {
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.viewModel = mainViewModel

        binding?.webView?.apply {
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = false
            settings.displayZoomControls = false
            settings.defaultTextEncodingName = "utf-8"
            loadUrl(args.url)
            webChromeClient = MyChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding?.progressBar?.visibleOrGone(true)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding?.tvTitle?.text = view.title
                    binding?.progressBar?.visibleOrGone(false)
                    try {
                        view.loadUrl(
                            "javascript:(window.onload = function() { " +
                                    "document.getElementsByTagName('footer')[0].parentNode." +
                                    "removeChild(document.getElementsByTagName('footer')[0]);" +
                                    "document.getElementsByClassName('buttonizer-group-0-0-1')[0].parentNode." +
                                    "removeChild(document.getElementsByClassName('buttonizer-group-0-0-1')[0]);" +
                                    "document.getElementsByClassName('infinite-mobile-header-wrap')[0].parentNode.removeChild(" +
                                    "document.getElementsByClassName('infinite-mobile-header-wrap')[0]);" +
                                    "document.addEventListener('scroll', function(e) {" +
                                    "document.getElementById('infinite-mobile-header').style.display = 'none'" +
                                    "});" +
                                    "})()"
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private open inner class MyChromeClient : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0

        override fun onProgressChanged(view: WebView, progress: Int) {
            super.onProgressChanged(view, progress)
            binding?.loadingProgress = progress + 10
            if (progress >= 100) binding?.tvTitle?.text = view.title
        }

        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else {
                BitmapFactory.decodeResource(context?.resources, 2130837573)
            }
        }

        override fun onHideCustomView() {
            (activity?.window?.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            activity?.requestedOrientation = mOriginalOrientation
            mCustomViewCallback?.onCustomViewHidden()
            mCustomViewCallback = null
            binding?.appbar?.visibleOrGone(true)
        }

        override fun onShowCustomView(paramView: View, paramCustomViewCallback: CustomViewCallback) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }

            mCustomView = paramView
            mCustomView?.setBackgroundColor(Color.BLACK)
            mOriginalOrientation = requireActivity().requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (activity?.window?.decorView as FrameLayout).addView(
                mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )

            binding?.appbar?.visibleOrGone(false)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.webView?.onPause()
    }

    override fun onObserveData() {
        observeData(mainViewModel.actionClick, ::handleActionClick)
    }

    private fun handleActionClick(action: MainActionClick) {
        if (action == MainActionClick.GoBack) dismiss()
    }

}