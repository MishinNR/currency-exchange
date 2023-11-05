package util.properties;

import org.aeonbits.owner.ConfigFactory;

public class Properties {
    public static final DatabaseProperties DATABASE_PROPERTIES = ConfigFactory.create(DatabaseProperties.class);
}
