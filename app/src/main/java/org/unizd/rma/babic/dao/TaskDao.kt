package org.unizd.rma.babic.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.unizd.rma.babic.models.Task


@Dao
interface TaskDao {


   @Query("SELECT * FROM Task ORDER BY date DESC")
    fun getTaskList() : Flow<List<Task>>



//    @Query("""SELECT * FROM Task ORDER BY
//        CASE WHEN :isAsc = 1 THEN taskTitle END ASC,
//        CASE WHEN :isAsc = 0 THEN taskTitle END DESC""")
//    fun getTaskListSortByTaskTitle(isAsc: Boolean) : Flow<List<Task>>
//
//    @Query("""SELECT * FROM Task ORDER BY
//        CASE WHEN :isAsc = 1 THEN date END ASC,
//        CASE WHEN :isAsc = 0 THEN date END DESC""")
//    fun getTaskListSortByTaskDate(isAsc: Boolean) : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

//
    // First way
    @Delete
    suspend fun deleteTask(task: Task) : Int
//
//
    // Second Way
    @Query("DELETE FROM Task WHERE taskId == :taskId")
    suspend fun deleteTaskUsingId(taskId: String) : Int
//
//
    @Update
    suspend fun updateTask(task: Task): Int
//
//
    @Query("UPDATE Task SET taskTitle=:title, description = :description  , surface = :surface, imageUri = :imageUri   WHERE taskId = :taskId")
    suspend fun updateTaskPaticularField(taskId:String,title:String,description:String, surface: String, imageUri: String): Int

//
//    @Query("SELECT * FROM Task WHERE taskTitle LIKE :query ORDER BY date DESC")
//    fun searchTaskList(query: String) : Flow<List<Task>>
}