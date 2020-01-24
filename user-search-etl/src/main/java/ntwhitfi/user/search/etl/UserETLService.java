package ntwhitfi.user.search.etl;

import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.common.repository.IRepository;
import ntwhitfi.user.search.etl.model.HRUserRecord;
import ntwhitfi.user.search.etl.model.UserETLResponse;
import org.joda.time.DateTime;

import java.util.*;

public class UserETLService implements IUserETLService {
    private IRepository<List<HRUserRecord>> hrUserRecordRepository;
    private IRepository<List<UserRecord>> userRecordRepository;

    @SuppressWarnings("unchecked")
    private static Map<String, String> roleToDepartmentMap = new Gson().fromJson(System.getenv("ROLE_TO_DEPARTMENT_MAP"), Map.class);

    public UserETLService(final IRepository<List<HRUserRecord>> hrUserRecordRepository, final IRepository<List<UserRecord>> userRecordRepository) {
        this.hrUserRecordRepository = hrUserRecordRepository;
        this.userRecordRepository = userRecordRepository;
    }

    @Override
    public List<HRUserRecord> getAllHrUserRecords() {
        return hrUserRecordRepository.getAllRecords();
    }

    @Override
    public UserETLResponse pushUserRecords(List<UserRecord> userRecords) {
        try {
            userRecordRepository.pushRecords(userRecords);
            return UserETLResponse.builder().statusCode(200).statusMessage("Success!").build();
        } catch (Exception ex) {
            return UserETLResponse.builder().statusCode(500).statusMessage("ERROR: " + Arrays.toString(ex.getStackTrace())).build();
        }
    }

    @Override
    public List<UserRecord> transformHrUserRecords(List<HRUserRecord> hrUserRecords) {
        List<UserRecord> transformedUserRecords = new ArrayList<>();

        //transform each hr record to the user search record format
        hrUserRecords.forEach(hrUserRecord -> {
            transformedUserRecords.add(UserRecord.builder()
                    .active(getActiveRecordStatus(hrUserRecord))
                    .department(roleToDepartmentMap.getOrDefault(hrUserRecord.getRole(), "Unknown"))
                    .firstName(hrUserRecord.getFirstName())
                    .lastName(hrUserRecord.getLastName())
                    .searchTerm(getSearchTerm(hrUserRecord))
                    .id(hrUserRecord.getEmpId())
                    .build()
            );
        });

        return transformedUserRecords;
    }

    //Return false if the record contains a termination date that has already passed. Otherwise return true
    private Boolean getActiveRecordStatus(HRUserRecord hrUserRecord) {
        return StringUtils.isNullOrEmpty(hrUserRecord.getTerminationDate()) ||
                new DateTime(hrUserRecord.getTerminationDate()).isAfterNow();
    }

    //Return the searchTerm attribute for the record. This is created from the searchable terms
    private String getSearchTerm(HRUserRecord hrUserRecord) {
        return String.format("%s_%s_%s", hrUserRecord.getFirstName().toLowerCase(),
                hrUserRecord.getLastName().toLowerCase(),
                roleToDepartmentMap.getOrDefault(hrUserRecord.getRole(), "Unknown").toLowerCase());
    }
}
