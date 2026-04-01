package filesystem;

import be.kuleuven.cs.som.annotate.Raw;
import be.kuleuven.cs.som.annotate.Basic;
import java.util.Date;

/**
 * A class representing an item with a name and writable state
 * OOP Practicum 2
 *
 * @author Casper Vermeeren; Loïck Sansen
 * @Version 1.2
 *
 * @invar The name of the item must always be valid
 *      | isValidName(getName())
 *
 * @invar The creation time must always be valid
 *      |isValidCreationTime(getCreationTime())
 *
 * @invar The modification time must always be valid
 *      | isValidModificationTime(getModificationTime())
 */

public abstract class DiskItem {

// =====================================================================
// Fields
// =====================================================================

    protected String name = null;
    protected String defaultName = null;
    protected Date creationTime = null;
    protected Date modificationTime = null;
    protected Directory parentDirectory = null;
    protected boolean writable = false;
    protected boolean terminated = false;


// =====================================================================
// Constructors
// =====================================================================

    /**
     * Initializes a new disk item with given parent directory, name and writability
     *
     * @effect This item's name will be set to the given name
     *      | setName(name)
     *
     * @effect This item's writability will be set to the given writability
     *      | setWritable(writable)
     *
     * @param parent
     *        The parent directory of this item
     *
     * @param name
     *        The name of this item
     *
     * @param writable
     *        The writability of this item
     */
    public DiskItem(Directory parent, String name, boolean writable){
        this.move(parent);
        this.setName(name);
        this.setWritable(writable);
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
    public abstract String getAbsolutePath();


    /**
     * Return the root of the item
     *
     * @return The root of the item
     */
    public abstract Directory getRoot();


    /**
     * Return the total disk usage of the item
     *
     * @return the total disk usage of the item
     */
    public abstract long getTotalDiskUsage();


    /**
     * Move this item to target directory
     * @throws IllegalStateException If the target directory does not exist
     *      | !canHaveAsParentDirectory(targetDirectory)
     *
     * @post Remove this item from the directory it was already in
     *      | this.getParentDirectory().hasAsItem(new)
     *
     * @post This item is added to given target directory
     *      | targetDirectory().hasAsItem(new)
     *
     * @post The target directory becomes the new parent directory
     *      |new.getParentDirectory() == targetDirectory
     *
     * @param target The target directory to which the item will be moved
     */
    public void move(Directory target) throws MoveException{
        if (target == null){
            throw new MoveException("Directory does not exist");
        }
        target.addItem(this);

        if(this.parentDirectory != null){
            this.parentDirectory.removeItem(this);
        }
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
        return terminated;
    }

    /**
     * Returns whether item is terminated
     *
     * @param terminated the new termination status of the item.
     */
    protected void setTerminated(boolean terminated) {
        this.terminated = terminated;
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
     * Checks whether the name of this item is valid
     *
     * @param name Name of the item
     *
     * @return NO RETURN IN ABSTRACT METHOD SINCE IT HAS NO BODY????????
     */
    protected abstract boolean isValidName(String name);

    /**
     * Return the default name of the item.
     *
     */
    protected abstract String getDefaultName();


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
        if (isValidName(name)){
            this.name = name;
        } else {
            this.name = getDefaultName();
        }
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
     * Checks the creation time of the item
     *
     * @param creationTime Creation time of the item
     *
     * @return True if and only if the creation time is not null
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
     * Check whether this item has an overlapping use period with the given file.
     *      The use period of a item spans from its creation time to its last modification time.
     *      Two items overlap if both have been modified and their use periods intersect.
     *
     * @param directory
     *        The directory to check overlap with
     *
     * @return True if and only if both items have a modification time and their use periods overlap
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
    public Directory getParentDirectory() {return parentDirectory;}


    /**
     * Checks whether an item has given directory as parent directory
     *
     * @return
     */
    public boolean canHaveAsParentDirectory(Directory directory){
        // Check if parent directory is valid
        if (parentDirectory == null || parentDirectory.isTerminated()) {
            return false;
        }

        // Directory cannot have itself as parent
        if (this == directory) {
            return false;
        }

        // Parent directory cannot already contain an item with the saùe name
        if (parentDirectory.containsDiskItemWithName(this.getName())){
            return false;
        }

        //Parent directory cannot be one of its own children
        if (parentDirectory.isDirectOrIndirectChildOf(this)) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether an item is contained directly or indirectly within a given directory
     *
     * @param directory Directory of the item
     *
     * @return True if and only if the item is directly contained within the given directory
     */
    public Directory isDirectOrIndirectChildOf(DiskItem directory){

    }

}