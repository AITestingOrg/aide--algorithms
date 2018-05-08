package org.aist.semanticnetwork.neo4j;

import java.util.List;

import org.aist.semanticnetwork.interfaces.IStatementResult;
import org.neo4j.driver.v1.StatementResult;

public class Neo4jStatementResult implements IStatementResult {
    private StatementResult statementResult;

    public Neo4jStatementResult(StatementResult statementResult) {
        this.statementResult = statementResult;
    }

    public Neo4jRecord single() {
        return new Neo4jRecord(statementResult.single());
    }

    public boolean hasNext() {
        return statementResult.hasNext();
    }

    public Neo4jRecord next() {
        return new Neo4jRecord(statementResult.next());
    }

    public Neo4jRecord peek() {
        return new Neo4jRecord(statementResult.peek());
    }

    public List<String> keys() {
        return statementResult.keys();
    }
}
