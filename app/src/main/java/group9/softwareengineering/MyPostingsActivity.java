package group9.softwareengineering;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyPostingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private ArrayList<Posting> postings = new ArrayList<>();

    private FirebaseUser currentUser;

    private FirestoreRecyclerAdapter adapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_postings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_postings_recycler_view);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        query = db.collection("postings").whereEqualTo("poster_id", currentUser.getUid()).whereEqualTo("completed" , false);

        final FirestoreRecyclerOptions<Posting> response = new FirestoreRecyclerOptions.Builder<Posting>()
                .setQuery(query, Posting.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Posting, PostingHolder>(response) {
            @Override
            public void onBindViewHolder(PostingHolder holder, int position, final Posting posting) {
                holder.setData(posting, getApplicationContext());
                holder.setOnClickListener(new PostingHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO: go to mypostedjob activity
                        if(response.getSnapshots().getSnapshot(position).get("sitter_found").equals("")){
                            Intent intent = new Intent(view.getContext(), PostingJobActivity.class);
                            intent.putExtra("start" , response.getSnapshots().getSnapshot(position).toObject(Posting.class).getStart_time().getTime());
                            intent.putExtra("end" , response.getSnapshots().getSnapshot(position).toObject(Posting.class).getEnd_time().getTime());
                            intent.putExtra("posting" , response.getSnapshots().getSnapshot(position).toObject(Posting.class));
                            intent.putExtra("id" , response.getSnapshots().getSnapshot(position).getId());
                            startActivity(intent);
                        } else {
                            Intent intent2 = new Intent(getApplicationContext() , AgreedJobInfoActivity.class);
                            intent2.putExtra("id" , response.getSnapshots().getSnapshot(position).getId());
                            startActivity(intent2);
                        }
                       // Intent intent = new Intent(view.getContext(), MyPostedJob.class);
                        //intent.putExtra("ids",response.getSnapshots().getSnapshot(position).getId());
                        //intent.putStringArrayListExtra("usersPetIds" , posting.getPetIDs());
                        //startActivity(intent);
                        //Toast.makeText(MyPostingsActivity.this, "This should go to the activity!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyPostingsActivity.this);

                        builder.setTitle(getString(R.string.delete_posting_dialog_title));
                        builder.setMessage(getString(R.string.delete_posting_dialog_message));

                        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("postings").document(response.getSnapshots().getSnapshot(position).getId()).delete();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.create().show();
                    }
                });
            }

            @Override
            public PostingHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.recycler_view_item_my_postings, group, false);

                return new PostingHolder(view);
            }
        };
        recyclerView.addItemDecoration(new LineDividerItemDecoration(this, R.drawable.line_divider));
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}


class PostingHolder extends RecyclerView.ViewHolder {
    private View view;

    PostingHolder(View itemView) {
        super(itemView);
        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    void setData(Posting posting, Context context) {
        TextView when = view.findViewById(R.id.my_postings_when);
        TextView description = view.findViewById(R.id.my_postings_description);
        ImageView imageView = view.findViewById(R.id.my_postings_image);

        // format dates
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        sdf.applyPattern("EEE, d MMM HH:mm");
        String date1 = sdf.format(posting.getStart_time());
        String date2 = sdf.format(posting.getEnd_time());

        when.setText(context.getString(R.string.job_duration, date1, date2));
        description.setText(posting.getDescription());
        Picasso.with(context).load(posting.getPhotoURL()).fit().centerCrop().into(imageView);
    }

    private PostingHolder.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(PostingHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }


}