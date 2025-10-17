package com.example.aplicacion_iot.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacion_iot.R
import com.example.aplicacion_iot.databinding.ActivityMainBinding
import com.example.aplicacion_iot.presentation.ui.adapters.EventAdapter
import com.example.aplicacion_iot.presentation.viewmodel.SensorViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SensorViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter
    private var selectedDurationSeconds: Int = 10 // Default duration

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i(TAG, "Permiso de notificaciones CONCEDIDO.")
            showSnackbar(R.string.permission_granted_snackbar)
        } else {
            Log.w(TAG, "Permiso de notificaciones DENEGADO.")
            showSnackbar(R.string.permission_denied_snackbar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Set initial state for Chips
        binding.chipGroupSoilStatus.check(R.id.chipDry)
        binding.chipGroupDuration.check(R.id.chip10s) // Default selection

        askForNotificationPermission()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter()
        binding.rvEvents.apply {
            val linearLayoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager = linearLayoutManager
            adapter = eventAdapter

            adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (positionStart == 0) {
                        binding.rvEvents.scrollToPosition(0)
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.pumpState.observe(this) { command ->
            val isPumpOn = command == "ENCENDER"
            // Update both cards based on pump status
            binding.cardStatus.isActivated = isPumpOn
            binding.cardControl.isActivated = isPumpOn

            val colorRes = when (command) {
                "ENCENDER" -> android.R.color.holo_blue_bright
                "APAGAR" -> android.R.color.darker_gray
                else -> android.R.color.holo_red_light
            }
            val color = ContextCompat.getColor(this, colorRes)
            binding.ivPumpStatus.setColorFilter(color)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { events ->
                    eventAdapter.submitList(events)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    // Disable/Enable controls while loading
                    binding.manualControlsLayout.alpha = if (isLoading) 0.5f else 1.0f
                    binding.btnStartIrrigation.isEnabled = !isLoading
                    binding.chipGroupDuration.isEnabled = !isLoading
                }
            }
        }
    }

    private fun setupListeners() {
        binding.chipGroupSoilStatus.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val isWet = checkedIds[0] == R.id.chipWet
                viewModel.onStatusChanged(isWet)
            }
        }

        binding.chipGroupDuration.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                selectedDurationSeconds = when (checkedIds[0]) {
                    R.id.chip30s -> 30
                    R.id.chip60s -> 60
                    else -> 10
                }
            }
        }

        binding.btnStartIrrigation.setOnClickListener {
            viewModel.startManualIrrigation(selectedDurationSeconds)
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun askForNotificationPermission() {
        // ... (code remains the same)
    }

    private fun showSnackbar(stringResId: Int) {
        // ... (code remains the same)
    }
}
