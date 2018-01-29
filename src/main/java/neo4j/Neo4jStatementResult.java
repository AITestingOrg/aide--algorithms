package neo4j;

import interfaces.IRecord;
import interfaces.IStatementResult;

import java.util.List;
import org.neo4j.driver.v1.StatementResult;

public class Neo4jStatementResult implements IStatementResult {
    private StatementResult statementResult;

    public Neo4jStatementResult(StatementResult statementResult) {
        this.statementResult = statementResult;
    }

    public IRecord single() {
        return new Neo4jRecord(statementResult.single());
    }

    public boolean hasNext() {
        return statementResult.hasNext();
    }

    public IRecord next() {
        return new Neo4jRecord(statementResult.next());
    }

    public IRecord peek() {
        return new Neo4jRecord(statementResult.peek());
    }

    public List<String> keys() {
        return statementResult.keys();
    }
}
