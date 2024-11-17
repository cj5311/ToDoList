package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.TodoEntity

//RecyclerView에 Todo 목록을 표시하기 위한 어댑터 클래스
class TodoRecyclerViewAdapter(
    private val todoList: ArrayList<TodoEntity>,//클래스가 받는 인수 정의->표시할 todo데이터 목록
    private val listener: OnItemLongClickListener // 항목을 길게 클릭했을 때 호출될 리스너
) : RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {
    // 어뎁터를 상속받음 -> 아래에 정의한 내부클래스로 정의된 클래스
    // 뷰홀더패턴 : 각 뷰객체를 뷰홀더에 보관한후, 참조기능을 사용하므로, 반복매서드 호출을 줄여 속도개선.

    //내부클래스 정의 : 각 Todo 항목에 데이터를 바인딩 하기 위해, 리사이클러뷰의 홀더클래스 상속
    //바인딩할 레이아웃 인수로 정의, 카멜체로 자동으로 연결된 이름 변환
    inner class MyViewHolder(binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

        // 아이템명이 tv_importance, tv_title인 객체를 가져와 새로운 변수로 정의
        val tv_importance = binding.tvImportance
        val tv_title = binding.tvTitle

        // 메인 레이아웃의 이름이 root로 지정되어 있음
        val root = binding.root
    }

    // 리사이클뷰를 상속했을때 필수적으로 구현해야할 함수 3개 정의
    // 뷰홀더를 생성하는 함수 : 레이아웃을 바인딩해서 뷰객체를 만들고, 뷰객체가 연결된 홀더객체를 만든다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // xml 레이아웃을 가져와 뷰 객체로 생성 // inflate : xml 함수를 view 객체로 변환. 부풀리다.
        val binding: ItemTodoBinding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // 생성된 뷰객체를 사용하여 MyViewHolder 객체를 생성해 반환
        return MyViewHolder(binding)
    }

    // 뷰홀더에 데이터를 맵핑하고, 기능을 제어하는 부분.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 현재 위치의 Todo 데이터를 가져옴
        val todoData = todoList[position]

        // 중요도(importance) 값에 따라 중요도 텍스트뷰의 배경 색상을 설정
        when (todoData.importance) {
            1 -> {
                holder.tv_importance.setBackgroundResource(R.color.red) // 중요도가 1일 때 빨간색
            }
            2 -> {
                holder.tv_importance.setBackgroundResource(R.color.yellow) // 중요도가 2일 때 노란색
            }
            3 -> {
                holder.tv_importance.setBackgroundResource(R.color.green) // 중요도가 3일 때 녹색
            }
        }

        // 홀더에 저장된 뷰객체에 데이터 맵핑
        holder.tv_importance.text = todoData.importance.toString() // 중요도 값 표시
        holder.tv_title.text = todoData.title // 제목 표시

        // 항목을 길게 클릭했을 때 호출될 리스너 설정
        holder.root.setOnLongClickListener {
            listener.onLongClick(position) // 리스너의 onLongClick 메서드를 호출해 현재 위치 전달
            false // false를 반환하여 추가 클릭 이벤트가 발생하지 않도록 설정
        }
    }

    // 총 아이템의 개수를 반환
    override fun getItemCount(): Int {
        return todoList.size
    }
}
