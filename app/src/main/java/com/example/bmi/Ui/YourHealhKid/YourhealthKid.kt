package com.example.bmi.Ui.YourHealhKid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.Database.Dbhelper
import com.example.bmi.R
import com.example.bmi.Ui.BmiCalculator.BmiCalculatorActivity
import com.example.bmi.databinding.ActivityYourhealthKidBinding
import java.util.Locale

class YourhealthKid : BaseActivity<ActivityYourhealthKidBinding,BaseViewModel>() {
    override fun createBinding() = ActivityYourhealthKidBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val intentBmi = intent.getFloatExtra("bmikid", 0.0f)
        Log.d("bmikiddd", intentBmi.toString());
        val formattedBmi = String.format(Locale.US,"%.2f", intentBmi)
        binding.tvBmiIndex.setText(formattedBmi)
        binding.bmiClock.setBMIKid(intentBmi)
        binding.imgBack.setOnClickListener {
            startActivity(Intent(this, BmiCalculatorActivity::class.java))
        }
        val gender = intent.getStringExtra("gender")
        val age = intent.getIntExtra("age", 0)
        val date = intent.getIntExtra("date", 0)
        val month = intent.getStringExtra("month")
        val year = intent.getIntExtra("year", 0)
        val weight = intent.getFloatExtra("weight", 0.0f)
        val height = intent.getFloatExtra("height", 0.0f)
        val selectedHeightUnit = intent.getStringExtra("selectedHeightUnit")
        val selectedWeightUnit = intent.getStringExtra("selectedWeightUnit")

        var monthNumber = ""
        if (month.equals("Jan")){
            monthNumber = "01"
        }else if (month.equals("Feb")){
            monthNumber = "02"
        }else if (month.equals("Mar")){
            monthNumber = "03"
        }else if (month.equals("Apr")){
            monthNumber = "04"
        }else if (month.equals("May")){
            monthNumber = "05"
        }else if (month.equals("Jun")){
            monthNumber = "06"
        }else if (month.equals("Jul")){
            monthNumber = "07"
        }else if (month.equals("Aug")){
            monthNumber = "08"
        }else if (month.equals("Sep")){
            monthNumber = "09"
        }else if (month.equals("Oct")){
            monthNumber = "10"
        }else if (month.equals("Nov")){
            monthNumber = "11"
        }else if (month.equals("Dec")){
            monthNumber = "12"
        }


        binding.yourProfile.setText("$gender | $age years old | $date/$monthNumber/$year")
        binding.yourWeightAndHeight.setText("$height $selectedHeightUnit | $weight $selectedWeightUnit")
        val interBold = ResourcesCompat.getFont(this, R.font.inter_bold)
        if (intentBmi <= 14.8) {
            binding.tvYourEvaluate.setText(getString(R.string.you_are_underweight))
            binding.tvYourEvaluate.setTextColor(ContextCompat.getColor(this,R.color._0077FE))
            binding.tvBmiIndex.setTextColor(ContextCompat.getColor(this,R.color._0077FE))
            binding.cstUnderWeight.background = ContextCompat.getDrawable(this, R.drawable.custom_background_underweight)
            binding.imgUnderWeight.setImageResource(R.drawable.ic_img_selected_bmi)
            binding.tvUnderWeight.typeface = interBold
            binding.tvUnderWeightIndex.typeface = interBold
            binding.tvUnderWeight.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.tvUnderWeightIndex.setTextColor(ContextCompat.getColor(this,R.color.white))

        } else if (intentBmi > 14.8 && intentBmi < 18.1) {
            binding.tvYourEvaluate.setText(getString(R.string.you_are_healthy))
            binding.tvYourEvaluate.setTextColor(ContextCompat.getColor(this,R.color._00ad50))
            binding.tvBmiIndex.setTextColor(ContextCompat.getColor(this,R.color._00ad50))
            binding.cstNormal.background = ContextCompat.getDrawable(this, R.drawable.custom_background_healthy)
            binding.imgNormal.setImageResource(R.drawable.ic_img_selected_bmi)
            binding.tvnormal.typeface = interBold
            binding.tvnormalIndex.typeface = interBold
            binding.tvnormal.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.tvnormalIndex.setTextColor(ContextCompat.getColor(this,R.color.white))
        } else if (intentBmi >= 18.1 && intentBmi <= 19.1) {
            binding.tvYourEvaluate.setText(getString(R.string.you_are_overweight))
            binding.tvYourEvaluate.setTextColor(ContextCompat.getColor(this,R.color.F36F56))
            binding.tvBmiIndex.setTextColor(ContextCompat.getColor(this,R.color.F36F56))
            binding.cstOverweight.background = ContextCompat.getDrawable(this, R.drawable.custom_background_overweight)
            binding.imgOverweight.setImageResource(R.drawable.ic_img_selected_bmi)
            binding.tvOverWeight.typeface = interBold
            binding.tvOverWeightIndex.typeface = interBold
            binding.tvOverWeight.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.tvOverWeightIndex.setTextColor(ContextCompat.getColor(this,R.color.white))

        } else {
            binding.tvYourEvaluate.setText(getString(R.string.you_are_obese))
            binding.tvYourEvaluate.setTextColor(ContextCompat.getColor(this,R.color.FF2600))
            binding.tvBmiIndex.setTextColor(ContextCompat.getColor(this,R.color.FF2600))
            binding.cstObese.background = ContextCompat.getDrawable(this, R.drawable.custom_background_obese)
            binding.imgObese.setImageResource(R.drawable.ic_img_selected_bmi)
            binding.tvObese.typeface = interBold
            binding.tvObeseIndex.typeface = interBold
            binding.tvObese.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.tvObeseIndex.setTextColor(ContextCompat.getColor(this,R.color.white))
        }

    }
}