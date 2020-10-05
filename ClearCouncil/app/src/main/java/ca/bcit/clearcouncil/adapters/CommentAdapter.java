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

import ca.bcit.clearcouncil.Comment;
import ca.bcit.clearcouncil.R;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private final String TAG = "COMMENT_ADAPTER";

    private Context mContext;
    private int mResource;

    static class ViewHolder {
        TextView commentNameView;
        TextView commentDateView;
        TextView commentBodyView;
    }

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = Objects.requireNonNull(getItem(position)).getName();
        String date  = Objects.requireNonNull(getItem(position)).getTime();
        String body = Objects.requireNonNull(getItem(position)).getBody();

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.commentNameView = convertView.findViewById(R.id.comment_name);
            holder.commentDateView = convertView.findViewById(R.id.comment_date);
            holder.commentBodyView = convertView.findViewById(R.id.comment_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.commentNameView.setText(name);
        holder.commentBodyView.setText(body);
        holder.commentDateView.setText(date);

        return convertView;
    }
}
