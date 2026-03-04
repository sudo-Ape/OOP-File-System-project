import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import java.util.Date;

/**
 * Basic class file
 * OGP Practicum 1
 *
 *
 * @author Casper Vermeren; Loïck Sansen
 * @version 1.11
 */

public class File {
    private String name;
    private boolean writable;
    private int size; // should not be final since then impossible to modify
    private Date creationTime;
    private Date modificationTime;
    private static final int maxSize = Integer.MAX_VALUE;

    // Constructors

    /**
     * Initialize this new file with given name, size 0 and writable state true.
     *
     * @effect This name is set to given name
     *      | setName(name)
     *
     * @param name
     *        the name of this new file
     */
    public File(String name){
        this.setName(name);
        this.setSize(0);
        this.writable = true;
        this.creationTime = new Date();
        this.modificationTime = null;
    }


    /**
     * Initialize this new file with given name, given size and given writeable state.
     *
     * @effect This name is set to given name
     *      | setName(name)
     *
     * @effect This size is set to given size
     *      |setSize(size)
     *
     * @effect Writability is set to given writability
     *      |setWritability(writable)
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
        this.setName(name);
        this.setSize(size);
        this.setWritable(writable);
        this.creationTime = new Date();
        this.modificationTime = null;   // A file only gets a modification time at the moment it is modified for the first time.
    }

    // Other methods

    /**
     * Set the name for a file
     *
     * @post If given name only contains letters, numbers, dots ('.'), dashes ('-'), and underscores ('_') and is not empty,
     *       name is given name
     * @post If given name contains illegal characters, name is given name filtered from these illegal characters
     * @post If given name or filtered given name is empty, name is 'New-File'
     *
     * @throws IllegalStateException If file is not writable
     *      | !isWritable()
     *
     * @param name The given name of the file
     */

    public void setName(String name) throws IllegalStateException {
        // Check if file is writable
        if (!this.isWritable()) {
            throw new IllegalStateException("File is not writable");
        }
        // check if given character in name are legal
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = "New-File";
        }
    }

    /**
     * Returns whether the given size is a valid size
     *
     * @param size Size to check
     *
     * @return True if the size is positive and less than or equal to the maxSize
     *      | size >= 0 && size <= maxSize
     */
    public boolean canAcceptSize(int size){
        return size >=0 && size <= maxSize;
    }

    /**
     * Set the size for the given file
     *
     * @pre The given size is a valid size
     *      | canAcceptSize(size)
     *
     * @post Size is given size
     *      | new.getSize() == size
     *
     * @throws IllegalStateException If file is not writable
     *      | !isWritable()
     *
     * @param size The given size of the file
     */
    public void setSize(int size) throws IllegalStateException {
        if (this.isWritable()) {
            this.size = size;
            this.modificationTime = new Date();
        } else {
            throw new IllegalStateException("File is not writable");
        }
    }

    /**
     * Set the writability of the file
     *
     * @param writable writability of the file
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }


    /**
     * Set the creation time of the file
     *
     * @post If given date is invalid, set createTime to new date object
     *
     * @param creationTime Creation date for the file, which is the current system time
     */
    public void setCreationTime(Date creationTime) {
        if (creationTime != null) {
            this.creationTime = creationTime;
        } else {
            this.creationTime = new Date();
        }
    }

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
     * Get the size of the file
     *
     * @return Size of the file
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the name of the file
     *
     * @return name of the file
     */
    public String getName() {
        return name;
    }

    /**
     * Get the writability of the file
     *
     * @return Writability of the file
     */
    public boolean getWritable() {
        return writable;
    }

    /**
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
        this.setSize(this.size + amount);
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
     *
     * @pre amount should be positive
     *      | amount > 0
     *
     * @post the file size equals the old file size minus the given amount
     *       | new.size == size - amount
     *
     * @note nominal because it concerns a quantity.
     */
    public void shorten(int amount){
        this.setSize(this.size - amount);
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


    /**
     * Get the writability of the file
     *
     * @return Writability of the file
     */
    public boolean isWritable(){
        return this.writable;
    }

    // Overlapping use period
    /**
     *
     * @param other
     *        file to be checked if it overlaps with this file
     *
     * @return the result is true if and only if both files have a modification time and their use periods overlap.
     *      | result == (this. modificationTime != null &&
     *      | other.modificationTime != null &&
     *      | other.creationTime.getTime() < this.modificationTime.getTime() &&
     *      | this.creationTime.getTime() < other.modificationTime.getTIme())
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

