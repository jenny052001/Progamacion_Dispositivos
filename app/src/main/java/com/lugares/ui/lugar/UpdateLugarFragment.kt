package com.lugares.ui.lugar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.view_model.LugarViewModel





class UpdateLugarFragment : Fragment() {
    // se recien los parametros por argumentos.
    private val args by navArgs<UpdateLugarFragmentArgs>()


    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: LugarViewModel


    // objeto para escuchar audio
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this)[LugarViewModel::class.java]

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        // Coloco la info del lugar en los campos del fragmento para modificar.
        binding.run {
            binding.etNombre.setText(args.lugar.nombre)
            binding.etCorreo.setText(args.lugar.correo)
            binding.etTelefono.setText(args.lugar.telefono)
            binding.etWeb.setText(args.lugar.Web)
            binding.tvAltura.text=args.lugar.altura.toString()
            binding.tvLatitud.text=args.lugar.latitud.toString()
            binding.tvLongitud.text=args.lugar.longitud.toString()

            // se trabaja en el tema del audio
            if(args.lugar.rutaAudio?.isNotEmpty()==true){
                // existe una ruta de audio
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(args.lugar.rutaAudio)
                mediaPlayer.prepare()
                binding.btPlay.isEnabled=true //boton para escuchar el audio


            }else{ // no hay ruta deaudio, esta vacía
                binding.btPlay.isEnabled=false //apagar el boton

            }

            // se trabaja en el tema de la imagen
            if(args.lugar.rutaImagen?.isNotEmpty()==true){
                // existe una ruta de una imagen
                Glide.with(requireContext())
                    .load(args.lugar.rutaImagen)
                    .fitCenter()// la imagen queda en el centro
                    .into(binding.imagen)


            }else{ // no hay ruta deaudio, esta vacía
                binding.btPlay.isEnabled=false //apagar el boton

            }

            binding.btPlay.setOnClickListener{mediaPlayer.start() } // hace que suene el audio
            binding.btUpdateLugar.setOnClickListener { UpdateLugar() }

            binding.btEmail.setOnClickListener { escribirCorreo() }
            binding.btPhone.setOnClickListener { realizarLlamada() }
            binding.btWeb.setOnClickListener { verWeb() }




            // Se indica que esta pantalla tiene un menu personalizado
            setHasOptionsMenu(true)

            return binding.root

        }

    }

    private fun verWeb() {
        val recurso = binding.etWeb.text.toString()
        if (recurso.isNotEmpty()) {
           val accion =Intent(Intent.ACTION_VIEW,Uri.parse("hhttp:/$recurso"))
            startActivity(accion) // Carga el app de correo

        } else {
            Toast.makeText(
                requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }

    }

    private fun realizarLlamada(){
            val recurso = binding.etTelefono.text.toString()
            if (recurso.isNotEmpty()) {
                val accion = Intent(Intent.ACTION_CALL) // para hacer la llamada
                accion.data = Uri.parse("tel:$recurso")//nombre o telefono que le di en "recurso"
                //validar si tiene permisos para hacer la llamada
                if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)!=
                    PackageManager.PERMISSION_GRANTED){ // PackageManager saber si el permiso de la llamda se ha otorgado.
               // sino se ha otorgado el permiso de hacer llamadas, se pide el permiso
                    requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105) // aparece un mensaje de permisos para hacer la llamada.

                }else { // si se tiene permisos se hace la llamada

                requireActivity().startActivity(accion) // metodo para levantar la llamada
                }

            } else {
                Toast.makeText(
                    requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
            }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Consulta si se dio click en el icono borrar
        if(item.itemId==R.id.menu_delete){
            deleteLugar()

        }
        return super.onOptionsItemSelected(item)


    }
    private fun deleteLugar(){
        val pantalla = AlertDialog.Builder(requireContext())
        pantalla.setTitle(R.string.delete)

        pantalla.setMessage(getString(R.string.seguroBorrar)+"${args.lugar.nombre}?")

        pantalla.setPositiveButton(getString((R.string.si))) {_,_ ->
            lugarViewModel.deletelugar(args.lugar)
            findNavController().navigate(R.id.action_nav_lugar_to_UpdateLugarFragment)
        }
        pantalla.setNegativeButton(getString(R.string.no)) {_,_ ->}
        pantalla.create().show()

    }
        private fun escribirCorreo() {
        val recurso = binding.etCorreo.text.toString()
        if (recurso.isNotEmpty()) {
            val accion = Intent(Intent.ACTION_SEND)
            accion.type = "message/rfc822" // esto es un correo electronico
            accion.putExtra(Intent.EXTRA_EMAIL, arrayOf(recurso))
            accion.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.msg_saludos) + " " + binding.etCorreo.text)
            accion.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_mensaje_correo))
            startActivity(accion) // Carga el app de correo

        } else {
            Toast.makeText(
                requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }

    }

    private fun UpdateLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (nombre.isNotEmpty()) {
            val lugar = Lugar(args.lugar.id,nombre, correo, telefono, web, 0.0, 0.0, 0.0, "", "")

            lugarViewModel.saveLugar(lugar)

            Toast.makeText(requireContext(),getString(R.string.msg_lugar_update),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_lugar_to_UpdateLugarFragment) // esta funcion nos envia a la pantalla principal


        } else { // mensaje de error
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



