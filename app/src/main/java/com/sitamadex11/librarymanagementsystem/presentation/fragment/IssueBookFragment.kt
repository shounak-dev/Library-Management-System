package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentIssueBookBinding

class IssueBookFragment : Fragment() {
    private lateinit var binding: FragmentIssueBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_issue_book, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pageTitle = "ISSUE BOOK"
        binding.buttonTitle = "ISSUE"
    }
}