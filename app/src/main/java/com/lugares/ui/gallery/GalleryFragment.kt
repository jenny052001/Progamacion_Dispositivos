package com.lugares.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.lugares.databinding.FragmentGalleryBinding
import com.lugares.model.Lugar
import com.lugares.view_model.LugarViewModel

class GalleryFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    //Para interactuar con el mapa de la vista
    private lateinit var googleMap: GoogleMap
    private var mapReady= false

    // Se toma la info de lugares
    private lateinit var lugarViewModel: LugarViewModel



    // Cuando se crea el activity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // se solicita la actualizacion del mapa para presentarlo en la pantalla
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume() // se descargue el mapa
        binding.map.getMapAsync(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        map.let{
            googleMap= it
            mapReady= true
            // Se buscan los logares para dibujarlos en el mapa
            lugarViewModel.getAllData.observe(viewLifecycleOwner){ lugares ->
            updateMap(lugares)
                ubicaGps()

            }

        }

    }

    private fun ubicaGps() {
        val ubicacion: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {  //Si no tengo los permisos entonces los solicito
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                105)
        } else {  //Si se tienen los permisos
                var latitud = 9.97
                var longitud = -84.00
                ubicacion.lastLocation.addOnSuccessListener { location: Location? ->

                if (location!=null) {
                    latitud =location.latitude
                    longitud=location.longitude
                } else {
                    var latitud = 9.97
                    var longitud = -84.00
                }
            }
                    .addOnFailureListener{
                        latitud= 9.97
                        longitud= -84.00
                    }
            val camara = CameraUpdateFactory.newLatLngZoom(
                com.google.android.gms.maps.model.LatLng(latitud,longitud),15f)
            googleMap.animateCamera(camara)

        }
    }


    // Si el mapa esta listo, dibuja las marcas de los lugares
    private fun updateMap(lugares: List<Lugar>) {
        if (mapReady){
            lugares.forEach{
                if (it.latitud?.isFinite()==true&& it.longitud?.isFinite()==true){
                    val marca = com.google.android.gms.maps.model.LatLng(it.latitud, it.longitud)
                    googleMap.addMarker(MarkerOptions().position(marca).title(it.nombre))
                }

            }
        }

    }
}
