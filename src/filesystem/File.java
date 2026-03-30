package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Date;


/**
 * A class representing a file with a name, size, and writable state.
 * OGP Practicum 1
 *
 * @author Casper Vermeren; Loïck Sansen
 * @version 1.12
 *
 * @invar The name of the file must always be valid
 *      | isValidName(getName())
 *
 * @invar The size of the file must always be valid
 *      | canHaveAsSize(getSize())
 *
 * @invar The creation time must always be valid
 *      | isValidCreationTime(getCreationTime())
 *
 * @invar The modification time must always be valid
 *      | canHaveAsModificationTime(getModificationTime())
 */
public class File extends DiskItem {

    // =====================================================================
    // Fields
    // =====================================================================

    private String name = null;
    private boolean writable = false;
    private int size = 0;
    private Date creationTime = null;
    private Date modificationTime = null;

    private static final int maxSize = Integer.MAX_VALUE;


    // =====================================================================
    // Constructors
    // =====================================================================

    /**
     * Initialize this new file with given name, size 0 and writable state true.
     *
     * @effect This new file is initialized with the given name, size 0 and writable state true
     *      | this(name, 0, true)
     *
     * @param name
     *        The name of this new file
     */
    @Raw
    public File(String name) {
        this(name, 0, true);
    }

    /**
     * Initialize this new file with given name, given size and given writable state.
     *
     * @effect The writability is set to the given writable state
     *      | setWritable(writable)
     *
     * @effect The name is set to the given name
     *      | setName(name)
     *
     * @post The size is set to the given size
     *      | new.getSize() == size
     *
     * @post The creation time is set to the current system time
     *      | new.getCreationTime() != null
     *
     * @post The modification time is null
     *      | new.getModificationTime() == null
     *
     * @param name
     *        The name of this new file
     * @param size
     *        The size of this new file
     * @param writable
     *        Whether this file can be modified.
     *        True if modifications are allowed, false if the file is read-only.
     */
    @Raw
    public File(String name, int size, boolean writable) {
        this.setWritable(writable);     // must come first — setName/setSize check isWritable()
        this.setName(name);
        this.size = size;               // set directly to avoid modificationTime side effect
        this.creationTime = new Date();
        this.modificationTime = null;
    }


    // =====================================================================
    // Name
    // =====================================================================

    /**
     * Return the name of the file.
     *
     * @return The name of the file
     */
    @Basic @Raw
    public String getName() {
        return name;
    }

