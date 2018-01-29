package aist.incremental.learning;

import interfaces.IDriverAdapter;
import interfaces.IPath;

import java.util.List;
import neo4j.Neo4jDriverAdapter;

public class GeneralizationWithDirection implements IGeneralizationStrategy {
    private IDriverAdapter driver;

    GeneralizationWithDirection(String uri, String user, String password) {
        driver = new Neo4jDriverAdapter(uri, user, password);
    }

    @Override
    public boolean findIfRelationExists(String key1, String value1, String key2, String value2, String rel) {
        List<IPath> results = driver.checkGeneralizedConnection(key1, value1, key2, value2, rel);
        return !results.isEmpty();
    }

    @Override
    public void close() {
        driver.close();
    }
}
