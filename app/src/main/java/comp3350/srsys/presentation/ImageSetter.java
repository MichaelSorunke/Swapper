package comp3350.srsys.presentation;

import android.content.Context;
import android.widget.ImageView;

import com.example.swapper.R;

import java.util.List;

import comp3350.srsys.objects.Listing;

public class ImageSetter {
    enum Type {
        None,
        Books,
        Clothes,
        Electronics,
        Games,
        Anything,
        Furniture
    }

    ImageView image;
    String category;
    Type imageType;

    public ImageSetter(ImageView image, String category)
    {
        this.category = category;
        this.image = image;
        imageType = Type.valueOf(category);
    }

    public void setImage()
    {
        switch(imageType)
        {
            case None:
                image.setImageResource(R.drawable.gallery);
                break;
            case Books:
                image.setImageResource(R.drawable.books);
                break;
            case Electronics:
                image.setImageResource(R.drawable.electronics);
                break;
            case Furniture:
                image.setImageResource(R.drawable.furniture);
                break;
            case Games:
                image.setImageResource(R.drawable.games);
                break;
            case Clothes:
                image.setImageResource(R.drawable.clothing);
                break;
            case Anything:
                image.setImageResource(R.drawable.star);
                break;
        }
    }

}
