package filesystem;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Date;


/**
 * A class representing a file with a name, size, and writable state.
 * OOP Practicum 1
 *
 * @author Casper Vermeren; Loïck Sansen
 * @version 1.12
 *
 *
 * @invar The size of the file must always be valid
 *      | canHaveAsSize(getSize())
 */

public class File extends DiskItem {

    // =====================================================================
    // Fields
    // =====================================================================

    private static final String defaultName = "New-File";
    private FileType type;
    private int size = 0;
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
    public File(Directory parent, String name) {
        this(parent, name, 0, true, FileType.TXT);
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
    public File(Directory parent, String name, int size, boolean writable, FileType type) {
        super(parent, name, writable);
        this.type = type;
        this.setSize(size);
    }

    /**
     * Initialize this new file with given parent directory, name and type
     *
     * @param parent
     *        The parent directory of this file
     *
     * @param name
     *        The name of this file
     *
     * @param type
     *        The type of this file
     */
    public File(Directory parent, String name, FileType type) {
        this(parent, name, 0, true, type);
    }

    // =====================================================================
    // Name
    // =====================================================================

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
    protected boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z0-9_.\\-]+");
    }

    /**
     * Return the default name of this file item
     *
     * @return Default name "New-File"
     */
    @Override
    public String getDefaultName(){
        return "New-File";
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
    // Other
    // =====================================================================

    /**
     * Returns the absolute path of this file.
     *
     * @return absolute path of this file
     */
    @Override
    public String getAbsolutePath(){
        return getParentDirectory().getAbsolutePath() + "/" + getName() + "." + getType().getExtension();
    }


    /**
     * Returns the total disk usage of this file.
     *
     * @return file size
     */
    @Override
    public long getTotalDiskUsage(){
        return this.getSize();
    }

    /**
     * Returns the root directory to which an item directly or indirectly belongs
     *
     * @return
     */
    @Override
    public Directory getRoot(){
        return getParentDirectory().getRoot();
    }

    /**
     * Returns the type of this item
     *
     * @return type of this item
     */
    public FileType getType() {
        return type;
    }


}