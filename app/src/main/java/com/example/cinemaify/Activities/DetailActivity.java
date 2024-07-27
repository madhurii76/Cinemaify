package com.example.cinemaify.Activities;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinemaify.Adapters.ActorsListAdapter;
import com.example.cinemaify.Adapters.CategoryEachFilmListAdapter;
import com.example.cinemaify.Domains.FilmItem;
import com.example.cinemaify.R;
import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressBar;
    private TextView titleTxt,movieRateTxt,movieTimeTxt,MovieSummaryInfo,movieActorsInfo;
    private int idFilm;
    private ImageView pic2,backImg;
    private RecyclerView.Adapter adapterActorList, adapterCategory;
    private  RecyclerView recyclerViewActors,recyclerViewCategory;
    private NestedScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idFilm=getIntent().getIntExtra("id",0);
        initView();
        sendRequest();
    }

    private void sendRequest() {
        mRequestQueue= Volley.newRequestQueue(this);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(GONE);

        mStringRequest=new StringRequest(Request.Method.GET,"https://moviesapi.ir/api/v1/movies/" +idFilm,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                progressBar.setVisibility(GONE);
                scrollView.setVisibility(View.VISIBLE);
                FilmItem item=gson.fromJson(response, FilmItem.class);

                Glide.with(DetailActivity.this)
                        .load(item.getPoster())
                        .into(pic2);
                titleTxt.setText(item.getTitle());
                movieRateTxt.setText(item.getImdbRating());
                movieTimeTxt.setText(item.getRuntime());
                MovieSummaryInfo.setText(item.getPlot());
                movieActorsInfo.setText(item.getActors());

                if(item.getImages()!=null){
                    adapterActorList=new ActorsListAdapter(item.getImages());
                    recyclerViewActors.setAdapter(adapterActorList);
                }
                if(item.getGenres()!=null){
                    adapterCategory=new CategoryEachFilmListAdapter(item.getGenres());
                    recyclerViewCategory.setAdapter(adapterCategory);
                }

            }

            }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void initView() {
        titleTxt=findViewById(R.id.movieNameTxt);
        progressBar=findViewById(R.id.progressBarDetail);
        scrollView=findViewById(R.id.scrollView2);
        pic2=findViewById(R.id.picDetail);
        movieRateTxt=findViewById(R.id.movieStar);
        movieTimeTxt=findViewById(R.id.movieTime);
        MovieSummaryInfo=findViewById(R.id.movieSummary);
        movieActorsInfo=findViewById(R.id.movieActorInfo);
        backImg=findViewById(R.id.backImg);
        recyclerViewCategory=findViewById(R.id.genreView);
        recyclerViewActors=findViewById(R.id.imagesRecycler);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}