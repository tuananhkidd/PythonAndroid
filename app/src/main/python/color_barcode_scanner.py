# import the necessary packages
import cv2
import imutils
import numpy as np

from transform import four_point_transform

num_cols = 4
num_groups = 6
len_group = 8
thickness = 10
white_pad = 5
black_pad = 3
dim = 0


def localize(image, width=800):
    # load the image and compute the ratio of the old height
    # to the new height, clone it, and resize it
    orig = image.copy()
    ratio = image.shape[1] / width
    image = imutils.resize(image, width=width)

    # convert the image to grayscale, blur it, and find edges in the image
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    ret, edged = cv2.threshold(gray, 150, 255, 0)

    # cv2.imshow("Edged", edged)

    # find the contours in the edged image, keeping only the
    # largest ones, and initialize the screen contour
    cnts = cv2.findContours(edged.copy(), cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    cnts = sorted(cnts, key=cv2.contourArea, reverse=True)[:5]

    # loop over the contours
    for c in cnts:
        # approximate the contour
        peri = cv2.arcLength(c, True)
        approx = cv2.approxPolyDP(c, 0.01 * peri, True)

        # if our approximated contour has four points, then we
        # can assume that we have found our screen
        if len(approx) == 4:
            # apply the four point transform to obtain a top-down
            # view of the original image
            check, warped = four_point_transform(orig, approx.reshape(4, 2) * ratio)

            if check:
                # show the contour (outline) of the piece of paper
                cv2.drawContours(image, [approx], -1, (0, 0, 255), 2)
                # warped = warped[white_pad:-white_pad, white_pad // 2:-white_pad, :]
                cv2.imshow("Detect", image)
                # cv2.waitKey(0)
                return warped

    return None


def validate_barcode_id(barcode_id):
    """
        Validate the barcode id is valid ?
    :param barcode_id: string, Detected barcode id
    :return: boolean
    """
    cur_len_group = 0
    cur_num_cols = 0
    cur_num_groups = 0
    for cluster in list(barcode_id):
        cluster = int(cluster)
        cur_len_group += cluster
        cur_num_cols += 1
        if cur_len_group == len_group:
            if cur_num_cols != num_cols:
                return False
            else:
                cur_len_group = 0
                cur_num_cols = 0
                cur_num_groups += 1
        elif cur_len_group > len_group:
            return False

    if cur_num_groups != num_groups:
        return False

    return True


def cal_barcode_id(image):
    """
        Calculate barcode id from detected color barcode image
    :param image: Color barcode image
    :return: string, Barcode ID
    """
    try:
        # using sobel filter to detect cluster intersection
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        gradX = cv2.Sobel(gray, ddepth=cv2.CV_32F, dx=1, dy=0, ksize=3)
        absGradX = np.absolute(gradX)
        ret, thresh = cv2.threshold(absGradX, 50, 255, 0)

        # erosion followed by dilation to removing noise
        rectKernel = cv2.getStructuringElement(cv2.MORPH_RECT, (1, 3))
        thresh = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, rectKernel)

        # cv2.imshow("Warped", image)
        # cv2.imshow("Gray", gray)
        # cv2.imshow("Gradient", absGradX)
        # cv2.imshow("Thresh", thresh)

        # Calculate the length of clusters of colorbar
        clusters = []
        previous_cluster = -10  # ignore the first and last edge
        center = thresh.shape[0] // 2
        for i in range(thresh.shape[1]):
            if thresh[center][i] != 0:
                if i < previous_cluster + 5:
                    continue
                cluster = i - previous_cluster
                # print(cluster)
                clusters.append(cluster)
                previous_cluster = i

        clusters = clusters[1:]
        min_cluster = min(clusters)
        for tolerance in range(-2, 3):
            tol_min_cluster = min_cluster + tolerance
            # print("Min cluster:", tol_min_cluster)
            # print("Max cluster:", max(clusters))
            barcode = "".join([str(round(c / tol_min_cluster)) for c in clusters])
            # print("=>Scanned ID:", barcode)

            if validate_barcode_id(barcode):
                return barcode

        return None
    except Exception as e:
        print("Exception:", e)
        return None


def scan(byte_image, width=800):
    print ("ahihi")
    print (type(byte_image))
    print(byte_image)
    nparr = np.fromstring(byte_image, np.uint8)
    image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    barcode = localize(image, width)
    if barcode is None:
        return None
    barcode_id = cal_barcode_id(barcode)
    return barcode_id
