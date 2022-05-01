package com.example.to_dolist.firestore

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.to_dolist.CompletedTasksFragment
import com.example.to_dolist.activities.SignInActivity
import com.example.to_dolist.activities.SignUpActivity
import com.example.to_dolist.activities.SplashActivity
import com.example.to_dolist.ToDoListsFragment
import com.example.to_dolist.models.Task
import com.example.to_dolist.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

open class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun storeSignedUpUserDataInFirestore(activity: Activity, userInfo: User) {
        mFirestore.collection("Users")
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is SignUpActivity -> {
                        activity.userSuccessfullySignedUp()
                    }
                }
            }
    }

    fun getCurrentUserData(activity: Activity) {
        mFirestore.collection("Users")
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)

                when (activity) {


                    is SplashActivity -> {
                        activity.sendUserNameToMainActivityFromSplash(loggedInUser)
                    }
                    is SignInActivity -> {
                        activity.sendUserNameToMainActivityFromSignIn(loggedInUser)
                    }
                }

            }
    }

    fun getCurrentUserdataFromFragment(fragment: Fragment) {
        mFirestore.collection("Users")
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                when (fragment) {

                    is CompletedTasksFragment -> {
                        if(loggedInUser != null) {
                            fragment.getCompletedTaskUserDetails(loggedInUser)
                        }
                    }
                    is ToDoListsFragment -> {
                        if (loggedInUser != null) {
                            fragment.getUserDetails(loggedInUser)
                        }
                    }

                }
            }
    }

    fun getCurrentUserId(): String {
        if (FirebaseAuth.getInstance().currentUser != null) {
            return FirebaseAuth.getInstance().currentUser!!.uid
        } else
            return ""
    }


     fun storeTaskInDatabase(fragment: Fragment,task:Task,user:User){
         mFirestore.collection("Task")
             .document()
             .set(task, SetOptions.merge())
             .addOnSuccessListener {
                 when(fragment) {
                     is ToDoListsFragment ->  {
                         fragment.scheduleNotification()
                 }
                 }
             }
             .addOnFailureListener {
                 Toast.makeText(fragment.context,it.message,Toast.LENGTH_LONG).show()
             }

     }
    fun getCurrentUsersTask(fragment: Fragment){
        mFirestore.collection("Task")
            .whereEqualTo("id",getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->
                val taskList = ArrayList<Task>()
                for(i in document.documents){
                    val task = i.toObject(Task::class.java)!!
                    taskList.add(task)
                }
                when(fragment){
                    is ToDoListsFragment ->
                        fragment.showInRecyclerView(taskList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(fragment.context,it.message,Toast.LENGTH_LONG).show()
            }
    }

    fun addCompletedTaskList(completedTasks:Task){
        mFirestore.collection("CompletedTasks")
            .document()
            .set(completedTasks, SetOptions.merge())
            .addOnSuccessListener {
              //  Toast.makeText(fragment.context,"Good work",Toast.LENGTH_LONG).show()
            }
    }

    fun getCurrentUserCompletedTasksList(fragment: Fragment){
        mFirestore.collection("CompletedTasks")
            .whereEqualTo("id",getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->
                val completedTaskList = ArrayList<Task>()
                for(i in document.documents){
                    val completedTasks = i.toObject(Task::class.java)!!
                    completedTaskList.add(completedTasks)
                }
                when(fragment){
                    is CompletedTasksFragment -> {
                        fragment.showListInRecyclerView(completedTaskList)
                    }
                }


            }
    }
    fun removeFromTaskList(task:Task){
        val taskQuery = mFirestore
            .collection("Task")
            .whereEqualTo("name",task.name)
            .whereEqualTo("date",task.date)
            .whereEqualTo("time",task.time)
            .get()
            .addOnSuccessListener {
                document ->
                for(i in document.documents){
                    mFirestore.collection("Task").document(i.id).delete()
                }
            }
    }
}







