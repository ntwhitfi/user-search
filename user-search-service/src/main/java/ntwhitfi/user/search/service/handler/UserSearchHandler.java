package ntwhitfi.user.search.service.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.service.config.DaggerUserSearchComponent;
import ntwhitfi.user.search.service.config.UserSearchComponent;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.service.model.UserSearchRequest;
import ntwhitfi.user.search.service.model.UserSearchResponse;
import ntwhitfi.user.search.service.IUserSearchService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserSearchHandler implements RequestHandler<UserSearchRequest, UserSearchResponse> {

    private static String QUERY_TYPE = System.getenv("QUERY_TYPE");
    private static String GET_ALL_TYPE = System.getenv("GET_ALL_TYPE");
    private Gson gson = new Gson();

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
        log.info("User search request: " + gson.toJson(userSearchRequest));

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

        UserSearchResponse userSearchResponse = UserSearchResponse.builder().statusCode(statusCode).statusMessage(statusMessage).userRecords(userRecords).build();

        log.info("User search response: " + gson.toJson(userSearchResponse));
        return userSearchResponse;
    }
}
