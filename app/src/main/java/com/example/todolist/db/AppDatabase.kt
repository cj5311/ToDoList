package com.example.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 데이터베이스 생성
@Database(entities = [TodoEntity::class], version = 1) // 1. 데이터베이스 정의: TodoEntity 클래스에 정의된 엔티티를 포함하고, 버전을 1로 설정
abstract class AppDatabase : RoomDatabase() { // 2. RoomDatabase를 상속하여 데이터베이스 클래스 정의

    abstract fun getTodoDao(): TodoDao // 3. Dao 객체 반환을 위한 추상 함수 정의 (TodoDao 타입)

    // 싱글톤 패턴 구현: 여러 클래스에서 하나의 DB 객체를 공유하여 메모리와 자원을 효율적으로 사용하도록 함.
    companion object {
        val databaseName = "db_todo" // 데이터베이스 파일의 이름 설정. 앱의 내부 스토리지에 이 이름으로 데이터베이스가 생성됨.
        var appDatabase: AppDatabase? = null // AppDatabase 인스턴스를 저장할 변수. 초기에는 null로 설정.

        // getInstance 함수: AppDatabase 객체를 생성하거나 기존 객체를 반환하는 함수
        fun getInstance(context: Context): AppDatabase? {
            // 데이터베이스 인스턴스가 null인 경우에만 새롭게 빌드하여 생성
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    context.applicationContext, // 앱의 전체 컨텍스트를 사용하여 메모리 누수 방지
                    AppDatabase::class.java, // 생성할 데이터베이스 클래스 지정
                    databaseName // 데이터베이스 파일 이름 지정
                )
                    .fallbackToDestructiveMigration() // 마이그레이션 정의가 없을 때 데이터베이스를 재설치하여 데이터 손실을 방지
                    .build() // 데이터베이스 빌드 완료
            }
            return appDatabase // 데이터베이스 인스턴스 반환
        }
    }
}