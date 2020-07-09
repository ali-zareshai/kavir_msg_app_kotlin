package com.kavirelectronic.ali.kavir_info.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.zxing.Result
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.fragments.ActiveFragment
import com.kavirelectronic.ali.kavir_info.models.ActiveRespone
import com.kavirelectronic.ali.kavir_info.server.LoginServer
import com.kavirelectronic.ali.kavir_info.utility.RetrofitClientInstance
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Utility
import dmax.dialog.SpotsDialog
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class QrCodeScanerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    private var dialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_qr_code_scaner);
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.please_wait)
                .build()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 5)
            }
        }
        Utility.getSecretCode(this)
        mScannerView = ZXingScannerView(this) // Programmatically initialize the scanner view
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera() // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera() // Stop camera on pause
    }

    override fun handleResult(result: Result) {
        dialog!!.show()
        if (checkQr(result.text)) {
            sendQrCode(result.text)
        } else {
            finishPage(getString(R.string.qr_is_wrong))
        }
    }

    private fun checkQr(qr: String): Boolean {
        return if (SaveItem.getItem(this, SaveItem.APK_ID, "").equals(calApkCodeFromQr(qr.trim { it <= ' ' }.split("").toTypedArray()), ignoreCase = true)) {
            true
        } else false
    }

    private fun calApkCodeFromQr(qr: Array<String>): String {
        return try {
            val mid = qr[1] + qr[2] + qr[3] + qr[4] + ""
            val q5 = qr[5].toInt()
            var uid = ""
            for (i in 0 until q5) {
                uid = uid + qr[i + 6]
            }
            uid = qr[5] + uid
            mid + uid
        } catch (e: Exception) {
            Log.e("calApkCodeFromQr:", e.message)
            ""
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun finishPage(msg: String?) {
        ActiveFragment.message_tv!!.text = msg
        if (msg.equals("user access set", ignoreCase = true)) {
            ActiveFragment.result_tv!!.text = getString(R.string.success)
            ActiveFragment!!.result_tv!!.setTextColor(Color.GREEN)
        } else {
            ActiveFragment.result_tv!!.text = getString(R.string.error)
            ActiveFragment.result_tv!!.setTextColor(Color.RED)
        }
        dialog!!.dismiss()
        finish()
    }

    private fun sendQrCode(qCode: String) {
        mScannerView!!.stopCameraPreview()
        val retrofit = RetrofitClientInstance.retrofitInstance
        val loginServer = retrofit!!.create(LoginServer::class.java)
        loginServer.activeUser(qCode, SaveItem.getItem(this, SaveItem.S_CODE, ""))!!.enqueue(object : Callback<ActiveRespone?> {
            override fun onFailure(call: Call<ActiveRespone?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<ActiveRespone?>, response: Response<ActiveRespone?>) {
                finishPage(response.body()!!.message)
            }

        })
    }
}