package com.example.to_dolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_dolist.adapters.TaskListAdapter
import com.example.to_dolist.databinding.FragmentCompletedTasksBinding
import com.example.to_dolist.firestore.FirestoreClass
import com.example.to_dolist.models.Task
import com.example.to_dolist.models.User


class CompletedTasksFragment : Fragment() {

    private lateinit var adapter:TaskListAdapter
    private var binding:FragmentCompletedTasksBinding?= null
    private lateinit var userDetails:User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedTasksBinding.inflate(layoutInflater)

        FirestoreClass().getCurrentUserdataFromFragment(this)
        return binding?.root
    }

    fun getCompletedTaskUserDetails(user : User){
        userDetails = user
        FirestoreClass().getCurrentUserCompletedTasksList(this)
    }

    fun showListInRecyclerView(completedTaskList : ArrayList<Task>){
        binding?.recViewCompletedTasks?.layoutManager = LinearLayoutManager(context)
        adapter = TaskListAdapter(requireContext(),completedTaskList)
        binding?.recViewCompletedTasks?.setHasFixedSize(true)

        binding?.recViewCompletedTasks?.adapter = adapter

        adapter.notifyDataSetChanged()

    }

}