package com.example.sixth.modules.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sixth.MainActivity
import com.example.sixth.databinding.FragmentCarBinding
import com.example.sixth.util.setGone
import com.example.sixth.util.setVisible
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CarsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewBinding by lazy { FragmentCarBinding.inflate(layoutInflater) }

    private val viewModel: CarsViewModel by viewModels()
    private val adapter by lazy {
        CarsAdapter {
            onCarClick(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = viewBinding.root

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }

        viewModel.progress.observe(this@CarsFragment, {
            if (it) {
                showStateLayout()
                viewBinding.stateLayout.showProgress()
            } else {
                viewBinding.stateLayout.hideProgress()
                hideStateLayout()
            }
        })

        viewModel.carsList.observe(this@CarsFragment, {
            if (it.isEmpty()) {
                showStateLayout()
                viewBinding.stateLayout.showMessage(
                    title = "Empty",
                    message = "Car information has not been successfully received.",
                    negativeText = "Close App",
                    onNegativeClick = {
                        (activity as MainActivity).finish()
                    },
                    positiveText = "Try Again",
                    onPositiveClick = {
                        viewModel.request()
                    })
            } else {
                hideStateLayout()
                adapter.updateData(it)
            }
        })

        viewModel.errorMessage.observe(this@CarsFragment, {
            showStateLayout()
            viewBinding.stateLayout.showMessage(
                title = "Error",
                message = it,
                negativeText = "Close App",
                onNegativeClick = {
                    (activity as MainActivity).finish()
                },
                positiveText = "Try Again",
                onPositiveClick = {
                    viewModel.request()
                })
        })

    }

    private fun showStateLayout() {
        viewBinding.recyclerView.setGone()
        viewBinding.stateLayout.setVisible()
    }

    private fun hideStateLayout() {
        viewBinding.recyclerView.setVisible()
        viewBinding.stateLayout.setGone()
    }

    private fun onCarClick(carsItem: CarsItem) {
        Toast.makeText(
            requireContext(),
            carsItem.name + " has been selected",
            Toast.LENGTH_SHORT
        ).show()
    }
}