package ca.bcit.clearcouncil.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ca.bcit.clearcouncil.CouncilMember;
import ca.bcit.clearcouncil.R;
import ca.bcit.clearcouncil.adapters.CouncilMemberAdapter;

/**
 * THE DASHBOARD FRAGMENT -- HOLDS ALL THE CARDS OF VOTES
 */
public class VoteDescriptionFragment extends Fragment {
    public static final String desc_id = "DESCRIPTION_ID";
    public static final String members_id = "MEMBERS_ID";
    public static final String date_id = "DATE_ID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_description, container, false);
        TextView description_content = view.findViewById(R.id.description_content);
        TextView date_content = view.findViewById(R.id.date_content);
        ListView members_list = view.findViewById(R.id.list_members);

        if (this.getArguments() != null) {
            String description = this.getArguments().getString(desc_id);
            String date = this.getArguments().getString(date_id);
            ArrayList<CouncilMember> members = this.getArguments().getParcelableArrayList(members_id);

            description_content.setText(description);
            date_content.setText(date);

            TextView textView = view.findViewById(R.id.link_to_search);
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='http://www.google.com/search?q=" + description + "&as_sitesearch=council.vancouver.ca'>Search for more information</a>";
            textView.setText(Html.fromHtml(text));

            CouncilMemberAdapter adapter = new CouncilMemberAdapter(getContext(), R.layout.member_listitem, members);
            members_list.setAdapter(adapter);
        }

        return view;
    }
}