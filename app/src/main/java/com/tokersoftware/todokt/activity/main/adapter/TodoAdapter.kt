package com.tokersoftware.todokt.activity.main.adapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.activity.main.model.TodoModel
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(val context: Context, val todos: MutableList<TodoModel>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(todo: TodoModel)
        //fun onLongClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.main_recyclerview_item,parent,false)
        return TodoViewHolder(v)

    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        var isNightMode = false
        val nightModeFlags: Int = context.getResources().getConfiguration().uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            isNightMode = true
        }

        val todo = todos[position]
        holder.titleTextView.text = todo.title
        holder.dateTextView.text = todo.date

        // Check for days if is in future or past
        val dateObject = formatDate(todo.date!!)

        // Get the current date
        val currentDate = formatDate(getCurrentDay())

        val currentDay = dateObject.date == currentDate.date &&
                dateObject.month == currentDate.month &&
                dateObject.year == currentDate.year


        // Before Day
        if (dateObject.before(currentDate)) {
            holder.dateTextView.setTextColor(ContextCompat.getColor(context, R.color.red_500))
        } else if (currentDay) {
            // Current Day
            holder.dateTextView.setTextColor(ContextCompat.getColor(context, R.color.orange_500))
        } else {
            // Future Day
            if (!isNightMode)
                holder.dateTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            else
                holder.dateTextView.setTextColor(ContextCompat.getColor(context, R.color.white))

        }

        if(isNightMode){
            holder.titleTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.titleTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        holder.itemView.setOnClickListener{
            itemClickListener.onItemClick(todo)
        }
    }

    private fun formatDate(dateString: String): Date {
        // Convert the incoming date to the appropriate format
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.parse(dateString)!!
    }

    private fun getCurrentDay(): String {
        // Şu anki tarihi dd/MM/yyyy formatında al
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

     inner class TodoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val titleTextView : TextView =  view.findViewById(R.id.titleTextView)
        val dateTextView : TextView = view.findViewById(R.id.dateTextView)
    }
}