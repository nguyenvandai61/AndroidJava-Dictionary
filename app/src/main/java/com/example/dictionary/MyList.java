package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyList extends ArrayAdapter<String> implements Filterable {
    Context context;
    DatabaseAccess.Mode mode;
    List<String> arrayList;

    List<String> tempItems;
    List<String> suggestions;

    public MyList(@NonNull Context context, List arrayList, DatabaseAccess.Mode mode) {
        super(context, R.layout.list_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;

        tempItems = arrayList;
        suggestions = new ArrayList<>();

        this.mode = mode;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        final TextView tvLiWord = (TextView) rowView.findViewById(R.id.tv_item_word);
//        TextView tvLiContent = (TextView) rowView.findViewById(R.id.tv_item_content);

        tvLiWord.setText(arrayList.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WordView.class);
                String word = tvLiWord.getText().toString();
                intent.putExtra("word", word);

                DatabaseAccess db = DatabaseAccess.getInstance(context, mode);
                db.open();
                String wordHtml = db.getMean(word);
                db.close();


                intent.putExtra("contentHtml", wordHtml);
                context.startActivity(intent);
            }
        });

        return rowView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (String word : tempItems) {
                    if (word.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(word);
                        System.out.println(word);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<String> filterList = (ArrayList<String>) filterResults.values;
            if (filterList != null && filterResults.count > 0) {
                clear();
                for (String word : filterList) {
                    add(word);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
