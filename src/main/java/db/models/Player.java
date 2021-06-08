package db.models;

public class Player {
  int id;
  String name;

  public Player() {
  }

  public Player(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if(!(o instanceof Player))
      return false;

    Player other = (Player) o;
    return this.name.equals(other.name) && this.id == other.id;
  }

  @Override
  public final int hashCode() {
    return this.id + this.name.hashCode();
  }
}
