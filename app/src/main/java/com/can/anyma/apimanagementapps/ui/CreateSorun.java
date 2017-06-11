package com.can.anyma.apimanagementapps.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.can.anyma.apimanagementapps.R;
import com.can.anyma.apimanagementapps.model.Sorun;
import com.can.anyma.apimanagementapps.retrofit.api.ApiService;
import com.can.anyma.apimanagementapps.retrofit.api.RetroClient;
import com.can.anyma.apimanagementapps.utils.FileUtils;
import com.can.anyma.apimanagementapps.utils.InternetConnection;
import com.github.clans.fab.FloatingActionButton;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSorun extends AppCompatActivity {

    String[] SPINNERLIST = {"Ankara", "Eskişehir", "İstanbul", "İzmir"};
    private View parentView;
    ImageButton photo;
    View layout;
    private static final int CAMERA_REQUEST = 66;
    public static final int PICK_IMAGE = 100;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sorun);

        final EditText yetkili = (EditText) findViewById(R.id.Yetkili);
        final EditText aciklama = (EditText) findViewById(R.id.Aciklama);
        parentView = findViewById(R.id.parentLayout);
        photo = (ImageButton) findViewById(R.id.Photo);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        final MaterialBetterSpinner bolge = (MaterialBetterSpinner)findViewById(R.id.Bolge);
        bolge.setAdapter(arrayAdapter);

        FloatingActionButton post = (FloatingActionButton) findViewById(R.id.Post);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            photo.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            photo.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sorun sorun = new Sorun(
                        yetkili.getText().toString(),
                        bolge.getText().toString(),
                        aciklama.getText().toString()

                );



                createRequest(sorun);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check that request code matches ours:
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            uri = data.getData();
            uploadFile(uri);
        }

        layout = findViewById(R.id.layout);
        Snackbar.make(layout, "Fotoğraf Kaydedildi", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                photo.setEnabled(true);
            }
        }
    }




    private void createRequest(final Sorun sorun) {

        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(CreateSorun.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("I am getting your JSON");
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            /**
             * Calling JSON
             */
            Call<Sorun> call = api.createSorun(sorun);

            final Sorun tempSorun = sorun;

            /**
             * Enqueue Callback will be call when get response...
             */

            call.enqueue(new Callback<Sorun>() {
                @Override
                public void onResponse(Call<Sorun> call, Response<Sorun> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if(response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        //uploadFile();

                        Intent intent = new Intent(CreateSorun.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        Snackbar.make(parentView, "Something went wrong.", Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Sorun> call, Throwable t) {
                    Toast.makeText(CreateSorun.this, "Bir şeyler yanlış gitti :(", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Snackbar.make(parentView, "Internet Connection Not Available.", Snackbar.LENGTH_LONG).show();
        }

    }


    private void uploadFile(Uri fileUri) {
        File file = FileUtils.getFile(this, fileUri);

        RequestBody reqFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        ApiService api = RetroClient.getApiService();

        Call<ResponseBody> call = api.uploadPhoto(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}




