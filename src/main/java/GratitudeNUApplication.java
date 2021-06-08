import com.fasterxml.jackson.databind.ObjectMapper;
import config.ApplicationConfig;
import db.dao.GameDAO;
import db.dao.InMemoryGameDAO;
import gratitudeNUGame.GratitudeNUGameManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import java.io.File;
import resources.GratitudeNUResource;

public class GratitudeNUApplication extends Application<ApplicationConfig> {

  public static void main(String[] args) throws Exception {
    new GratitudeNUApplication().run(args);
  }
  @Override
  public String getName() {
    return "GratitudeNUApplication";
  }

  public void run(ApplicationConfig applicationConfig, Environment environment) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    GameDAO gameDAO = objectMapper.readValue(new File(applicationConfig.getSeedDataLocation()), InMemoryGameDAO.class);

    GratitudeNUGameManager gameManager = new GratitudeNUGameManager(gameDAO);

    GratitudeNUResource gratitudeNUResource = new GratitudeNUResource(gameManager);

    environment.jersey().register(gratitudeNUResource);
  }
}
