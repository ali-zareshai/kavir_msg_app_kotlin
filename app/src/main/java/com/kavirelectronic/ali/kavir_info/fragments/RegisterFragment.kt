package com.kavirelectronic.ali.kavir_info.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.LoginActivity
import com.kavirelectronic.ali.kavir_info.models.RegisterModel
import com.kavirelectronic.ali.kavir_info.server.LoginServer
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Utility
import com.valdesekamdem.library.mdtoast.MDToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterFragment : Fragment(), View.OnClickListener {
    private var nameEd: EditText? = null
    private var emailEd: EditText? = null
    private var phoneEd: EditText? = null
    private var passEd: EditText? = null
    private var registerBtn: TextView? = null
    private var loadingProgressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Utility.getSecretCode(activity)
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        nameEd = view.findViewById<View>(R.id.nameuser) as EditText
        emailEd = view.findViewById<View>(R.id.emailuser) as EditText
        phoneEd = view.findViewById<View>(R.id.mobileuser) as EditText
        passEd = view.findViewById<View>(R.id.pswrdd) as EditText
        registerBtn = view.findViewById<View>(R.id.registerBtn) as TextView
        loadingProgressBar = view.findViewById<View>(R.id.progress_register) as ProgressBar
        registerBtn!!.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.registerBtn -> sendRegisterDate()
        }
    }

    private fun sendRegisterDate() {
        if (checkData()) {
            return
        }
        sendData(nameEd!!.text.toString(), emailEd!!.text.toString(), phoneEd!!.text.toString(), passEd!!.text.toString())
    }

    private fun sendData(name: String, email: String, phone: String, password: String) {
        loadingProgressBar!!.visibility = View.VISIBLE
        Log.e("s:", Utility.calSCode(activity.applicationContext, phone.split("").toTypedArray()))
        Log.e("phone:", phone)
        val retrofit = RetrofitClientInstance.retrofitInstance
        val loginServer = retrofit!!.create(LoginServer::class.java)
        loginServer!!.registerUser(name, phone, email, password, Utility.calMID(phone.split("").toTypedArray()), Utility.calSCode(activity.applicationContext, phone.split("").toTypedArray()))!!.enqueue(object : Callback<RegisterModel?> {
            override fun onResponse(call: Call<RegisterModel?>, response: Response<RegisterModel?>) {
                if (response.body()!!.status.equals("success", ignoreCase = true)) {
                    SaveItem.setItem(activity.applicationContext, SaveItem.S_CODE, response.body()!!.scode)
                    SaveItem.setItem(activity.applicationContext, SaveItem.MID_CODE, response.body()!!.mid)
                    SaveItem.setItem(activity.applicationContext, SaveItem.USER_ID, response.body()!!.userId)
                    SaveItem.setItem(activity.applicationContext, SaveItem.REGISTER_PHONE, phone)
                    MDToast.makeText(activity.applicationContext, activity.applicationContext.getString(R.string.register_success), 2500, MDToast.TYPE_SUCCESS).show()
                    emptyInputs()
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    MDToast.makeText(activity.applicationContext, response.body()!!.message, 2500, MDToast.TYPE_ERROR).show()
                }
                loadingProgressBar!!.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<RegisterModel?>, t: Throwable) {
                MDToast.makeText(activity.applicationContext, activity.applicationContext.getString(R.string.error_in_connection), 2500, MDToast.TYPE_ERROR).show()
                loadingProgressBar!!.visibility = View.INVISIBLE
                Log.e("onFailure:", t.toString())
            }
        })
    }

    private fun emptyInputs() {
        nameEd!!.setText("")
        emailEd!!.setText("")
        phoneEd!!.setText("")
        passEd!!.setText("")
    }

    private fun checkData(): Boolean {
        if (nameEd!!.text.toString().isEmpty() || emailEd!!.text.toString().isEmpty() || phoneEd!!.text.toString().isEmpty() || passEd!!.text.toString().isEmpty() || phoneEd!!.text.toString().length < 10) {
            MDToast.makeText(activity.applicationContext, activity.getString(R.string.full_all_fields), 2500, MDToast.TYPE_WARNING).show()
            return true
        }
        if (!isEmailValid(emailEd!!.text.toString())) {
            MDToast.makeText(activity.applicationContext, activity.getString(R.string.email_not_valid), 2500, MDToast.TYPE_WARNING).show()
            return true
        }
        return false
    }

    fun isEmailValid(email: String): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
        val inputStr: CharSequence = email
        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(inputStr)
        return if (matcher.matches()) true else false
    }

    companion object {
        private var fragment: Fragment? = null
        fun newInstance(): Fragment? {
            if (fragment == null) {
                fragment = RegisterFragment()
            }
            return fragment
        }
    }
}