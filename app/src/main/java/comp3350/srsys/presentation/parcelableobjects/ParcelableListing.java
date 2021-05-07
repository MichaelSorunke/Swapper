package comp3350.srsys.presentation.parcelableobjects;

import android.os.Parcel;
import android.os.Parcelable;

import comp3350.srsys.objects.Listing;

public class ParcelableListing implements Parcelable {
    private Listing listing;

    public ParcelableListing(Listing listing) {
        this.listing = listing;
    }

    protected ParcelableListing(Parcel in) {
        listing = new Listing(in.readInt(), in.readString(), in.readInt(), in.readString(), in.readString());
        listing.getDatePosted().setTime(in.readLong());
        listing.setDescription(in.readString());
    }

    public Listing getListing() {
        return listing;
    }

    public static final Creator<ParcelableListing> CREATOR = new Creator<ParcelableListing>() {
        @Override
        public ParcelableListing createFromParcel(Parcel in) {
            return new ParcelableListing(in);
        }

        @Override
        public ParcelableListing[] newArray(int size) {
            return new ParcelableListing[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(listing.getID());
        parcel.writeString(listing.getTitle());
        parcel.writeInt(listing.getPriority());
        parcel.writeString(listing.getCategory());
        parcel.writeString(listing.getUserEmail());
        parcel.writeLong(listing.getDatePosted().getTime());
        parcel.writeString(listing.getDescription());
    }
}