package comp3350.srsys.exceptions.crudexceptions;

public class DeletingNonExistentCategoryException extends Exception {
    public DeletingNonExistentCategoryException(String message) {
        super(message);
    }
}
