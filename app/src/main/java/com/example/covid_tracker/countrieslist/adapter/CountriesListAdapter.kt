package com.example.covid_tracker.countrieslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid_tracker.countrieslist.models.Country
import com.example.covid_tracker.databinding.CountryItemBinding

class CountriesListAdapter(
    private val countriesList: MutableList<Country> = mutableListOf()
): RecyclerView.Adapter<CountriesListAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemBinding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bind(countriesList[position])

    override fun getItemCount(): Int = countriesList.size

    fun updateCountries(data: List<Country>) {
        countriesList.clear()
        countriesList.addAll(data)
        notifyDataSetChanged()
    }

    fun addCountry(country: Country) {
        if (!countriesList.contains(country)) {
            countriesList.add(country)
            notifyItemInserted(countriesList.size - 1)
        }
    }

    class CountryViewHolder(private val itemBinding: CountryItemBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(country: Country) {
            itemBinding.tvCountryName.text = country.name
        }

    }

}