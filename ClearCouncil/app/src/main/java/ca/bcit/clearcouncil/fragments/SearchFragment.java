package ca.bcit.clearcouncil.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Objects;

import ca.bcit.clearcouncil.R;
import ca.bcit.clearcouncil.Vote;
import ca.bcit.clearcouncil.VoteDetailActivity;
import ca.bcit.clearcouncil.utilities.EndlessRecyclerViewScrollListener;
import ca.bcit.clearcouncil.utilities.JSONParseUtils;
import ca.bcit.clearcouncil.utilities.NetworkUtils;
import ca.bcit.clearcouncil.adapters.VoteAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * SEARCH PAGE
 */
public class SearchFragment extends Fragment {
    private static RecyclerView listVotes;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private OkHttpClient client;
    private VoteAdapter voteAdapter;
    private String current_query = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View searchView = inflater.inflate(R.layout.fragment_search, null);

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setColorFilter(getResources().getColor(R.color.colorNavText),
                android.graphics.PorterDuff.Mode.SRC_IN);

        ImageView searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear_white_24dp);

        listVotes = searchView.findViewById(R.id.list_search_votes);
        layoutManager = new LinearLayoutManager(getActivity());
        listVotes.setLayoutManager(layoutManager);

        voteAdapter = new VoteAdapter(Vote.votes);
        listVotes.setAdapter(voteAdapter);

        // Reset adapter
        Vote.votes.clear();
        voteAdapter.updateData(Vote.votes);

        voteAdapter.setOnItemClickListener(new VoteAdapter.ClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                Intent intent = new Intent(getActivity(), VoteDetailActivity.class);
                intent.putExtra(VoteDetailActivity.EXTRA_VOTE_ID, pos);
                startActivity(intent);
            }
        });

        client = new OkHttpClient();

        SearchView searchMenu = searchView.findViewById(R.id.search_view);
        searchMenu.setIconifiedByDefault(false);

        searchMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                current_query = query;
                Vote.votes.clear();
                voteAdapter.updateData(Vote.votes);
                scrollListener.resetState();

                Request request = new Request.Builder().url(NetworkUtils.searchVoteUrl(current_query, 0)).build();
                client.newCall(request).enqueue(new SearchFragment.SearchResponseCallback());
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextPage(page);
            }
        };
        listVotes.addOnScrollListener(scrollListener);

        return searchView;
    }

    private void loadNextPage(int offset) {
        if (!current_query.equals("")) {
            Request request = new Request.Builder().url(NetworkUtils.searchVoteUrl(current_query, offset)).build();
            client.newCall(request).enqueue(new SearchFragment.SearchResponseCallback());
        }
    }

    private class SearchResponseCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                assert response.body() != null;
                final String myResponse = response.body().string();
                if (getActivity() == null)
                    return;
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Vote.updateVotes(JSONParseUtils.parseAllVotes(myResponse));
                        voteAdapter.updateData(Vote.votes);
                    }
                });
            }
        }
    }
}
