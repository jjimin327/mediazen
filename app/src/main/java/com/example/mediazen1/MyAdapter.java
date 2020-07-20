package com.example.mediazen1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<NextResultActivity.item> mDataset;
    Bitmap bitmap;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //그 안의 요소(한 줄에 들어가는 요소)를 찾아가는 ViewHolder는 "클래스"이다.

        public TextView textView_subtitle;
        public TextView textView_content;
        public ImageView imageView_title;

        public MyViewHolder(View v) {
            super(v);

            textView_subtitle = v.findViewById(R.id.textView_subtitle);
            imageView_title = v.findViewById(R.id.imageView_title);
            textView_content = v.findViewById(R.id.textView_content);
        }
    }

    public MyAdapter(ArrayList<NextResultActivity.item> myDataset) {
        //각 줄마다 보여줄 값을 들고 있는 원본 데이터 mDataset 변수

        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //한줄에 이미지파일을 연결하는건 이 부분
        //RecyclerView의 항목화면 연결은 onCreateViewHolder"함수"

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView_subtitle.setText(mDataset.get(position).getName());
        holder.textView_content.setText(mDataset.get(position).getEmail());

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(mDataset.get(position).getUrlToImage());
                    //URL url = new URL("http://tong.visitkorea.or.kr/cms/resource/35/2627135_image2_1.jpg");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            holder.imageView_title.setImageBitmap(bitmap);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
