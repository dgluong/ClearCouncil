package ca.bcit.clearcouncil.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import ca.bcit.clearcouncil.Comment;
import ca.bcit.clearcouncil.R;
import ca.bcit.clearcouncil.adapters.CommentAdapter;
import ca.bcit.clearcouncil.utilities.CommentDatabase;

public class CommentFragment extends Fragment {

    private CommentDatabase database;
    private ValueEventListener database_listener;
    private String vote_id;

    private ListView comment_list;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID).substring(0, 5);

        final View view = inflater.inflate(R.layout.fragment_comment, container, false);

        final EditText search_editText = view.findViewById(R.id.id_search_EditText);
        Button button = view.findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = search_editText.getText().toString();
                String name = "User " + android_id;
                if (!body.isEmpty()) {
                    writeComment(name, body);
                    search_editText.setText("");

                    // Hide keyboard after search
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        comment_list = view.findViewById(R.id.list_comments);

        if (this.getArguments() != null) {
            vote_id = this.getArguments().getString("vote_id");
        }

        database = new CommentDatabase(vote_id);

        database_listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getAllComments(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        database.getReference().addValueEventListener(database_listener);
        return view;
    }


    //Close connection, clear resources
    @Override
    public void onStop() {
        super.onStop();
        if (database_listener != null) {
            database.removeEvenentListener(database_listener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (database_listener != null) {
            database.getReference().addValueEventListener(database_listener);
        }
    }

    //Retrieves all the comments
    private void getAllComments(DataSnapshot data) {

        ArrayList<Comment> comments = new ArrayList<>();

        for (DataSnapshot item : data.child(vote_id).getChildren()) {
            Comment comment = new Comment();
            //Sets comment, Firebase is weird and makes us do it like this
            try {
                comment.setBody(item.getValue(Comment.class).getBody());
                comment.setName(item.getValue(Comment.class).getName());
                comment.setTime(item.getValue(Comment.class).getTime());
                comments.add(comment);

            } catch (NullPointerException e) {
                Log.d("Null pointer ", e.toString());
            }
        }
        Collections.reverse(comments);

        if (getContext() != null) {
            CommentAdapter adapter = new CommentAdapter(getContext(), R.layout.comment_listitem, comments);
            comment_list.setAdapter(adapter);
        }
    }

    private void writeComment(String name, String body) {
        SimpleDateFormat df = new SimpleDateFormat( "MMM d, yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone(("PST")));
        String current_time = df.format(new Date());
        database.writeComment(name, body, current_time);
    }


}
