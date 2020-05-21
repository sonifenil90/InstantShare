package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UsersFeed extends AppCompatActivity {
    RecyclerView imageList;
    Bitmap bp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feed);

        imageList = (RecyclerView) findViewById(R.id.imageList);
        final ArrayList<Bitmap> bmp_images = new ArrayList<>();

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        setTitle(username+"'s Photos");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username",username);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0)
                {

                    for(ParseObject object : objects)
                    {
                        final int i=0;
                        Log.i("HI",object.toString());
                        ParseFile file = (ParseFile) object.get("image");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data!=null && e==null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    bmp_images.add(bitmap);
                                    imageList.setLayoutManager(new LinearLayoutManager(UsersFeed.this));
                                    imageList.setAdapter(new ProgrammingAdapter(bmp_images));

                                }
                            }
                        });

                    }
                }
                else
                {
                    if(username.equals("My Profile"))
                        Toast.makeText(UsersFeed.this,"You have no photos shared!",Toast.LENGTH_LONG).show();
                    else
                    Toast.makeText(UsersFeed.this,username+" has no photos shared!",Toast.LENGTH_LONG).show();
                }

            }

        });


    }
}
