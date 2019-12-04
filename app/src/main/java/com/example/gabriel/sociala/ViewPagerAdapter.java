package com.example.gabriel.sociala;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import static android.view.LayoutInflater.*;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> selectedImageUri;

    public ViewPagerAdapter(Context context, List<String> selectedImageUri){
        this.context = context;
        this.selectedImageUri = selectedImageUri;
    }

    @Override
    public int getCount() {
        return selectedImageUri.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.photo_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

        String image_path = selectedImageUri.get(position);
        Uri fileUri = Uri.parse(image_path);
        imageView.setImageURI(fileUri);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
