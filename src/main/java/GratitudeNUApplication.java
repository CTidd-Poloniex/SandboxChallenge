import com.fasterxml.jackson.databind.ObjectMapper;
import config.ApplicationConfig;
import db.dao.GameDAO;
import db.dao.InMemoryGameDAO;
import gratitudeNUGame.GratitudeNUGameManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import java.io.File;
import java.util.EnumSet;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import resources.GratitudeNUResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

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

    final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }
}
