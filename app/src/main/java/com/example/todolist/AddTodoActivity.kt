package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.todolist.databinding.ActivityAddTodoBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.TodoDao
import com.example.todolist.db.TodoEntity

class AddTodoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddTodoBinding
    
    //db 객체 호출
    lateinit var db : AppDatabase
    lateinit var todoDao : TodoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //싱글톤으로 데이터베이스 생성했기 때문에, 메인엑티비티에서 호출한 객체와 동일한 객체이다.
        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        //버튼클릭했을때 인서트 함수 실행
        binding.btnComplete.setOnClickListener {
            insertTodo()
        }
    }


    private fun insertTodo() {

        //텍스트뷰의 텍스트와, 라디오그룹의 각 버튼아이디 호출
        val todoTitle = binding.edtTitle.text.toString()
        var todoImportance = binding.radioGroup.checkedRadioButtonId

        Log.v("todoImportance","$todoImportance")
        var impData = 0;

        when(todoImportance){
            R.id.btn_high -> {
                impData = 1;
            }
            R.id.btn_middle -> {
                impData = 2
            }
            R.id.btn_low->{
                impData = 3
            }
        }

        if(impData ==0 || todoTitle.isBlank()){
            Toast.makeText(this,"모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show()
        }else{
            Thread{
                todoDao.insertTodo(TodoEntity(null,todoTitle,impData))
                runOnUiThread {
                    Toast.makeText(this,"할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }


    }
}