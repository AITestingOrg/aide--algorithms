package aist.incremental.learning;

import interfaces.IDriverAdapter;
import interfaces.INode;
import interfaces.IPath;
import interfaces.IRelationship;

import java.util.List;
import neo4j.Neo4jDriverAdapter;

public class GeneralizationDemo {
    private static IDriverAdapter driver;

    static {
        driver = new Neo4jDriverAdapter("bolt://localhost:7687");
    }

    private IGeneralizationStrategy strategy;

    public static void main(String... args) {
        GeneralizationDemo demo = new GeneralizationDemo();
        demo.setGeneralizationStrategy(new GeneralizationWithDirection(
            "bolt://localhost:7687", "neo4j", "password"));
        int deleted = driver.deleteRelationship("name", "Toyota", "name", "person", "MOVE");
        System.out.println(deleted);
        driver.createRelationship("name", "Toyota", "name", "car", "EX");
        INode n = driver.getNode(2066);
        System.out.println(n);
        List<INode> list = driver.getRelatedNodes("name", "car");
        System.out.println(driver.getRelationshipsBetween("name", "car", "name", "transportation"));
        List<IPath> paths =
                driver.checkGeneralizedConnection("name", "Toyota", "name", "person", "MOVE");
        System.out.println();
        for (IPath path : paths) {
            System.out.println("This is one of the paths:");
            for (IRelationship step : path.relationships()) {
                System.out.print(driver.getNode(step.startNodeId()).get("name"));
                System.out.print("-" + step.type() + "->");
                System.out.println(driver.getNode(step.endNodeId()).get("name"));
            }
        }
        System.out.println(demo.checkRelation("name", "Toyota", "name", "person", "MOVE"));
        demo.strategy.close();
        driver.close();
    }

    void setGeneralizationStrategy(IGeneralizationStrategy strategy) {
        this.strategy = strategy;
    }

    boolean checkRelation(String key1, String value1, String key2, String value2, String rel) {
        return strategy.findIfRelationExists(key1, value1, key2, value2, rel);
    }
}
