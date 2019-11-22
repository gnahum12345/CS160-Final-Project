package com.example.gabriel.sociala;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewFlipper viewFlipper;
    RecyclerView rv_friends;
    RecyclerView rv_explore;
    TextView btn_friends;
    TextView btn_explore;
    private Context context;
    private float initialX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);

        context = this;

        viewFlipper = findViewById(R.id.view_flipper);
        rv_friends = (RecyclerView)findViewById(R.id.rvFriends);
        rv_explore = (RecyclerView)findViewById(R.id.rvExplore);
        createHomeGrids(rv_friends);
        createHomeGrids(rv_explore);

        btn_explore = (TextView) findViewById(R.id.btn_explore);
        btn_friends = (TextView) findViewById(R.id.btn_friends);
        btn_friends.setOnClickListener(this);
        btn_explore.setOnClickListener(this);

        selectFriend();
    }

    private void createHomeGrids(RecyclerView v) {
        Bitmap[] bitmaps;
        if (v == (RecyclerView)findViewById(R.id.rvFriends)) {
            bitmaps = getFriendsBitmaps();

        } else {
            bitmaps = getExploreBitmaps();
        }
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(bitmaps);
        v.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        v.setAdapter(myRecyclerAdapter);
    }

    private Bitmap[] getFriendsBitmaps() {
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

    private Bitmap[] getExploreBitmaps() {
        Bitmap[] tempBitmaps = new Bitmap[9];
        tempBitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample);
        tempBitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample1);
        tempBitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample2);
        tempBitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample3);
        tempBitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample4);
        tempBitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample5);
        tempBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample6);
        tempBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.image_sample7);
        tempBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.mushroom);

        return tempBitmaps;
    }

    @Override
    public void onClick(View view) {
        Log.d("mayTag", "Click!!!!");
        if (view.getId() == R.id.btn_explore) {
            if (viewFlipper.getDisplayedChild() != 0) {
                selectExplore();
            }
        } else {
            if (viewFlipper.getDisplayedChild() != 1) {
                selectFriend();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        Log.d("myTag", "Touch!!!!!!!!");
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;

            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

//                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_from_left));
//                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_from_left));

                    selectFriend();
                } else {
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

//                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_from_right));
//                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_from_right));

                    selectExplore();
                }
                break;
        }
        return true;
    }

    private void selectFriend() {
        viewFlipper.setDisplayedChild(1);
        btn_friends.setTextColor(getApplication().getColor(R.color.colorPrimary));
        btn_friends.setTextSize(25);
        btn_explore.setTextColor(getApplication().getColor(R.color.colorLightGrey));
        btn_explore.setTextSize(15);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_left));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right));
    }

    private void selectExplore() {
        viewFlipper.setDisplayedChild(0);
        btn_explore.setTextColor(getApplication().getColor(R.color.colorPrimary));
        btn_explore.setTextSize(25);
        btn_friends.setTextColor(getApplication().getColor(R.color.colorLightGrey));
        btn_friends.setTextSize(15);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_left));
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
