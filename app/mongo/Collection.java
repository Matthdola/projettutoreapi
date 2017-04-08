package mongo;

import com.mongodb.*;
import org.bson.types.ObjectId;
import plugins.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Collection {
    public static QueryResult insert(String collectionName, Document document, Function<DuplicateKeyException, Error> onDuplicateKeyError) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        try {
            DBObject bson = document.toBson();
            collection.insert(bson, WriteConcern.ACKNOWLEDGED);

            ObjectId id = (ObjectId) bson.get(Document.ID);

            if (id != null) {
                document.setId(id.toString());
            }

            return document;

        } catch (DuplicateKeyException e) {
            return onDuplicateKeyError.apply(e);

        } catch (MongoTimeoutException e) {
            return new Error(Error.DATABASE_ACCESS_TIMEOUT, "request timeout.");

        } catch (MongoException e) {
            return new Error(Error.MONGO_ERROR, e.getMessage());

        } catch (Exception e) {
            return new Error(Error.UNEXPECTED, e.getMessage());
        }
    }

    public static QueryResult update(String collectionName, Document document, Function<DuplicateKeyException, Error> onDuplicateKeyError) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        if (!ObjectId.isValid(document.getId())) {
            return new Error(Error.NOT_FOUND, "no " + document.getDocumentName() + " matches the id " + document.getId() + ".");
        }

        try {
            DBObject bson = document.toBson();

            collection.update(new BasicDBObject(Document.ID, new ObjectId(document.getId())), bson);

            return document;

        } catch (DuplicateKeyException e) {
            return onDuplicateKeyError.apply(e);

        } catch (MongoTimeoutException e) {
            return new Error(Error.DATABASE_ACCESS_TIMEOUT, "request timeout.");

        } catch (MongoException e) {
            return new Error(Error.UNEXPECTED, e.getMessage());
        }
    }

    public static QueryResult delete(String collectionName, Document document) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        if (!ObjectId.isValid(document.getId())) {
            return new Error(Error.NOT_FOUND, "no " + document.getDocumentName() + " matches the id " + document.getId() + ".");
        }

        try {
            collection.remove(new BasicDBObject(Document.ID, new ObjectId(document.getId())));

            DBObject bson = document.toBson();

            MongoDB.getTrash(collectionName).insert(bson, WriteConcern.ACKNOWLEDGED);

            return document;

        } catch (MongoTimeoutException e) {
            return new Error(Error.DATABASE_ACCESS_TIMEOUT, "request timeout.");

        } catch (MongoException e) {
            return new Error(Error.UNEXPECTED, e.getMessage());
        }
    }


    public static QueryResult restore(String collectionName, String id, Function<DBObject, Document> fromBsonFn, Function<DuplicateKeyException, Error> onDuplicateKeyError) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        if (!ObjectId.isValid(id)) {
            return new Error(Error.NOT_FOUND, "can not restore object with id " + id + ".");
        }

        BasicDBObject query = new BasicDBObject(Document.ID, new ObjectId(id));

        try (DBCursor cursor = MongoDB.getTrash(collection.getName()).find(query)) {
            if (!cursor.hasNext()) {
                return new Error(Error.NOT_FOUND, "can not restore object with id " + id + ".");
            }

            DBObject bson = cursor.next();

            Document document = fromBsonFn.apply(bson);

            MongoDB.getTrash(collectionName).remove(bson);

            return insert(collectionName, document, onDuplicateKeyError);
        }
    }

    public static QueryResult findById(String collectionName, String id, Function<DBObject, Document> fromBsonFn, String notFoundErrorMessage) {
        if (!ObjectId.isValid(id)) {
            return new Error(Error.NOT_FOUND, notFoundErrorMessage);
        }

        DBObject query = new BasicDBObject(Document.ID, new ObjectId(id));

        return find(collectionName, query, fromBsonFn, notFoundErrorMessage);
    }

    public static QueryResult find(String collectionName, DBObject query, Function<DBObject, Document> fromBsonFn, String notFoundErrorMessage) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        try (DBCursor cursor = collection.find(query)) {
            if (!cursor.hasNext()) {
                return new Error(Error.NOT_FOUND, notFoundErrorMessage);
            }

            DBObject bson = cursor.next();

            return fromBsonFn.apply(bson);

        } catch (MongoTimeoutException e) {
            return new Error(Error.DATABASE_ACCESS_TIMEOUT, "request timeout.");

        } catch (Exception e) {
            return new Error(Error.UNEXPECTED, e.getMessage());
        }
    }

    public static long count(String collectionName, DBObject query){
        DBCollection collection = MongoDB.getCollection(collectionName);
        return collection.count(query);
    }

    public static QueryResult findAll(String collectionName, DBObject query, Function<DBObject, Document> fromBsonFn){
        DBCollection collection = MongoDB.getCollection(collectionName);
        long totalCount = collection.count(query);

        try (DBCursor cursor = collection.find(query).sort(new BasicDBObject(Document.CREATED_AT, -1))) {
            List<QueryResult> results = new ArrayList<>();

            while (cursor.hasNext()) {
                DBObject bson = cursor.next();

                results.add(fromBsonFn.apply(bson));
            }

            PaginatedQueryResult result = new PaginatedQueryResult(collectionName, results);
            result.setPage(1);
            result.setPerPage((int)totalCount);
            result.setPageCount(cursor.size());
            result.setTotalCount(totalCount);

            return result;

        } catch (MongoTimeoutException e) {
            return new Error(Error.DATABASE_ACCESS_TIMEOUT, "request timeout.");

        } catch (Throwable t) {
            return new Error(Error.UNEXPECTED, t.getMessage());
        }
    }

    public static QueryResult paginate(String collectionName, DBObject query, int page, int perPage, Function<DBObject, QueryResult> fromBsonFn) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        long totalCount = collection.count(query);

        if (page <= 0 || perPage <= 0) {
            return new PaginatedQueryResult(collectionName, new ArrayList<>(), page, perPage, 0, totalCount);
        }

        int offset = page > 1 ? (page - 1) * perPage : 0;
        long lastPage = totalCount / perPage;

        if (totalCount % perPage > 0) {
            lastPage += 1;
        }

        if (page > lastPage) {
            return new PaginatedQueryResult(collectionName, new ArrayList<>(), page, perPage, 0, totalCount);
        }

        try (DBCursor cursor = collection.find(query).skip(offset).limit(perPage).sort(new BasicDBObject(Document.CREATED_AT, -1))) {
            List<QueryResult> results = new ArrayList<>();

            while (cursor.hasNext()) {
                DBObject bson = cursor.next();

                results.add(fromBsonFn.apply(bson));
            }

            PaginatedQueryResult result = new PaginatedQueryResult(collectionName, results);
            result.setPage(page);
            result.setPerPage(perPage);
            result.setPageCount(cursor.size());
            result.setTotalCount(totalCount);

            return result;

        } catch (MongoTimeoutException e) {
            return new Error(Error.DATABASE_ACCESS_TIMEOUT, "request timeout.");

        } catch (Throwable t) {
            return new Error(Error.UNEXPECTED, t.getMessage());
        }
    }

    public static boolean exists(String collectionName, String id) {
        DBCollection collection = MongoDB.getCollection(collectionName);

        if (!ObjectId.isValid(id)) {
            return false;
        }

        try {
            BasicDBObject query = new BasicDBObject(Document.ID, new ObjectId(id));

            return collection.count(query) > 0;

        } catch (Exception e) {
            return false;
        }
    }
}
