package com.example.tushar.destinationdiscussions;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import android.text.format.DateFormat;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class discussionpage extends AppCompatActivity {

    @BindView(R.id.go)
    FloatingActionButton fab;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.titletext)
    TextView titletext;
    @BindView(R.id.list)
    ListView list_messages;
    private String title;
    private DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussionpage);
        ButterKnife.bind(this);

        title=getIntent().getExtras().getString("Title");
        titletext.setText(title);

        dref= FirebaseDatabase.getInstance().getReference().child("Discussions").child(title);
        dref.keepSynced(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dref.push().setValue(new listmodel(message.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
                 message.setText("");
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
      FirebaseListAdapter<listmodel> adapter=new FirebaseListAdapter<listmodel>(
              this,
              listmodel.class,
              R.layout.listview,
              dref

      ) {
          @Override
          protected void populateView(View v, listmodel model, int position) {
              TextView messag=(TextView)v.findViewById(R.id.loadmsg);
              TextView emails=(TextView)v.findViewById(R.id.loadname);
              TextView date=(TextView)v.findViewById(R.id.loaddate);

              messag.setText(model.getMessage());
              emails.setText(model.getEmail());
              date.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getTime()));



          }
      };
      list_messages.setAdapter(adapter);
    }
}
