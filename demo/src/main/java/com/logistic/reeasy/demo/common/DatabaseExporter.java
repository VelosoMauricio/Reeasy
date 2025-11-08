package com.logistic.reeasy.demo.common;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.sql2o.Sql2o;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseExporter {

    private static final Logger log = LoggerFactory.getLogger(DatabaseExporter.class);

    private final Sql2o sql2o;

    @Value("${export.file.path}")
    private Resource outputResource;

    public DatabaseExporter(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public void exportDbToTxt() throws Exception {
        try (var con = sql2o.open()) {
            List<String> tables = con.createQuery("SHOW TABLES").executeScalarList(String.class);

            tables.remove("Scans");
            tables.remove("Enterprise");
            tables.remove("Context");
            tables.remove("UsersDetails");
            tables.remove("Messages");

//            File file = new File(System.getProperty("user.dir"), outputPath);
            File file = outputResource.getFile();

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                for (String table : tables) {
                    writer.write("=== Tabla: " + table + " ===\n");

                    List<Map<String, Object>> rows = con.createQuery("SELECT * FROM " + table)
                            .executeAndFetchTable()
                            .asList();

                    for (Map<String, Object> row : rows) {
                        writer.write(row.toString() + "\n");
                    }

                    writer.write("\n");
                }
            }

            log.info("Exporting db to: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("ERROR on export db", e);
            throw e;
        }
    }
}