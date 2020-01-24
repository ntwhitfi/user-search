package ntwhitfi.user.search.etl;

import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.etl.model.HRUserRecord;
import ntwhitfi.user.search.etl.model.UserETLResponse;

import java.util.List;

public interface IUserETLService {
    List<HRUserRecord> getAllHrUserRecords();
    UserETLResponse pushUserRecords(List<UserRecord> userRecords);
    List<UserRecord> transformHrUserRecords(List<HRUserRecord> hrUserRecords);
}
