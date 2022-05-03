package com.example.covid_tracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covid_tracker.db.CountryEntry
import com.example.covid_tracker.databinding.CountryItemBinding
import com.example.covid_tracker.ui.countrylist.CountriesListFragmentDirections
import java.time.format.DateTimeFormatter

class CountriesListAdapter: RecyclerView.Adapter<CountriesListAdapter.CountryViewHolder>() {

    private var countries: ArrayList<CountryEntry> = arrayListOf()
    private var filteredCountries: ArrayList<CountryEntry> = arrayListOf()

    private val countryFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint.toString().trim().lowercase()

            filteredCountries = if (query.isEmpty()) {
                countries
            } else {
                val filteredList = ArrayList<CountryEntry>()

                countries
                    .filter {
                        it.name.lowercase().contains(query)
                    }
                    .forEach {
                        filteredList.add(it)
                    }

                filteredList
            }

            return FilterResults().apply { values = filteredCountries }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredCountries = if (results?.values == null) {
                ArrayList()
            } else {
                results.values as ArrayList<CountryEntry>
            }

            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val itemBinding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bind(filteredCountries[position])

    override fun getItemCount(): Int = filteredCountries.size

    fun getFilter(): Filter {
        return countryFilter
    }

    fun setCountries(countries: ArrayList<CountryEntry>) {
        this.countries = countries
        this.filteredCountries = countries
        notifyDataSetChanged()
    }

    fun getCountryAt(position: Int) = filteredCountries[position]

    class CountryViewHolder(
        private val itemBinding: CountryItemBinding
    ): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(countryEntry: CountryEntry) {
            itemBinding.tvCountryItemName.text = countryEntry.name
            Glide
                .with(itemBinding.civCountryItemFlag)
                .load(countryEntry.flagUrl)
                .centerCrop()
                .into(itemBinding.civCountryItemFlag)

            itemView.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(CountriesListFragmentDirections.actionCountriesListFragmentToCountryDetailsFragment(countryEntry.name))
            }
        }

    }

}