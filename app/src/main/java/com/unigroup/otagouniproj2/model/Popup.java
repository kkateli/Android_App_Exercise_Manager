package com.unigroup.otagouniproj2.model;

import com.unigroup.otagouniproj2.data.IPopup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
* Popup class is each event View
* */
public class Popup implements IPopup {
    Integer _id;
    String title;
    String description;
    String quote;
    Calendar startTime;
    Calendar endTime;

    public Popup(Integer _id, String title, String description, String startTime, String endTime, String quote) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.quote = quote;
        Calendar cal = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(startTime));
            calEnd.setTime(sdf.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.startTime = (Calendar) cal.clone();
        this.endTime = (Calendar) calEnd.clone();
    }

    public Popup() {

    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Integer getId() {
        return _id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getQuote() {
        return quote;
    }

    @Override
    public Boolean isAutohide() {
        return false;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    @Override
    public Calendar getEndTime() {
        return endTime;
    }
}
