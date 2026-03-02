import java.util.Date;

public class File {
    private String name;
    private boolean writable;
    private int size; // should not be final since then impossible to modify
    private final Date creationTime;
    private Date modificationTime;
    private static final int maxSize = Integer.MAX_VALUE;


    /**
     * Return the creation time of the file.
     *      The creation time is the current system time when creating a new file object
     * @return creation time of file object
     */
    public Date getCreationTime(){
        return creationTime;
    }

    /**
     * Return the modification time of the file.
     *      The modification time is the last recorded moment of when the size or name
     *      of the file has been modified successfully.
     * @return modification time of the file object
     */
    public Date getModificationTime(){
        return modificationTime;
    }


    /**
     * Initialize this new file with given name, given size and given writeable state.
     *
     * @param name
     *        the name of this new file
     * @param size
     *        the size of this new file
     * @param writable
     *        whether this file can be modified.
     *        true if modifications are allowed and false if the file is read-only.
     *
     * @note Constructor which makes a file with certain name, certain size and state which indicates if file is writable or not.
     */
    public File(String name, int size, boolean writable)  {
        this.name = name;
        this.size = size;
        this.writable = writable;
        this.creationTime = new Date();
        this.modificationTime = null;   // A file only gets a modification time at the moment it is modified
                                        // for the first time.
    }


    /**
     * Initialize this new file with given name, size 0 and writable state true.
     *
     * @param name
     *        the name of this new file
     */
    public File(String name){
        this.name = name;
        this.size = 0;
        this.writable = true;
        this.creationTime = new Date();
        this.modificationTime = null;
    }


    /**
     *
     * @param amount
     *        amount of bytes that will be added to the current file size
     *
     * @pre the file size should be positive and smaller than the maximum file size.
     *      The resulting size does not exceed the maximum file size
     *      | size > 0 && size < maxSize
     *      | size + amount <= maxSize
     *
     * @post the file size equals the old file size plus the given amount.
     *       | new.size == old.size + amount
     *
     * @note nominal because it concerns a quantity.
     */
    public void enlarge(int amount){
        this.size += amount;
    }

    // Mutator: shorten
    /**
     *
     * @param amount
     *        amount of bytes that will be removed from the current file size
     *
     * @pre the file size should be positive and smaller than the maximum file size.
     *      the resulting size is positive.
     *      | size > 0 && size < maxSize
     *      | size - amount > 0
     *
     * @post the file size equals the old file size minus the given amount
     *       | new.size == old.size - amount
     *
     * @note nominal because it concerns a quantity.
     */
    public void shorten(int amount){
        this.size -= amount;
    }

    /**
     *
     * @param name
     *        name of the file to be checked
     *
     * @return boolean
     *         returns true if the file name is valid and false otherwise.
     *
     * @post true if and only if the name is non-null, contains at least one character,
     *       and exclusively consists of letters, digits, dots ('.'), dashes ('-') and underscores ('_').
     *
     * @note A valid file name must contain at least one character and may only
     *       contain letters, digits, dots, dashes, and underscores.
     */

    private static boolean isValidName(String name){
        // checking if string contains at least 1 character (string cannot be empty)
        if (name == null || name.length() == 0){
            return false;
        } else {
            for (int i = 0; i < name.length(); i++) {

                char c = name.charAt(i); //charAt(i) takes the character at position i

                if (!Character.isLetter(c) &&
                    !Character.isDigit(c)
                && c != '-'
                && c != '_'
                && c != '.') {
                    return false;
                }
            }
            return true;
        }
    }


    // Overlapping use period
    /**
     *
     * @param other
     *        file to be checked if it overlaps with this file
     *
     * @return boolean
     *         returns true if it overlaps and false otherwise.
     *
     * @post the result is true if and only if both files have a modification time and their use periods overlap.
     *       | result == (this. modificationTime != null &&
     *       | other.modificationTime != null &&
     *       | other.creationTime.getTime() < this.modificationTime.getTime() &&
     *       | this.creationTime.getTime() < other.modificationTime.getTIme())
     *
     * @note total (nominal or defensive was allowed)
     */

    public boolean hasOverlappingUsePeriod(File other){
        // Check if file been modified?
        if (this.modificationTime == null || other.modificationTime == null) {return false;}

        // Get the start and end of the use period for each file
        long thisStart = this.creationTime.getTime();
        long thisEnd = this.modificationTime.getTime();

        long otherStart = other.creationTime.getTime();
        long otherEnd = other.modificationTime.getTime();

        // check if file.modificationTime overlaps with other.creationTime !
        boolean overlap = (otherStart < thisEnd && thisStart < otherEnd);
        return overlap;
    }
}
