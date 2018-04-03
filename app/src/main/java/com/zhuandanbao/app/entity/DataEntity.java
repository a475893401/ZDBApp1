package com.zhuandanbao.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 数据解析通用
 */
public class DataEntity implements Parcelable {
	public String id;// id
	public String icon;// 图片
	public String title;// 标题
	public String des;// 介绍
	public String timestamp;// 时间�?
	public String adddate;// 添加时间
	public String quote;// 来源
	public String extra_1;// 保留字段
	public String extra_2;// 保留字段
	public String extra_3;// 保留字段
	public String extra_4;// 保留字段
	public String extra_5;// 保留字段
	public String extra_6;// 保留字段
	public String extra_7;// 保留字段
	public String extra_8;// 保留字段
	public String extra_9;// 保留字段
	public String extra_10;// 保留字段
	public String extra_11;// 保留字段
	public String extra_12;// 保留字段
	public String extra_13;// 保留字段
	public String extra_14;// 保留字段
	public int     gradeColor; 
	public int     grade;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(icon);
		dest.writeString(title);
		dest.writeString(des);
		dest.writeString(timestamp);
		dest.writeString(adddate);
		dest.writeString(quote);
		dest.writeString(extra_1);
		dest.writeString(extra_2);
		dest.writeString(extra_3);
		dest.writeString(extra_4);
		dest.writeString(extra_5);
		dest.writeString(extra_6);
		dest.writeString(extra_7);
		dest.writeString(extra_8);
		dest.writeString(extra_9);
		dest.writeString(extra_10);
		dest.writeString(extra_11);
		dest.writeString(extra_12);
		dest.writeString(extra_13);
		dest.writeString(extra_14);
		dest.writeInt(gradeColor);
		dest.writeInt(grade);
	}

	public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
		@Override
		public DataEntity[] newArray(int size) {
			return new DataEntity[size];
		}
		@Override
		public DataEntity createFromParcel(Parcel source) {
			return new DataEntity(source);
		}
	};

	public DataEntity(Parcel in) {
		this.id = in.readString();
		this.icon = in.readString();
		this.title = in.readString();
		this.des = in.readString();
		this.timestamp = in.readString();
		this.adddate = in.readString();
		this.quote = in.readString();
		this.extra_1 = in.readString();
		this.extra_2 = in.readString();
		this.extra_3 = in.readString();
		this.extra_4 = in.readString();
		this.extra_5 = in.readString();
		this.extra_6 = in.readString();
		this.extra_7 = in.readString();
		this.extra_8 = in.readString();
		this.extra_9 = in.readString();
		this.extra_10 = in.readString();
		this.extra_11 = in.readString();
		this.extra_12 = in.readString();
		this.extra_13 = in.readString();
		this.extra_14 = in.readString();
		this.gradeColor = in.readInt();
		this.grade = in.readInt();
	}

	public DataEntity(){
		
	} 
	
	public DataEntity(String s1) {
		this.extra_1 = s1;
	}
	
	public DataEntity(String s1, String s2) {
		this.extra_1 = s1;
		this.extra_2 = s2;
	}
	
	public DataEntity(String s1, String s2, String s3) {
		this.extra_1 = s1;
		this.extra_2 = s2;
		this.extra_3 = s3;
	}
	
	public DataEntity(String s1, String s2, String s3, String s4) {
		this.extra_1 = s1;
		this.extra_2 = s2;
		this.extra_3 = s3;
		this.extra_4 = s4;
	}
	
}
