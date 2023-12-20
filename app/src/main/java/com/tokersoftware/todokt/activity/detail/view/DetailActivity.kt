package com.tokersoftware.todokt.activity.detail.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.classes.DatabaseHelper
import com.tokersoftware.todokt.databinding.ActivityDetailBinding
import com.tokersoftware.todokt.activity.main.model.TodoModel
import com.tokersoftware.todokt.activity.main.view.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    var selectedTODO : TodoModel = TodoModel()
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        //Init Database
        val dbHelper = DatabaseHelper(this)


        if (intent.extras != null){
            try {
                selectedTODO  = intent.getParcelableExtra<TodoModel>("todo")!!
                if (selectedTODO != null) {
                    binding.titleEditText.setText(selectedTODO.title)
                    binding.dateTextView.setText(selectedTODO.date)
                    binding.messageEditText.setText(selectedTODO.message)
                }
            } catch (e : java.lang.Exception){
                e.stackTrace
            }

        }

        calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
        calendar.setTime(sdf.parse(selectedTODO.date));// all done


        binding.updateBTN.setOnClickListener{
            val title = binding.titleEditText.text.toString()
            val date = binding.dateTextView.text.toString()
            val message = binding.messageEditText.text.toString()

            val todo = TodoModel(selectedTODO.id, title, date, message)

            if(dbHelper.updateTodo(todo)){
                this.Toast(getString(R.string.successfully_update))
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                this.Toast("Error")
            }
        }
        binding.deleteBTN.setOnClickListener{
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.are_you_sure))
            alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_delete))
            alertDialog.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                dbHelper.deleteDataByID(selectedTODO.id.toString())
                this.Toast(getString(R.string.successfully_delete))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            alertDialog.setNegativeButton(getString(R.string.no)){ dialog, which ->
                //Nothing to do
            }
            alertDialog.show()






        }

        binding.dateTextView.setOnClickListener {
            showDatePicker()
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
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    fun Context.Toast(message: String){
        android.widget.Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}