package com.example.catagentdeployer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.example.catagentdeployer.ui.theme.CatAgentDeployerTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import androidx.core.graphics.createBitmap

class MainActivity : ComponentActivity() {
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var locationPermissionsGranted by remember {
                mutableStateOf(areLocationPermissionsGranted())
            }

            var shouldShowLocationPermissionRationale by remember {
                mutableStateOf(false)
            }

            var currentUserLocation by remember {
                mutableStateOf(LatLng(0.0, 0.0))
            }
            
            fun getUserLocation() {
                val cancellationTokenSource = CancellationTokenSource()
                lifecycleScope.launch @SuppressLint("MissingPermission") {
                    suspendCancellableCoroutine { continuation ->
                        fusedLocationClient.getCurrentLocation(
                            PRIORITY_HIGH_ACCURACY,
                            cancellationTokenSource.token
                        ).addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                currentUserLocation = LatLng(location.latitude, location.longitude)
                            }
                        }
                        continuation.invokeOnCancellation {
                            cancellationTokenSource.cancel()
                        }
                    }
                }
            }

            val requestLocationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    locationPermissionsGranted = permissions.values.all { it }
                    if (locationPermissionsGranted) {
                        getUserLocation()
                    } else {
                        shouldShowLocationPermissionRationale =
                            shouldShowLocationPermissionRationale()
                    }
                })

            fun requestLocationPermission() {
                requestLocationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }

            CatAgentDeployerTheme {

                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) { innerPadding ->
                    val cameraPositionState = rememberSaveable(
                        currentUserLocation,
                        saver = CameraPositionState.Saver
                    ) {
                        CameraPositionState(
                            position = CameraPosition
                                .fromLatLngZoom(currentUserLocation, 15f)
                        )
                    }
                    val markerState = rememberSaveable(
                        currentUserLocation,
                        saver = MarkerState.Saver
                    ) {
                        MarkerState(position = currentUserLocation)
                    }
                    // Quick permission state helpers for camera and notifications
                    var cameraPermissionGranted by rememberSaveable {
                        mutableStateOf(isCameraPermissionGranted())
                    }
                    var notificationPermissionGranted by rememberSaveable {
                        mutableStateOf(isNotificationPermissionGranted())
                    }

                    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            cameraPermissionGranted = granted
                        }
                    )

                    val requestNotificationPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            notificationPermissionGranted = granted
                        }
                    )

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            onMapClick = { latLng ->
                                markerState.position = latLng
                            },
                            // Enable zoom controls and my location button
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = true,
                                myLocationButtonEnabled = true,
                                compassEnabled = true,
                                mapToolbarEnabled = true
                            ),
                            // Enable showing user's location
                            properties = MapProperties(
                                isMyLocationEnabled = locationPermissionsGranted,
                                mapType = MapType.NORMAL
                            )
                        ) {
                            if (markerState.position.latitude != 0.0 &&
                                markerState.position.longitude != 0.0
                            ) {
                                val icon by remember {
                                    mutableStateOf(
                                        getBitmapDescriptorFromVector(
                                            R.drawable.target_icon
                                        )
                                    )
                                }
                                Marker(
                                    state = markerState,
                                    title = "Deploy Here",
                                    icon = icon
                                )
                            }
                        }

                        // Overlay three styled icon buttons in the top-right corner of the map
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                        ) {
                            // Location button (top)
                            Button(
                                onClick = {
                                    if (!locationPermissionsGranted) {
                                        requestLocationPermission()
                                    } else {
                                        // already granted: fetch location
                                        getUserLocation()
                                    }
                                },
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Location",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Location", color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.padding(start = 8.dp))
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Notification button (middle)
                            Button(
                                onClick = {
                                    if (!isNotificationPermissionGranted()) {
                                        requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        notificationPermissionGranted = true
                                    }
                                },
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Notification",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Notification", color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.padding(start = 8.dp))
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Camera button (bottom)
                            Button(
                                onClick = {
                                    if (!isCameraPermissionGranted()) {
                                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    } else {
                                        cameraPermissionGranted = true
                                    }
                                },
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CameraAlt,
                                    contentDescription = "Camera",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Camera", color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (locationPermissionsGranted) {
                                getUserLocation()
                            } else {
                                shouldShowLocationPermissionRationale =
                                    shouldShowLocationPermissionRationale()
                            }
                            if (!locationPermissionsGranted &&
                                !shouldShowLocationPermissionRationale
                            ) {
                                requestLocationPermission()
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Text(text = "Get location (${currentUserLocation})")
                    }

                    val scope = rememberCoroutineScope()

                    LaunchedEffect(
                        key1 = shouldShowLocationPermissionRationale,
                        block = {
                            if (shouldShowLocationPermissionRationale) {
                                scope.launch {
                                    val userAction = snackbarHostState.showSnackbar(
                                        message = "The app will not work without knowing your precise location",
                                        actionLabel = "Approve",
                                        duration = SnackbarDuration.Indefinite,
                                        withDismissAction = true
                                    )
                                    when (userAction) {
                                        SnackbarResult.ActionPerformed -> {
                                            requestLocationPermission()
                                        }

                                        SnackbarResult.Dismissed -> {}
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun shouldShowLocationPermissionRationale() =
        shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) || shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun areLocationPermissionsGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun isCameraPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun isNotificationPermissionGranted(): Boolean {
        // POST_NOTIFICATIONS exists starting API 33 (TIRAMISU)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Before API 33, notification permission is granted at install time
            true
        }
    }

    private fun getBitmapDescriptorFromVector(
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? = ContextCompat.getDrawable(
        this,
        vectorDrawableResourceId
    )?.let { vectorDrawable ->
        vectorDrawable.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val drawableWithTint = DrawableCompat.wrap(vectorDrawable)
        DrawableCompat.setTint(drawableWithTint, Color.DKGRAY)
        val bitmap = createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        drawableWithTint.draw(canvas)
        BitmapDescriptorFactory.fromBitmap(bitmap)
            .also { bitmap.recycle() }
    }
}
