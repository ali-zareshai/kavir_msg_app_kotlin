package com.kavirelectronic.ali.kavir_info.fragments

import android.app.Fragment
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.QrCodeScanerActivity
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.valdesekamdem.library.mdtoast.MDToast

class ActiveFragment : Fragment() {
    private var activeCode: TextView? = null
    private var scanCodeBtn: Button? = null
    private var saveClipBoradBtn: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle): View {
        val view = inflater.inflate(R.layout.fragment_active, container, false)
        scanCodeBtn = view.findViewById<View>(R.id.scan_code_btn) as Button
        activeCode = view.findViewById<View>(R.id.active_code_tv) as TextView
        saveClipBoradBtn = view.findViewById<View>(R.id.save_clipbord) as ImageButton
        result_tv = view.findViewById<View>(R.id.result_tv) as TextView
        message_tv = view.findViewById<View>(R.id.message_tv) as TextView
        activeCode!!.text = FormatHelper.toEngNumber(SaveItem.getItem(activity, SaveItem.APK_ID, ""))
        saveClipBoradBtn!!.setOnClickListener {
            setClipboard(activity.applicationContext, SaveItem.getItem(activity, SaveItem.APK_ID, ""))
            MDToast.makeText(activity, activity.getString(R.string.copied), 2500, MDToast.TYPE_INFO).show()
        }
        scanCodeBtn!!.setOnClickListener { activity.startActivity(Intent(activity.applicationContext, QrCodeScanerActivity::class.java)) }
        return view
    }

    //    private String calActiveCode(){
    //        String mid =SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.MID_CODE,"");
    //        String userid = SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.USER_ID,"");
    //        if (!mid.equalsIgnoreCase("") && !userid.equalsIgnoreCase(""))
    //            return mid+userid;
    //        else {
    //            MDToast.makeText(getActivity(),getActivity().getString(R.string.login_please),2500,MDToast.TYPE_INFO).show();
    //            return "";
    //        }
    //
    //    }
    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
        } else {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.primaryClip = clip
        }
    }

    companion object {
        private var fragment: Fragment? = null
        var result_tv: TextView? = null
        var message_tv: TextView? = null
        fun newInstance(): Fragment? {
            if (fragment == null) {
                fragment = ActiveFragment()
            }
            return fragment
        }
    }
}