package config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.Valid;

public class ApplicationConfig extends Configuration {
  @Valid
  private String seedDataLocation;

  @JsonProperty
  public String getSeedDataLocation() {
    return seedDataLocation;
  }

  @JsonProperty
  public void setSeedDataLocation(String seedDataLocation) {
    this.seedDataLocation = seedDataLocation;
  }
}
