import numpy as np
import cv2


def runtime():
    cap = cv2.VideoCapture(1)
    cap.set(15, -10)

    i = 0
    while 1:
        _, frame = cap.read()
        cv2.imwrite(f"img {i}.png", frame)
        frameHSV = cv2.cvtColor(frame, cv2.COLOR_RGB2HSV)
        step1 = cv2.inRange(frameHSV, np.array([0, 160, 85]), np.array([100, 255, 200]))
        cv2.imshow("show", step1)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        i += 1
    cap.release()
    cv2.destroyAllWindows()


def prossace():
    i = 0
    while 1:
        frame = cv2.imread(f"img {i}.png")
        frameHSV = cv2.cvtColor(frame, cv2.COLOR_RGB2HSV)
        frameHSV = cv2.inRange(frameHSV, np.array([0, 180, 85]), np.array([100, 255, 255]))
        frameHSV = cv2.filter2D(frameHSV, -1, np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]]))
        frameHSV = cv2.medianBlur(frameHSV, 27)
        cv2.imshow("show", frameHSV)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        if cv2.waitKey(1) & 0xFF == ord('n'):
            i += 1

        contours, hierarchy = cv2.findContours(frameHSV, 1, 2)
        if contours:
            if len(contours) == 1:
                rect = cv2.minAreaRect(contours[0])
                M = (cv2.boxPoints(rect)[0] + cv2.boxPoints(rect)[1] + cv2.boxPoints(rect)[2] + cv2.boxPoints(rect)[3]) / 4
                print("1 rects", M)
            elif len(contours) == 2:
                rectL = cv2.minAreaRect(contours[0])
                rectR = cv2.minAreaRect(contours[1])
                leftM = (cv2.boxPoints(rectL)[0] + cv2.boxPoints(rectL)[1] + cv2.boxPoints(rectL)[2] + cv2.boxPoints(rectL)[3]) / 4
                rightM = (cv2.boxPoints(rectR)[0] + cv2.boxPoints(rectR)[1] + cv2.boxPoints(rectR)[2] + cv2.boxPoints(rectR)[3]) / 4
                print("2 rects", leftM, rightM)
        i += 1
        i = i % 264
    cv2.destroyAllWindows()


def main() -> object:
    print("starting: proccase")
    prossace()

if __name__ == '__main__':
    main()
