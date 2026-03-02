import java.util.Date;

public class File {
    private String name;
    private boolean writable;
    private int size; // should not be final since then impossible to modify
    private final Date creationTime;
    private Date modificationTime;

    private static final int maxSize = Integer.MAX_VALUE;


    public File(String name, int size, boolean writable)  {
        this.name = name;
        this.size = 0;
        this.writable = writable;
        this.creationTime = new Date();
        this.modificationTime = null;   // A file only gets a modification time at the moment it is modified
                                        // for the first time.
    }

    // Mutator: enlarge
    /**
     *
     * @param amount
     *        amount of bytes that will be added to the current file size
     *
     * @pre the file size should be positive and smaller than the maximuum file size.
     *      The new size after adding the given amount should still be smaller than the maximum file size
     *      size > 0 && size < maxSize
     *      size + amount < maxSize
     *
     * @post file size increases by the given amount
     *       size += amount
     *
     * @note nominaal want te maken met grootte.
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
     *      The new size after subtracting the given amount should remain positive.
     *      size > 0 && size < maxSize
     *      size - amount > 0
     *
     * @post this file size is reduced by the given amount
     *      size -= amount
     *
     * @note nominaal want te maken met grootte.
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
     *       and exclusively consists of letters, digits, dots ('.') , dashes ('-') and underscores ('_').
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
     * @post true if modification time of this file and modification time of the other file are both not null.
     *
     * @note total, nominal or defensive free of choice
     *
     */

    public boolean hasOverlappingUsePeriod(File other){
        // Has file been modified?
        if (this.modificationTime == null || other.modificationTime == null) {return false;}

        // Get the start and end of the use period of each file
        long thisStart = this.creationTime.getTime();
        long thisEnd = this.modificationTime.getTime();

        long otherStart = other.creationTime.getTime();
        long otherEnd = other.modificationTime.getTime();

        // check if file.modificationTime overlaps with other.creationTime
        boolean overlap = !(thisStart < otherEnd && thisEnd < otherStart); // ???????????????????????
        return overlap;
    }
}

