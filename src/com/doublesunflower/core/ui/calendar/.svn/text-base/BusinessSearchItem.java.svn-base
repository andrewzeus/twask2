/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doublesunflower.core.ui.calendar;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class BusinessSearchItem implements Parcelable {
    
    private Location mLocation;
    private String mEventTitle;
    private String mBusinessName;
    
    
    public BusinessSearchItem(Parcel in) {
    	Location tempLocation = new Location("gps");
    	tempLocation.setLatitude(in.readDouble());
    	tempLocation.setLongitude(in.readDouble());
    	
        mLocation = new Location (tempLocation);
        mEventTitle = in.readString();
        mBusinessName = in.readString();
    }
    
    public BusinessSearchItem(double latitude, double longitude,
            String eventtitle, String businessname) {
    	Location tempLocation = new Location("gps");
    	tempLocation.setLatitude(latitude);
    	tempLocation.setLongitude(longitude);
    	
        mLocation = new Location(tempLocation);
        mEventTitle = eventtitle;
        mBusinessName = businessname;
       
    }
    
  
    public Location getLocation() {
        return mLocation;
    }
    
    public String getEventTitle() {
        return mEventTitle;
    }
    
    public String getBusinessName() {
        return mBusinessName;
    }
    
   
    public static final Parcelable.Creator<BusinessSearchItem> CREATOR =
        new Parcelable.Creator<BusinessSearchItem>() {
        public BusinessSearchItem createFromParcel(Parcel in) {
            return new BusinessSearchItem(in);
        }

        public BusinessSearchItem[] newArray(int size) {
            return new BusinessSearchItem[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
       
        parcel.writeDouble(mLocation.getLatitude());
        parcel.writeDouble(mLocation.getLongitude());
        parcel.writeString(mEventTitle);
        parcel.writeString(mBusinessName);
        
   }
}