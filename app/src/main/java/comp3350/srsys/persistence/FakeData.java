package comp3350.srsys.persistence;

import java.util.ArrayList;
import java.util.List;

import comp3350.srsys.objects.Category;
import comp3350.srsys.objects.Listing;
import comp3350.srsys.objects.User;


public class FakeData {

    public static List<User> getUsers() {
        List<User> users = new ArrayList<User>();

        users.add(new User("oxlong24@myumanitoba.ca", "Mike", "Oxlong", "Dontthinkaboutitok@!2"));
        users.add(new User("dreamjournal@gmail.com", "George", "Lopez", "Electricidad@!2"));
        users.add(new User("youwantmyname@hotmail.com", "Hugh", "Mungus", "What@!2"));
        users.add(new User("RockandRoll@gmail.com", "Ronald", "McDonald", "wantFriesWithThat@!2"));
        users.add(new User("sarah@myumanitoba.ca", "Jessica", "Parker", "Horse@!2"));
        users.add(new User("superbad@gmail.com", "Mick", "Lovin", "Bootypoppin@!2"));

        return users;
    }

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();

        categories.add(new Category("Clothing"));
        categories.add(new Category("Books"));
        categories.add(new Category("Electronics"));
        categories.add(new Category("Funiture"));

        return categories;
    }
    
    public static List<Listing> getListings() {
        List<Listing> listings = new ArrayList<Listing>();

        listings.add(new Listing(1, "Well Worn Socks", 1, "Clothing", "superbad@gmail.com"));
        listings.add(new Listing(2, "Used Dream Journal", 1, "Books", "dreamjournal@gmail.com"));
        listings.add(new Listing(3, "Lost TV Remote", 1, "Electronic", "sarah@myumanitoba.ca"));
        listings.add(new Listing(4, "Well Used Toilet", 1, "Funiture", "oxlong24@myumanitoba.ca"));

        return listings;
    }
}