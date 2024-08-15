package com.example.bmi.Ui.BmiCalculator

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.Database.Dbhelper
import com.example.bmi.Notifycation.ScheduleBmiReminder
import com.example.bmi.R
import com.example.bmi.Ui.YourHealhKid.YourhealthKid
import com.example.bmi.Ui.YourHealthOld.YourHealthActivity
import com.example.bmi.databinding.ActivityBmiCalculatorBinding
import com.example.bmi.view.tap
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.logging.Handler

class BmiCalculatorActivity : BaseActivity<ActivityBmiCalculatorBinding, BaseViewModel>() {
    private var selectedHeightUnit: String = "cm"
    private var oldSelectedHeightUnit: String = "cm"
    private var  selectedWeightUnit: String = "kg"
    var weightInput = 0f
    var heightInput = 0f
    var age = 0

    override fun createBinding() = ActivityBmiCalculatorBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()
    override fun initView() {
        super.initView()
        setFontDatePicker()
        setGender()
        setAge()
        setupDatePickers()
        unitConversion()

        binding.imgBack.tap {
            finish()
        }
        age = 20
        if (binding.edtYearsOld.text.toString() != null) {
            binding.edtYearsOld.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    age = s.toString().toIntOrNull() ?: 20
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }


        binding.btnCalculate.tap {
            Log.d("AgeCheck", "Age: $age")
            bmiCalculatorCheckWeight()
            val dbHelper = Dbhelper(this)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            val gender = binding.tvGender.text.toString()
            val ageText = binding.edtYearsOld.text.toString()
            val heightText = binding.edtHeight.text.toString()
            val weightText = binding.edtWeight.text.toString()

            dbHelper.saveUserData(date, gender, ageText, heightText, weightText)

        }

    }
    override fun onResume() {
        super.onResume()
        val scheduleBmiReminder = ScheduleBmiReminder(this)
        scheduleBmiReminder.scheduleReminder()
    }



    private fun bmiCalculatorOld(weightKg: Float) {
        val heightInMeters = when (selectedHeightUnit) {
            "cm" -> heightInput / 100
            "ft" -> feetToCm(heightInput) / 100
            "inch" -> inchToCm(heightInput) / 100
            else -> heightInput / 100
        }

        val bmi = weightKg / (heightInMeters * heightInMeters)
        Log.d("bmi", bmi.toString())

        val bmiCategory = when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }

        val intent = Intent(this, YourHealthActivity::class.java)
        intent.putExtra("bmi", bmi)

        intent.putExtra("gender", binding.tvGender.text.toString())
        intent.putExtra("age", age)
        intent.putExtra("date", binding.datePicker.value)
        intent.putExtra("month", binding.monthPicker.displayedValues[binding.monthPicker.value])
        intent.putExtra("year", binding.yearPicker.value)
        intent.putExtra("weight", weightKg)
        intent.putExtra("height", heightInput)
        intent.putExtra("selectedHeightUnit",selectedHeightUnit)
        intent.putExtra("selectedWeightUnit",selectedWeightUnit)

