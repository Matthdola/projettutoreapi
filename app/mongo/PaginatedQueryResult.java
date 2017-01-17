package mongo;


import java.util.ArrayList;
import java.util.List;

public class PaginatedQueryResult implements QueryResult {
    private List<QueryResult> results;

    private int page;

    private int perPage;

    private long pageCount;

    private long totalCount;

    private String collectionName;

    public PaginatedQueryResult() {
        this.results = new ArrayList<>();
    }

    public PaginatedQueryResult(String collectionName, List<QueryResult> results) {
        this.collectionName = collectionName;
        this.results = results;
    }

    public PaginatedQueryResult(String collectionName, List<QueryResult> results, int page, int perPage, long pageCount, long totalCount) {
        this.collectionName = collectionName;
        this.results = results;
        this.page = page;
        this.perPage = perPage;
        this.pageCount = pageCount;
        this.totalCount = totalCount;
    }

    @Override
    public boolean isError() {
        return false;
    }

    public boolean hasNext() {
        return perPage > 0 && (perPage * page) < totalCount;
    }

    public boolean hasPrevious() {
        return page > 1 && page <= getLastPage();
    }

    public List<QueryResult> getResults() {
        return results;
    }

    public void setResults(List<QueryResult> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNextPage() {
        return page + 1;
    }

    public long getLastPage() {
        if (perPage <= 0) {
            return 0;
        }

        long last = totalCount / perPage;

        if (totalCount % perPage > 0) {
            last += 1;
        }

        return last;
    }

    public int getPreviousPage() {
        return page - 1;
    }


    public int getFirstPage() {
        return 1;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
