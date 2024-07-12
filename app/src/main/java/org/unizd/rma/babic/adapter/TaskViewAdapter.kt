package org.unizd.rma.babic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.unizd.rma.babic.R
import org.unizd.rma.babic.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskViewAdapter(
    private val deleteUpdateCallBack: (type: String, Position: Int, task: Task) -> Unit
) : RecyclerView.Adapter<TaskViewAdapter.ViewHolder>() {

    private val taskList = arrayListOf<Task>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleText)
        val descText: TextView = itemView.findViewById(R.id.descText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val surface: TextView = itemView.findViewById(R.id.surface)
        val delImg: ImageView = itemView.findViewById(R.id.deleteImg)
        val editImg: ImageView = itemView.findViewById(R.id.editImg)
        val flagImage: ImageView = itemView.findViewById(R.id.flagImage)
        val countryText: TextView = itemView.findViewById(R.id.country)
    }

    fun addAllTask(newTaskList: List<Task>) {
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_task_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        holder.titleTxt.text = task.title
        holder.descText.text = task.description
        holder.surface.text = task.surface
        holder.countryText.text = task.country

//        // Load flag image using Glide library
//        Glide.with(holder.itemView.context)
//            .load(task.flagurl)
//            .into(holder.flagImage)



        // Load image from imageUri using Glide library
        Glide.with(holder.itemView.context)
            .load(task.imageUri)
            .into(holder.flagImage)

        holder.delImg.setOnClickListener {
            if (holder.adapterPosition != -1) {
                deleteUpdateCallBack("delete",holder.adapterPosition, task)
            }
        }

        holder.editImg.setOnClickListener {
            if (holder.adapterPosition != -1) {
                deleteUpdateCallBack("update",holder.adapterPosition, taskList[position])
            }
        }


        val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.dateText.text = dateFormat.format(task.date)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
