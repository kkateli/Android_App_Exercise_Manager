package domain;

import java.util.List;

/**
 * Created by harrysmac on 17/05/18.
 */

public class UserTimetable {

    private List<TimetableActivity> activitylist;


    public List<TimetableActivity> getActivitylist() {
        return activitylist;
    }

    public void setActivitylist(List<TimetableActivity> activitylist) {
        this.activitylist = activitylist;
    }
}