    /**
     * Set tbe name of the file directly
     *
     * @post  If the given name is valid, the name is set to the given name
     *      | isValidName(name) ==> new.getName().equals(name)
     *
     * @post If the given name is invalid, the name is set to the default name
     *      | !isValidName(name) ==> new.getName().equals("New-File")
     *
     * @param name
     *       The new name for the file
     *
     * @note @Raw used to signal that this method may be called on an object that is not yet fully initialized
     *       (size and creationTime have not been set yet).
     */
    @Raw
    private void setName(String name){
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = "New-File";
        }
    }

    /**
     * Change the name of the file
     *
     * @effect The name is set to the given name
     *      | setName(name)
     *
     * @post The modification time is updated
     *      | new.getModificationTime() != null
     *
     * @throws IllegalStateException If the file is not writable
     *      | !isWritable()
     *
     * @param name
     *        The new name for the file
     *
     * @note Invalid names are handled by setName() which falls back to "New-File"
     */
    public void changeName(String name) throws IllegalStateException {
        // Check if file is writable
        if (!this.isWritable()) {
            throw new IllegalStateException("File is not writable");
        }
        this.setName(name);
        this.modificationTime = new Date();
    }

    /**
     * Check whether the given name is a valid file name.
     *
     * @param name
     *        The name to check
     *
     * @return True if and only if the name is non-null, non-empty,
     *         and contains only letters, digits, dots, dashes or underscores
     *      | result == (name != null && name.matches("[a-zA-Z0-9_.\\-]+"))
     */
    private static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z0-9_.\\-]+");
    }


    // =====================================================================
    // Size
    // =====================================================================

    /**
     * Return the size of the file.
     *
     * @return The size of the file
     */
    @Basic @Raw
    public int getSize() {
        return size;
    }

    /**
     * Set the size for the new file
     *
     * @post The size is set to the given size
     *      | new.getSize() == size
     *
     * @param size
     *      size of the file
     */
    @Basic @Raw
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Set the size of the file.
     *
     * @post The size is set to the given size
     *      | new.getSize() == size
     *
     * @post The modification time is updated
     *      | new.getModificationTime() != null
     *
     * @pre The given size must be valid
     *      | canHaveAsSize(size)
     *
     * @throws IllegalStateException If the file is not writable
     *      | !isWritable()
     *
     * @param size
     *        The new size for the file
     */
    public void changeSize(int size) throws IllegalStateException {
        if (!this.isWritable()) {
            throw new IllegalStateException("File is not writable");
        }
        this.size = size;
        this.modificationTime = new Date();
    }

    /**
     * Check whether the given size is a valid size.
     *
     * @param size
     *        The size to check
     *
     * @return True if and only if the size is non-negative and does not exceed the maximum size
     *      | result == (size >= 0 && size <= maxSize)
     */
    public boolean canHaveAsSize(int size) {
        return size >= 0 && size <= maxSize;
    }

    /**
     * Enlarge the file by the given amount of bytes.
     *
     * @pre The amount must be positive
     *      | amount > 0
     *
     * @pre The resulting size must not exceed the maximum size
     *      | getSize() + amount <= maxSize
     *
     * @effect The size is set to the current size plus the given amount
     *      | changeSize(getSize() + amount)
     */
    public void enlarge(int amount) {
        this.changeSize(this.size + amount);
    }

    /**
     * Shorten the file by the given amount of bytes.
     *
     * @pre The amount must be positive
     *      | amount > 0
     *
     * @pre The resulting size must be non-negative
     *      | getSize() - amount >= 0
     *
     * @effect The size is set to the current size minus the given amount
     *      | changeSize(getSize() - amount)
     */
    public void shorten(int amount) {
        this.changeSize(this.size - amount);
    }


    // =====================================================================
    // Writability
    // =====================================================================

    /**
     * Return whether the file is writable.
     *
     * @return True if the file is writable, false if it is read-only
     */
    @Basic @Raw
    public boolean isWritable() {
        return this.writable;
    }

    /**
     * Set the writability of the file.
     *
     * @post The writability is set to the given value
     *      | new.isWritable() == writable
     *
     * @param writable
     *        The new writability state of the file
     */
    @Basic @Raw
    public void setWritable(boolean writable) {
        this.writable = writable;
    }


    // =====================================================================
    // Time
    // =====================================================================

    /**
     * Return the creation time of the file.
     *      The creation time is the system time at the moment this file was created.
     *
     * @return The creation time of the file
     */
    @Basic @Raw
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Return the modification time of the file.
     *      The modification time is the last moment at which the name or size
     *      of the file was successfully modified. Null if never modified.
     *
     * @return The modification time of the file, or null if never modified
     */
    @Basic @Raw
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Checks the creation time of the file
     *
     * @param creationTime
     *      Creation time of the file
     *
     * @return True if and only if the creationtime is not null.
     *      creationTime != null
     *      | result == (creationTime != null)
     */
    public static boolean isValidCreationTime(Date creationTime){
        return creationTime != null;
    }

    /**
     * Checks if the modification time is valid
     *
     * @param modificationTime
     *      Modification time of the file
     *
     * @return True if and only if modification time is null or
     *         modification time of the file comes after the creation time of the file
     *      | result == (modificiationTime == null || modificationTime.after(getCreationTime))
     */
    public boolean canHaveAsModificationTime(Date modificationTime) {
        return modificationTime == null ||
                modificationTime.after(this.getCreationTime());
    }

    /**
     * Set the creation time of the file.
     *
     * @post The creation time is set to the given time if valid, otherwise it is set to the current system time.
     *      | new.getCreationTime() == (creationTime != null ? creationTime : new Date())
     *
     * @param creationTime
     *      The creation time to set
     */
    private void setCreationTime(Date creationTime) {
        if (creationTime != null) {
            this.creationTime = this.getCreationTime();
        } else {
            this.creationTime = new Date();
        }
    }

    /**
     * Check whether this file has an overlapping use period with the given file.
     *      The use period of a file spans from its creation time to its last modification time.
     *      Two files overlap if both have been modified and their use periods intersect.
     *
     * @param other
     *        The file to check overlap with
     *
     * @return True if and only if both files have a modification time and their use periods overlap
     *      | result == (this.getModificationTime() != null &&
     *      |            other.getModificationTime() != null &&
     *      |            other.getCreationTime().getTime() < this.getModificationTime().getTime() &&
     *      |            this.getCreationTime().getTime() < other.getModificationTime().getTime())
     *
     * @note total programming style
     */
    public boolean hasOverlappingUsePeriod(File other) {
        if (this.modificationTime == null || other.modificationTime == null) {
            return false;
        }

        long thisStart  = this.creationTime.getTime();
        long thisEnd    = this.modificationTime.getTime();
        long otherStart = other.creationTime.getTime();
        long otherEnd   = other.modificationTime.getTime();

        return otherStart < thisEnd && thisStart < otherEnd;
    }
}