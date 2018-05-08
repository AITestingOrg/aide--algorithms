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
    public boolean findIfRelationExists(String node1Value, String node2Value, String rel) {
        List<IPath> results = driver.checkGeneralizedConnection(node1Value, node2Value, rel);
        return !results.isEmpty();
    }

    @Override
    public void close() {
        driver.close();
    }
}
