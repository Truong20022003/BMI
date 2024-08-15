package com.example.bmi.Ui.Permission

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.Ui.Home.HealthyCheck
import com.example.bmi.databinding.ActivityPermissionBinding

class PermissionActivity : BaseActivity<ActivityPermissionBinding, BaseViewModel>() {
    override fun createBinding() = ActivityPermissionBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Chỉ kiểm tra quyền nếu phiên bản Android là 13 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissions()

            binding.imgSwUnSelected.setOnClickListener {
                requestPermissions()
            }
        } else {
            // Nếu là Android 12 trở xuống, không cần xin quyền
            binding.imgSwUnSelected.visibility = View.GONE
            binding.imgSwSelected.visibility = View.VISIBLE
        }

        binding.btnGetStarted.setOnClickListener{
            startActivity(Intent(this,HealthyCheck::class.java))
            finish()
        }
    }

    private fun checkPermissions() {
        // Kiểm tra xem quyền POST_NOTIFICATIONS và SCHEDULE_EXACT_ALARM đã được cấp chưa
        val isNotificationGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        val isAlarmGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.SCHEDULE_EXACT_ALARM
        ) == PackageManager.PERMISSION_GRANTED

        if (isNotificationGranted && isAlarmGranted) {
            binding.imgSwUnSelected.visibility = View.INVISIBLE
            binding.imgSwSelected.visibility = View.VISIBLE
        } else {
            binding.imgSwUnSelected.visibility = View.VISIBLE
            binding.imgSwSelected.visibility = View.INVISIBLE
        }
    }

    private fun requestPermissions() {
        // Kiểm tra lý do người dùng từ chối quyền
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM
            )
        ) {
            Toast.makeText(
                this,
                "Bạn cần cấp quyền để nhận thông báo và đặt lịch chính xác.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.SCHEDULE_EXACT_ALARM
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isNotificationGranted = permissions[android.Manifest.permission.POST_NOTIFICATIONS] ?: false
            val isAlarmGranted = permissions[android.Manifest.permission.SCHEDULE_EXACT_ALARM] ?: false

            if (isNotificationGranted && isAlarmGranted) {
                binding.imgSwUnSelected.visibility = View.INVISIBLE
                binding.imgSwSelected.visibility = View.VISIBLE
                Toast.makeText(this, "Các quyền đã được cấp.", Toast.LENGTH_SHORT).show()
            } else {
                binding.imgSwUnSelected.visibility = View.VISIBLE
                binding.imgSwSelected.visibility = View.INVISIBLE
                Toast.makeText(this, "Các quyền bị từ chối.", Toast.LENGTH_SHORT).show()
            }
        }
}
