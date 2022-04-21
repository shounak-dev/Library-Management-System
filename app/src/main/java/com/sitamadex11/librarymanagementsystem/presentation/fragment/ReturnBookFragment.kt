package com.sitamadex11.librarymanagementsystem.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.FragmentIssueBookBinding

class ReturnBookFragment : Fragment() {
    private lateinit var binding: FragmentIssueBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issue_book, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pageTitle = "RETURN BOOK"
        binding.buttonTitle = "RETURN"
    }
}