package com.example.covid_tracker.countrieslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid_tracker.databinding.CountryItemBinding

class CountriesListAdapter(
    // ToDo: change string to model class when created
    private val countriesList: MutableList<String> = mutableListOf()
): RecyclerView.Adapter<CountriesListAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemBinding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bind(countriesList[position])

    override fun getItemCount(): Int = countriesList.size

    class CountryViewHolder(private val itemBinding: CountryItemBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(country: String) {

        }

    }

}