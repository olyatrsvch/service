package com.example.messagemanager.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.messagemanager.R
import com.example.messagemanager.databinding.FragmentMessagesBinding
import com.example.messagemanager.domain.GetDataWorker
import com.example.messagemanager.domain.Helper
import com.example.messagemanager.domain.MessageRepositoryImpl
import com.example.messagemanager.presentation.adapters.MarginItemDecoration
import com.example.messagemanager.presentation.adapters.MessageAdapter
import com.example.messagemanager.presentation.viewmodels.MessageViewModel
import com.example.messagemanager.presentation.viewmodels.MessageViewModelFactory


class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private val viewModel: MessageViewModel by activityViewModels {
        MessageViewModelFactory(MessageRepositoryImpl())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MessageAdapter { position -> onListItemClick(position) }
        val recyclerView = binding.recyclerView
        recyclerView.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.itemMargin))
        )
        recyclerView.adapter = adapter

        viewModel.messageList.observe(viewLifecycleOwner) { messages ->
            messages?.let { items ->
                adapter.submitList(items)
            }}


        binding.fabUpload.setOnClickListener {
            val uploadDataRequest: WorkRequest = OneTimeWorkRequestBuilder<GetDataWorker>().build()
            activity?.let { WorkManager.getInstance(it.applicationContext).enqueue(uploadDataRequest) }
            activity?.let {
                WorkManager.getInstance(it.applicationContext)
                    .getWorkInfoByIdLiveData(uploadDataRequest.id)
                    .observe(viewLifecycleOwner, Observer { info ->
                        if (info != null && info.state.isFinished) {
                            val result = info.outputData.getString("success")

                            viewModel._messageList.postValue(Helper.deserializeFromJson(result))
                        }
                    })
            }
        }
    }

    private fun onListItemClick(position: Int) {
        Log.d("POSITION", "position: $position")
        val dialog = EditFragment(position)
        dialog.show(requireActivity().supportFragmentManager, "editTitle")
    }
}