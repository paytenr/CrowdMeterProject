import time
import random  # just for test

while True:
    # Replace this with your actual motion detection
    count = random.randint(0, 20)

    # Write count to file
    with open("count.txt", "w") as f:
        f.write(str(count))

    print("Updated count:", count)
    time.sleep(1)  # updates every second for test