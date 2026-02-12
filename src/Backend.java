import java.io.*;
import java.util.*;

public class Backend implements BackendInterface {

    private IterableSortedCollection<GameRecord> tree;
    private String completionTimeFilter = null;
    private Comparable<GameRecord> minLevelFilter = null;
    private Comparable<GameRecord> maxLevelFilter = null;


    public Backend(IterableSortedCollection<GameRecord> tree) {
        this.tree = tree;
        this.completionTimeFilter = null;
        this.minLevelFilter = null;
        this.maxLevelFilter = null;
    }
    // Your constructor must have the signature above. All methods below must
    // use the provided tree to store, sort, and iterate through records. This
    // will enable you to create some tests that use the placeholder tree, and
    // others that make use of a working tree, depending on what is passed
    // into this constructor.

    /** Add and stores the specified record to the tree. Don't forget that the GameRecord
     *  must have the Comparator set. This will be used to store these records in order within your
     *  tree, and to retrieve them by level range in the getRange method.
     * @param record the game record to add
     */
    @Override
    public void addRecord(GameRecord record) {
        this.tree.insert(record);
    }
  
    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided records.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other record qualities.
     * After reading records from the file, the records are inserted into
     * the tree passed to this backend's constructor. This will be used to store these records in order within your
     * tree, and to retrieve them by level range in the getRange method.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    @Override
    public void readData(String filename) throws IOException {
        try {
            File file = new File(filename); // access file 

                if (file.exists()) {
                    Scanner inFile = new Scanner(file);

                    int lineNum = 0;

                    Map<String, Integer> columns = new HashMap<>();

                    while (inFile.hasNextLine()) {
                        
                        String line = inFile.nextLine();
                        String[] token = line.split("," ); // Tokenize current line.

                        // Parse to find which column is associated with each data type.
                        if (lineNum == 0) {
                            for (int i = 0; i < token.length; i++) {
                                columns.put(token[i].toLowerCase(), i);
                            }
                        } else { // Parse each individual record for data, then add new GameRecord object to the tree.
                            GameRecord newRecord = new GameRecord(
                                token[columns.get("name")], // name
                                GameRecord.Continent.valueOf(token[columns.get("continent")]), // location
                                Integer.parseInt(token[columns.get("score")]), // score
                                Integer.parseInt(token[columns.get("collectables")]), // collectables
                                Integer.parseInt(token[columns.get("level")]), // level
                                token[columns.get("completion_time")] // completionTime
                            ); 

                            this.addRecord(newRecord); // Add new record to tree.
                        }
                        lineNum++;
                    }

                    inFile.close();
                } else {
                    System.err.println("File not found: " + file.getAbsolutePath());
                }
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of names from the tree passed to the constructor.
     * The records should be ordered by the record's level, and fall within
     * the specified range of level values.  This level range will
     * also be used by future calls to filterRecords and getTopTen.
     *
     * If a completion time filter has been set using the filterRecords method
     * below, then only records that pass that filter should be included in the
     * list of names returned by this method.
     *
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a 
     * null argument for the high parameter means that there is no maximum 
     * level to include in the returned list.
     *
     * @param low is the minimum level of records in the returned list
     * @param high is the maximum level of records in the returned list
     * @return List of names for all records from low to high that pass any
     *     set filter, or an empty list when no such records can be found
     */
    @Override
    public List<String> getAndSetRange(Integer low, Integer high) {
        // Create empty GameRecord objects that hold low and high level values to pass to iterator
        if (low != null && high != null) {
            GameRecord min = new GameRecord(null, null, 0, 0, low, null);
            GameRecord max = new GameRecord(null, null, 0, 0, high, null);
            
            // Set iterator bounds.
            this.tree.setIteratorMin(min);
            this.tree.setIteratorMax(max);

            this.minLevelFilter = min;
            this.maxLevelFilter = max;
        }

        

        // Retrieve iterator and set up filtered list.
        Iterator<GameRecord> iterator = this.tree.iterator();
        List<GameRecord> records = new ArrayList<>();

        // Copy names of all GameRecord objects within the tree that are within the bounds.
        while (iterator.hasNext()) {
            GameRecord currentRecord = iterator.next();
            records.add(currentRecord);
        }

        // Remove records that have a lower completion time value than the current completionTimeFilter.
        if (this.completionTimeFilter != null) {
            for(int i = 0; i < records.size(); i++) {
                GameRecord record = records.get(i);
                if (record.getCompletionTime().compareTo(this.completionTimeFilter) > 0) {
                    records.remove(record);
                    i--;
                }
            }
        }

        

        // Sort records by level value in ascending order.
        records.sort(null);

        // Create string list for storing player names in same order.
        List<String> namesList = new ArrayList<>();

        // Load player names from sorted GameRecord objects
        for (GameRecord record : records) {
            namesList.add(record.getName());
        }

        return namesList; // Return filtered list of names.
    }

    /**
     * Retrieves a list of record names that have a completion time that is smaller than the specified
     * completion time.  
     * Similar to the getRange method: this list of record names should be ordered by the records'
     * level, and should only include records that fall within the specified
     * range of level values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high level bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and getTopTen methods.
     *
     * When null is passed as the completion time to this method, then no 
     * completion time filter should be used.  This clears the filter.
     *
     * @param completion time filters returned record names to only include records that
     *     have a completion time that are smaller than the specified value. Formatted hhh:mm:ss
     * @return List of names for records that meet this filter requirement and
     *     are within any previously set level range, or an empty list 
     *     when no such records can be found
     */
    @Override
    public List<String> applyAndSetFilter(String time) {
        // Inherit current iterator bounds.
        this.tree.setIteratorMin(this.minLevelFilter);
        this.tree.setIteratorMax(this.maxLevelFilter);

        // Set time filter
        this.completionTimeFilter = time;

        // Retrieve iterator object and setup records list.
        Iterator<GameRecord> iterator = this.tree.iterator();
        List<GameRecord> records = new ArrayList<>();
        
        // Add records from iterator to list.
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        // Remove records that have a lower completion time value than the given time.
        for(int i = 0; i < records.size(); i++) {
            GameRecord record = records.get(i);
            if (record.getCompletionTime().compareTo(time) > 0) {
                records.remove(record);
                i--;
            }
        }

        // Sort records list by level in ascending order.
        records.sort(null);

        // Create string list for storing player names in same order.
        List<String> namesList = new ArrayList<>();

        // Load player names from sorted GameRecord objects
        for (GameRecord record : records) {
            namesList.add(record.getName());
        }

        return namesList; // Return filtered list of names.
    }

    /**
     * This method returns a list of record names representing the top
     * ten records with the most collectables that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to filteredRecords.  The order of the record names 
     * in this returned list is up to you.
     *
     * If fewer than ten such records exist, return all of them.  And return an
     * empty list when there are no such records.
     *
     * @return List of ten record names with the most collectables
     */
    @Override
    public List<String> getTopTen() {
        // Inherit current iterator bounds.
        this.tree.setIteratorMin(this.minLevelFilter);
        this.tree.setIteratorMax(this.maxLevelFilter);

        // Retrieve iterator object and setup records list.
        Iterator<GameRecord> iterator = this.tree.iterator();
        List<GameRecord> records = new ArrayList<>();
        
        // Add records from iterator to list.
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        // Remove records that have a lower completion time value than the current completionTimeFilter.
        if (this.completionTimeFilter != null) {
            for(int i = 0; i < records.size(); i++) {
                GameRecord record = records.get(i);
                if (record.getCompletionTime().compareTo(this.completionTimeFilter) > 0) {
                    records.remove(record);
                    i--;
                }
            }
        }

        // Set up array for top 10 list sorted by # of collectables in descending order.
        ArrayList<GameRecord> topTen = new ArrayList<>();

        // For all records, compare with each existing member of top 10, and place accordingly.
        for (GameRecord record : records) {
            if (topTen.size() == 0) {
                topTen.add(record);
            } else {
                int indexToInsert = 0;
                for (int i = 0; i < topTen.size(); i++) {
                    GameRecord recordToCompare = topTen.get(i);
                    if (recordToCompare != null) {

                        // if record has more collectables than recordToCompare, then shift recordToCompare down by 1 slot and replace it with record.
                        if (record.getCollectables() < recordToCompare.getCollectables()) {
                            indexToInsert = i + 1;
                        }

                        if (i >= 10) {
                            topTen.remove(i);
                        } 
                    }
                }
                topTen.add(indexToInsert, record);
            }
        }

        // Create string list for storing player names in same order.
        List<String> namesList = new ArrayList<>();

        // Load player names from TopTen array
        for (GameRecord record : topTen) {
            if (record != null) {
                namesList.add(record.getName());
            }
        }

        return namesList; // Return filtered list of names.
    }
}
