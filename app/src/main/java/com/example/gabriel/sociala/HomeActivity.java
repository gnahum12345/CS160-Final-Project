package com.example.gabriel.sociala;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);
        rvMain = (RecyclerView)findViewById(R.id.rvMain);
        Bitmap[] bitmaps = getBitmaps();
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(bitmaps);
        rvMain.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvMain.setAdapter(myRecyclerAdapter);
    }

    private Bitmap[] getBitmaps() {
       Bitmap[] tempBitmaps = new Bitmap[9];
       tempBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample);
       tempBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample1);
       tempBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample2);
       tempBitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample3);
       tempBitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample4);
       tempBitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample5);
       tempBitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample6);
       tempBitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample7);
       tempBitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.mushroom);

       return tempBitmaps;
    }

    private class MyRecyclerAdapter extends RecyclerView.Adapter<GridHolder>{

        Bitmap[] bitmaps;

        public MyRecyclerAdapter(Bitmap[] bitmaps) {
            this.bitmaps = bitmaps;
        }

        @NonNull
        @Override
        public GridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.home_grid_rv, viewGroup, false);
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridHolder gridHolder, int i) {
            gridHolder.imageView.setImageBitmap(bitmaps[i]);
            gridHolder.textView.setText("User " + i + ": Hello Please help me make a caption on this pic, I want to post it on my Ins");
        }

        @Override
        public int getItemCount() {
            return bitmaps.length;
        }
    }

    private class GridHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public GridHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
            textView = itemView.findViewById(R.id.tvCaption);
        }
    }
}
