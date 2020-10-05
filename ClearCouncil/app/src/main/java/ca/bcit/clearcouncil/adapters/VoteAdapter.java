package ca.bcit.clearcouncil.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.bcit.clearcouncil.R;
import ca.bcit.clearcouncil.Vote;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    private ArrayList<Vote> votes;
    private static ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView date;
        TextView title;
        TextView description;
        TextView vote_status;
        TextView meeting_type;


        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image = view.findViewById(R.id.image);
            date = view.findViewById(R.id.date);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            vote_status = view.findViewById(R.id.confirm_deny);
            meeting_type = view.findViewById(R.id.category);
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        VoteAdapter.clickListener = clickListener;
    }

    public VoteAdapter(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    public void updateData(ArrayList<Vote> newVotes) {
        votes.clear();
        votes.addAll(newVotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vote_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView image = holder.image;
        TextView date = holder.date;
        TextView title = holder.title;
        TextView description = holder.description;
        TextView vote_status = holder.vote_status;
        TextView meeting_type = holder.meeting_type;

        Vote vote = votes.get(position);
        String mDrawableName = vote.returnImage();
        Context context = holder.itemView.getContext();
        int resourceId = context.getResources().getIdentifier(mDrawableName, "drawable",
                context.getPackageName());

        String voteText = "#" + vote.getVoteNumber();

        image.setImageResource(resourceId);
        date.setText(vote.getVoteDate());
        title.setText(voteText);
        description.setText(vote.getDescription());

        String decision = vote.getDecision();
        vote_status.setText(decision);
        if (decision.equals("Carried") || decision.equals("Carried Unanimously")) {
            vote_status.setBackgroundResource(R.drawable.rounded_success);
        } else {
            vote_status.setBackgroundResource(R.drawable.rounded_fail);
        }

        meeting_type.setText(vote.getMeetingType());
    }

    @Override
    public int getItemCount() {
        return votes.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
