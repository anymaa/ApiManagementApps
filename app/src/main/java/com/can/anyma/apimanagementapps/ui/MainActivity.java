package com.can.anyma.apimanagementapps.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.can.anyma.apimanagementapps.R;
import com.can.anyma.apimanagementapps.adapter.SorunAdapter;
import com.can.anyma.apimanagementapps.model.Sorun;
import com.can.anyma.apimanagementapps.retrofit.api.ApiService;
import com.can.anyma.apimanagementapps.retrofit.api.RetroClient;
import com.can.anyma.apimanagementapps.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    /**
     * Views
     */
    private ListView listView;
    private View parentView;

    private List<Sorun> sorunList;
    private SorunAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Array List for Binding Data from JSON to this List
         */
        sorunList = new ArrayList<>();

        parentView = findViewById(R.id.parentLayout);

        /**
         * Getting List and Setting List Adapter
         */

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                //Snackbar.make(parentView, sorunList.get(position).getYetkili() + " - " + sorunList.get(position).getBolge(), Snackbar.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar
                        .make(parentView, sorunList.get(position).getYetkili() + " Kaydı Silinsin mi?", Snackbar.LENGTH_LONG)
                        .setAction("Evet", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DeleteRequest(sorunList.get(position).getId(), position);
                            }
                        });
                snackbar.show();
            }

        });


        /**
         * Just to know onClick and Printing Hello Toast in Center.
         */

        SorunRequest();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull final View view) {
                Intent intent = new Intent(MainActivity.this, CreateSorun.class);
                startActivity(intent);
            }
        });
    }



    private void SorunRequest() {
        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("I am getting your JSON");
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            /**
             * Calling JSON
             */
            Call<List<Sorun>> call = api.getSorunlar();

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<List<Sorun>>() {
                @Override
                public void onResponse(Call<List<Sorun>> call, Response<List<Sorun>> response) {
                    //Dismiss Dialog
                    dialog.dismiss();

                    if(response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        sorunList = response.body();

                        /**
                         * Binding that List to Adapter
                         */
                        adapter = new SorunAdapter(MainActivity.this, sorunList);
                        listView.setAdapter(adapter);

                    } else {
                        Snackbar.make(parentView, "Something went wrong.", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Sorun>> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        } else {
            Snackbar.make(parentView, "Internet Connection Not Available.", Snackbar.LENGTH_LONG).show();
        }

    }



    private void DeleteRequest(int id, int position) {
        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("I am getting your JSON");
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();
            final Sorun tempSorun = new Sorun(
                    sorunList.get(position).getYetkili(),
                    sorunList.get(position).getBolge(),
                    sorunList.get(position).getAciklama()
            );
            /**
             * Calling JSON
             */
            Call<Sorun> call = api.deleteSorun(id);

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

                        Snackbar snackbar = Snackbar
                                .make(parentView, "Sorun Kaydı Silindi!", Snackbar.LENGTH_LONG)
                                .setAction("Geri Al", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        createRequest(tempSorun);
                                        Snackbar snackbar1 = Snackbar.make(parentView, "Kayıt Geri Yüklendi!", Snackbar.LENGTH_SHORT);
                                        snackbar1.show();
                                        SorunRequest();
                                    }
                                });

                        snackbar.show();
                        SorunRequest();


                    } else {
                        Snackbar.make(parentView, "Something went wrong.", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Sorun> call, Throwable t) {
                    dialog.dismiss();
                }
            });

        } else {
            Snackbar.make(parentView, "Internet Connection Not Available.", Snackbar.LENGTH_LONG).show();
        }

    }



    private void createRequest(Sorun sorun) {

        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("I am getting your JSON");
            dialog.show();

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            /**
             * Calling JSON
             */
            Call<Sorun> call = api.createSorun(sorun);


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

                    } else {
                        Snackbar.make(parentView, "Something went wrong.", Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Sorun> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Bir şeyler yanlış gitti :(", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Snackbar.make(parentView, "Internet Connection Not Available.", Snackbar.LENGTH_LONG).show();
        }

    }
}

