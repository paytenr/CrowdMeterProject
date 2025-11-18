import cv2
import imutils
import time
from py4j.java_gateway import JavaGateway, CallbackServerParameters

# Connect to Java Gateway (assumes Java GatewayServer is running)
gateway = JavaGateway(callback_server_parameters=CallbackServerParameters())
java_app = gateway.entry_point  # Java GUI object

# Motion detector setup
capture = cv2.VideoCapture(0)
if not capture.isOpened():
    print("Error: Camera failed to open. Check macOS Camera permissions!")
    exit()

time.sleep(2.0)  # Give camera time to warm up
capture.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

# Stabilize initial background
for i in range(30):
    ret, start_frame = capture.read()
    if not ret:
        print("Error reading camera frame")
        exit()
    start_frame = imutils.resize(start_frame, width=500)
    start_frame = cv2.cvtColor(start_frame, cv2.COLOR_BGR2GRAY)
    start_frame = cv2.GaussianBlur(start_frame, (21, 21), 0)

# Motion detection variables
count = 0
prev_count = None
person_entered_side = None
person_counted = False
frame_width = 500
center_line_x = frame_width // 2

try:
    while True:
        ret, frame = capture.read()
        if not ret:
            print("Error reading camera frame")
            break

        frame = imutils.resize(frame, width=500)
        frame_bw = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        frame_bw = cv2.GaussianBlur(frame_bw, (5, 5), 0)

        # Difference from previous frame
        difference = cv2.absdiff(frame_bw, start_frame)
        threshold = cv2.threshold(difference, 25, 255, cv2.THRESH_BINARY)[1]
        start_frame = frame_bw

        # Find contours
        contours, _ = cv2.findContours(threshold.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        person_detected = False
        centroid_x = None

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
            x, y, w, h = cv2.boundingRect(largest_contour)
            centroid_x = x + w // 2
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)

        # Motion count logic
        if person_detected:
            if person_entered_side is None:
                person_entered_side = "left" if centroid_x < center_line_x else "right"
                person_counted = False
            elif not person_counted:
                if person_entered_side == "left" and centroid_x > center_line_x:
                    count -= 1  # right → left
                    person_counted = True
                    print(f"Right → Left detected. Count = {count}")
                elif person_entered_side == "right" and centroid_x < center_line_x:
                    count += 1  # left → right
                    person_counted = True
                    print(f"Left → Right detected. Count = {count}")
        else:
            person_entered_side = None
            person_counted = False

        # Draw center line & count
        cv2.line(frame, (center_line_x, 0), (center_line_x, frame.shape[0]), (0, 0, 255), 2)
        cv2.putText(frame, f"Count: {count}", (10, 30),
                    cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)

        # Update Java GUI ONLY if count changed
        if count != prev_count:
            try:
                java_app.sendResult(str(count))
                prev_count = count
            except Exception as e:
                print("[Python] Failed to send count to Java:", e)

        # Display OpenCV windows
        cv2.imshow("Motion Counter", frame)
        cv2.imshow("Threshold", threshold)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

finally:
    print("Final count:", count)
    capture.release()
    cv2.destroyAllWindows()
