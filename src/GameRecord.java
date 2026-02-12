public class GameRecord implements Comparable<GameRecord> {

  private String name;
  private GameRecord.Continent location;
  private int score; //calculated by 46*collectables + 50*level + 17933
  private int collectables;
  private int level; //levels completed
  private String completionTime; //formatted in hhh:mm:ss

  //constructor
  public GameRecord(String name, GameRecord.Continent location, int score, int collectables, int level, String completionTime) {
    this.name = name;
    this.location = location;
    this.score = score;
    this.collectables = collectables;
    this.level = level;
    this.completionTime = completionTime;
  }

  //accessors
  public String getName() {return this.name;}
  public GameRecord.Continent getContinent(){return this.location;}
  public int getScore() {return this.score;}
  public int getCollectables() {return this.collectables;}
  public int getLevel() {return this.level;}
  public String getCompletionTime() {return this.completionTime;}


  // comparisons are made using level, larger numbers are bigger
  @Override
  public int compareTo(GameRecord other) {
    return this.level-other.level;
  }

  protected static enum Continent {
    AFRICA, ASIA, ANTARCTICA, AUSTRALIA, EUROPE, NORTH_AMERICA, SOUTH_AMERICA
  }

  @Override
  public String toString() {
    return this.name + ", " + this.location + ", " + this.score + ", " + this.collectables + ", " + this.level + ", (" + this.completionTime + ")";
  }

}
