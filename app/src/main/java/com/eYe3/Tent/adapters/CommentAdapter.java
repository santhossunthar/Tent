package com.eYe3.Tent.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.eYe3.Tent.ComProfileActivity;
import com.eYe3.Tent.models.Comment;
import com.eYe3.Tent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    int sum=0;
    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.comment_layout,parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {

        Glide.with(mContext).load(mData.get(position).getUserphoto()).into(holder.profileImage);
        holder.userName.setText(mData.get(position).getUsername());
        holder.com_content.setText(mData.get(position).getContent());

        String time=timestampToString((long)mData.get(position).getTimestamp()) ;
        holder.com_time.setText(covertTimeToText(time));

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proact=new Intent(mContext, ComProfileActivity.class);
                proact.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                proact.putExtra("comKey",mData.get(position).getComKey());
                proact.putExtra("uid",mData.get(position).getUid());
                proact.putExtra("userPhoto",mData.get(position).getUserphoto());
                proact.putExtra("userName",mData.get(position).getUsername());
                proact.putExtra("userStatus",mData.get(position).getUserstatus());
                mContext.startActivity(proact);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: Setup comment profile intent activity
            }
        });

        String postKey=mData.get(position).getPostKey();
        String comKey=mData.get(position).getComKey();

        DatabaseReference comRef= FirebaseDatabase.getInstance().getReference().child("CommentReply")
                .child(postKey).child(comKey);
        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    sum= (int) dataSnapshot.getChildrenCount();
                    String count=Integer.toString(sum);
                    holder.comReplies.setText(count+" replies");
                }else
                {
                    holder.comReplies.setText("0 replies");
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

    private String covertTimeToText(String dataDate) {

        String convertTime = null;
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                if (second == 1) {
                    convertTime = second + " second " + suffix;
                } else {
                    convertTime = second + " seconds " + suffix;
                }
            } else if (minute < 60) {
                if (minute == 1) {
                    convertTime = minute + " minute " + suffix;
                } else {
                    convertTime = minute + " minutes " + suffix;
                }
            } else if (hour < 24) {
                if (hour == 1) {
                    convertTime = hour + " hour " + suffix;
                } else {
                    convertTime = hour + " hours " + suffix;
                }
            } else if (day >= 7) {
                if (day >= 365) {
                    long tempYear = day / 365;
                    if (tempYear == 1) {
                        convertTime = tempYear + " year " + suffix;
                    } else {
                        convertTime = tempYear + " years " + suffix;
                    }
                } else if (day >= 30) {
                    long tempMonth = day / 30;
                    if (tempMonth == 1) {
                        convertTime = (day / 30) + " month " + suffix;
                    } else {
                        convertTime = (day / 30) + " months " + suffix;
                    }
                } else {
                    long tempWeek = day / 7;
                    if (tempWeek == 1) {
                        convertTime = (day / 7) + " week " + suffix;
                    } else {
                        convertTime = (day / 7) + " weeks " + suffix;
                    }
                }
            } else {
                if (day == 1) {
                    convertTime = day + " day " + suffix;
                } else {
                    convertTime = day + " days " + suffix;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("TimeAgo", e.getMessage() + "");
        }
        return convertTime;
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd HH:mm:ss",calendar).toString();
        return date;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView userName,com_content,com_time, comReply, comReplies;

        public CommentViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.com_user_img);
            userName = itemView.findViewById(R.id.com_user_name);
            com_content = itemView.findViewById(R.id.com_content);
            com_time = itemView.findViewById(R.id.com_time);
            comReply=itemView.findViewById(R.id.com_reply_btn);
            comReplies=itemView.findViewById(R.id.com_replies);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo: Setup comment reply intent activity
                }
            });

        }
    }
}
