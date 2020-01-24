package ntwhitfi.user.search.etl.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.etl.IUserETLService;
import ntwhitfi.user.search.etl.UserETLService;
import ntwhitfi.user.search.etl.config.DaggerUserETLComponent;
import ntwhitfi.user.search.etl.config.UserETLComponent;
import ntwhitfi.user.search.etl.model.HRUserRecord;
import ntwhitfi.user.search.etl.model.UserETLRequest;
import ntwhitfi.user.search.etl.model.UserETLResponse;

import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
public class UserETLHandler implements RequestHandler<UserETLRequest, UserETLResponse> {
    private static String USER_ETL_START_COMMAND = System.getenv("USER_ETL_START_COMMAND");
    private Gson gson = new Gson();

    private IUserETLService userETLService;

    public UserETLHandler() {
        this(DaggerUserETLComponent.builder().build());
    }

    public UserETLHandler(UserETLComponent userETLComponent) {
        this(userETLComponent.userETLService());
    }

    public UserETLHandler(IUserETLService userETLService) {
        this.userETLService = userETLService;
    }


    @Override
    public UserETLResponse handleRequest(UserETLRequest userETLRequest, Context context) {
        //check the command from the request
        if(USER_ETL_START_COMMAND.equals(userETLRequest.getCommand())) {
            //pull all the HR user records
            List<HRUserRecord> hrUserRecords = userETLService.getAllHrUserRecords();
            log.info("User records being processed: " + gson.toJson(hrUserRecords));

            //transform the records to user search record format
            List<UserRecord> transformedUserRecords = userETLService.transformHrUserRecords(hrUserRecords);
            log.info("Transformed user records: " + gson.toJson(transformedUserRecords));

            //push the transformed records to the UserSearchTable
            return userETLService.pushUserRecords(transformedUserRecords);
        }

        //return error response if the command was not found
        return UserETLResponse.builder()
                .statusCode(400)
                .statusMessage("ERROR: Command " + userETLRequest.getCommand() + " is not supported")
                .build();
    }
}
