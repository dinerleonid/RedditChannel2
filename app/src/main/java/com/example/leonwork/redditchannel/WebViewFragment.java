package com.example.leonwork.redditchannel;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class WebViewFragment extends Fragment implements View.OnClickListener{
    OnHeadlineSelectedListener mCallback;
    private WebView webView;
    private Button btnAddToFav;
    private SharedPreferences topicUrl;
    private String url;
    private FavoritesDBHelper helper;
    private Channel topic;
    private  boolean isTopicExist;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        helper = new FavoritesDBHelper(getContext());
        if(getArguments() !=  null) {
            topic = getArguments().getParcelable("topic");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_view_fragment, container, false);
        topicUrl = PreferenceManager.getDefaultSharedPreferences(getContext());
        btnAddToFav = (Button) v.findViewById(R.id.btnAddToFav);
        btnAddToFav.setOnClickListener(this);
        // Reference "http://stackoverflow.com/questions/31159149/using-webview-in-fragment"
        webView = (WebView) v.findViewById(R.id.webView1);
        url = topicUrl.getString("topicUrl", "https://google.com");
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        checkTopicInFavorites();
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                addRemoveTopicToFavorites();
                break;
        }
    }

    private void checkTopicInFavorites(){
        isTopicExist = isFavoriteIsExist(topic.getTopicID());

        if (isTopicExist){
            btnAddToFav.setBackgroundResource(R.drawable.removefavorite);
        } else {
            btnAddToFav.setBackgroundResource(R.drawable.addtofav);
        }
    }

    private boolean isFavoriteIsExist(String favoriteId){
        ArrayList<Channel> allFavorites= helper.getAllFavorites();
        boolean isExist = false;
        for (int i=0; i < allFavorites.size(); i++){
            if (allFavorites.get(i).getTopicID().equals(favoriteId)){
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private void addRemoveTopicToFavorites(){
        if (!isTopicExist){
            helper.insertFavorite(topic);
            btnAddToFav.setBackgroundResource(R.drawable.removefavorite);
            mCallback.changeInFavorite();
            isTopicExist = true;
            Toast.makeText(getActivity(), topic.getTopicTitle() + " added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            isTopicExist = false;
            btnAddToFav.setBackgroundResource(R.drawable.addtofav);
            long internalID = getInternalId(topic.getTopicID());
            helper.deleteFavorite(internalID);
            mCallback.changeInFavorite();
            Toast.makeText(getActivity(), topic.getTopicTitle() + " removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private long getInternalId(String favoriteId){
        ArrayList<Channel> allFavorites= helper.getAllFavorites();
        long internalID = 0;
        for (int i=0; i < allFavorites.size(); i++){
            if (allFavorites.get(i).getTopicID().equals(favoriteId)){
                internalID = allFavorites.get(i).getIdInternal();
                break;
            }
        }
        return internalID;
    }

    public interface OnHeadlineSelectedListener {
        void changeInFavorite();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        MainActivity.tabLayout.setVisibility(View.VISIBLE);
    }
}
