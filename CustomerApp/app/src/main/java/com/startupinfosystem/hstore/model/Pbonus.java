
package com.startupinfosystem.hstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Pbonus implements Parcelable{

    @SerializedName("product_bonus")
    private String mProductBonus;
    @SerializedName("product_type")
    private String mProductType;

    protected Pbonus(Parcel in) {
        mProductBonus = in.readString();
        mProductType = in.readString();
    }


    public static final Creator<Pbonus> CREATOR = new Creator<Pbonus>() {
        @Override
        public Pbonus createFromParcel(Parcel in) {
            return new Pbonus(in);
        }

        @Override
        public Pbonus[] newArray(int size) {
            return new Pbonus[size];
        }
    };

    public String getProductBonus() {
        return mProductBonus;
    }

    public void setProductBonus(String productBonus) {
        mProductBonus = productBonus;
    }

    public String getProductType() {
        return mProductType;
    }

    public void setProductType(String productType) {
        mProductType = productType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProductBonus);
        dest.writeString(mProductType);
    }
}
