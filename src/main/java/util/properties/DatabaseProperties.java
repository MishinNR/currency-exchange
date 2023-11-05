package util.properties;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.*;

@LoadPolicy(LoadType.MERGE)
@Sources({"classpath:application.properties"})
public interface DatabaseProperties extends Config {
    @Key("db.driver")
    String dbDriver();

    @Key("db.url")
    String dbUrl();

    @Key("db.username")
    String dbUsername();

    @Key("db.password")
    String dbPassword();
}
