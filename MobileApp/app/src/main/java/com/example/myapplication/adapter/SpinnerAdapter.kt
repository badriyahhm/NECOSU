package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class SpinnerAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: List<String>
) : ArrayAdapter<String>(context, resource, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.location_layout, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)

        // Set a different background color for the dropdown items
        textView.setBackgroundColor(ContextCompat.getColor(context, R.color.periwinkle))

        return view
    }
}
