package com.rilin.lzy.mybase.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rilintech on 16/4/6.
 */
public class UserBean implements Parcelable {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHosp_id() {
        return hosp_id;
    }

    public void setHosp_id(String hosp_id) {
        this.hosp_id = hosp_id;
    }

    public String getFirst_insp() {
        return first_insp;
    }

    public void setFirst_insp(String first_insp) {
        this.first_insp = first_insp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMarital_state() {
        return marital_state;
    }

    public void setMarital_state(String marital_state) {
        this.marital_state = marital_state;
    }

    public String getDeath_time() {
        return death_time;
    }

    public void setDeath_time(String death_time) {
        this.death_time = death_time;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPath_type() {
        return path_type;
    }

    public void setPath_type(String path_type) {
        this.path_type = path_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBsa() {
        return bsa;
    }

    public void setBsa(String bsa) {
        this.bsa = bsa;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * name : 技术支持
     * birthday :
     * hosp_id :
     * first_insp :
     * phone :
     * age :
     * sex :
     * marital_state :
     * death_time :
     * weight :
     * path_type :
     * address :
     * bsa :
     * height :
     * phone2 :
     * phone1 :

     * patient_id :
     */

    private String name;
    private String birthday;
    private String hosp_id;
    private String first_insp;
    private String phone;
    private String age;
    private String sex;
    private String marital_state;
    private String death_time;
    private String weight;
    private String path_type;
    private String address;
    private String bsa;
    private String height;
    private String phone2;
    private String phone1;
    private String patient_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeString(this.birthday);
        dest.writeString(this.hosp_id);
        dest.writeString(this.first_insp);
        dest.writeString(this.phone);
        dest.writeString(this.age);
        dest.writeString(this.sex);
        dest.writeString(this.marital_state);
        dest.writeString(this.death_time);
        dest.writeString(this.weight);
        dest.writeString(this.path_type);
        dest.writeString(this.address);
        dest.writeString(this.bsa);
        dest.writeString(this.height);
        dest.writeString(this.phone2);
        dest.writeString(this.phone1);
        dest.writeString(this.patient_id);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        this.birthday = in.readString();
        this.hosp_id = in.readString();
        this.first_insp = in.readString();
        this.phone = in.readString();
        this.age = in.readString();
        this.sex = in.readString();
        this.marital_state = in.readString();
        this.death_time = in.readString();
        this.weight = in.readString();
        this.path_type = in.readString();
        this.address = in.readString();
        this.bsa = in.readString();
        this.height = in.readString();
        this.phone2 = in.readString();
        this.phone1 = in.readString();
        this.patient_id = in.readString();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
