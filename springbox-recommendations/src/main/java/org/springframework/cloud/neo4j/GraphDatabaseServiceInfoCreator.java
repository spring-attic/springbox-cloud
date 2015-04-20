package org.springframework.cloud.neo4j;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;

public class GraphDatabaseServiceInfoCreator extends CloudFoundryServiceInfoCreator<GraphDatabaseServiceInfo> {
    public GraphDatabaseServiceInfoCreator() {
        super(new Tags("pivotal", "neo4j"), "neo4j");
    }

    @Override
    public GraphDatabaseServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        @SuppressWarnings("unchecked")
        Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
        String id = (String) serviceData.get("name");

        if (uriKeyMatchesScheme(serviceData)) {
            return new GraphDatabaseServiceInfo(id, getStringFromCredentials(credentials, "neo4jUri"));
        } else {
            String host = getStringFromCredentials(credentials, "host");
            String username = getStringFromCredentials(credentials, "username");
            String password = getStringFromCredentials(credentials, "password");
            int httpPort = getIntFromCredentials(credentials, "http_port");
            return new GraphDatabaseServiceInfo(id, "http", host, httpPort, username, password, "/db/data");
        }
    }

}
