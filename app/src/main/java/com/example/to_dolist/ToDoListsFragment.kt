package com.example.to_dolist

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.adapters.TaskListAdapter
import com.example.to_dolist.databinding.FragmentToDoListsBinding
import com.example.to_dolist.firestore.FirestoreClass
import com.example.to_dolist.models.Task
import com.example.to_dolist.models.User
import com.example.to_dolist.notification.Notification
import com.example.to_dolist.notification.channelID
import com.example.to_dolist.notification.notificationId
import com.example.to_dolist.utils.SwipeToDeleteCallBack
import java.util.*
import kotlin.collections.ArrayList


class ToDoListsFragment : Fragment() {

    private var time :Long = 0L
    private lateinit var  adapter : TaskListAdapter
    private lateinit var binding : FragmentToDoListsBinding
    private var taskList = ArrayList<Task>()
    private  lateinit var userDetails:User
    private var completedTasksList = ArrayList<Task>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentToDoListsBinding.inflate(layoutInflater)
        createNotificationChannel()
        binding.btnAddTasks.setOnClickListener {
            showAddTaskDialog()
        }

        FirestoreClass().getCurrentUserdataFromFragment(this)


        return binding.root
    }

    fun getUserDetails(user:User){
        userDetails = user
        FirestoreClass().getCurrentUsersTask(this)
    }

    private fun showAddTaskDialog(){

        val dialog= context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_add_task)
        val addBtn : Button = dialog!!.findViewById(R.id.btn_add)

//
//        date.setOnClickListener{
//            val c = Calendar.getInstance()
//            val year =
//                c.get(Calendar.YEAR)
//            val month = c.get(Calendar.MONTH)
//            val day = c.get(Calendar.DAY_OF_MONTH)
//            val dpd = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener {
//                    view,year,monthOfYear,dayOfMonth ->
//                val sDayOfMonth = if(dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
//                val sMonthOfYear =
//                    if((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
//                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
//                date.text = selectedDate
//            },year,month,day
//            )
//            dpd.show()
//        }
//
//        time.setOnClickListener {
//          val mTimePicker : TimePickerDialog
//          val currentTime = Calendar.getInstance()
//            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
//            val minute = currentTime.get(Calendar.MINUTE)
//            mTimePicker = TimePickerDialog(requireContext(),object :TimePickerDialog.OnTimeSetListener{
//                override fun onTimeSet(p0: TimePicker?, hourOfday: Int, minuteSelected: Int) {
//                    time.setText(String.format("%d : %d",hourOfday,minuteSelected))
//                }
//            },hour,minute,false)
//            mTimePicker.show()
//        }

        addBtn.setOnClickListener {

            val name  = dialog.findViewById<TextView?>(R.id.task_name).text.toString()

            val id = FirestoreClass().getCurrentUserId()

            val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)
            val hour = timePicker.hour
            val minute = timePicker.minute
            val timeText = (hour + minute).toString()

            val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker)
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            val dateText = "$day/$month/$year"

            getTime(day,month,year,hour,minute)

            val task = Task(id,name,dateText,timeText)
            time = getTime(day, month, year, hour, minute)
            taskList.add(task)
            FirestoreClass().storeTaskInDatabase(this,task,userDetails)
            FirestoreClass().getCurrentUsersTask(this)
            dialog.dismiss()
        }
        val cancelBtn : Button = dialog.findViewById(R.id.btn_cancel)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
    fun showInRecyclerView(taskList : ArrayList<Task>){
        binding.recViewTasks.layoutManager = LinearLayoutManager(context)
        adapter = TaskListAdapter(requireContext(),taskList)
        binding.recViewTasks.setHasFixedSize(true)


        binding.recViewTasks.adapter = adapter
        adapter.notifyDataSetChanged()

        val swipeToDeleteCallBack = object  : SwipeToDeleteCallBack(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val position = viewHolder.adapterPosition
                        taskList.removeAt(position)
                        adapter.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recViewTasks)
    }

    private fun createNotificationChannel(){
        val name = "Notify Task"
        val desc = "Due Time Running Out"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID,name,importance)
        channel.description = desc
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

     fun scheduleNotification(){
        val intent = Intent(context, Notification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         alarmManager.setExactAndAllowWhileIdle(
             AlarmManager.RTC_WAKEUP,
             time,
             pendingIntent
         )

    }

     fun getTime(day:Int, month:Int, year:Int,  hour:Int, minute:Int) :Long{
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day,hour,minute)
        return calendar.timeInMillis - 180000
    }
}