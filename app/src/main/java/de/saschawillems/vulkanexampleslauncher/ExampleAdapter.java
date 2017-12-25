package de.saschawillems.vulkanexampleslauncher;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ExampleAdapter extends BaseAdapter {

    private ArrayList<VulkanExample> mDataSource;
    private Context mContext;
    private LayoutInflater mInflater;

    public ExampleAdapter(Context context, ArrayList<VulkanExample> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.list_item_example, parent, false);

        TextView titleTextView =(TextView) view.findViewById(R.id.example_list_title);
        TextView descTextView = (TextView) view.findViewById(R.id.example_list_description);
        TextView stateTextView = (TextView) view.findViewById(R.id.example_list_state);

        VulkanExample example = (VulkanExample)getItem(position);
        titleTextView.setText(example.title);
        descTextView.setText(example.description);

        if (example.installed) {
            stateTextView.setText("Installed");
            stateTextView.setTextColor(Color.parseColor("#007000"));
        } else {
            titleTextView.setTextColor(Color.parseColor("#ABABAB"));
            descTextView.setTextColor(Color.parseColor("#ABABAB"));
            stateTextView.setText("Not installed");
            stateTextView.setTextColor(Color.parseColor("#FF0000"));
            view.setBackgroundColor(Color.parseColor("#DBDBDB"));
        }

        return view;
    }
}
