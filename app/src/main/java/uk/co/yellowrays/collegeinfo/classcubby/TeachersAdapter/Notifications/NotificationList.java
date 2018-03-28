package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Notifications;

/**
 * Created by Vino on 3/14/2018.
 */

public class NotificationList {

    private String notificationid,notificationmsg,notificationtype;
    private String postedbyname,postedbyimage,notificationpostedtime,notificationreaddate;

    public NotificationList(String notificationid, String notificationmsg, String notificationtype,
                            String postedbyname, String postedbyimage, String notificationpostedtime,
                            String notificationreaddate) {
        this.notificationid = notificationid;
        this.notificationmsg = notificationmsg;
        this.notificationtype = notificationtype;
        this.postedbyname = postedbyname;
        this.postedbyimage = postedbyimage;
        this.notificationpostedtime = notificationpostedtime;
        this.notificationreaddate = notificationreaddate;
    }

    public String getnotificationid() {
        return this.notificationid;
    }

    public String getnotificationmsg() {
        return this.notificationmsg;
    }

    public String getnotificationtype() {
        return this.notificationtype;
    }

    public String getpostedbyname() {
        return this.postedbyname;
    }

    public String getpostedbyimage() {
        return this.postedbyimage;
    }

    public String getnotificationpostedtime() {
        return this.notificationpostedtime;
    }

    public String getnotificationreaddate() {
        return this.notificationreaddate;
    }

}
