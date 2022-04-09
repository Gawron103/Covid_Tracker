package com.example.covid_tracker.countrylistscreen.countrieslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covid_tracker.db.CountryEntry
import com.example.covid_tracker.countrylistscreen.countrieslist.view.CountriesListFragmentDirections
import com.example.covid_tracker.databinding.CountryItemBinding

class CountriesListAdapter: RecyclerView.Adapter<CountriesListAdapter.CountryViewHolder>() {

    private var countries: List<CountryEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemBinding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bind(countries[position])

    override fun getItemCount(): Int = countries.size

    fun setCountries(countries: List<CountryEntry>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    fun getCountryAt(position: Int) = countries[position]

    class CountryViewHolder(
        private val itemBinding: CountryItemBinding
    ): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(countryEntry: CountryEntry) {
            itemBinding.tvCountryItemName.text = countryEntry.name
            Glide
                .with(itemBinding.ivCountryItemFlag)
                .load(countryEntry.flagUrl)
                .centerCrop()
                .into(itemBinding.ivCountryItemFlag)

            itemView.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(CountriesListFragmentDirections.actionCountriesListFragmentToCountryDetailsFragment(countryEntry.name))
            }
        }

    }

}