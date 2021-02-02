package com.kavirelectronic.ali.kavir_info.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.LoginModel
import com.kavirelectronic.ali.kavir_info.server.LoginServer
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Utility
import com.valdesekamdem.library.mdtoast.MDToast
import net.igenius.customcheckbox.CustomCheckBox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var loginBtn: TextView? = null
    private var exitBtn: TextView? = null
    private var qrcodeImg: ImageView? = null
    private var userNameEdit: EditText? = null
    private var passwordEdit: EditText? = null
    private var saveLoginCk: CustomCheckBox? = null
    private var progressBarLogin: ProgressBar? = null
    private var togglePassBtn: ImageButton? = null
    private var isShowPass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Utility.getSecretCode(this)
        loginBtn = findViewById<View>(R.id.loginBtn) as TextView
        qrcodeImg = findViewById<View>(R.id.qrcode) as ImageView
        userNameEdit = findViewById<View>(R.id.usernameEd) as EditText
        passwordEdit = findViewById<View>(R.id.passwordEd) as EditText
        saveLoginCk = findViewById<View>(R.id.save_pass_ckb) as CustomCheckBox
        progressBarLogin = findViewById<View>(R.id.progress_login) as ProgressBar
        exitBtn = findViewById<View>(R.id.exist_login) as TextView
        togglePassBtn = findViewById<View>(R.id.toggle_pass) as ImageButton
        loginBtn!!.setOnClickListener(this)
        qrcodeImg!!.setOnClickListener(this)
        exitBtn!!.setOnClickListener(this)
        togglePassBtn!!.setOnClickListener(this)
        //        Toast.makeText(this, Utility.getUniqueIMEIId(this), Toast.LENGTH_LONG).show();
        /////////////// set save user pass
        userNameEdit!!.setText(SaveItem.getItem(this, SaveItem.USERNAME_LOGIN, ""))
        passwordEdit!!.setText(SaveItem.getItem(this, SaveItem.PASSWORD_LOGIN, ""))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.qrcode -> {
                val i = Intent(this@LoginActivity, QrCodeScanerActivity::class.java)
                startActivity(i)
            }
            R.id.loginBtn -> loginProcess()
            R.id.exist_login -> finish()
            R.id.toggle_pass -> togglePass()
        }
    }

    private fun togglePass() {
        if (isShowPass) {
            passwordEdit!!.transformationMethod = PasswordTransformationMethod()
            isShowPass = false
            togglePassBtn!!.setBackgroundResource(R.drawable.show_eye)
        } else {
            passwordEdit!!.transformationMethod = null
            isShowPass = true
            togglePassBtn!!.setBackgroundResource(R.drawable.hide_eye)
        }
    }

    private fun saveUsername(userName: String, password: String) {
        if (saveLoginCk!!.isChecked) {
            SaveItem.setItem(this, SaveItem.USERNAME_LOGIN, userName)
            SaveItem.setItem(this, SaveItem.PASSWORD_LOGIN, password)
        }
    }

    private fun loginProcess() {
        if (!checkUserPass()) {
            MDToast.makeText(this, getString(R.string.full_all_fields), 2500, MDToast.TYPE_ERROR).show()
            return
        }
        val userName = FormatHelper.toEngNumber(userNameEdit!!.text.toString().trim { it <= ' ' })
        val password = FormatHelper.toEngNumber(passwordEdit!!.text.toString().trim { it <= ' ' })
        loginUser(userName, password)
    }

    private fun checkUserPass(): Boolean {
        return if (userNameEdit!!.text.toString().isEmpty() || passwordEdit!!.text.toString().isEmpty()) {
            false
        } else true
    }

    private fun loginUser(username: String, password: String) {
        progressBarLogin!!.visibility = View.VISIBLE
        var scode: String? = ""
        if (username.equals(SaveItem.getItem(this, SaveItem.REGISTER_PHONE, ""), ignoreCase = true)) {
            scode = Utility.calSCode(this, SaveItem.getItem(this, SaveItem.REGISTER_PHONE, "").split(""))
        } else {
            scode = Utility.calSCode(this, username.split(""))
            Log.e("phone=>>>>:", username)
        }
        Log.e("scode:", scode)
        val retrofit = RetrofitClientInstance.retrofitInstance
        val loginServer = retrofit!!.create(LoginServer::class.java)
        loginServer.loginUser(username, password, scode)?.enqueue(object : Callback<LoginModel?> {
            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                Log.e("onResponse login:",response.message())
                if (response.isSuccessful) {
                    if (response.body()!!.result.equals("success", ignoreCase = true)) {
                        saveLoginData(response.body())
                        saveUsername(username, password)
                        val categoryActivity = CategoryActivity.context as CategoryActivity?
                        categoryActivity!!.finish()
                        startActivity(Intent(this@LoginActivity, CategoryActivity::class.java))
                        MDToast.makeText(applicationContext, getString(R.string.welcome), 2500, MDToast.TYPE_SUCCESS).show()
                        finish()
                    } else {
                        MDToast.makeText(applicationContext, response.body()!!.message, 2500, MDToast.TYPE_WARNING).show()
                    }
                } else {
                    MDToast.makeText(applicationContext, getString(R.string.error_in_connection), 2500, MDToast.TYPE_WARNING).show()
                }
                progressBarLogin!!.visibility = View.GONE
            }

        })
    }

    private fun saveLoginData(body: LoginModel?) {
        SaveItem.setItem(this, SaveItem.USER_FIRST_NAME, body!!.firstName)
        SaveItem.setItem(this, SaveItem.USER_LAST_NAME, body.lastName)
        SaveItem.setItem(this, SaveItem.USER_EMAIL, body.email)
        SaveItem.setItem(this, SaveItem.USER_NAME, body.displayName)
        SaveItem.setItem(this, SaveItem.USER_MOBILE, body.mobile)
        SaveItem.setItem(this, SaveItem.USER_ID, body.userId)
        SaveItem.setItem(this, SaveItem.USER_COOKIE, body.cookie)
        SaveItem.setItem(this, SaveItem.MID_CODE, Utility.calMID(body.mobile!!.split("").toTypedArray()))
        SaveItem.setItem(this, SaveItem.S_CODE, Utility.calSCode(this, body!!.mobile!!.split("")))
        Utility.calApkId(this, body!!.mobile!!.split("").toTypedArray())
    }
}