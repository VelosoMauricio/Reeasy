FROM python:3.11-slim

RUN apt-get update && apt-get install -y libgl1 libglib2.0-0 libxcb1 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir torch torchvision --index-url https://download.pytorch.org/whl/cpu && \
    pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 11434

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "11434"]
