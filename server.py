from fastapi import FastAPI
import cv2

app = FastAPI()

@app.get("/")
def home():
    return {"message": "Python server is live"}

@app.get("/detect")
def detect():
    cam = cv2.VideoCapture(0)
    success, frame = cam.read()
    cam.release()

    if not success:
        return {"error"}
    
    return {"faces_dectected": "placeholder"}