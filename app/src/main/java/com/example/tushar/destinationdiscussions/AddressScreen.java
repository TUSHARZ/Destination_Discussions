package com.example.tushar.destinationdiscussions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Address;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tushar.destinationdiscussions.postingscreen.pd;

public class AddressScreen extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
@BindView(R.id.titles) public RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    private DatabaseReference titleref;


    String address;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_screen);
        ButterKnife.bind(this);

        auth=FirebaseAuth.getInstance();
        address=getIntent().getExtras().getString("Address");
    titleref=FirebaseDatabase.getInstance().getReference().child(address);
        titleref.keepSynced(true);

        pd=new ProgressDialog(this);
        recyclerView.setHasFixedSize(true);


        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo n=cm.getActiveNetworkInfo();
        if(!(n!=null&&n.isConnected())){
            Toast.makeText(this,"Internet Not Available, Swipe Down to Refresh",Toast.LENGTH_LONG).show();
        }
     refreshLayout.setOnRefreshListener(this);
        pd.setMessage("Loading");
        pd.show();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        titleref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               pd.dismiss();
           }

         @Override
          public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<model,titleholder> adapter=new FirebaseRecyclerAdapter<model, titleholder>(
                model.class,R.layout.title,titleholder.class,titleref
        ) {
            @Override
            protected void populateViewHolder(titleholder viewHolder, model model, final int position) {
               final String postkey=getRef(position).getKey();

              viewHolder.view.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Intent intent=new Intent(AddressScreen.this,discussionpage.class);
                      intent.putExtra("Title",postkey);
                      startActivity(intent);
                  }
              });

                viewHolder.setImage(model.getImage(),getApplicationContext());




                viewHolder.settitle(model.getTitle());


            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.newmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id){

            case R.id.men:

                Intent intent=new Intent(AddressScreen.this,postingscreen.class);
                intent.putExtra("Address",address);
                startActivity(intent);
                break;
            case R.id.logout:
                Logout();
                break;

        }
        return true;


    }

    @Override
    public void onRefresh() {

        titleref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
          refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static class titleholder extends RecyclerView.ViewHolder{
    View view;

    public titleholder(View itemView) {
        super(itemView);
        view=itemView;
    }
    public  void settitle(String title){
        TextView tv=(TextView)view.findViewById(R.id.textView);
        tv.setText(title);


    }
    public void setImage(final String Image, final Context ctx){
        final ImageView image=(ImageView)view.findViewById(R.id.imageView);
        Picasso.with(ctx).load(Image).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(Image).into(image);

            }
        });

    }}

    public void Logout(){
        AuthUI.getInstance().signOut(this);
        startActivity(new Intent(AddressScreen.this,MainActivity.class));
       // checkuser();
    }

//    private void checkuser() {
//        if(auth.getCurrentUser()==null){
//            startActivity(new Intent(AddressScreen.this,MainActivity.class));
//        }
//        else {
//            startActivity(new Intent(AddressScreen.this, AddressScreen.class));
//        }
//    }

    @Override
    public void onBackPressed() {
       new  AlertDialog.Builder(this).setTitle("DO You Want to Exit").setPositiveButton("Exit", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               Intent intent=new Intent(Intent.ACTION_MAIN);
               intent.addCategory(Intent.CATEGORY_HOME);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
           }
       }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
           }
       }).create().show();
    }
}


