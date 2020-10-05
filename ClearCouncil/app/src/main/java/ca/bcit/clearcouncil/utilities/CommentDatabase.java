package ca.bcit.clearcouncil.utilities;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import ca.bcit.clearcouncil.Comment;

public class CommentDatabase {

    private String vote_id;
    private DatabaseReference reference;


    public CommentDatabase(String vote_id) {
        this.vote_id = vote_id;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.reference = database.getReference();
    }

    public DatabaseReference getReference(){
        return reference;
    }

    public void removeEvenentListener(ValueEventListener listener) {
        this.reference.removeEventListener(listener);
    }

    /**
     * Writes comment to the Database
     * @param name Name of the user
     * @param body Body of the comment
     * @param time Time of the comment
     */
    public void writeComment(String name, String body, String time) {
        Comment comment = new Comment(name, body, time);
        reference.child(vote_id).push().setValue(comment);
    }


}


