package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Document;
import org.bson.types.ObjectId;
import play.Logger;
import play.mvc.Http;

import java.io.*;
import java.text.SimpleDateFormat;

import java.util.Base64;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {

    public static String getUniqueFilename() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");

        return format.format(new Date());
    }

    public static boolean zipFiles(File[] files, String zipFilePath) {
        byte[] buffer = new byte[1024];

        try {
            FileOutputStream os = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(os);

            for (File file : files) {
                ZipEntry entry = new ZipEntry(file.getName());

                zos.putNextEntry(entry);

                FileInputStream in = new FileInputStream(file);

                int len;
                while ((len = in.read(buffer)) > 0) {

                    zos.write(buffer, 0, len);
                }

                in.close();
                zos.closeEntry();
            }

            zos.close();

            return true;
        } catch (Exception e) {
            Logger.debug(e.getMessage());
            return false;
        }
    }

    public static String encodeInBase64(String filePath) {
        try {
            File file = new File(filePath);
            byte[] bytes = loadFile(file);

            return Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            Logger.debug(e.getMessage());

            return null;
        }
    }



    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0, read = 0;
        while (offset < bytes.length && (read = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += read;
        }

        if (offset < bytes.length) {
            throw new IOException("could not entirely read the file.");
        }

        is.close();

        return bytes;
    }

    public static DBObject inQueryFromJson(JsonNode json) {
        BasicDBList list = new BasicDBList();

        for (JsonNode node : json) {
            list.add(new ObjectId(node.textValue()));
        }

        return new BasicDBObject(Document.ID, new BasicDBObject("$in", list));
    }
}
