package ntwhitfi.user.search.request.authorizer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class AuthPolicyCustom {
    // IAM Policy Constants
    public static final String VERSION = "Version";
    public static final String STATEMENT = "Statement";
    public static final String EFFECT = "Effect";
    public static final String ACTION = "Action";
    public static final String NOT_ACTION = "NotAction";
    public static final String RESOURCE = "Resource";
    public static final String NOT_RESOURCE = "NotResource";
    public static final String CONDITION = "Condition";
    public static final String ALLOW_EFFECT = "Allow";
    public static final String DENY_EFFECT = "Deny";

    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, ALL
    }

    String principalId;
    transient AuthPolicyCustom.PolicyDocument policyDocumentObject;
    Map<String, Object> PolicyDocument;

    public AuthPolicyCustom(String principalId, AuthPolicyCustom.PolicyDocument policyDocumentObject) {
        this.principalId = principalId;
        this.policyDocumentObject = policyDocumentObject;
    }

    public Map<String, Object> getPolicyDocument() {
        Map<String, Object> serializablePolicy = new HashMap<>();
        serializablePolicy.put(VERSION, policyDocumentObject.Version);

        List<Statement> statements = policyDocumentObject.getStatement();
        List<Map<String, Object>> serializableStatementList = new ArrayList<>();

        statements.forEach(statement -> {
            Map<String, Object> serializableStatement = new HashMap<>();
            serializableStatement.put(EFFECT, statement.Effect);
            serializableStatement.put(ACTION, statement.Action);
            serializableStatement.put(RESOURCE, statement.getResource());
            serializableStatement.put(CONDITION, statement.getCondition());
            serializableStatementList.add(serializableStatement);
        });
        serializablePolicy.put(STATEMENT, serializableStatementList);
        return serializablePolicy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PolicyDocument {
        //Policy doc constants
        private static String EXECUTE_API_ARN_FORMAT = "arn:aws:execute-api:%s:%s:%s/%s/%s/%s";
        private static String Version = "2012-10-17";

        //Statements for the policy doc
        private Statement allowStatement;
        private Statement denyStatement;
        private List<Statement> statements;

        //Policy doc context
        private String region;
        private String awsAccountId;
        private String restApiId;
        private String apiGatewayStage;

        public PolicyDocument(String region, String awsAccountId, String restApiId, String apiGatewayStage) {
            this.region = region;
            this.awsAccountId = awsAccountId;
            this.restApiId = restApiId;
            this.apiGatewayStage = apiGatewayStage;
            allowStatement = Statement.getEmptyInvokeStatement(ALLOW_EFFECT);
            denyStatement = Statement.getEmptyInvokeStatement(DENY_EFFECT);
            this.statements = new ArrayList<>();
            this.statements.add(allowStatement);
            this.statements.add(denyStatement);
        }

        public List<Statement> getStatement() {
            return statements;
        }

        public void allowMethod(HttpMethod httpMethod, String resourcePath) {
            addResourceToStatement(allowStatement, httpMethod, resourcePath);
        }

        public void denyMethod(HttpMethod httpMethod, String resourcePath) {
            addResourceToStatement(denyStatement, httpMethod, resourcePath);
        }

        public void addStatement(AuthPolicyCustom.Statement statement) {
            statements.add(statement);
        }

        private void addResourceToStatement(Statement statement, HttpMethod httpMethod, String resourcePath) {
            // resourcePath must start with '/'
            // to specify the root resource only, resourcePath should be an empty string
            if (resourcePath.equals("/")) {
                resourcePath = "";
            }
            //remove the "/" from the start of the path if it exists
            String resource = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
            String method = httpMethod == HttpMethod.ALL ? "*" : httpMethod.toString();
            statement.addResource(String.format(EXECUTE_API_ARN_FORMAT, region, awsAccountId, restApiId, apiGatewayStage, method, resource));
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Statement {
        private static String EXECUTE_API_ACTION = "execute-api:Invoke";

        private String Effect;
        private String Action;
        Map<String, Map<String, Object>> Condition;

        private List<String> resources;

        public Statement(String effect, String action, List<String> resources, Map<String, Map<String, Object>> condition) {
            this.Effect = effect;
            this.Action = action;
            this.resources = resources;
            this.Condition = condition;
        }

        public List<String> getResource() {
            return resources;
        }

        public static Statement getEmptyInvokeStatement(String effect) {
            return new Statement(effect, EXECUTE_API_ACTION, new ArrayList<>(), new HashMap<>());
        }

        public void addResource(String resource) {
            resources.add(resource);
        }

        public void addCondition(String operator, String key, Object value) {
            Condition.put(operator, Collections.singletonMap(key, value));
        }
    }
}
