package com.example.tushar.destinationdiscussions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tushar.destinationdiscussions.postingscreen.pd;

public class Addresses extends AppCompatActivity {

    @BindView(R.id.text)  AutoCompleteTextView tv;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.gogogo)
    ImageView imageView;
    @BindView(R.id.post) Button post;
    private DatabaseReference users;
    private FirebaseAuth auth;
    private String useradd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);

        ButterKnife.bind(this);
        auth=FirebaseAuth.getInstance();

pd=new ProgressDialog(this);
        pd.setMessage("Setting up Account..");
        users= FirebaseDatabase.getInstance().getReference().child("Users");
        users.keepSynced(true);
        //tv=(AutoCompleteTextView)findViewById(R.id.text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Shake).duration(500).playOn(imageView);
            }
        });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.address,android.R.layout.simple_spinner_item);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String username=name.getText().toString();
                if(!(TextUtils.isEmpty(username)&&TextUtils.isEmpty(useradd))){
                    pd.show();
                Toast.makeText(getApplicationContext(),useradd,Toast.LENGTH_LONG).show();
                users.child(auth.getCurrentUser().getUid()).child("Name").setValue(username);
                users.child(auth.getCurrentUser().getUid()).child("Address").setValue(useradd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent=new Intent(Addresses.this,AddressScreen.class);
                        intent.putExtra("Address",useradd);
                        startActivity(intent);
                    }
                });


            }}});


        tv.setAdapter(adapter);
      tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              useradd=adapterView.getItemAtPosition(i).toString();
              Toast.makeText(getApplicationContext(),useradd,Toast.LENGTH_LONG).show();





          }
      });

    }
}
