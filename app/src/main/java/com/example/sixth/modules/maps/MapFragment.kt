package com.example.sixth.modules.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sixth.MainActivity
import com.example.sixth.databinding.FragmentMapBinding
import com.example.sixth.util.setGone
import com.example.sixth.util.setVisible
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewBinding by lazy { FragmentMapBinding.inflate(layoutInflater) }
    private val viewModel: MapsViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = viewBinding.root

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.mapView.apply {
            onCreate(savedInstanceState)
            onResume()
            getMapAsync(this@MapFragment)
        }
        viewModel.progress.observe(viewLifecycleOwner, {
            if (it) {
                showStateLayout()
                viewBinding.stateLayout.showProgress()
            } else {
                viewBinding.stateLayout.hideProgress()
                hideStateLayout()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
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

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.also {
            mMap = it
            viewModel.markerOptionsPair.observe(this, { list ->
                if (list.isEmpty()) {
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
                    list.forEach { pair ->
                        mMap.addMarker(pair.first)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pair.second, 12.0f));
                    }
                }
            })

        }
    }

    private fun showStateLayout() {
        viewBinding.mapView.setGone()
        viewBinding.stateLayout.setVisible()
    }

    private fun hideStateLayout() {
        viewBinding.mapView.setVisible()
        viewBinding.stateLayout.setGone()
    }
}