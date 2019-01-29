package com.unigroup.otagouniproj2.decoration;

import android.graphics.Rect;

import com.unigroup.otagouniproj2.data.IEvent;
import com.unigroup.otagouniproj2.data.IPopup;
import com.unigroup.otagouniproj2.view.DayView;
import com.unigroup.otagouniproj2.view.EventView;
import com.unigroup.otagouniproj2.view.PopupView;

public interface CdvDecoration {

    EventView getEventView(IEvent event, Rect eventBound, int hourHeight, int seperateHeight);

    PopupView getPopupView(IPopup popup, Rect eventBound, int hourHeight, int seperateHeight);

    DayView getDayView(int hour);
}
