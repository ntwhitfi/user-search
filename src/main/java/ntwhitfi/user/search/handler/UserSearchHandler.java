package ntwhitfi.user.search.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import ntwhitfi.user.search.config.DaggerUserSearchComponent;
import ntwhitfi.user.search.config.UserSearchComponent;
import ntwhitfi.user.search.model.UserRecord;
import ntwhitfi.user.search.model.UserSearchRequest;
import ntwhitfi.user.search.model.UserSearchResponse;
import ntwhitfi.user.search.service.IUserSearchService;

import java.util.ArrayList;
import java.util.List;

public class UserSearchHandler implements RequestHandler<UserSearchRequest, UserSearchResponse> {

    private static String QUERY_TYPE = System.getenv("QUERY_TYPE");
    private static String GET_ALL_TYPE = System.getenv("GET_ALL_TYPE");

    private IUserSearchService userSearchService;

    public UserSearchHandler(IUserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    public UserSearchHandler(final UserSearchComponent userSearchComponent) {
        this(userSearchComponent.userSearchService());
    }

    public UserSearchHandler() {
        this(DaggerUserSearchComponent.create());
    }

    @Override
    public UserSearchResponse handleRequest(UserSearchRequest userSearchRequest, Context context) {
        List<UserRecord> userRecords = new ArrayList<>();
        int statusCode;
        String statusMessage;

        //Search for records if the request type is a query.
        if(userSearchRequest.getType().equals(QUERY_TYPE)) {
            userRecords = userSearchService.searchUserRecords(userSearchRequest.getQuery());
            statusCode = 200;
            statusMessage = "Success!";
        }
        //Return all records if the search type is a get all request
        else if (userSearchRequest.getType().equals(GET_ALL_TYPE)) {
            userRecords = userSearchService.getAllUserRecords();
            statusCode = 200;
            statusMessage = "Success!";
        }
        //If no valid type was found, return an error response.
        else {
            statusCode = 400;
            statusMessage = "Invalid message type provided.";
        }

        return UserSearchResponse.builder().statusCode(statusCode).statusMessage(statusMessage).userRecords(userRecords).build();
    }
}
