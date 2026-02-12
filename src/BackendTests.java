import java.io.IOException;
import java.util.List;

public class BackendTests {

    /**
     * Tests the readData method to see if data is correctly inserted from "records.csv" by
     * validating that the final value in the imported list has the name "bobaGl1mm3r".
     * 
     * Also tests the getAndSetRange method to see if list is correctly filtered by setting the low value
     * to 0, and the high value to 400. Then, it checks if the filtered list has size = 1 and contains the
     * name "v0idt3mp0".
     * 
     * Then, performs another test to see if results are accurate when there is a previously set completion_time
     * filter using applyAndSetFilter() with a filter time of "005:00:00."
     * 
     * @return True if the imported list contains "bobaGl1mmer", if the filtered list contains 
     * only 1 name and it contains "v0idt3mp0," and if after setting a completion_time
     * restriction of < "005:00:00," filtered list is empty, otherwise returns false.
     * @throws IOException
     */
    public static boolean roleTest1() throws IOException {

        // Instantiate test tree and backend.
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend tester = new Backend(tree);

        // Read in data from records.csv, should only add final value of .csv file.
        tester.readData("src\\records.csv");

        // Retrieve full list of names without any filter.
        List<String> fullList = tester.getAndSetRange(null, null);

        // Check to see if list contains the name of the final record from .csv file.
        if (!fullList.getLast().equals("bobaGl1mm3r")) {
            return false;
        }

        // Set range to 0-400.
        List<String> filteredList = tester.getAndSetRange(0, 400);

        // Check to see if filteredList has only one value with the name "v0idt3mp0."
        if (filteredList.size() != 1 || !filteredList.contains("v0idt3mp0")) {
            return false;
        }

        // With previously set range, set filter to < "005:00:00"
        List<String> filteredList2 = tester.applyAndSetFilter("005:00:00");

        // Check to see if filteredList2 is empty.
        if (!filteredList2.isEmpty()) {
            return false;
        }

        return true;
    }


    /** 
     * Tests the applyAndSetFilter() method both with and without a previously set range restriction using
     * getAndSetRange().
     */
    public static boolean roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();

        // Make a new record to insert into the tree.
        GameRecord newRecord = new GameRecord(
            "heimerlF", 
            GameRecord.Continent.NORTH_AMERICA, 
            50000, 
            2000, 
            800, 
            "001:32:59"
        );

        // Insert newly created value into the tree.
        tree.insert(newRecord);
        
        // Instantiate test Backend object.
        Backend tester = new Backend(tree);
        
        // Set filter to max
        List<String> list = tester.applyAndSetFilter("999:99:99");

        // Check if list contains "xXxgamer47xXx" and has 4 values stored.
        if (!list.contains("xXxgamer47xXx") || list.size() != 4) {
            return false;
        }

        // Set time filter to < "635:00:00."
        List<String> filteredList = tester.applyAndSetFilter("635:00:00");


        // Check if list contains "heimerlF" and "v0idt3mp0" and has 2 values stored and doesn't contain "xXxgamer47xXx"
        if (!filteredList.contains("heimerlF") || !filteredList.contains("v0idt3mp0") || filteredList.size() != 2 || filteredList.contains("xXxgamer47xXx")) {
            return false;
        }

        // With previously set time filter, set range to 600-800.
        List<String> filteredList2 = tester.getAndSetRange(600, 800);

        // Check if list contains "heimerlF" and has 1 value stored and doesn't contain "xXxgamer47xXx" or "v0idt3mp0"
        if (!filteredList2.contains("heimerlF") || filteredList2.size() != 1 || filteredList2.contains("xXxgamer47xXx") || filteredList2.contains("v0idt3mp0")) {
            return false;
        }

        return true;
    }

    /**
     * Tests getTopTen() method with both time and range filters, and without.
     * @return
     */
    public static boolean roleTest3() {
        Tree_Placeholder tree = new Tree_Placeholder();

        // Make a new record to insert into the tree.
        GameRecord newRecord = new GameRecord(
            "the_law", 
            GameRecord.Continent.NORTH_AMERICA, 
            99999, 
            9999, 
            5000, 
            "000:05:01"
        );

        // Insert newly created value into the tree.
        tree.insert(newRecord);
        
        // Instantiate test Backend object.
        Backend tester = new Backend(tree);

        // Retrieve top ten names list.
        List<String> topTen1 = tester.getTopTen();

        // Check to make sure user "the_law" is in first place, and "speedRoyalty" is in last out of all top ten (or less) users,
        // Also check if top ten list has four entries.
        if (!topTen1.get(0).equals("the_law") || !topTen1.getLast().equals("speedRoyalty") || topTen1.size() != 4) {
            return false;
        }

        // Set range to 0-1000.
        List<String> topTen2 = tester.getAndSetRange(0, 1000);

        // Check to make sure topTen2 doesn't contain "the_law," and has "v0idt3mp0" in first place, and has three entries.
        if (!topTen2.get(0).equals("v0idt3mp0") || topTen2.contains("the_law") || topTen2.size() != 3) {
            return false;
        }

        // Set time filter to < "650:00:00".
        List<String> topTen3 = tester.applyAndSetFilter("650:00:00");

        // Check to make sure topTen2 doesn't contain "the_law" or "xXxgamer47xXx," has "v0idt3mp0" in first place, and has two entries.
        if (!topTen3.get(0).equals("v0idt3mp0") || topTen3.contains("the_law") || topTen3.contains("xXxgamer47xXx") || topTen3.size() != 2) {
            return false;
        }

        return true;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("RoleTest1: " + roleTest1());

        System.out.println("RoleTest2: " + roleTest2());

        System.out.println("RoleTest3: " + roleTest3());
    }

}
