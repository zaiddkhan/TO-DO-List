package com.example.to_dolist.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.firestore.FirestoreClass
import com.example.to_dolist.models.Task

class TaskListAdapter(val context:Context,
                      val list:ArrayList<Task>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var completedTasksList = ArrayList<Task>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      val view = LayoutInflater.from(context).inflate(R.layout.item_task,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model :Task = list[position]
        if(holder is MyViewHolder){
            holder.taskName.text = model.name
            holder.dueDate.text = model.date
            holder.dueTime.text = model.time
            }
         holder.itemView.setOnClickListener {
             val dialog = Dialog(context)
             dialog.setContentView(R.layout.dialog_completed_task)
             val cancelButton = dialog.findViewById<ImageButton>(R.id.task_not_completed)
             cancelButton.setOnClickListener {
                 dialog.dismiss()
             }
             val completedButton = dialog.findViewById<ImageButton>(R.id.task_completed)
             completedButton.setOnClickListener {
                 val completedTask = list[position]
                 completedTasksList.add(completedTask)
                 FirestoreClass().addCompletedTaskList(completedTask)
                 FirestoreClass().removeFromTaskList(completedTask)
                 list.removeAt(position)
                 notifyItemRemoved(position)
                 dialog.dismiss()
             }
             dialog.show()
         }
    }





    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){


        val taskName : TextView = itemView.findViewById(R.id.task_name)
        val dueDate : TextView = itemView.findViewById(R.id.due_date)
        val dueTime : TextView = itemView.findViewById(R.id.due_time)

        }
    }
