package comp3350.srsys.objects;

import java.io.Serializable;
import java.util.Date;

public class Listing implements Serializable {

    private String title;
    private final int id; //to distinguish two listings in same type/category
    private final Date datePosted;
    private String category;
    private int priority;
    private String description;
    private String userEmail;

    public Listing(int id, String title, int priority, String category, String userEmail) {
        this.title = title;
        this.datePosted = new Date();
        this.id = id;
        this.priority = priority;
        this.category = category;
        this.userEmail = userEmail;
        this.description = "";
    }

    public Listing(int id, String title, int priority, String category, String userEmail, String description) {
        this.title = title;
        this.datePosted = new Date();
        this.id = id;
        this.priority = priority;
        this.category = category;
        this.userEmail = userEmail;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String newUserEmail) {
        userEmail = newUserEmail;
    }

    public int getID() {
        return id;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int newPriority) {
        priority = newPriority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String newCategory) {
        category = newCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String toString() {
        String result = "Post Date: " + this.getDatePosted();
        return result;
    }

    /* Listings are equivalent if their IDs are the same and the user emails are the same */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Listing) {
            boolean titlesAreEqual = this.title.equals(((Listing) obj).title);
            boolean descriptionAreEqual = this.description.equals(((Listing) obj).description);
            boolean emailsAreEqual = this.userEmail.equals(((Listing) obj).getUserEmail());

            return titlesAreEqual && descriptionAreEqual && emailsAreEqual;
        }
        return false;
    }
}