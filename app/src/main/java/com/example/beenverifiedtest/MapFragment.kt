package com.example.beenverifiedtest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MapFragment : Fragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private lateinit var map: GoogleMap
    private lateinit var searchView: SearchView
    private var PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_maps, container, false)
        mapView =
            view.findViewById<View>(R.id.map) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        getSearchViewText()
    }

    private fun checkUserLocationPermission () {
        if (checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
        }else{
            getUserLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkUserLocationPermission()
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation(){
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager?

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                val lat = location!!.latitude
                val lon = location.longitude

                val latlng: LatLng? = LatLng(lat, lon)
                map.addMarker(MarkerOptions().position(latlng!!).title("Your Location"))
                val cu = CameraUpdateFactory.newLatLngZoom(latlng, 15F)
                map.animateCamera(cu)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED){
                    getUserLocation()
                    map.isMyLocationEnabled
                }
            }else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                //todo: Add dialog and ask for permissions again
                Toast.makeText(context, "Ocupo permisos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getSearchViewText () {
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                findPlacesInMap(query)
                return false
            }

        })
    }

    private fun findPlacesInMap(name: String){
        var addressList: List<Address>? = null
        val geocoder = Geocoder(activity)

        try {
            addressList = geocoder.getFromLocationName(name, 5)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if(addressList?.isEmpty()!!){
            //todo: Implement places not found
        }else{
            map.clear()
            for(i in addressList.indices){
                val address = addressList[0]
                val latlng: LatLng? = LatLng(address.latitude, address.longitude)
                map.addMarker(MarkerOptions().position(latlng!!).title(name))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 99F))
            }
        }

    }

    override fun onResume() {
        mapView!!.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView!!.onDestroy()
    }

}