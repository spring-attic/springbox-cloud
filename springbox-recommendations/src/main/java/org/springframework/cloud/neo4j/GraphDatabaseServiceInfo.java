package org.springframework.cloud.neo4j;

import org.springframework.cloud.service.UriBasedServiceInfo;

public class GraphDatabaseServiceInfo extends UriBasedServiceInfo {

    public GraphDatabaseServiceInfo(String id, String uriString) {
        super(id, uriString);
    }

    public GraphDatabaseServiceInfo(String id, String scheme, String host, int port, String username, String password, String path) {
        super(id, scheme, host, port, username, password, path);
    }
}
