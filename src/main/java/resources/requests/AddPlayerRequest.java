package resources.requests;

import javax.validation.constraints.NotNull;

public class AddPlayerRequest {

  @NotNull
  String name;

  public AddPlayerRequest() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
