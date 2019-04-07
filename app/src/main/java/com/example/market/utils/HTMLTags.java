package com.example.market.utils;

public class HTMLTags {
    public static String removeHTMLTags(String text)
    {
       int start=text.indexOf("<p>")+3;
       int end=text.lastIndexOf("</p>");

       return text.substring(start,end);
    }
}
