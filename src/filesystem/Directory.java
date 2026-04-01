package filesystem;

import java.util.ArrayList;

public class Directory extends DiskItem{

    // =====================================================================
    // Fields
    // =====================================================================

    private static final String defaultName = "New-Directory";


    // =====================================================================
    // Constructors
    // =====================================================================


    /**
     * Initialize a new directory item with given parent directory, name and writability
     *
     * @param parent Parent directory of this directory
     *
     * @param name Name of this directory
     *
     * @param writable Writability status of this directory
     */
    public Directory(Directory parent, String name, boolean writable){
        super(parent, name, writable);
    }


    /**
     * Initialize a new directory item with given parent and name
     *
     * @param parent Parent directory of this directory
     *
     * @param name Name of this directory
     */
    public Directory(Directory parent, String name){
        super(parent, name, true);
    }

    public Directory(String name, boolean writable) {
        super(nullroot, name, writable)
    }

    // =====================================================================
    // Methods
    // =====================================================================

    public void addItem(DiskItem item){

    }

    public void removeItem(DiskItem item){

    }

    @Override
    public String getAbsolutePath() {

    }

    @Override
    public Directory getRoot() {

    }

    @Override
    public long getTotalDiskUsage() {

    }

    @Override
    public void terminate() {

    }

    /**
     * Check whether the given name is a valid directory name.
     *
     * @param name
     *        The name to check
     *
     * @return True if and only if the name is non-null, non-empty,
     *         and contains only letters, digits, dashes or underscores
     *      | result == (name != null && name.matches("[a-zA-Z0-9_\\-]+"))
     */
    @Override
    protected boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z0-9_\\-]+");
    }
}
;