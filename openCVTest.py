import numpy as np
import math
import cv2
import constants


def d(pt1, pt2):
    return math.sqrt((pt1[0] - pt2[0]) ** 2 + (pt1[1] - pt2[1]) ** 2)


def sticers(img):
    frameHSV = cv2.cvtColor(img, cv2.COLOR_RGB2HSV)
    frameHSV = cv2.inRange(frameHSV, np.array([0, 180, 85]), np.array([100, 255, 255]))
    frameHSV = cv2.filter2D(frameHSV, -1, np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]]))
    frameHSV = cv2.medianBlur(frameHSV, 27)

    contours, hierarchy = cv2.findContours(frameHSV, 1, 2)

    frameHSV = cv2.cvtColor(frameHSV, cv2.COLOR_GRAY2RGB)


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
        frame_hsv = cv2.cvtColor(frame, cv2.COLOR_RGB2HSV)
        frame_hsv = cv2.inRange(frame_hsv, np.array([0, 180, 85]), np.array([100, 255, 255]))
        frame_hsv = cv2.filter2D(frame_hsv, -1, np.array([[-1, -1, -1], [-1, 9, -1], [-1, -1, -1]]))
        frame_hsv = cv2.medianBlur(frame_hsv, 27)

        height, width = frame_hsv.shape

        contours, hierarchy = cv2.findContours(frame_hsv, 1, 2)

        frame_hsv = cv2.cvtColor(frame_hsv, cv2.COLOR_GRAY2RGB)
        if contours:
            if len(contours) == 1:
                rect = cv2.minAreaRect(contours[0])

                box = cv2.boxPoints(rect)
                box = np.int0(box)

                D = constants.FOCAL_LENGTH * math.sqrt(constants.STICKER_AREA / (d(box[0], box[1]) * d(box[0], box[3])))

                print(f"D = {D}")
                cv2.drawContours(frame_hsv, [box], -1, (0, 0, 255), 2)

            elif len(contours) == 2:
                rectL = cv2.minAreaRect(contours[0])
                rectR = cv2.minAreaRect(contours[1])

                boxL = cv2.boxPoints(rectL)
                boxL = np.int0(boxL)
                DL = constants.FOCAL_LENGTH * math.sqrt(
                    constants.STICKER_AREA / (d(boxL[0], boxL[1]) * d(boxL[0], boxL[3])))
                pixel_middle_L = (boxL[0] + boxL[3]) / 2

                boxR = cv2.boxPoints(rectR)
                boxR = np.int0(boxR)
                DR = constants.FOCAL_LENGTH * math.sqrt(
                    constants.STICKER_AREA / (d(boxR[0], boxR[1]) * d(boxR[0], boxR[3])))
                pixel_middle_R = (boxR[0] + boxR[3]) / 2

                if pixel_middle_L[0] > pixel_middle_R[1]:
                    tempBox = boxR
                    tempD = DR
                    tempPixel_middle = pixel_middle_R

                    boxR = boxL
                    DR = DL
                    pixel_middle_R = pixel_middle_L

                    boxL = tempBox
                    DL = tempD
                    pixel_middle_L = tempPixel_middle

                if pixel_middle_L[0] > width / 2:
                    D = DL
                    pixel_middle = pixel_middle_L
                else:
                    D = DR
                    pixel_middle = pixel_middle_R

                Dx = D * math.sin(constants.FOV * 2 * (pixel_middle[0]) / width)
                Dy = D * math.sin(constants.FOV * 2 * (pixel_middle[1]) / height)
                print(pixel_middle_L + pixel_middle_R)
                print(f"D = {D}, Dx = {Dx}, Dy = {Dy}")
                Dz = math.sqrt(D ** 2 - Dx ** 2 - Dy ** 2)
                print(f"Dx = {Dx}, Dy = {Dy}, Dz = {Dz}")

                angle = math.atan(Dx / Dz)

                print(f"D = {D}, angle = {math.degrees(angle)}")
                cv2.drawContours(frame_hsv, [boxL], -1, (0, 0, 255), 2)
                cv2.drawContours(frame_hsv, [boxR], -1, (0, 0, 255), 2)

        cv2.imshow("show", frame_hsv)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        if cv2.waitKey(1) & 0xFF == ord('n'):
            i += 1
            print(i)

        #i += 1
        i = i % 264
    cv2.destroyAllWindows()


def main() -> object:
    print(f"starting: proccase")
    cv2.waitKey(1)
    prossace()


if __name__ == '__main__':
    main()
