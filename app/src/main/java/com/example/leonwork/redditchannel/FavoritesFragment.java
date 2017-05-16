package com.example.leonwork.redditchannel;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FavoritesFragment extends android.support.v4.app.Fragment {

    public static  RecyclerView favoritesRV;
    private FavoritesDBHelper helper;
    private ChannelAdapter adapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState){
        super.onViewCreated( view, savedInstanceState);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorites_fragment, container, false);
        favoritesRV = (RecyclerView) v.findViewById(R.id.favoritesRV);
        favoritesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        helper = new FavoritesDBHelper(getContext());
        adapter = new ChannelAdapter(getContext(), helper.getAllFavorites());
        adapter.notifyItemInserted(helper.getAllFavorites().size()+1);//DataSetChanged();
        favoritesRV.setAdapter(adapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            populate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populate();
    }
    public void populate() {
        adapter.setList( helper.getAllFavorites());
        adapter.notifyDataSetChanged();
        favoritesRV.setAdapter(adapter);
    }

    public void changeFavorite(){
        adapter.setList( helper.getAllFavorites());
        adapter.notifyDataSetChanged();
    }
}
