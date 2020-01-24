package ntwhitfi.user.search.service;

import lombok.AllArgsConstructor;
import ntwhitfi.user.search.model.UserRecord;
import ntwhitfi.user.search.repository.IRepository;

import java.util.List;

public class UserSearchService implements IUserSearchService{
    private IRepository<List<UserRecord>> userRecordRepository;

    public UserSearchService(IRepository<List<UserRecord>> userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    @Override
    public List<UserRecord> searchUserRecords(String query) {
        //TODO: Add logic to strip special characters from search
        return userRecordRepository.getRecords(query);
    }

    @Override
    public List<UserRecord> getAllUserRecords() {
        return userRecordRepository.getAllRecords();
    }

}
