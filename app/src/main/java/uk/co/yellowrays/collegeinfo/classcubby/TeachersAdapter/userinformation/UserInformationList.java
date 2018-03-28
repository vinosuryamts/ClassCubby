package uk.co.yellowrays.collegeinfo.classcubby.TeachersAdapter.userinformation;

/**
 * Created by Vino on 3/14/2018.
 */

public class UserInformationList {

    private String salutation,firstname,lastname;
    private String gender,addressline1,addressline2,userimage;
    private String cityname,pincode,contactnumber,altcontactnumber,emailid,occupation,dob;

    public UserInformationList(String salutation, String firstname, String lastname,
                               String gender, String addressline1, String addressline2,
                               String userimage,String cityname, String pincode, String contactnumber,
                               String altcontactnumber, String emailid, String occupation, String dob) {
        this.salutation = salutation;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.userimage = userimage;
        this.cityname = cityname;
        this.pincode = pincode;
        this.contactnumber = contactnumber;
        this.altcontactnumber = altcontactnumber;
        this.emailid = emailid;
        this.occupation = occupation;
        this.dob = dob;
    }

    public String getsalutation() {
        return this.salutation;
    }

    public String getfirstname() {
        return this.firstname;
    }

    public String getlastname() {
        return this.lastname;
    }

    public String getgender() {
        return this.gender;
    }

    public String getaddressline1() {
        return this.addressline1;
    }

    public String getaddressline2() {
        return this.addressline2;
    }

    public String getuserimage() {
        return this.userimage;
    }

    public String getcityname() {
        return this.cityname;
    }

    public String getpincode() {
        return this.pincode;
    }

    public String getcontactnumber() {
        return this.contactnumber;
    }

    public String getaltcontactnumber() {
        return this.altcontactnumber;
    }

    public String getemailid() {
        return this.emailid;
    }

    public String getoccupation() {
        return this.occupation;
    }

    public String getdob() {
        return this.dob;
    }

}
