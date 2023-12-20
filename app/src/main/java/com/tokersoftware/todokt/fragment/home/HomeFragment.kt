package com.tokersoftware.todokt.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokersoftware.todokt.activity.addtodo.view.AddTODOActivity
import com.tokersoftware.todokt.classes.DatabaseHelper
import com.tokersoftware.todokt.databinding.FragmentHomeBinding
import com.tokersoftware.todokt.activity.detail.view.DetailActivity
import com.tokersoftware.todokt.activity.main.adapter.TodoAdapter
import com.tokersoftware.todokt.activity.main.model.TodoModel
import java.text.SimpleDateFormat

class HomeFragment : Fragment(), TodoAdapter.ItemClickListener {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(requireContext())
    }

    fun init(context: Context){
        //Initialize Views
        binding.addBtn.setOnClickListener{
            startActivity(Intent(context, AddTODOActivity::class.java))
        }

        val dbHelper = DatabaseHelper(context)
        //Get Datas from DB
        val todoArrayFromDB = dbHelper.readData()
        //Sort by date
        todoArrayFromDB.sortBy { it ->
            // Convert the incoming date to the appropriate format
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = dateFormat.parse(it.date)!!
            formattedDate
        }

        val adapter = TodoAdapter(context ,todoArrayFromDB, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(todo: TodoModel) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("todo", todo)
        startActivity(intent)
    }
}