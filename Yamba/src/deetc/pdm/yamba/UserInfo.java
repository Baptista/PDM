package deetc.pdm.yamba;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable
{
	//Campos privados
	private int id;
	private String name;
	private String time;
	private String text;
	
	//Propriedades
	public int getId(){return id;}
	public String getName(){return name;}
	public String getTime(){return time;}
	public String getText(){return text;}
	
	public void setId(int id){ this.id = id; }
	public void setName(String name){ this.name = name;}
	public void setTime(String time){ this.time = time;}
	public void setText(String text){ this.text = text;}
	
	
	//Construtores
	public UserInfo(){};
	
	public UserInfo(int _id, String _name, String _time, String _text){
		setId(_id);
		setName(_name);
		setTime(_time);
		setText(_text);
	};
	
	private UserInfo(Parcel in){
		setId(in.readInt());
		setName(in.readString());
		setTime(in.readString());
		setText(in.readString());
    }
	
	//ToString
	public String toString() {return "ID: "+id+"\nName: "+name+"\nTime: "+time+"\nText: "+text;}

	//Parcelable
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int args) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(time);
		dest.writeString(text);
    }
	
	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>()
	{
		public UserInfo createFromParcel(Parcel in) {
			return new UserInfo(in);
		}
		
		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};
}