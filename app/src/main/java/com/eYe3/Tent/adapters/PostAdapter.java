package com.eYe3.Tent.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eYe3.Tent.ClickPostActivity;
import com.eYe3.Tent.models.Post;
import com.eYe3.Tent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context mContext;
    List<Post> mData ;
    int sum=0;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.activity_post,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.userName.setText(mData.get(position).getUsername());
        holder.postDesc.setText(mData.get(position).getDescription());
        Glide.with(mContext).load(mData.get(position).getUserphoto()).into(holder.imgPostProfile);

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String dateString = formatter.format(new Date(Long.parseLong(mData.get(position).getTimestamp().toString())));
        holder.postTime.setText(dateString);

        SimpleDateFormat xformatter = new SimpleDateFormat("dd MMM yyyy");
        String dateStringX = xformatter.format(new Date(Long.parseLong(mData.get(position).getTimestamp().toString())));
        holder.postDate.setText(dateStringX);

        String postKey=mData.get(position).getPostKey();
        DatabaseReference comRef= FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(postKey);

        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    sum= (int) dataSnapshot.getChildrenCount();
                    String count=Integer.toString(sum);
                    holder.comCount.setText(count+" Comments");
                }else
                {
                    holder.comCount.setText("0 Comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postDesc, comCount;
        TextView userName, postDate, postTime;
        ImageView imgPostProfile;

        public MyViewHolder(View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.post_user_name);
            postDesc = itemView.findViewById(R.id.post_desc);
            postTime=itemView.findViewById(R.id.post_time);
            postDate=itemView.findViewById(R.id.post_date);
            imgPostProfile = itemView.findViewById(R.id.post_user_img);
            comCount=itemView.findViewById(R.id.post_com_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext,ClickPostActivity.class);
                    int position = getAdapterPosition();
                    postDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    postDetailActivity.putExtra("description",mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto",mData.get(position).getUserphoto());
                    postDetailActivity.putExtra("userName",mData.get(position).getUsername());
                    postDetailActivity.putExtra("userId",mData.get(position).getUserId());
                    postDetailActivity.putExtra("userStatus",mData.get(position).getUserstatus());

                    long timestamp  = (long) mData.get(position).getTimestamp();
                    postDetailActivity.putExtra("postTime",timestamp) ;

                    mContext.startActivity(postDetailActivity);
                }
            });
        }
    }
}
