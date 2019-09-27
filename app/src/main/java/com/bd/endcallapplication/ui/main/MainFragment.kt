package com.bd.endcallapplication.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bd.endcallapplication.R

class MainFragment : Fragment() {

    val listPermission: Array<String> = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.CALL_PHONE
    )
    val requestPermissionCode: Int = 1001

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            &&ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(activity!!, listPermission, requestPermissionCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}
