package com.can.anyma.apimanagementapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.can.anyma.apimanagementapps.R;
import com.can.anyma.apimanagementapps.model.Sorun;

import java.util.List;

/**
 * Created by anyma on 16.05.2017.
 */

public class SorunAdapter extends ArrayAdapter<Sorun> {

    List<Sorun> contactList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public SorunAdapter(Context context, List<Sorun> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        contactList = objects;
    }

    @Override
    public Sorun getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Sorun item = getItem(position);

        vh.textViewName.setText(item.getYetkili());
        vh.textViewEmail.setText(item.getBolge());
        //Picasso.with(context).load(item.getProfilePic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        //public final ImageView imageView;
        public final TextView textViewName;
        public final TextView textViewEmail;

        private ViewHolder(RelativeLayout rootView, /*ImageView imageView,*/ TextView textViewName, TextView textViewEmail) {
            this.rootView = rootView;
            //this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewEmail = textViewEmail;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            //ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewEmail = (TextView) rootView.findViewById(R.id.textViewEmail);
            return new ViewHolder(rootView, /*imageView,*/ textViewName, textViewEmail);
        }
    }
}