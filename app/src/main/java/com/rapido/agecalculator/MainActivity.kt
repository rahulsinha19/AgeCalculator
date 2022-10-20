package com.rapido.agecalculator

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var tvSelectedDate : TextView? = null
    private var tvAgeInMinutes : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker : Button = findViewById(R.id.btn_date_picker)
        tvSelectedDate = findViewById(R.id.tv_date)
        tvAgeInMinutes = findViewById(R.id.tv_age)

        btnDatePicker.setOnClickListener {
            clickDatePicker()
        }
    }

    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,
            // Setting up DatePickerDialog.OnDateSetListener
            // The variable which will be not be used can be replaced with an underscore
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                tvSelectedDate?.text = selectedDate //sets the text of tvSelectedDate
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                // This will allow to create an actual date object out of sdf. So, we're parsing from the selectedDate (which is just a string) and converting this
                // into the format that we want to use in the date object. And this date object will later be used to calculate the age in minutes.
                val theDate = sdf.parse(selectedDate)

                // NULL SAFETY just to ensure the code doesn't crashes
                // The code will only run if the date is not empty
                theDate?.let {
                    val selectedDateInMinutes = theDate.time / 60000 // time is in milliseconds, 1000 for converting into seconds and 60 into minutes

                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    // Will only execute if the currentDate is not empty
                    currentDate?.let {
                        val currentDateInMinutes = currentDate.time / 60000 // time is in milliseconds, 1000 for converting into seconds and 60 into minutes
                        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes

                        tvAgeInMinutes?.text = differenceInMinutes.toString()
                    }
                }
            },
            year,
            month,
            day
        )

        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000 // restricted the user to select a date from future, and can only be selected from past
        dpd.show()
    }
}