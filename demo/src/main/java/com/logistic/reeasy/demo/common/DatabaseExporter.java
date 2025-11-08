package com.logistic.reeasy.demo.common;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sql2o.Sql2o;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

@Component
public class DatabaseExporter {

    //Dependencia del jackson para pasar a JSON 
    private final ObjectMapper mapper = new ObjectMapper(); 

    private Sql2o sql2o;

    @Value("${export.file.path:data_dump.json}") 
    private String outputPath;

    public DatabaseExporter(Sql2o sql2o){
        this.sql2o = sql2o; 
    }

    public void exportDbToJson() throws Exception{ 
        try (var con = sql2o.open()) {
            //lista con todos los nombres de las tablas
            List<String> tables = con.createQuery("SHOW TABLES").executeScalarList(String.class);

            // mapa que tiene tabla filas
            Map<String, Object> data = new java.util.HashMap<>();

            //metemos al json fila -> valores 
            for (String table : tables) {
                List<Map<String, Object>> rows = con.createQuery("SELECT * FROM " + table)
                        .executeAndFetchTable()
                        .asList();
                data.put(table, rows);
            }

            File file = new File(System.getProperty("user.dir"), outputPath);

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs(); // crea carpeta si no existe
                System.out.println("cree la ca");
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            System.out.println("Ruta bd: " + file.getAbsolutePath());
        }
    }
}
