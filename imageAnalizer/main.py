import base64
import io
from typing import Any, List

import uvicorn
from fastapi import FastAPI, HTTPException

# Añadimos ImageDraw e ImageFont para pintar sobre la imagen
from PIL import Image, ImageDraw, ImageFont
from pydantic import BaseModel
from ultralytics import YOLO

app = FastAPI(title="Reeasy Image Analyzer Service")

# constantes para el preprocesamiento
BOTELLA_ETIQUETA = 39  # id de las obtejetos identificados como botellas
UMBRAL_CONFIANZA = 0.6  # mayores a 60% botella

# Cargar el modelo YOLO-cls entrenado
cls_model_path = "model/yolo26n-cls-reeasy-3.pt"
try:
    cls_model = YOLO(cls_model_path)
    print(f"Modelo {cls_model_path} cargado exitosamente.")
except Exception as e:
    print(f"Error: No se pudo cargar {cls_model_path}. Error: {e}")
    exit(1)

detector_model_path = "model/yolo26m.pt"
try:
    detector_model = YOLO(detector_model_path)
    print(f"Modelo {detector_model_path} cargado exitosamente.")
except Exception as e:
    print(f"Error: No se pudo cargar {detector_model_path}. Error: {e}")
    exit(1)


class ScanRequest(BaseModel):
    image: str


class Bottle(BaseModel):
    type: str
    xyxy: List[int]
    image: Any


class BottleResponse(BaseModel):
    type: str
    xyxy: List[int]


class ScanResponse(BaseModel):
    details: List[BottleResponse]
    image: str  # Esta contendrá la imagen base64 modificada con los recuadros


def detect_bottles(image):
    """
    Funcion para detectar botellas en la imagen
    y recortar las areas detectadas
    """
    detected_bottle = []
    results = detector_model(image)

    for box in results[0].boxes:
        if box.cls == BOTELLA_ETIQUETA and box.conf >= UMBRAL_CONFIANZA:
            xmin, ymin, xmax, ymax = map(int, box.xyxy[0].tolist())
            detected_bottle.append(
                Bottle(
                    image=image.crop((xmin, ymin, xmax, ymax)),
                    xyxy=[xmin, ymin, xmax, ymax],
                    type="",
                )
            )

    return detected_bottle


@app.post("/api/scan", response_model=ScanResponse)
async def scan_image(req: ScanRequest):
    response: List[BottleResponse] = []
    if not req.image:
        raise HTTPException(status_code=400, detail="No se proporcionó imagen.")

    base64_img = req.image

    # Decodificar imagen
    try:
        if base64_img.startswith("data:image"):
            base64_img = base64_img.split(",")[1]
        image_data = base64.b64decode(base64_img)
        image = Image.open(io.BytesIO(image_data)).convert("RGB")
    except Exception as e:
        raise HTTPException(
            status_code=400, detail=f"Error al decodificar la imagen: {e}"
        )

    # Ejecutar el modelo YOLO
    detected_bottles = detect_bottles(image)

    # Preparamos el objeto para dibujar sobre la imagen original
    draw = ImageDraw.Draw(image)

    # Intentar cargar una fuente por defecto del sistema, si falla usa la estándar
    try:
        font = ImageFont.load_default()
    except Exception:
        font = None

    for bottle in detected_bottles:
        results = cls_model(bottle.image)

        # Procesar resultados de clasificación
        try:
            top1_index = results[0].probs.top1
            class_name = results[0].names[top1_index]

            plastic_type = class_name.upper()
            bottle.type = plastic_type

            response.append(BottleResponse(xyxy=bottle.xyxy, type=bottle.type))

            # --- DIBUJAR EN LA IMAGEN ---
            xmin, ymin, xmax, ymax = bottle.xyxy

            # Dibujamos el recuadro (outline verde, ancho de línea 4)
            draw.rectangle([xmin, ymin, xmax, ymax], outline="green", width=4)

            # Añadimos la etiqueta de texto arriba del recuadro
            # Si el espacio es muy arriba, ajustamos la posición vertical
            text_position = (xmin + 5, ymin - 15 if ymin > 20 else ymin + 5)
            draw.text(text_position, bottle.type, fill="green", font=font)

        except Exception as e:
            print("Advertencia: procesando resultado no-cls o error:", e)

    # --- CODIFICAR IMAGEN MODIFICADA A BASE64 ---
    buffered = io.BytesIO()
    # Guardamos la imagen modificada en memoria en formato JPEG
    image.save(buffered, format="JPEG")
    # Convertimos los bytes a string base64
    img_modificada_b64 = base64.b64encode(buffered.getvalue()).decode("utf-8")

    # Devolvemos la lista de detalles y la nueva imagen en base64
    return ScanResponse(details=response, image=img_modificada_b64)


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=11434, reload=True)
