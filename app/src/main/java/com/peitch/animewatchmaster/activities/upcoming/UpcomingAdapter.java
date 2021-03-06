package com.peitch.animewatchmaster.activities.upcoming;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.SeasonModel;
import com.peitch.animewatchmaster.model.UpcomingAnime;
import com.peitch.animewatchmaster.utils.Utils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * Created by abraham on 3/7/2016.
 */
public class UpcomingAdapter extends ArrayAdapter<SeasonModel> {

    public UpcomingAdapter(Context context,List<SeasonModel> models) {
        super(context,0,models);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SeasonModel model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_upcoming_row, parent, false);
        }

        AutofitTextView title = (AutofitTextView)convertView.findViewById(R.id.title);
        title.setText(model.getTitle());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);

        if(model.getImgurl() != null && !model.getImgurl().trim().isEmpty() && !model.getImgurl().equals("na")&& Utils.imgflag) {
            Picasso.with(getContext())
                    .load(model.getImgurl())
                    .fit()
                    .into(imageView);
        }  else {
            Picasso.with(getContext())
                    .load("http://www.anime-planet.com/inc/img/blank_main.jpg")
                    .fit()
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpcomingAnime anime = DBHelper.getInstance(getContext()).getUpcomingAnime(DBHelper.getInstance(getContext()).getAPAnimeID(model.getTitle()));

                Intent intent = new Intent(getContext(), UpcomingInfo.class);
                intent.putExtra("anime", anime);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });

        TextView ratings = (TextView)convertView.findViewById(R.id.rating);
        ImageView starimage = (ImageView)convertView.findViewById(R.id.ratingstar);
        if(model.getRating() > 0) {
            ratings.setText(""+model.getRating());
            starimage.setVisibility(View.VISIBLE);
        } else {
            ratings.setText("");
            starimage.setVisibility(View.GONE);
        }

        TextView type = (TextView)convertView.findViewById(R.id.type);
        type.setText(model.getAnimetype());


        return convertView;
    }

}
