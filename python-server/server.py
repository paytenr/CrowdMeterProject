from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=[""],  # allow JavaFX to call server
    allow_methods=[""],
    allow_headers=["*"],
)

people_counter = 0

@app.post("/simulate")
def simulate_face():
    global people_counter
    people_counter += 10
    return {"people_detected": 10, "total": people_counter}
    

@app.post("/motion")
def motion_detected():
    global people_counter
    people_counter += 1
    return {"people_detected": 1, "total": people_counter}

@app.get("/total")
def get_total():
    return {"total": people_counter}

#reset detections
@app.post("/reset")
def reset_counter():
    global people_counter
    people_counter = 0

    return{"total": people_counter}

