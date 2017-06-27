package com.example.tushar.destinationdiscussions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class postingscreen extends AppCompatActivity {

    private DatabaseReference dref;
    private static final int request=1;
    @BindView(R.id.imageset)
    ImageView image;
    @BindView(R.id.titleset)
    EditText titles;
    @BindView(R.id.btn)
    Button btn;
    public Uri galleryuri=null;
    private FirebaseStorage storage;
    private String address;
     static ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postingscreen);
        dref= FirebaseDatabase.getInstance().getReference();
        dref.keepSynced(true);
        ButterKnife.bind(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Posting");
        storage=FirebaseStorage.getInstance();
address=getIntent().getExtras().getString("Address");


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,request);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();


            }
        });
    }

    private void startPosting() {
        final String titleset=titles.getText().toString();

        if(!(TextUtils.isEmpty(galleryuri.toString())&&TextUtils.isEmpty(titleset))){
            pd.show();
        StorageReference sref=storage.getReference().child("Images").child(galleryuri.getLastPathSegment());
        sref.putFile(galleryuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                DatabaseReference drefa=dref.child(address).child(titleset);
                drefa.child("Title").setValue(titleset);
                drefa.child("Image").setValue(taskSnapshot.getDownloadUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                    }
                });
                Intent i=new Intent(postingscreen.this,AddressScreen.class);
                i.putExtra("Address",address);
                startActivity(i);

            }
        });


    }}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==request&&resultCode==RESULT_OK){
            galleryuri=data.getData();
            CropImage.activity(galleryuri)
                    .setGuidelines(CropImageView.Guidelines.ON).setFixAspectRatio(true).setAspectRatio(16,9)
                    .start(this);}

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    galleryuri=resultUri;
                    image.setImageURI(galleryuri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }


        }


