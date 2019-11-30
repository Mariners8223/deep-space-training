import numpy as np
import math
import cv2
import constants
import json


def d(pt1, pt2):
    return math.sqrt((pt1[0] - pt2[0]) ** 2 + (pt1[1] - pt2[1]) ** 2)


def distance_angle_frame(img, min_color, max_color, blur_val):
    frame_hsv = cv2.cvtColor(img, cv2.COLOR_RGB2HSV)
    frame_hsv = cv2.inRange(frame_hsv, min_color, max_color)
    frame_hsv = cv2.medianBlur(frame_hsv, blur_val)

    height, width = frame_hsv.shape

    contours, hierarchy = cv2.findContours(frame_hsv, 1, 2)

    frame_hsv = cv2.cvtColor(frame_hsv, cv2.COLOR_GRAY2RGB)
    if contours:
        if len(contours) == 1:
            rect = cv2.minAreaRect(contours[0])

            box = cv2.boxPoints(rect)
            box = np.int0(box)

            if d(box[0], box[1]) * d(box[0], box[3]) == 0:
                return None, None, frame_hsv
            D = constants.FOCAL_LENGTH * math.sqrt(constants.STICKER_AREA / (d(box[0], box[1]) * d(box[0], box[3])))
            pixel_middle = (box[0] + box[3]) / 2

            Dx = D * math.sin(constants.FOV * 2 * (pixel_middle[0]) / width)
            Dy = D * math.sin(constants.FOV * 2 * (pixel_middle[1]) / height)
            Dz = D ** 2 - Dx ** 2 - Dy ** 2
            if Dz < 0:
                return None, None, frame_hsv
            Dz = math.sqrt(Dz)

            angle = 60 - math.degrees(math.atan(Dz / Dx))

            cv2.drawContours(frame_hsv, [box], -1, (0, 0, 255), 2)

            return D, angle, frame_hsv
        elif len(contours) == 2:
            rectL = cv2.minAreaRect(contours[0])
            rectR = cv2.minAreaRect(contours[1])

            boxL = cv2.boxPoints(rectL)
            boxL = np.int0(boxL)
            if d(boxL[0], boxL[1]) * d(boxL[0], boxL[3]) == 0:
                return None, None, frame_hsv
            DL = constants.FOCAL_LENGTH * math.sqrt(
                constants.STICKER_AREA / (d(boxL[0], boxL[1]) * d(boxL[0], boxL[3])))
            pixel_middle_L = (boxL[0] + boxL[3]) / 2

            boxR = cv2.boxPoints(rectR)
            boxR = np.int0(boxR)
            if d(boxR[0], boxR[1]) * d(boxR[0], boxR[3]) == 0:
                return None, None, frame_hsv
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

            D = (DL + DR) / 2
            pixel_middle = (pixel_middle_L + pixel_middle_R) / 2

            Dx = D * math.sin(constants.FOV * 2 * (pixel_middle[0]) / width)
            Dy = D * math.sin(constants.FOV * 2 * (pixel_middle[1]) / height)
            Dz = D ** 2 - Dx ** 2 - Dy ** 2
            if Dz < 0:
                return None, None, frame_hsv
            Dz = math.sqrt(Dz)

            angle = 60 - math.degrees(math.atan(Dz / Dx))

            cv2.drawContours(frame_hsv, [boxL], -1, (0, 0, 255), 2)
            cv2.drawContours(frame_hsv, [boxR], -1, (0, 0, 255), 2)
            return D, angle, frame_hsv
    return None, None, frame_hsv


def main():
    data = json.load(open("custom.json", "r"))
    light = data["light"]
    blur = data["blur"]
    min_hsv = np.array(data["min"])
    max_hsv = np.array(data["max"])

    cap = cv2.VideoCapture(0)
    cap.set(15, light)
    i = 0
    while True:
        _, frame = cap.read()
        #frame = cv2.imread(f"img {i}.png")
        cv2.imshow("original", frame)

        D, angle, frame_edited = distance_angle_frame(frame, min_hsv, max_hsv, blur)
        cv2.imshow("original", frame)
        cv2.imshow("processed", frame_edited)
        print(f"D {D}, angle {angle}")
        if cv2.waitKey(1) & 0xFF == 'q':
            break
        i += 1
        i = i % 264
    cv2.destroyAllWindows()


if __name__ == '__main__':
    main()
