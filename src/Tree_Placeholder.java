import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This is a placeholder for the fully working Tree that will be developed in a
 * future week.  It is designed to help develop and test the functionality of
 * Backend code for Project1.  Note the limitations described below.
 */
public class Tree_Placeholder implements IterableSortedCollection<GameRecord> {

    // SortedCollection<GameRecord> methods: Only remembers the last record
    // that was added and confirms whether that record is contained in this
    // collection vs not.
    // This field is public to allow test code to access it.
    public GameRecord lastAddedGameRecord = null;

    @Override
    public void insert(GameRecord data) throws NullPointerException {
      this.lastAddedGameRecord = data; 
    }

    @Override
    public boolean contains(Comparable<GameRecord> find) {
      return find.compareTo(lastAddedGameRecord) == 0;
    }
    
    public int size() {
      if(lastAddedGameRecord == null) return 3;
      else return 4;
    }

    public boolean isEmpty() {
      return false;
    }
    
    public void clear() {
      throw new UnsupportedOperationException("cannot call on placeholder");
    }

    // IterableSortedCollection<GameRecord> methods: holds a fixed list of
    // the following three records that are ordered alphabetically by title.
    // If a this.lastAddedGameRecord exists, that is added to the list that is then
    // filtered to only contain values between the specified min and max in any
    // iterators that are created.
    private List<GameRecord> records = Arrays.asList(
        new GameRecord("speedRoyalty", GameRecord.Continent.EUROPE, 47403,120,479, "640:09:00"),
        new GameRecord("xXxgamer47xXx", GameRecord.Continent.ASIA, 48563,130,493, "956:48:46"),
        new GameRecord("v0idt3mp0", GameRecord.Continent.AFRICA, 42023,140,353, "634:06:42"));

    private Comparable<GameRecord> min = null;
    private Comparable<GameRecord> max = null;
    public void setIteratorMin(Comparable<GameRecord> min) { this.min = min; }
    public void setIteratorMax(Comparable<GameRecord> max) { this.max = max; }

    public Iterator<GameRecord> iterator() {
        List<GameRecord> tmp = new ArrayList<>(records); // make a copy of list
        if(lastAddedGameRecord != null) tmp.add(lastAddedGameRecord); // with added record

        // remove all records that are outside of the specified min-max range
        for(int i=0;i<tmp.size();i++)
            if( (min != null && min.compareTo(tmp.get(i)) > 0) ||
                (max != null && max.compareTo(tmp.get(i)) < 0)) {

                tmp.remove(i);
                i--;
            }

        // and return a new iterator that steps through the remaining values

        return tmp.iterator();
    }
}

