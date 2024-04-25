package com.example.test

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityMainBinding
import com.example.test.databinding.ListItemBinding


data class ToDoItem(val text: String, var isChecked: Boolean)

class MainActivity : AppCompatActivity() {
    private val toDoList = mutableListOf<ToDoItem>()
    private lateinit var adapter: CustomAdapter
    private lateinit var listView: ListView
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화
        adapter = CustomAdapter(this, toDoList)
        listView = binding.listView
        editText = binding.editText

        // 어댑터 적용
        listView.adapter = adapter

        // 할일 추가 버튼 이벤트

        binding.addBtn.setOnClickListener {
            addItemToList(0) // 새로운 항목을 맨 위에 추가
        }

        // 리스트 아이템 클릭 이벤트
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            toDoList[position].isChecked = !toDoList[position].isChecked
            adapter.notifyDataSetChanged()
        }
    }

    // 할일 추가
    private fun addItemToList(position: Int) {
        val inputText = editText.text.toString()
        if (inputText.isNotEmpty()) {
            toDoList.add(position, ToDoItem(inputText, false)) // 새로운 항목을 position 위치에 추가
            adapter.notifyDataSetChanged()
            editText.setText("")
            Log.d("logTest", "Added Item: $inputText")
        }
    }
}

class CustomAdapter(private val context: Context, private val toDoList: MutableList<ToDoItem>) : BaseAdapter() {
    private lateinit var binding: ListItemBinding
    override fun getCount() = toDoList.size

    override fun getItem(position: Int) = toDoList[position]

    override fun getItemId(position: Int) = position.toLong()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // ActivityMain의 하위 인스턴스 listItem 바인딩
        binding = ListItemBinding.inflate(LayoutInflater.from(context), parent, false)

        val item = toDoList[position]
        binding.checkbox.isChecked = item.isChecked
        binding.textView.text = item.text
        binding.textView.paintFlags = if (item.isChecked) binding.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else binding.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        binding.textView.setTextColor(if (item.isChecked) Color.GRAY else Color.BLACK)

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            toDoList[position].isChecked = isChecked
            binding.textView.paintFlags = if (isChecked) binding.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else binding.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            binding.textView.setTextColor(if (isChecked) Color.GRAY else Color.BLACK)
        }

        binding.deleteBtn.setOnClickListener {
            toDoList.removeAt(position)
            notifyDataSetChanged()
        }

        return binding.root
    }
}
