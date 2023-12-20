package com.tokersoftware.todokt.activity.addtodo.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.classes.DatabaseHelper
import com.tokersoftware.todokt.classes.NotificationHelper
import com.tokersoftware.todokt.databinding.ActivityAddtodoBinding
import com.tokersoftware.todokt.activity.main.view.MainActivity
import com.tokersoftware.todokt.activity.main.model.TodoModel
import java.text.SimpleDateFormat
import java.util.*

class AddTODOActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddtodoBinding
    private lateinit var calendar: Calendar

    private lateinit var selectedDateFromDialog: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddtodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){
        calendar = Calendar.getInstance()

        binding.dateTextView.setOnClickListener {
            showDatePicker()
        }

        binding.addBtn.setOnClickListener{
            val title = binding.titleEditText.text.toString()
            val message = binding.messageEditText.text.toString()
            AddToDatabase(title, message)
        }
    }

    fun AddToDatabase(title: String, message: String){
        val dbHelper = DatabaseHelper(this)
        val todo = TodoModel(0, title, selectedDateFromDialog, message)
        if(dbHelper.insertData(todo)){
            Toast.makeText(this, getText(R.string.successfully_save), Toast.LENGTH_SHORT).show()

            //Check if exists permission for notification
            val sharedPref = getSharedPreferences(getString(R.string.sharedpreferences_key), Context.MODE_PRIVATE)
            if (sharedPref.getBoolean(getString(R.string.sharedpreferences_notification), true)){
                val workManager = WorkManager.getInstance(this)
                val delay = calculateTriggerAtMillis(todo.date!!)

                val workRequest = OneTimeWorkRequest.Builder(NotificationHelper::class.java)
                    .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .build()

                workManager.enqueue(workRequest)
            }

            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    //Date Dialog
    fun showDatePicker(){
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                binding.dateTextView.text = formattedDate
                selectedDateFromDialog = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    fun calculateTriggerAtMillis(date: String): Long {
        val formattedDate = formatDate(date)

        val cal = Calendar.getInstance()
        cal.time = formattedDate
        return cal.timeInMillis
    }

    private fun formatDate(dateString: String): Date {
        // Convert the incoming date to the appropriate format
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.parse(dateString)!!
    }

}