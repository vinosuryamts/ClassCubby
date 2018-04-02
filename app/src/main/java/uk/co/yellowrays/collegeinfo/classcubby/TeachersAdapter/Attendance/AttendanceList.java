package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.Attendance;

/**
 * Created by Vino on 3/14/2018.
 */

public class AttendanceList {

    private String studentid,studentname,studentimage;
    private String rollnumber,attendancevalue;

    public AttendanceList(String studentid, String studentname, String studentimage,
                          String rollnumber, String attendancevalue) {
        this.studentid = studentid;
        this.studentname = studentname;
        this.studentimage = studentimage;
        this.rollnumber = rollnumber;
        this.attendancevalue = attendancevalue;
    }

    public String getstudentid() {
        return this.studentid;
    }

    public String getstudentname() {
        return this.studentname;
    }

    public String getstudentimage() {
        return this.studentimage;
    }

    public String getrollnumber() {
        return this.rollnumber;
    }

    public String getattendancevalue() {
        return this.attendancevalue;
    }

    public int getstudentidPos(String category) {
        return this.studentid.indexOf(category);
    }

    public int getstudentnamePos(String category) {
        return this.studentname.indexOf(category);
    }

    public int getstudentimagePos(String category) {
        return this.studentimage.indexOf(category);
    }

    public int getrollnumberPos(String category) {
        return this.rollnumber.indexOf(category);
    }

    public int getattendancevaluePos(String category) {
        return this.attendancevalue.indexOf(category);
    }

    public void setstudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setstudentname(String studentname) {
        this.studentname = studentname;
    }

    public void setstudentimage(String studentimage) {
        this.studentimage = studentimage;
    }

    public void setrollnumber(String rollnumber) {
        this.rollnumber = rollnumber;
    }

    public void setattendancevalue(String attendancevalue) {
        this.attendancevalue = attendancevalue;
    }



}
