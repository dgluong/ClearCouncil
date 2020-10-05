package ca.bcit.clearcouncil.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

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
 * THE DASHBOARD FRAGMENT -- HOLDS ALL THE CARDS OF VOTES
 */
public class DashboardFragment extends Fragment {
    private static RecyclerView listVotes;
    private LinearLayoutManager layoutManager;
    private OkHttpClient client;
    private VoteAdapter voteAdapter;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View dashboardView = inflater.inflate(R.layout.fragment_dashboard, null);


        listVotes = dashboardView.findViewById(R.id.list_votes);
        layoutManager = new LinearLayoutManager(getActivity());
        listVotes.setLayoutManager(layoutManager);

        voteAdapter = new VoteAdapter(Vote.votes);
        listVotes.setAdapter(voteAdapter);

        voteAdapter.setOnItemClickListener(new VoteAdapter.ClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                Intent intent = new Intent(getActivity(), VoteDetailActivity.class);
                intent.putExtra(VoteDetailActivity.EXTRA_VOTE_ID, pos);
                startActivity(intent);
            }
        });

        // Reset index and vote information
        int pageIndex = 0;
        Vote.votes = new ArrayList<>();

        client = new OkHttpClient();
        Request request = new Request.Builder().url(NetworkUtils.allVotesUrl(pageIndex)).build();
        client.newCall(request).enqueue(new VoteResponseCallback());

        listVotes.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextPage(page);
            }
        });

        return dashboardView;
    }

    private void loadNextPage(int offset) {
        Request request = new Request.Builder().url(NetworkUtils.allVotesUrl(offset)).build();
        client.newCall(request).enqueue(new VoteResponseCallback());
    }

    private class VoteResponseCallback implements Callback {
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
                getActivity().runOnUiThread(new Runnable() {
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