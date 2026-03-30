package filesystem;

import be.kuleuven.cs.som.annotate.Raw;
import be.kuleuven.cs.som.annotate.Basic;
import java.util.Date;

public abstract class DiskItem {

// =====================================================================
// Fields
// =====================================================================

    private String name = null;
    private Date creationTime = null;
    private Date modificationTime = null;
    private Directory parentDirectory = null;
    private boolean writable = false;
    private boolean isTerminated = false;

// =====================================================================
// Constructors
// =====================================================================

    /**
     * Initialize a new disk item
     *
     * @param name name of this item
     * @param writable Whether this item is writable
     */
    protected DiskItem(String name, boolean writable) {
        this.setWritable(writable);
        this.setName(name);
        this.creationTime = new Date();
        this.modificationTime = null;
    }

// ===============================
// Methods
// ===============================

    /** Return the absolute path of the item
     *
     * @return the absolute path of the item;
     */
    public abstract String getAbsolutePath() {return absolutePath};


    /**
     * Return the root of the item
     *
     * @return The root of the item
     */
    public abstract Directory getRoot() {return root};


    /**
     * Return the total disk usage of the item
     *
     * @return the total disk usage of the item
     */
    public abstract long getTotalDiskUsage() {return totalDiskUsage};


    /**
     * Move this item to given target location
     *
     * @param target Location where item is moved to.
     */
    public void move(Directory target){

    }

    /**
     * Terminates (delete) this item
     *
     * @post This item is marked as terminated
     *      | new.isTerminated()
     *
     * @post This item is removed from its parent directory
     *      | new.getParentDirectory() == null
     */
    public abstract void terminate();

    /**
     * Returns if the item is terminated or not
     *
     * @return true if and only if the item is not terminated
     */
    public boolean isTerminated(){
        return isTerminated;
    }


// =====================================================================
// Name
// =====================================================================

    /**
     * Return the name of the item.
     *
     * @return The name of the item
     */
    @Basic @Raw
    public String getName() {
        return name;
    }

    /**
     * Set the name of the item directly
     *
     * @post  If the given name is valid, the name is set to the given name
     *      | isValidName(name) ==> new.getName().equals(name)
     *
     * @post If the given name is invalid, the name is set to the default name
     *      | !isValidName(name) ==> new.getName().equals(getDefaultName())
     *
     * @param name
     *       The new name for the item
     */
    @Raw
    private void setName(String name){
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = getDefaultName();
        }
    }

    /**
     * Change the name of the item
     *
     * @effect The name is set to the given name
     *      | setName(name)
     *
     * @post The modification time is updated
     *      | new.getModificationTime() != null
     *
     * @throws IllegalStateException If the item is not writable
     *      | !isWritable()
     *
     * @param name
     *        The new name for the item
     *
     */
    public void changeName(String name) throws IllegalStateException {
        // Check if item is writable
        if (!this.isWritable()) {
            throw new IllegalStateException("Item is not writable");
        }
        this.setName(name);
        this.modificationTime = new Date();
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
     * Set the writability of the item.
     *
     * @post The writability is set to the given value
     *      | new.isWritable() == writable
     *
     * @param writable
     *        The new writability state of the item
     */
    @Basic @Raw
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

// =====================================================================
// Time
// =====================================================================

    /**
     * Return the creation time of the item.
     *      The creation time is the system time at the moment this item was created.
     *
     * @return The creation time of the item
     */
    @Basic @Raw
    public Date getCreationTime() {
        return creationTime;
    }


    /**
     * Return the modification time of the item.
     *      The modification time is the last moment at which the name or size
     *      of the item was successfully modified. Null if never modified.
     *
     * @return The modification time of the item, or null if never modified
     */
    @Basic @Raw
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Checks the creation time of the item
     *
     * @param creationTime Creation time of the item
     *
     * @return True if and only if the creationtime is not null
     *      | result == (creationTime != null)
     */
    public static boolean isValidCreationTime(Date creationTime){
        return creationTime != null;
    }

    /**
     * Checks if the modification time is valid
     *
     * @param modificationTime Modification time of the item
     *
     * @return True if and only if modification time is null or
     *         modification time comes after the creation time
     *      | result == (modificationTime == null || modificationTime.after(getCreationTime()))
     */
    public boolean canHaveAsModificationTime(Date modificationTime) {
        return modificationTime == null ||
                modificationTime.after(this.getCreationTime());
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
    public boolean hasOverlappingUsePeriod(DiskItem directory) {
        if (this.modificationTime == null || directory.modificationTime == null) {
            return false;
        }

        long thisStart  = this.creationTime.getTime();
        long thisEnd    = this.modificationTime.getTime();
        long otherStart = directory.creationTime.getTime();
        long otherEnd   = directory.modificationTime.getTime();

        return otherStart < thisEnd && thisStart < otherEnd;
    }



// =====================================================================
// Directory Specific
// =====================================================================

    /**
     * Set the parent directory of the item
     *
     * @param parent The new parent directory (can be null for root directories)
     *
     * @note protected so subclasses and Directory can acces it.
     */
    protected void setParentDirectory(Directory parent){
        this.parentDirectory = parent;
    }


    /**
     * Return the parent directory of the item.
     *
     * @return The parent directory of the item.
     */
    public Directory getParentDirectory {return parentDirectory;}


    /**
     * Checks whether an item is contained directly or indirectly within a given directory
     *
     * @param directory Directory of the item
     *
     * @return True if and only if the item is directly contained within the given directory
     */
    public Directory isDirectOrIndirectChildOf(Directory directory){

    }



}