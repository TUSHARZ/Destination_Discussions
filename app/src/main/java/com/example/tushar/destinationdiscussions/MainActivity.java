package com.example.tushar.destinationdiscussions;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.TimeZoneFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Address;

import static android.R.style.Theme;
import static com.example.tushar.destinationdiscussions.postingscreen.pd;

public class MainActivity extends AppCompatActivity {
    private SignInButton gsign;
    private GoogleApiClient mGoogleApiClient;
private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener listener;
    private static final int RC_SIGN_IN=1;
    private static final String TAG="Main";
    private DatabaseReference dref;
    RelativeLayout layout;
    public static  final int request=2;
    Snackbar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
     pd=new ProgressDialog(this);
        pd.setCancelable(false);

        pd.setMessage("Checking Login...Please Wait");

        layout=(RelativeLayout)findViewById(R.id.main);
        gsign=(SignInButton)findViewById(R.id.signin);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_LONG);

            } else {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, request);
            }

        }
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo info=cm.getActiveNetworkInfo();
        gsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(info!=null&&info.isConnected()){
                if(mAuth.getCurrentUser()!=null){
                    pd.show();
                  checkuser();
                }}

            }
        };
        sb= Snackbar.make(layout,"Internet Not Available",Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        dref= FirebaseDatabase.getInstance().getReference().child("Users");

        if(info!=null && info.isConnected())

        {
            pd.show();
        if(mAuth.getCurrentUser()==null) {
//        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setTheme(R.style.FirebaseUI).setProviders(AuthUI.GOOGLE_PROVIDER, AuthUI.FACEBOOK_PROVIDER,
//                AuthUI.EMAIL_PROVIDER).build(),RC_SIGN_IN);}
            pd.dismiss();
            Toast.makeText(getApplicationContext(),"You Need to Login Using G+",Toast.LENGTH_LONG).show();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Toast.makeText(getApplicationContext(),"Error"+connectionResult.getErrorMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();






           }
        else

        {  checkuser();
           }}
        else

        sb.show();




        }


   private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
  GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
 if (result.isSuccess()) {
// Google Sign In was successful, authenticate with Firebase
 GoogleSignInAccount account = result.getSignInAccount();
 firebaseAuthWithGoogle(account);
 } else {
 // Google Sign In failed, update UI appropriately
 // ...
}
 }






else
Toast.makeText(this,"Sorry for the Inconvience sir",Toast.LENGTH_LONG).show();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });}


    public void checkuser(){
        if(mAuth.getCurrentUser()!=null){
            DatabaseReference st=dref.child(mAuth.getCurrentUser().getUid());

        st.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Address")){
                   String address=dataSnapshot.child("Address").getValue().toString();


                    Intent i=new Intent(MainActivity.this,AddressScreen.class);
                    pd.dismiss();
                    i.putExtra("Address",address);
                    startActivity(i);

                }
                else
                {pd.dismiss();
                    startActivity(new Intent(MainActivity.this,Addresses.class));}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}

    }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    }






