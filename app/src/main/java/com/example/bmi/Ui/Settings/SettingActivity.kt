package com.example.bmi.Ui.Settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.BuildConfig
import com.example.bmi.R
import com.example.bmi.Ui.Language.LanguageSettingActivity
import com.example.bmi.Ui.dialog.RatingDialog
import com.example.bmi.Utils.SharePrefUtils
import com.example.bmi.databinding.ActivitySettingBinding
import com.example.bmi.view.tap
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory


class SettingActivity : BaseActivity<ActivitySettingBinding, BaseViewModel>() {

    private var check = false

    override fun createBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        binding.tvVersionApp.setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME)

        binding.clLanguage.tap {
            val intent = Intent(this, LanguageSettingActivity::class.java)
            startActivity(intent)
        }

        binding.clRate.tap {
            showRateDialog()
        }

        binding.clPolicy.tap {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(""))
            startActivity(intent)
        }

        binding.clShare.tap {
            if (!check) {
                share()
            }
        }

        binding.tvBack.tap {
            finish()
        }

        if (SharePrefUtils.isRated(this@SettingActivity)) {
            binding.clRate.visibility = View.GONE
        } else {
            binding.clRate.visibility = View.VISIBLE
        }


    }

    private fun showRateDialog() {
        check = true

        val ratingDialog = RatingDialog(this@SettingActivity)
        ratingDialog.init(this@SettingActivity, object : RatingDialog.OnPress {
            override fun send(s: Int) {
                Toast.makeText(
                    this@SettingActivity,
                    getString(R.string.thank_you),
                    Toast.LENGTH_SHORT
                ).show()
                binding.clRate.visibility = View.GONE
                SharePrefUtils.forceRated(this@SettingActivity)
                ratingDialog.dismiss()
            }

            override fun rating(s: Int) {
                onRateAppNew()
                binding.clRate.visibility = View.GONE
                SharePrefUtils.forceRated(this@SettingActivity)
                ratingDialog.dismiss()
            }

            override fun cancel() {
                ratingDialog.dismiss()
            }

            override fun later() {
                ratingDialog.dismiss()
            }

            override fun gotIt() {
                ratingDialog.dismiss()
            }
        })

        ratingDialog.show()
        ratingDialog.setOnDismissListener {
            check = false
        }
    }

    private fun rateAppOnStoreNew() {
        val packageName = baseContext.packageName
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    private fun onRateAppNew() {
        val manager: ReviewManager?
        var reviewInfo: ReviewInfo?
        manager = ReviewManagerFactory.create(this)
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SharePrefUtils.forceRated(this)
                reviewInfo = task.result
                val flow: Task<Void> =
                    manager.launchReviewFlow(this, reviewInfo!!)
                flow.addOnSuccessListener {
                    rateAppOnStoreNew()
                }
            }
        }
    }

    private fun share() {
        check = true
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intentShare.putExtra(
            Intent.EXTRA_TEXT,
            "${getString(R.string.app_name)}\nhttps://play.google.com/store/apps/details?id=${this.packageName}"
        )
        startActivity(Intent.createChooser(intentShare, "Share"))
    }

    override fun onResume() {
        super.onResume()
        check = false
    }

}