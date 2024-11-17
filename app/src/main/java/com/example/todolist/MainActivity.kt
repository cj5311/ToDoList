package com.example.todolist

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.TodoDao
import com.example.todolist.db.TodoEntity

class MainActivity : AppCompatActivity() , OnItemLongClickListener{

    //바인딩 객체 호출
    private lateinit var binding : ActivityMainBinding

    // db 연결객체 호출
    private lateinit var db : AppDatabase
    private lateinit var todoDao : TodoDao
    private lateinit var todoList : ArrayList<TodoEntity>
    
    // 어뎁터 객체 호출
    private lateinit var adapter : TodoRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1.메인 레이아웃 바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2.db 객체 호출
        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()
        
        // db 객체를 사용해 리싸이클뷰러뷰가 정의된 어뎁터를 호출하고, 어뎁터와 레이아웃 메니저를 메인 뷰객체에 연결해준다.
        getAllTodoList()
        
        // 할일추가하기 버튼 클릭시 동작 제어
        binding.btnAddTodo.setOnClickListener {
            // 페이지 이동 정의하고 실행.
            val intent = Intent(this,AddTodoActivity::class.java)
            startActivity(intent)
        }
    }



    private fun getAllTodoList(){
        // 백그라운드에서 db작업이 수행될 수 있도록 쓰레드 생성
        Thread{
            todoList = ArrayList(todoDao.getAllTodo())
            setRecyclerView() // 뷰객체에 어뎁터와 레이아웃을 할당해 준다.
        }.start()
    }

    private fun setRecyclerView(){
        //메인쓰레드에서 작동되도록 설정
        runOnUiThread {
            // 어뎁터 객체 생성
            adapter = TodoRecyclerViewAdapter(todoList, this)

            // 바인딩 객체에 어뎁터, 레이아웃메니저 정의
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(this)
        }
    }

    // 다른 페이지에 갔다가 돌아왔을때 업데이트 해주는 함수.
    override fun onRestart() {
        super.onRestart()
        getAllTodoList()
    }

    override fun onLongClick(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert_title))
        builder.setMessage(getString(R.string.alert_message))
        builder.setNegativeButton(getString(R.string.alert_no),null)
        builder.setPositiveButton(getString(R.string.alert_yes),
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteTodo(position)
                }
            }
            )
        builder.show()
    }

    private fun deleteTodo(position: Int){
        Thread{
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)
            runOnUiThread {
                adapter.notifyDataSetChanged()
                Toast.makeText(this,"삭제되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}