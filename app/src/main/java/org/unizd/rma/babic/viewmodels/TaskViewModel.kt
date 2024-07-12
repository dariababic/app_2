package org.unizd.rma.babic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.unizd.rma.babic.models.Task
import org.unizd.rma.babic.repository.TaskRepository
import org.unizd.rma.babic.utils.Resource

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)


    fun getTaskList() = taskRepository.getTaskList()


    fun insertTask(task: Task) : MutableLiveData<Resource<Long>>{
      return taskRepository.insertTask(task) as MutableLiveData<Resource<Long>>
    }

    fun deleteTAsk(task: Task) : MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTask(task) as MutableLiveData<Resource<Int>>
    }

    fun deleteTaskUsingId(taskId: String) : MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTaskUsingId(taskId) as MutableLiveData<Resource<Int>>
    }

    fun updateTask(task: Task) : MutableLiveData<Resource<Int>>{
        return taskRepository.updateTask(task) as MutableLiveData<Resource<Int>>
    }

    fun updateTaskPaticularField(taskId: String, title: String,description: String,surface: String, imageUri: String ) : MutableLiveData<Resource<Int>>{
        return taskRepository.updateTaskPaticularField(taskId,title,description,surface, imageUri) as MutableLiveData<Resource<Int>>
    }


//

//
//    fun deleteTaskUsingId(taskId: String){
//        taskRepository.deleteTaskUsingId(taskId)
//    }
//
//    fun updateTask(task: Task) {
//        taskRepository.updateTask(task)
//    }

}