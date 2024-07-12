package org.unizd.rma.babic.repository

import android.app.ActivityManager.TaskDescription
import android.app.Application
import android.icu.text.CaseMap.Title
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.unizd.rma.babic.dao.TaskDao
import org.unizd.rma.babic.database.TaskDatabase
import org.unizd.rma.babic.models.Task
import org.unizd.rma.babic.utils.Resource
import org.unizd.rma.babic.utils.StatusResult

class TaskRepository(application: Application) {



 private  val taskDao: TaskDao = TaskDatabase.getInstance(application).taskDao

    fun getTaskList() = flow{

    emit(Resource.Loading())

        try {

            val result = taskDao.getTaskList()
            emit(Resource.Success(result))

        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }

    fun insertTask(task: Task): Any = MutableLiveData<Resource<Long>> ().apply{
        postValue(Resource.Loading())

     try {

      CoroutineScope(Dispatchers.IO).launch {
       val result = taskDao.insertTask(task)
      postValue(Resource.Success(result))
      }

     } catch (e : Exception){
  postValue(Resource.Error(e.message.toString()))

     }
    }

    fun deleteTask(task: Task): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTask(task)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }


    fun deleteTaskUsingId(taskId : String): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTaskUsingId(taskId)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }


    fun updateTask(task: Task): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTask(task)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }


    fun updateTaskPaticularField(taskId: String, title: String,description: String, surface: String, imageUri: String): Any = MutableLiveData<Resource<Int>> ().apply{
        postValue(Resource.Loading())

        try {

            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTaskPaticularField(taskId,title,description,surface,imageUri)
                postValue(Resource.Success(result))
            }

        } catch (e : Exception){
            postValue(Resource.Error(e.message.toString()))

        }
    }
//    fun searchTaskList(query: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                _taskStateFlow.emit(Loading())
//                val result = taskDao.searchTaskList("%${query}%")
//                _taskStateFlow.emit(Success("loading", result))
//            } catch (e: Exception) {
//                _taskStateFlow.emit(Error(e.message.toString()))
//            }
//        }
//    }


    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {

    }
}