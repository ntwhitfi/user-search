package ntwhitfi.user.search.repository;

public interface IRepository<T> {

    T getRecords(String query);
    T getAllRecords();

}
