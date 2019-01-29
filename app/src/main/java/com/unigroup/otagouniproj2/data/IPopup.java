package com.unigroup.otagouniproj2.data;

import java.io.Serializable;

public interface IPopup extends ITimeDuration, Serializable {

    Integer getId();

    String getTitle();

    String getDescription();

    void setTitle(String s);

    String getQuote();

    Boolean isAutohide();
}
