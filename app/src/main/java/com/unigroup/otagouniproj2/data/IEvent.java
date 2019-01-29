package com.unigroup.otagouniproj2.data;

/**
 * Created by jiaweili on 22/04/18.
 */

public interface IEvent extends ITimeDuration {
    String getName();

    int getColor();
}
