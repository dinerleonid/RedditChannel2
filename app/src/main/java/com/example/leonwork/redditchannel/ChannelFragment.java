package com.example.leonwork.redditchannel;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ChannelFragment extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener{
    private SearchView searchView;
    private RecyclerView recyclerView = null;
    private ChannelAdapter adapter;
    private TextView tvTitle, loading;
    private static final String BASE_URL = "https://www.reddit.com/r/Android.json";
    private static final String TOPIC_BASE_URL = "https://www.reddit.com";
    private String nextPage = "?after=";
    private ArrayList<Channel> topicsList = new ArrayList<>();
    private long idInternal;
    private ProgressBar progressBar;

    public ChannelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        MainActivity.tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.channel_fragment, container, false);

        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        loading = (TextView) v.findViewById(R.id.loading);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        searchView = (SearchView) v.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        adapter = new ChannelAdapter(getContext(), topicsList);
        recyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_wait)  ;
        progressBar.setVisibility(View.INVISIBLE);
        parseJson(BASE_URL);

        return v;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() >= 3) {
            filterTopics(query);
        } else {
            filterTopics("");

        }
        return true;
    }

    public ArrayList<Channel> filterTopics(String searchWord) {
        ArrayList<Channel> filteredList = new ArrayList<>();
        if (searchWord.equalsIgnoreCase("")){
            filteredList =  this.topicsList;
        } else {
            Channel currentTopic;
            String currentTopicTitle;
            for (int i = 0 ; i < this.topicsList.size() ; i++){
                currentTopic = this.topicsList.get(i);
                currentTopicTitle = currentTopic.getTopicTitle();
                if (currentTopicTitle.toLowerCase().contains(searchWord.toLowerCase())){
                    filteredList.add(currentTopic);

                }
            }
        }
        bindTopicsToRecyclerView(filteredList);
        return filteredList;
    }

    public void bindTopicsToRecyclerView(ArrayList<Channel> filteredTopics){
        adapter = new ChannelAdapter(getContext(), filteredTopics);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 3) {
            filterTopics(newText);
        } else {
            filterTopics("");
        }
        return true;
    }

    private void parseJson (String url){
        progressBar.setVisibility(View.VISIBLE);

        class ParseJson extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String url_builder;
                URL url_item;
                HttpURLConnection connection_item;
                BufferedReader reader_item;
                StringBuilder builder_item = new StringBuilder();
                JSONObject topic;

                try {
                    url_builder = BASE_URL;
                    url_item = new URL(url_builder);
                    connection_item = (HttpURLConnection) url_item.openConnection();
                    if (connection_item.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        noInternet();
                    }
                    reader_item = new BufferedReader(new InputStreamReader(connection_item.getInputStream()));
                    // read first line from stream
                    String line_item = reader_item.readLine();
                    // loop while there is data in the string (successfully read a line of text)
                    while (line_item != null) {
                        builder_item.append(line_item);
                        // try to read next line
                        line_item = reader_item.readLine();
                    }

                    JSONObject found_list = new JSONObject(builder_item.toString());
                    JSONObject data = found_list.getJSONObject("data");
                    JSONArray found_results = data.getJSONArray("children");

                    for (int j = 0; j < found_results.length(); j++) {
                        topic = found_results.getJSONObject(j);
                        String topicTitle = topic.getJSONObject("data").getString("title");
                        String topicThumbnail = topic.getJSONObject("data").getString("thumbnail");
                        String topicUrl = TOPIC_BASE_URL + topic.getJSONObject("data").getString("permalink");
                        String topicID = topic.getJSONObject("data").getString("id");
                        topicsList.add(new Channel(topicTitle, topicThumbnail, topicUrl, idInternal, topicID));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter.setList(topicsList);
                progressBar.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.INVISIBLE);
            }
        }
        ParseJson gj = new ParseJson();
        gj.execute(url);
    }

    public void noInternet(){
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
    }
}
