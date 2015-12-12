package com.mattmellor.textswiperapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/19/2015.
 */
public class Article {

    private Context mainContext;
    private AutoResizeTextView articleTextBox;
    private List<String> pages;
    private String storyName;
    
    //Abstraction Function
    //  Representing an article by a list of Pages
    //  pages are strings that have been built dependent on the size of @id/article_setup
    //  width and height
    //
    //Rep invariant
    //
    //
    //Safety from Rep Exposure
    //


    /**
     * Constructor for an Article
     * @param fileName FileName of the file to be taken from Assets
     * @param mainContext context of the app that allows us to read the Story
     */
    public Article(String fileName, Context mainContext, View AutoResizeTextView, int numCharsPerPage){
        this.storyName = fileName;
        this.pages = new ArrayList<>();
        this.mainContext = mainContext;
        this.articleTextBox = (AutoResizeTextView) AutoResizeTextView;
        createPages(readFile(fileName), numCharsPerPage);
        //createPages(readFile(fileName));
    }

    /**
     * Get a particular page of the article
     * @param pageNumber representing a page number
     *                   pageNumber < #pages
     * @return String representing a page
     */
    public String getPage(int pageNumber){
        return this.pages.get(pageNumber);
    }

    /**
     *
     * @return Integer representing the numberOfPages
     *  in the article
     */
    public Integer getNumPages(){
        return pages.size();
    }

    /**
     * Get the entire article as a String
     * @return String representing the entire article
     */
    public String overallArticleText(){
        List<String> raw = readFile(storyName);
        String out = "";
        for(String line: raw){
            out += line;
        }
        return out;
    }

    /**
     * Create pages of the article based on a particular numCharsPerPage
     * @param rawStoryLines Representing the story in line
     * @param numCharsPerPage How many characters desired per page
     *                        numCharsPerPage > 0
     */
    public void createPages(List<String> rawStoryLines, int numCharsPerPage){
        List<String> pages = new ArrayList<>();
        String allText = overallArticleText();
        recursiveSliceFrontString(allText,numCharsPerPage);
    }

    /**
     *
     * @param rawStoryLines Representing the story in Lines
     */
    public void createPages(List<String> rawStoryLines){
        int numLinesDesired = 5;
        String page = "";
        int currentLinesPerPage = 0;
        for(String line: rawStoryLines){
            currentLinesPerPage++;
            page += line;
            if(currentLinesPerPage == numLinesDesired){
                pages.add(page);
                page = "";
                currentLinesPerPage = 0;
            }
        }

    }


    private String recursiveSliceFrontString(String splittableString, int numChars){
        if(splittableString.length() < numChars){
            this.pages.add(splittableString);
            return "end";
        }
        else{
            this.pages.add(splittableString.substring(0,numChars));
            return(recursiveSliceFrontString(splittableString.substring(numChars),numChars));
        }

    }


    /**
     *
     * @param fileName representing the file to readFrom in res/Assets
     * @return String representing the
     */
    private List<String> readFile(String fileName){
        AssetManager assetManager = mainContext.getAssets();
        InputStream input_stream;
        String story = fileName;
        ArrayList<String> linesOfStory = new ArrayList<>();
        //read the input from assetManager
        try {
            int counter = 0;
            input_stream = assetManager.open(story);
            BufferedReader buffedRead = new BufferedReader(new InputStreamReader(input_stream));
            String line;
            while((line = buffedRead.readLine()) != null) {
                linesOfStory.add(line);
            }
            input_stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesOfStory;
    }

    /**
     * Get the AutoResizeTextView height before the AutoResizeTextView will render
     * @param AutoResizeTextView the AutoResizeTextView to measure
     * @return the height of the AutoResizeTextView
     */
    public static int getAutoResizeTextViewHeight(AutoResizeTextView AutoResizeTextView) {
        WindowManager wm =
                (WindowManager) AutoResizeTextView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        AutoResizeTextView.measure(widthMeasureSpec, heightMeasureSpec);
        return AutoResizeTextView.getMeasuredHeight();
    }

}
