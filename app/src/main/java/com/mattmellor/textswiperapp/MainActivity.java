package com.mattmellor.textswiperapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an article Object
        String articleName = "aschenputtel";
        Context thisContext = getApplicationContext();
        final AutoResizeTextView articleTextView = (AutoResizeTextView) findViewById(R.id.article_text_box);
        int numCharsPerPage = 1000;
        final Article article = new Article(articleName, thisContext, articleTextView, numCharsPerPage);
        final int numPages = article.getNumPages();
        articleTextView.setText(article.getPage(0));

        articleTextView.setOnTouchListener(new OnSwipeTouchListener(thisContext) {
            @Override
            public void onSwipeLeft() {
                if (currentPage < numPages) {
                    currentPage++;
                }
                articleTextView.setText(article.getPage(currentPage));
            }

            @Override
            public void onSwipeRight() {
                if (currentPage > 0) {
                    currentPage--;
                }
                articleTextView.setText(article.getPage(currentPage));
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


