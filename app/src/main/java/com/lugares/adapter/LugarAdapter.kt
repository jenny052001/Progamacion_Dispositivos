package com.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar
import com.lugares.ui.lugar.LugarFragmentDirections

class LugarAdapter: RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

// una lista paa gestionar la información de los lugares

    private var lista = emptyList<Lugar>()


    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        : RecyclerView.ViewHolder( itemBinding.root){

            fun dibuja(lugar:Lugar){
                itemBinding.tvNombre.text= lugar.nombre
                itemBinding.tvCorreo.text= lugar.correo
                itemBinding.tvTelefono.text= lugar.telefono
                itemBinding.tvWeb.text= lugar.Web

                //mostrar la imagen del lugar en el card
                Glide.with(itemBinding.root.context)
                    .load(lugar.rutaImagen)
                    .circleCrop() // hace la imagen redonda
                    .into(itemBinding.imagen)

                // se activa el click para modficiar un luagr
                itemBinding.vistaFila.setOnClickListener{
                    val accion= LugarFragmentDirections
                        .actionNavLugarToUpdateLugarFragment(lugar)
                    itemView.findNavController().navigate(accion)
                }


            }


    }
// Aca se cea una "cajita" del reciclador, solo la estructura
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder { // cuando se crea una cajita en memoria
       val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context), // poder cosntruir la cajita
        parent, false)
        return  LugarViewHolder(itemBinding) // devuelve la cajita con la información
    }

    // Acá se va a solicitar "dibujar" una cajita, según el elemento de lista.
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {

    val lugar= lista[position]
        holder.dibuja(lugar)

    }

    override fun getItemCount(): Int {

        return lista.size
    }
    fun setData(lugares:List<Lugar>){
        lista= lugares
        notifyDataSetChanged() 
    }
}