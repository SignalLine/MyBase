package com.rilin.lzy.mybase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lzy on 16/9/30.
 */
public class ClockModel implements Parcelable {
    private int id;
    //时间
    private String time;
    //重复
    private String repeat;
    //标签
    private String tag;
    //间隔
    private String space;
    //铃音
    private String ring;
    //uuid
    private String uuid;
    //服务开关
    private String mSwitch;


    public static final Creator<ClockModel> CREATOR = new Creator<ClockModel>() {
        @Override
        public ClockModel createFromParcel(Parcel in) {

            ClockModel model = new ClockModel();

            model.id = in.readInt();
            model.time = in.readString();
            model.repeat = in.readString();
            model.tag = in.readString();
            model.space = in.readString();
            model.ring = in.readString();
            model.uuid = in.readString();
            model.mSwitch = in.readString();

            return model;
        }

        @Override
        public ClockModel[] newArray(int size) {
            return new ClockModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getmSwitch() {
        return mSwitch;
    }

    public void setmSwitch(String mSwitch) {
        this.mSwitch = mSwitch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(time);
        dest.writeString(repeat);
        dest.writeString(tag);
        dest.writeString(space);
        dest.writeString(ring);
        dest.writeString(uuid);
        dest.writeString(mSwitch);
    }
}
