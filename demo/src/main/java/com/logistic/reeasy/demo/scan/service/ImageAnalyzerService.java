package com.logistic.reeasy.demo.scan.service;

import org.springframework.stereotype.Service;

@Service
public class ImageAnalyzerService {
}

// // 1. Clases de tu proyecto
// import com.logistic.reeasy.demo.scan.model.ScanModel;

// // 2. Jackson (Para parsear JSON)
// import com.fasterxml.jackson.databind.ObjectMapper;

// // 3. SDK de Google AI / Vertex AI (Para interactuar con Gemini)
// import com.google.cloud.ai.genai.v1.GenerativeModelServiceClient;
// import com.google.cloud.ai.genai.v1.GenerateContentConfig;
// import com.google.cloud.ai.genai.v1.Schema;
// import com.google.cloud.ai.genai.v1.Type;
// import com.google.cloud.ai.genai.v1.Part;
// import com.google.cloud.ai.genai.v1.Content;
// import com.google.cloud.ai.genai.v1.GenerateContentResponse; // Aunque no se usa directamente, es bueno tenerla para referencia.
// import com.google.protobuf.ByteString;

// // 4. Utilidades de Java (Para Base64, Fecha y Listas)
// import java.time.LocalDateTime;
// import java.util.Base64;
// import java.util.List;

// public class ImageAnalyzerService {

//     private final ObjectMapper objectMapper = new ObjectMapper(); 
//     // Usamos el ObjectMapper de Jackson para convertir el String JSON en ScanModel
    
//     // Esquema de la subclase ItemDetallado (array items_detectados)
//     private final Schema ITEM_DETALLADO_SCHEMA = Schema.newBuilder()
//         .setType(Type.OBJECT)
//         .putProperties("cantidad", Schema.newBuilder().setType(Type.INTEGER).setDescription("Número de botellas de este tipo.").build())
//         .putProperties("tipo_botella", Schema.newBuilder().setType(Type.STRING).setDescription("Describe el material y color de la botella (ej: 'Vidrio Claro', 'PET Azul').").build())
//         .putProperties("es_reciclable", Schema.newBuilder().setType(Type.BOOLEAN).setDescription("True si la botella puede ser reciclada.").build())
//         .addRequired("cantidad")
//         .addRequired("tipo_botella")
//         .build();

//     // Esquema principal de ScanModel
//     private final Schema SCAN_MODEL_SCHEMA = Schema.newBuilder()
//         .setType(Type.OBJECT)
//         .putProperties("fecha_analisis", Schema.newBuilder().setType(Type.STRING).setDescription("La fecha actual en formato ISO8601.").build())
//         .putProperties("id_usuario", Schema.newBuilder().setType(Type.INTEGER).setDescription("El ID del usuario que solicita el análisis.").build())
//         .putProperties("items_detectados", Schema.newBuilder()
//             .setType(Type.ARRAY)
//             .setDescription("Lista de todos los tipos de botellas detectadas en la imagen.")
//             .setItems(ITEM_DETALLADO_SCHEMA)
//             .build())
//         .addRequired("fecha_analisis")
//         .addRequired("id_usuario")
//         .addRequired("items_detectados")
//         .build();


//     public ScanModel scanImage(String base64Image, Long userId) throws Exception {
        
//         // 1. Preparar la Imagen Base64 y el Prompt
//         byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        
//         Part imagePart = Part.newBuilder()
//             .setInlineData(Part.InlineData.newBuilder()
//                 .setMimeType("image/jpeg") // Ajustar el MIME Type si es necesario
//                 .setData(ByteString.copyFrom(imageBytes)))
//             .build();
            
//         String promptText = String.format(
//             "Analiza esta imagen e identifica todos los tipos de botellas. Genera una lista de items_detectados. Asigna la fecha actual ('%s') y el id de usuario ('%d') al objeto JSON.", 
//             LocalDateTime.now().toString(), 
//             userId
//         );
//         Part promptPart = Part.newBuilder().setText(promptText).build();

//         // 2. Configurar la llamada a Gemini para Salida JSON Estructurada
//         GenerateContentConfig config = GenerateContentConfig.newBuilder()
//             .setResponseMimeType("application/json") // ¡Fuerza la salida JSON!
//             .setResponseSchema(SCAN_MODEL_SCHEMA) // Usa el esquema de tu clase ScanModel
//             .build();
        
//         Content content = Content.newBuilder()
//             .addParts(imagePart) 
//             .addParts(promptPart)
//             .build();
            
//         // 3. Llamar a la API de Gemini
//         String jsonString;
//         try (GenerativeModelServiceClient client = GenerativeModelServiceClient.create()) {
//             var response = client.generateContent(
//                 "gemini-2.5-flash", 
//                 List.of(content),
//                 config
//             );
            
//             // Extraer la cadena de texto JSON pura de la respuesta del SDK
//             jsonString = response.getCandidates(0).getContent().getParts(0).getText();
            
