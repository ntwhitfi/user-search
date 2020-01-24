package ntwhitfi.user.search.common.repository;

public interface IRepository<T> {

    T getRecords(String query);
    T getAllRecords();
    void pushRecords(T records);

}
