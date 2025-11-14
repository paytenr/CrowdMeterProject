from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=[""],  # allow JavaFX to call server
    allow_methods=[""],
    allow_headers=["*"],
)

face_counter = 0

@app.post("/simulate")
def simulate_face():
    global face_counter
    face_counter += 1
    return {"faces_detected": 1, "total": face_counter}

@app.post("/motion")
def motion_detected():
    global face_counter
    face_counter += 1
    return {"faces_detected": 1, "total": face_counter}

@app.get("/total")
def get_total():
    return {"total": face_counter}

#reset detections
@app.post("/reset")
def reset_counter():
    global face_counter
    face_counter = 0
    return{"total": face_counter}
