/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.internal.ChannelSendFileResponse;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener {


  TextView logOrsign;
  Button btn1;
  EditText username,password;
  ImageView imageView;
  RelativeLayout relativeLayout;
  String SString="<u>or SIGNUP</u>";
  String LString="<u>or LOGIN</u>";
  int LS = 0;
  // 0 = SIGNUP
  // 1 = LOGIN

  public void showUserList()
  {
    Intent intent = new Intent(getApplicationContext(),UserList.class);
    startActivity(intent);
  }


  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
    {
      action(view);
    }

    return false;
  }

  @Override
  public void onClick(View view) {

    if(view.getId() == R.id.imageView || view.getId() == R.id.relativeLayout)
    {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

  }

  public void action(View view)
  {
    String usrnm,psw;
    usrnm = username.getText().toString();
    psw = password.getText().toString();
    ParseUser user = new ParseUser();
    if(LS==1) {
      if (usrnm.equals("")&& psw.equals(""))
        Toast.makeText(getApplicationContext(), "Username and Password cannot be empty", Toast.LENGTH_LONG).show();
      else if (usrnm.equals(""))
        Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_LONG).show();
      else if (psw.equals(""))
        Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
      else {
        user.setUsername(usrnm);
        user.setPassword(psw);
        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {

            if (e == null) {
              Toast.makeText(getApplicationContext(), "SUCCESS, USER SIGNED UP!", Toast.LENGTH_LONG).show();

            } else {
              Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

          }
        });
      }

    }
    else
    {
      ParseUser.logInInBackground(usrnm, psw , new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(e==null && user!=null) {
            Log.i("Yayy!","Loggedin!") ;
            showUserList();
          }
          else {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
          }

        }
      });
    }
  }

  public void signup(View view)
  {
    if(LS==1)
    {
      logOrsign.setText(Html.fromHtml(SString));
      btn1.setText("LOGIN");
      LS=0;
    }
    else if(LS==0)
    {
      logOrsign.setText(Html.fromHtml(LString));
      btn1.setText("SIGN UP");
      LS=1;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("InstantShare");

    logOrsign = (TextView) findViewById(R.id.logOrsign);
    btn1 = (Button) findViewById(R.id.btn1);
    username = (EditText) findViewById(R.id.username);
    password = (EditText) findViewById(R.id.password);
    imageView = (ImageView) findViewById(R.id.imageView);
    relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


    password.setOnKeyListener(this);
    imageView.setOnClickListener(this);
    relativeLayout.setOnClickListener(this);

    logOrsign.setText(Html.fromHtml(SString));

    if(ParseUser.getCurrentUser() != null)
    {
      showUserList();
    }

/*
    //Make a new class and add data

    ParseObject age = new ParseObject("AGE");

    age.put("Name","Fenil");
    age.put("Age",20);

    age.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e==null)
        {
          Log.i("Success!","Age saved!");
        }
        else
        {
          e.printStackTrace();
        }
      }
    });


    //Update data or get data from parse

    ParseQuery<ParseObject> query = ParseQuery.getQuery("AGE");
    query.getInBackground("ghLsXlA44k", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if(e==null && object!=null)
        {
          object.put("Name","KK");
          object.saveInBackground();

          Log.i("Name",object.getString("Name"));
          Log.i("age",Integer.toString(object.getInt("Age")));
        }
      }
    });

    //Traverse with all the data


    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if(e==null)
        {
          for(ParseObject object : objects)
          {
            Log.i("username",object.getString("username"));
          }
        }
      }
    });

      //get all users from parse
      ParseQuery<ParseUser> query = ParseUser.getQuery();
      query.findInBackground(new FindCallback<ParseUser>() {
        @Override
        public void done(List<ParseUser> userObjects, ParseException error) {
          if (userObjects != null) {
            for (int i = 0; i < userObjects.size(); i++) {
              Log.i("Username: ",userObjects.get(i).getUsername().toString());
            }
          }
        }
      });
/*
    //Updating data with where function

    ParseQuery<ParseObject> query = ParseQuery.getQuery("AGE");
    query.whereGreaterThan("Age",17);
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        int a;
        if(e==null && objects!=null)
        {
          for(ParseObject object : objects)
          {
              a = object.getInt("Age") + 10;
              object.put("Age",a);
              object.saveInBackground();
          }
        }
      }
    });

    //Create a user

    ParseUser user = new ParseUser();
    user.setUsername("sonifenil90");
    user.setPassword("myPass");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e==null)
        {
          Log.i("SUCCESS!","User signed up");
        }
        else
        {
          e.printStackTrace();
        }
      }
    });

    //Log in

    ParseUser.logInInBackground("sonifenil90", "myPass", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if(e==null && user!=null)
        {
          Log.i("Yayy!","You Logged in!");
        }
        else
        {
          e.printStackTrace();
        }
      }
    });

    //GetCurrentUser and if you want to log them out

    ParseUser.logOut();
    if(ParseUser.getCurrentUser() != null)
    {
      Log.i("Signed in",ParseUser.getCurrentUser().getUsername());
    }
    else
    {
      Log.i("OOps","No one's Signed in!");
    }
    */


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }
}

