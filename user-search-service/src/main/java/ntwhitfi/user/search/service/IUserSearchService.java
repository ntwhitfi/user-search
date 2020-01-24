package ntwhitfi.user.search.service;

import ntwhitfi.user.search.common.model.UserRecord;

import java.util.List;

public interface IUserSearchService {

    List<UserRecord> searchUserRecords(String query);
    List<UserRecord> getAllUserRecords();
}
