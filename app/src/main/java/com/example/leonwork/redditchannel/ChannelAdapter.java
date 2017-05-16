package com.example.leonwork.redditchannel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import it.sephiroth.android.library.picasso.Picasso;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelHolder>{
    private Context context;
    private ArrayList<Channel> topics = new ArrayList<>();
    private OnTopicSelection listener;
    private SharedPreferences topicUrl;

    public ChannelAdapter(Context context, ArrayList<Channel> topics) {
        this.context = context;
        this.topics = topics;
        this.listener = (OnTopicSelection) context;
    }

    @Override
    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.topics_list_item, parent, false);
        return new ChannelHolder(v);
    }

    @Override
    public void onBindViewHolder(ChannelAdapter.ChannelHolder holder, final int position) {
        final Channel topic = topics.get(position);
        holder.bind(topics.get(position), context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicUrl = PreferenceManager.getDefaultSharedPreferences(context);
                listener.OnTopicSelected(topic);
                topicUrl.edit().putString("topicUrl", topics.get(position).getTopicUrl()).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }


    public class ChannelHolder extends RecyclerView.ViewHolder{
        private TextView topicTitle;
        ArrayList<Channel> topic = new ArrayList<>();
        private ImageView thumbnail;
        private String thumbnailUrl;

        public ChannelHolder(View topicView) {
            super(topicView);
            this.topic = topics;
            topicTitle = (TextView) topicView.findViewById(R.id.tvTitle);
            thumbnail = (ImageView) topicView.findViewById(R.id.thumbnail);
        }

        public void bind(Channel topic, Context context){
            this.topic = topics;
            topicTitle.setText(topic.getTopicTitle());
            thumbnailUrl = topic.getThumbnailUrl();
            if (thumbnailUrl.equals("") || thumbnailUrl.equals("self") || thumbnailUrl.equals("default")) {
                thumbnail.setImageResource(R.drawable.noimage);
            }else{
                Picasso.with(context).load(thumbnailUrl).into((thumbnail));
            }
        }
    }

    public interface OnTopicSelection{
        void OnTopicSelected(Channel topic);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setList(ArrayList<Channel>topics){
        this.topics = topics;
        notifyDataSetChanged();

    }
}
