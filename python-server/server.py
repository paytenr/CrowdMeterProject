import cv2
import numpy as np
import imutils
import time
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from py4j.java_gateway import JavaGateway, GatewayParameters

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=[""],  # allow JavaFX to call server
    allow_methods=[""],
    allow_headers=["*"],
    port = 25533
    gateway = JavaGateway(gateway_parameters = GatewayParameters(port = port))
    msgObjectFromJavaApp = gateway.entry_point
    print(msgObjectFromJavaApp.Message())

    # Shutdown the Java server when done
    gateway.shutdown()
    print("GatewayServer stopped from Python.")
)

people_counter = 0

@app.post("/simulate")
def simulate_face():
    global people_counter
    people_counter += 10
    return {"people_detected": 10, "total": people_counter}
    

@app.post("/motion")
def motion_detected():
    # motion detector
    capture = cv2.VideoCapture(0)
    time.sleep(2.0)  # Give the camera time to warm up

    capture.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
    capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

    # starting frame
    # Read and stabilize background for a few frames
    for i in range(30):
        ret, start_frame = capture.read()
        start_frame = imutils.resize(start_frame, width=500)
        start_frame = cv2.cvtColor(start_frame, cv2.COLOR_BGR2GRAY)
        start_frame = cv2.GaussianBlur(start_frame, (21, 21), 0)

    # variables
    count = 0
    person_active = False  # Tracks if person is currently in the frame
    frame_width = 500

    # Tracks person state
    person_entered_side = None  # 'left' or 'right'
    person_counted = False      # True if already counted

    # Define a center line
    center_line_x = frame_width // 2
    while True:
        ret, frame = capture.read()
        frame = imutils.resize(frame, width=500)

        frame_bw = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        frame_bw = cv2.GaussianBlur(frame_bw, (5, 5), 0)

        difference = cv2.absdiff(frame_bw, start_frame)
        threshold = cv2.threshold(difference, 25, 255, cv2.THRESH_BINARY)[1]
        start_frame = frame_bw

        contours, _ = cv2.findContours(threshold.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        person_detected = False
        centroid_x = None

        # Find largest contour (ignore small noise)
        max_area = 0
        largest_contour = None
        for contour in contours:
            area = cv2.contourArea(contour)
            if area < 3000:
                continue
            if area > max_area:
                max_area = area
                largest_contour = contour

        if largest_contour is not None:
            person_detected = True
            (x, y, w, h) = cv2.boundingRect(largest_contour)
            centroid_x = x + w // 2
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

        # Once-per-pass logic
        if person_detected:
            if person_entered_side is None:
                # Determine which side the person entered from
                person_entered_side = "left" if centroid_x < center_line_x else "right"
                person_counted = False

            elif not person_counted:
                # Check if person crossed center line
                if person_entered_side == "left" and centroid_x > center_line_x:
                    count -= 1  # right → left
                    person_counted = True
                    print(f"Right → Left detected. Count = {count}")
                elif person_entered_side == "right" and centroid_x < center_line_x:
                    count += 1  # left → right
                    person_counted = True
                    print(f"Left → Right detected. Count = {count}")

        else:
            # Person left frame → reset
            person_entered_side = None
            person_counted = False

        # Draw center line
        cv2.line(frame, (center_line_x, 0), (center_line_x, frame.shape[0]), (0, 0, 255), 2)

        # Display count
        cv2.putText(frame, f"Count: {count}", (10, 30),
        cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)

        cv2.imshow("Motion Counter", frame)
        cv2.imshow("Thresh", threshold)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    print("Final count:", count)
    capture.release()
    cv2.destroyAllWindows()
    #motion detetctor stop

@app.get("/total")
def get_total():
    return {"total": people_counter}

#reset detections
@app.post("/reset")
def reset_counter():
    global face_counter
    face_counter = 0

    return{"total": face_counter}

