package com.kavirelectronic.ali.kavir_info.utility

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Gravity
import com.kavirelectronic.ali.kavir_info.BuildConfig
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.models.SecretCodeModel
import com.kavirelectronic.ali.kavir_info.server.LoginServer
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance.retrofitInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem.getItem
import com.kavirelectronic.ali.kavir_info.utility.SaveItem.setItem
import com.tapadoo.alerter.Alerter
import com.valdesekamdem.library.mdtoast.MDToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.NetworkInterface
import java.util.*

object Utility {
    fun getUniqueIMEIId(context: Context): String {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ""
            }
            var imei: String? = null
            imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                telephonyManager.deviceId
            }
            Log.e("imei", "=$imei")
            return if (imei != null && !imei.isEmpty()) {
                imei
            } else {
                Build.SERIAL
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "not_found"
    }

    val macAddr: String
        get() {
            try {
                val all: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                    val macBytes = nif.hardwareAddress ?: return ""
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString().trim { it <= ' ' }.replace(":", "")
                }
            } catch (ex: Exception) {
            }
            return "02:00:00:00:00:00"
        }

    fun calSCode(context: Context?, numberPhone: Array<String>): String {
        return try {
            val n10 = numberPhone[numberPhone.size - 1].toInt()
            val n9 = numberPhone[numberPhone.size - 2].toInt()
            val sCode = getItem(context, SaveItem.raw_Scode, "")
            Log.e("sCode:", sCode)
            if (sCode.length > 0) {
                val index = n9 + n10
                return sCode.substring(index, index + 10)
            }
            ""
        } catch (e: Exception) {
            ""
        }
    }

    fun getNewVersion(context: Activity) {
        val retrofit = retrofitInstance
        val loginServer = retrofit!!.create(LoginServer::class.java)
        loginServer.getSecretCode(BuildConfig.VERSION_CODE.toString())!!.enqueue(object : Callback<SecretCodeModel?> {
            override fun onFailure(call: Call<SecretCodeModel?>, t: Throwable) {
                Log.e("fail get new version",t.message)
                MDToast.makeText(context,context.getString(R.string.error_in_connection),MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show()
            }

            override fun onResponse(call: Call<SecretCodeModel?>, response: Response<SecretCodeModel?>) {
                if (response.body()!!.result.equals("success", ignoreCase = true)) {
                    checkNewVersion(context, response.body())
                } else {
                    MDToast.makeText(context, response.body()!!.message, 2500, MDToast.TYPE_INFO).show()
                }
            }

        })
    }

    fun getSecretCode(context: Activity?) {
        val retrofit = retrofitInstance
        val loginServer = retrofit!!.create(LoginServer::class.java)
        loginServer.getSecretCode(BuildConfig.VERSION_CODE.toString())!!.enqueue(object : Callback<SecretCodeModel?> {
            override fun onFailure(call: Call<SecretCodeModel?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<SecretCodeModel?>, response: Response<SecretCodeModel?>) {
                if (response.body()!!.result.equals("success", ignoreCase = true)) {
                    val s_raw = response.body()!!.secretCode!!.trim { it <= ' ' }
                    setItem(context, SaveItem.raw_Scode, s_raw.substring(0, 30))
                } else {
                    MDToast.makeText(context, response.body()!!.message, 2500, MDToast.TYPE_INFO).show()
                }
            }

        })
    }

    private fun checkNewVersion(context: Activity, body: SecretCodeModel?) {
        if (body!!.versionCode == null) {
            return
        }
        if (body.versionCode!!.toInt() > BuildConfig.VERSION_CODE) {
            setItem(context, SaveItem.COME_NEW_VERSION, "1")
            setItem(context, SaveItem.NEW_VERSION_NAME, body.versionName)
            setItem(context, SaveItem.NEW_VERSION_URL, body.updateUrl)
        } else {
            setItem(context, SaveItem.COME_NEW_VERSION, "0")
        }
    }

    fun showAlertNewVersion(context: Activity, versionName: String, updateUrl: String?) {
        Alerter.create(context)
                .setTitle(context.getString(R.string.new_version))
                .setText(context.getString(R.string.version) + " " + versionName + " " + context.getString(R.string.available))
                .setDuration(10000)
                .setContentGravity(Gravity.CENTER)
                .setTitleTypeface(Typeface.createFromAsset(context.assets, "fonts/Vazir.ttf"))
                .setTextTypeface(Typeface.createFromAsset(context.assets, "fonts/sans.ttf"))
                .setIcon(R.drawable.ic_notifications_black_24dp)
                .setOnClickListener {
                    val uri = Uri.parse(updateUrl)
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    try {
                        context.startActivity(goToMarket)
                    } catch (e: ActivityNotFoundException) {
                        context.startActivity(Intent(Intent.ACTION_VIEW,
                                Uri.parse(updateUrl)))
                    }
                }
                .show()
    }

    fun calMID(numberPhone: Array<String>): String {
        return try {
            val n10 = numberPhone[numberPhone.size - 1].toInt()
            val n9 = numberPhone[numberPhone.size - 2].toInt()
            val mac = macAddr.split("").toTypedArray()
            return n9.plus(mac[n9+1].toInt()).plus(n10).plus(mac[n10+1].toInt()).toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun calApkId(context: Context?, numberPhone: Array<String>): String {
        return try {
            val mid = calMID(numberPhone)
            val uid = getItem(context, SaveItem.USER_ID, "")
            val len = uid.length
            setItem(context, SaveItem.APK_ID, mid + len + uid)
            mid + len + uid
        } catch (e: Exception) {
            ""
        }
    }
}