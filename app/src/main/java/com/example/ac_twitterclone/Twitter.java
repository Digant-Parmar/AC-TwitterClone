package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class Twitter extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String>tUsers;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        setTitle("Twitter");


        FancyToast.makeText(this,"Welcome "+ParseUser.getCurrentUser().getUsername(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

        listView = findViewById(R.id.listView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        ParseQuery<ParseUser>query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects.size()>0 && e == null){

                    for(ParseUser usr : objects){
                        tUsers.add(usr.getUsername());
                    }

                    listView.setAdapter(adapter);

                    for (String twitterUser : tUsers){
                        if(ParseUser.getCurrentUser().getList("fanOf")!=null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                            }
                        }
                    }

                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOut();
                Intent intent = new Intent(Twitter.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sendIcon:
                Intent intent1 = new Intent(Twitter.this,SendTweet.class);
                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView)view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(this," Checked "+tUsers.get(position),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().add("fanOf",tUsers.get(position));


        }else{
            FancyToast.makeText(this,"NotChecked "+tUsers.get(position),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(Twitter.this,"Saved ",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                }
            }
        });

    }
}