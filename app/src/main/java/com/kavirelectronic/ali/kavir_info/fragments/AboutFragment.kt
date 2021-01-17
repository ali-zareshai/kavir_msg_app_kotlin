package com.kavirelectronic.ali.kavir_info.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.kavirelectronic.ali.kavir_info.R

class AboutFragment : Fragment() {
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        webView = view.findViewById<View>(R.id.webview_about) as WebView
        webView!!.loadUrl("https://www.kavir.info/fa/about-apk/")
        return view
    }

    companion object {
        private var fragment: Fragment? = null
        fun newInstance(): Fragment? {
            if (fragment == null) {
                fragment = AboutFragment()
            }
            return fragment
        }
    }
}