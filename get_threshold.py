import json
import cv2
import numpy as np


def get_threshold():
    cap = cv2.VideoCapture(1)
    cap.set(15, -10)
    while True:
        _, frame = cap.read()
        cv2.imshow("original", frame)
        if cv2.waitKey(1) & 0xFF == 'q':
            break
    cv2.destroyAllWindows()


def main() -> object:
    get_threshold()


if __name__ == '__main__':
    main()