package com.kavirelectronic.ali.kavir_info.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.kavirelectronic.ali.kavir_info.R

class HelpFragment : Fragment() {
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle): View {
        val view = inflater.inflate(R.layout.fragment_help, container, false)
        webView = view.findViewById<View>(R.id.webview_help) as WebView
        //        webView.getSettings().setJavaScriptEnabled(true);
        webView!!.loadUrl("https://www.kavir.info/fa/help-apk/")
        return view
    }

    companion object {
        private var fragment: Fragment? = null
        fun newInstance(): Fragment? {
            if (fragment == null) {
                fragment = HelpFragment()
            }
            return fragment
        }
    }
}