//         } catch (Exception e) {
//             // Manejo de errores de la API, por ejemplo, loguear y lanzar una excepción de negocio
//             throw new RuntimeException("Error al comunicarse con la API de Gemini: " + e.getMessage(), e);
//         }
        
//         // 4. Parsear el String JSON a tu Clase ScanModel
//         try {
//             // Convierte la cadena JSON recibida en un objeto Java ScanModel
//             return objectMapper.readValue(jsonString, ScanModel.class);
//         } catch (Exception e) {
//             // Manejo de errores de parseo (si Gemini devolvió JSON inválido)
//             throw new RuntimeException("Error al parsear la respuesta JSON: " + e.getMessage() + "\nJSON: " + jsonString, e);
//         }
//     }
// }

// public class ImageAnalyzerService {

//     private final ObjectMapper objectMapper = new ObjectMapper(); 
//     // Usamos el ObjectMapper de Jackson para convertir el String JSON en ScanModel
    
//     // Esquema de la subclase ItemDetallado (array items_detectados)
//     private final Schema ITEM_DETALLADO_SCHEMA = Schema.newBuilder()
//         .setType(Type.OBJECT)
//         .putProperties("cantidad", Schema.newBuilder().setType(Type.INTEGER).setDescription("Número de botellas de este tipo.").build())
//         .putProperties("tipo_botella", Schema.newBuilder().setType(Type.STRING).setDescription("Describe el material y color de la botella (ej: 'Vidrio Claro', 'PET Azul').").build())
//         .putProperties("es_reciclable", Schema.newBuilder().setType(Type.BOOLEAN).setDescription("True si la botella puede ser reciclada.").build())
//         .addRequired("cantidad")
//         .addRequired("tipo_botella")
//         .build();

//     // Esquema principal de ScanModel
//     private final Schema SCAN_MODEL_SCHEMA = Schema.newBuilder()
//         .setType(Type.OBJECT)
//         .putProperties("fecha_analisis", Schema.newBuilder().setType(Type.STRING).setDescription("La fecha actual en formato ISO8601.").build())
//         .putProperties("id_usuario", Schema.newBuilder().setType(Type.INTEGER).setDescription("El ID del usuario que solicita el análisis.").build())
//         .putProperties("items_detectados", Schema.newBuilder()
//             .setType(Type.ARRAY)
//             .setDescription("Lista de todos los tipos de botellas detectadas en la imagen.")
//             .setItems(ITEM_DETALLADO_SCHEMA)
//             .build())
//         .addRequired("fecha_analisis")
//         .addRequired("id_usuario")
//         .addRequired("items_detectados")
//         .build();


//     public ScanModel scanImage(String base64Image, Long userId) throws Exception {
//         // TODO aca no falta importar las 
//         // 1. Preparar la Imagen Base64 y el Prompt
//         byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        
//         Part imagePart = Part.newBuilder()
//             .setInlineData(Part.InlineData.newBuilder()
//                 .setMimeType("image/jpeg") // Ajustar el MIME Type si es necesario
//                 .setData(ByteString.copyFrom(imageBytes)))
//             .build();
            
//         String promptText = String.format(
//             "Analiza esta imagen e identifica todos los tipos de botellas. Genera una lista de items_detectados. Asigna la fecha actual ('%s') y el id de usuario ('%d') al objeto JSON.", 
//             LocalDateTime.now().toString(), 
//             userId
//         );
//         Part promptPart = Part.newBuilder().setText(promptText).build();

//         // 2. Configurar la llamada a Gemini para Salida JSON Estructurada
//         GenerateContentConfig config = GenerateContentConfig.newBuilder()
//             .setResponseMimeType("application/json") // ¡Fuerza la salida JSON!
//             .setResponseSchema(SCAN_MODEL_SCHEMA) // Usa el esquema de tu clase ScanModel
//             .build();
        
//         Content content = Content.newBuilder()
//             .addParts(imagePart) 
//             .addParts(promptPart)
//             .build();
            
//         // 3. Llamar a la API de Gemini
//         String jsonString;
//         try (GenerativeModelServiceClient client = GenerativeModelServiceClient.create()) {
//             var response = client.generateContent(
//                 "gemini-2.5-flash", 
//                 List.of(content),
//                 config
//             );
            
//             // Extraer la cadena de texto JSON pura de la respuesta del SDK
//             jsonString = response.getCandidates(0).getContent().getParts(0).getText();
            
//         } catch (Exception e) {
//             // Manejo de errores de la API, por ejemplo, loguear y lanzar una excepción de negocio
//             throw new RuntimeException("Error al comunicarse con la API de Gemini: " + e.getMessage(), e);
//         }
        
//         // 4. Parsear el String JSON a tu Clase ScanModel
//         try {
//             // Convierte la cadena JSON recibida en un objeto Java ScanModel
//             return objectMapper.readValue(jsonString, ScanModel.class);
//         } catch (Exception e) {
//             // Manejo de errores de parseo (si Gemini devolvió JSON inválido)
//             throw new RuntimeException("Error al parsear la respuesta JSON: " + e.getMessage() + "\nJSON: " + jsonString, e);
//         }
//     }
// }