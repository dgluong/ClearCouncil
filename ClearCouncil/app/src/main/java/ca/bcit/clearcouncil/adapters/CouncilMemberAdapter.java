package ca.bcit.clearcouncil.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import ca.bcit.clearcouncil.CouncilMember;
import ca.bcit.clearcouncil.R;

public class CouncilMemberAdapter extends ArrayAdapter<CouncilMember> {
    public final String TAG = "COUNCIL_MEMBER_ADAPTER";

    private Context mContext;
    private int mResource;

    static class ViewHolder {
        TextView memberNameView;
        TextView memberPositionView;
    }

    public CouncilMemberAdapter(@NonNull Context context, int resource, @NonNull List<CouncilMember> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = Objects.requireNonNull(getItem(position)).getName();
        CouncilMember.Position memberPosition = Objects.requireNonNull(getItem(position)).getPosition();

        ViewHolder holder;

        CouncilMember member = new CouncilMember(name, memberPosition);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.memberNameView = convertView.findViewById(R.id.member_name);
            holder.memberPositionView = convertView.findViewById(R.id.member_position);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CouncilMember.Position pos = member.getPosition();
        holder.memberNameView.setText(member.getName());
        holder.memberPositionView.setText(pos.toString());
        if (pos == CouncilMember.Position.IN_FAVOUR) {
            holder.memberPositionView.setBackgroundResource(R.drawable.rounded_success);
        } else if (pos == CouncilMember.Position.IN_OPPOSITION) {
            holder.memberPositionView.setBackgroundResource(R.drawable.rounded_fail);
        } else {
            holder.memberPositionView.setBackgroundResource(R.drawable.rounded_absent);
        }

        return convertView;
    }
}
