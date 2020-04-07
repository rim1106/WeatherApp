package com.example.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.weatherapp.Model.City;
import com.example.weatherapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends BaseAdapter implements Filterable {

    private final Context mContext;
    private List<City> mResults;

    public SearchResultsAdapter(Context context, List<City> cities) {
        mContext = context;
        mResults = cities;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public City getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.city_list_item, parent, false);
        }

        City city = getItem(position);
        ((TextView) convertView.findViewById(R.id.city_name)).setText(city.getName());
        ((TextView) convertView.findViewById(R.id.city_position)).setText(city.getCountry() + ", " + city.getRegion());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                /*if (constraint != null) {
                    List<Books> books = findBooks(mContext, constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    filterResults.count = books.size();
                }*/
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    //mResults = (List<Books>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};

        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    /*private List<Book> findBooks(String bookTitle) {
        // GoogleBooksService is a wrapper for the Google Books API
        GoogleBooksService service = new GoogleBooksService (mContext, MAX_RESULTS);
        return service.findBooks(bookTitle);
    }*/
}
