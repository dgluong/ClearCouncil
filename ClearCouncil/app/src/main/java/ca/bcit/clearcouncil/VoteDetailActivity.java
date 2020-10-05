package ca.bcit.clearcouncil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import ca.bcit.clearcouncil.fragments.CommentFragment;
import ca.bcit.clearcouncil.fragments.VoteDescriptionFragment;
import ca.bcit.clearcouncil.utilities.JSONParseUtils;
import ca.bcit.clearcouncil.utilities.NetworkUtils;
import ca.bcit.clearcouncil.adapters.TabAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static ca.bcit.clearcouncil.Vote.votes;

public class VoteDetailActivity extends AppCompatActivity {
    public static final String EXTRA_VOTE_ID = "voteId";
    Vote vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);

        @SuppressWarnings("ConstantConditions") final int voteId = (Integer) getIntent().getExtras().get(EXTRA_VOTE_ID);
        vote = votes.get(voteId);
        final String voteNumber = vote.getVoteNumber();
        initialDisplayView(vote);

        //API CALL
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(NetworkUtils.councilMembersUrl(voteNumber)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final String myResponse = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<CouncilMember> councilMembers = JSONParseUtils.parseCouncilMembers(myResponse);
                            vote.setCouncilMembers(councilMembers);
                            displayDescription(vote);
                        }
                    });
                }
            }
        });

        final ImageView back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(back_button)) {
                    finish();
                }
            }
        });
    }

    private void initialDisplayView(Vote vote) {
        // PAGE STUFF HERE
        //Set's the text on the page
        String voteNumber = vote.getVoteNumber();
        TextView vote_num = findViewById(R.id.meeting_type_icon);
        String voteText = "Council #" + voteNumber;
        vote_num.setText(voteText);

        //Set image
        ImageView image = findViewById(R.id.vote_image);
        String mDrawableName = vote.returnImage();
        int resourceId = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        image.setImageResource(resourceId);

        //Set meeting type
        TextView meeting_type = findViewById(R.id.meeting_type);
        meeting_type.setText(vote.getMeetingType());

        //Set Confirm/Deny
        TextView vote_status = findViewById(R.id.confirm_deny);
        String decision = vote.getDecision();

        if (decision.equals("Carried") || decision.equals("Carried Unanimously")) {
            vote_status.setBackgroundResource(R.drawable.rounded_success);
        } else {
            vote_status.setBackgroundResource(R.drawable.rounded_fail);
        }

        vote_status.setText(decision);
    }

    private void displayDescription(Vote vote) {
        //Build Fragment
        TabAdapter adapter;
        TabLayout tabLayout;
        ViewPager viewPager;

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(descriptionFragment(vote), "DESCRIPTION");
        adapter.addFragment(commentFragment(), "COMMENTS");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private Fragment descriptionFragment(Vote vote) {
        Bundle bundle = new Bundle();

        bundle.putString(VoteDescriptionFragment.desc_id, vote.getDescription());
        bundle.putParcelableArrayList(VoteDescriptionFragment.members_id, vote.getCouncilMembers());
        bundle.putString(VoteDescriptionFragment.date_id, vote.getVoteDate());

        VoteDescriptionFragment description_fragment = new VoteDescriptionFragment();
        description_fragment.setArguments(bundle);

        return description_fragment;
    }

    private Fragment commentFragment() {

        Bundle bundle = new Bundle();
        bundle.putString("vote_id", vote.getVoteNumber());

        CommentFragment comment_fragment = new CommentFragment();
        comment_fragment.setArguments(bundle);

        return comment_fragment;
    }


}
