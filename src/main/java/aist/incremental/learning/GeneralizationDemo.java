package aist.incremental.learning;

import interfaces.IDriverAdapter;
import interfaces.INode;
import interfaces.IPath;
import interfaces.IRelationship;

import java.util.List;
import neo4j.Neo4jDriverAdapter;
import neo4j.Neo4jRelationship;

public class GeneralizationDemo {
    private static IDriverAdapter driver;

    static {
        driver = new Neo4jDriverAdapter("bolt://localhost:7687");
    }

    private IGeneralizationStrategy strategy;

    public static void main(String... args) {
        GeneralizationDemo demo = new GeneralizationDemo();
        driver.cleanLabel("Greeting");
        demo.setGeneralizationStrategy(new GeneralizationWithDirection(
            "bolt://localhost:7687", "neo4j", "test"));
        System.out.println(driver.getNode("car"));
        List<INode> list = driver.getRelatedNodes("car");

        List<IRelationship> rels = driver.getRelationshipsBetween("car", "person");
        List<IPath> paths =
                driver.checkGeneralizedConnection("Toyota", "person", "MOVE");

        System.out.println();
        for (IPath path : paths) {
            System.out.println("This is one of the paths:");
            for (IRelationship step : path.relationships()) {
                System.out.print(driver.getNode(step.startNodeId()).get("name"));
                System.out.print("-" + step.type() + "->");
                System.out.println(driver.getNode(step.endNodeId()).get("name"));
            }
        }
        System.out.println(demo.checkRelation("Toyota","person", "MOVE"));
        demo.strategy.close();
        driver.close();
    }

    void setGeneralizationStrategy(IGeneralizationStrategy strategy) {
        this.strategy = strategy;
    }

    boolean checkRelation(String value1, String value2, String rel) {
        return strategy.findIfRelationExists(value1, value2, rel);
    }
}
