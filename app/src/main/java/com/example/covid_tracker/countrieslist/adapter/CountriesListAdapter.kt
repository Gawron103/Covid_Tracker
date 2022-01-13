package com.example.covid_tracker.countrieslist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covid_tracker.countrieslist.models.Country
import com.example.covid_tracker.countrieslist.view.CountriesListFragmentDirections
import com.example.covid_tracker.databinding.CountryItemBinding

class CountriesListAdapter(
    private val countriesList: MutableList<Country> = mutableListOf(),
    private val countryDeleteCallback: (country:Country) -> Unit
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

    fun onSwipeItem(position: Int) {
        countryDeleteCallback(countriesList[position])
        countriesList.removeAt(position)
        notifyItemRemoved(position)
    }

    class CountryViewHolder(
        private val itemBinding: CountryItemBinding
    ): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(country: Country) {
            itemBinding.tvCountryName.text = country.name
            Glide
                .with(itemBinding.ivCountryFlag)
                .load("https://countryflagsapi.com/png/${country.name}")
                .centerCrop()
                .into(itemBinding.ivCountryFlag)

            itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(CountriesListFragmentDirections.actionCountriesListFragmentToCountryDetails(country.name))
            }
        }

    }

}