        val scheduleBmiReminder = ScheduleBmiReminder(this)
        scheduleBmiReminder.scheduleReminder()
        startActivity(intent)
        Log.d("bmiCategory", bmiCategory)
    }


    private fun bmiCalculatorCheckWeight() {
        if (age < 18) {
            if (weightInput > 0 && heightInput > 0) {
                val heightInMeters = when (selectedHeightUnit) {
                    "cm" -> heightInput / 100
                    "ft" -> feetToCm(heightInput) / 100
                    "inch" -> inchToCm(heightInput) / 100
                    else -> heightInput / 100
                }
                val bmikid = weightInput / (heightInMeters * heightInMeters)
                Log.d("bmikid", bmikid.toString())

                val bmiCategory = when {
                    bmikid <= 14.8 -> "Underweight"
                    bmikid in 14.9..18.0 -> "Normal weight"
                    bmikid in 18.1..19.1 -> "Overweight"
                    else -> "Obesity"
                }

                Log.d("bmiCategory", bmiCategory)

                val intent = Intent(this, YourhealthKid::class.java)
                intent.putExtra("bmikid", bmikid)

                // Add gender, age, date, month, year, weight, and height to the Intent
                intent.putExtra("gender", binding.tvGender.text.toString())
                intent.putExtra("age", age)
                intent.putExtra("date", binding.datePicker.value)
                intent.putExtra("month", binding.monthPicker.displayedValues[binding.monthPicker.value])
                intent.putExtra("year", binding.yearPicker.value)
                intent.putExtra("weight", weightInput)
                intent.putExtra("height", heightInput)
                intent.putExtra("selectedHeightUnit",selectedHeightUnit)
                intent.putExtra("selectedWeightUnit",selectedWeightUnit)
                val scheduleBmiReminder = ScheduleBmiReminder(this)
                scheduleBmiReminder.scheduleReminder()
                startActivity(intent)
            } else {
                // Handle error input
            }
        } else {
            if (weightInput > 0 && heightInput > 0) {
                if (binding.rdoLb.isChecked) {
                    val weightKg = lbToKg(weightInput)
                    bmiCalculatorOld(weightKg)
                } else {
                    bmiCalculatorOld(weightInput)
                }
            } else {
                // Handle error input
            }
        }
    }



    private fun setupDatePickers() {
        val currentDate = Calendar.getInstance()

        val months = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        binding.monthPicker.minValue = 0
        binding.monthPicker.maxValue = 11
        binding.monthPicker.displayedValues = months
        binding.monthPicker.value = currentDate.get(Calendar.MONTH)

        val currentYear = currentDate.get(Calendar.YEAR)
        binding.yearPicker.minValue = currentYear - 100
        binding.yearPicker.maxValue = currentYear + 100
        binding.yearPicker.value = currentYear

        updateDatePicker()

        binding.datePicker.value = currentDate.get(Calendar.DAY_OF_MONTH)
    }

    private fun updateDatePicker() {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.MONTH, binding.monthPicker.value)
        currentDate.set(Calendar.YEAR, binding.yearPicker.value)

        val maxDaysInMonth = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        binding.datePicker.minValue = 1
        binding.datePicker.maxValue = maxDaysInMonth

        if (binding.datePicker.value > maxDaysInMonth) {
            binding.datePicker.value = maxDaysInMonth
        }
    }


    private fun setAge() {
        val MIN_AGE = 1
        val MAX_AGE = 99

        binding.imgMinus.setOnClickListener {
            val currentAge = binding.edtYearsOld.text.toString().toIntOrNull() ?: MIN_AGE
            if (currentAge > MIN_AGE) {
                binding.edtYearsOld.setText((currentAge - 1).toString())
            }
        }

        binding.imgPlus.setOnClickListener {
            val currentAge = binding.edtYearsOld.text.toString().toIntOrNull() ?: MIN_AGE
            if (currentAge < MAX_AGE) {
                binding.edtYearsOld.setText((currentAge + 1).toString())
            }
        }

        binding.edtYearsOld.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val age = s.toString().toIntOrNull() ?: MIN_AGE
                if (age < MIN_AGE) {
                    binding.edtYearsOld.setText(MIN_AGE.toString())
                } else if (age > MAX_AGE) {
                    binding.edtYearsOld.setText(MAX_AGE.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun setGender() {
        binding.imgMale.setOnClickListener {
            binding.imgMale.setImageResource(R.drawable.ic_male_fill)
            binding.imgFemale.setImageResource(R.drawable.ic_female)
            binding.tvGender.text = getString(R.string.male)
        }
        binding.imgFemale.setOnClickListener {
            binding.imgMale.setImageResource(R.drawable.ic_male)
            binding.imgFemale.setImageResource(R.drawable.ic_female_fill)
            binding.tvGender.text = getString(R.string.female)
        }
    }

    private fun setFontDatePicker() {
        binding.datePicker.typeface = ResourcesCompat.getFont(this, R.font.inter_bold)
        binding.datePicker.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.inter_bold))
        binding.datePicker.selectedTextColor
        binding.monthPicker.typeface = ResourcesCompat.getFont(this, R.font.inter_bold)
        binding.monthPicker.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.inter_bold))
        binding.yearPicker.typeface = ResourcesCompat.getFont(this, R.font.inter_bold)
        binding.yearPicker.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.inter_bold))
    }

    fun unitConversion() {

        binding.rdoKg.isChecked = true
        binding.rdoCm.isChecked = true


        binding.edtWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                weightInput = s.toString().toFloatOrNull() ?: 0f
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.edtHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                heightInput = s.toString().toFloatOrNull() ?: 0f
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        binding.rdoCm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rdoKg.isChecked = true
                binding.rdoLb.isChecked = false
                oldSelectedHeightUnit = selectedHeightUnit

                if (oldSelectedHeightUnit.equals("ft")) {
                    heightInput = binding.edtHeight.text.toString().toFloat()
                    binding.edtHeight.setText(feetToCm(heightInput).toString())
                } else {
                    heightInput = binding.edtHeight.text.toString().toFloat()
                    binding.edtHeight.setText(inchToCm(heightInput).toString())
                }
                selectedHeightUnit = "cm"
                binding.rdoFt.isChecked = false
                binding.rdoIngches.isChecked = false

            }
        }
        binding.rdoFt.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rdoLb.isChecked = true
                binding.rdoKg.isChecked = false
                oldSelectedHeightUnit = selectedHeightUnit
                if (oldSelectedHeightUnit.equals("cm")) {
                    heightInput = binding.edtHeight.text.toString().toFloat()
                    binding.edtHeight.setText(cmToFeet(heightInput).toString())
                } else {
                    heightInput = binding.edtHeight.text.toString().toFloat()
                    binding.edtHeight.setText(inchToFeet(heightInput).toString())
                }
                selectedHeightUnit = "ft"
                binding.rdoCm.isChecked = false
                binding.rdoIngches.isChecked = false

            }
        }
        binding.rdoIngches.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                oldSelectedHeightUnit = selectedHeightUnit
                if (oldSelectedHeightUnit.equals("cm")) {
                    heightInput = binding.edtHeight.text.toString().toFloat()
                    binding.edtHeight.setText(cmToInch(heightInput).toString())
                } else {
                    heightInput = binding.edtHeight.text.toString().toFloat()
                    binding.edtHeight.setText(feetToInch(heightInput).toString())
                }
                selectedHeightUnit = "inch"
                binding.rdoLb.isChecked = true
                binding.rdoKg.isChecked = false
                binding.rdoCm.isChecked = false
                binding.rdoFt.isChecked = false
            }
        }
        binding.rdoKg.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.rdoCm.isChecked = true
                binding.rdoFt.isChecked = false
                binding.rdoIngches.isChecked = false
                binding.rdoLb.isChecked = false
                if (!binding.edtWeight.text.isEmpty()) {
                    weightInput = binding.edtWeight.text.toString().toFloat()
                    binding.edtWeight.setText(lbToKg(weightInput).toString())
                    selectedWeightUnit = "kg"
                }
            }
        }
        binding.rdoLb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (selectedHeightUnit.equals("inch")){
                    binding.rdoFt.isChecked = false
                    binding.rdoCm.isChecked = false
                    binding.rdoIngches.isChecked = true
                    binding.rdoKg.isChecked = false
                    if (!binding.edtWeight.text.isEmpty()) {
                        weightInput = binding.edtWeight.text.toString().toFloat()
                        binding.edtWeight.setText(kgToLb(weightInput).toString())
                        selectedWeightUnit = "lb"
                    }
                }else{
                    binding.rdoFt.isChecked = true
                    binding.rdoCm.isChecked = false
                    binding.rdoIngches.isChecked = false
                    binding.rdoKg.isChecked = false
                    if (!binding.edtWeight.text.isEmpty()) {
                        weightInput = binding.edtWeight.text.toString().toFloat()
                        binding.edtWeight.setText(kgToLb(weightInput).toString())
                        selectedWeightUnit = "lb"
                    }
                }


            }
        }
    }


    fun cmToFeet(cm: Float): Float {
        return cm / 30.48f
    }

    fun cmToInch(cm: Float): Float {
        return cm / 2.54f
    }

    fun feetToCm(feet: Float): Float {
        return feet * 30.48f
    }

    fun feetToInch(feet: Float): Float {
        return feet * 12.0f
    }

    fun inchToCm(inch: Float): Float {
        return inch * 2.54f
    }

    fun inchToFeet(inch: Float): Float {
        return inch / 12.0f
    }

    fun kgToLb(kg: Float): Float {
        return kg * 2.20462f
    }

    fun lbToKg(lb: Float): Float {
        return lb / 2.20462f
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}