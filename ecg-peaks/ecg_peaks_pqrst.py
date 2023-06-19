"""
This file comprises two functions:
1. "ecg_peaks()" which identifies the R, P, and T peak values, and
2. "plot.peaks()" that plots the signal and the peaks.
The code is well-commented and provides more insight into the methodology.
"""

import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import numpy as np
import scipy.signal as scs


def ecg_peaks(ecg, filt_noise_ecg):
    SIGNAL_THRESHOLD_PERC = 0.5
    TIME_DISTANCE_PERC = 0.5

# ======================================================================================================================
# ======================================================= R WAVE =======================================================
# ======================================================================================================================

# ----------------------------------------------------- Derivative -----------------------------------------------------

    # Getting the derivative of the filtered ECG signal.
    der_ecg = np.diff(filt_noise_ecg)

    # Getting all the peaks of the derivative of the ECG signal.
    all_peaks_der_ecg, _ = scs.find_peaks(der_ecg)

    # Finding the mean of all the peaks.
    mean_peaks_der_ecg = np.mean(der_ecg[all_peaks_der_ecg])

    # Finding the max value of the ECG signal.
    max_der_ecg = np.max(der_ecg)

    # Finding the mean of "mean_peaks_der_ecg" and "max_der_ecg".
    # This will be the minimum threshold for finding peaks.
    threshold_signal = np.mean([mean_peaks_der_ecg, max_der_ecg]) * SIGNAL_THRESHOLD_PERC

    # Find the peaks again, this time considering the minimum threshold.
    new_peaks_der_ecg, _ = scs.find_peaks(der_ecg, height = threshold_signal)
    # new_peaks_der_ecg_time = time[new_peaks_der_ecg]
    # new_peaks_der_ecg_time = new_peaks_der_ecg_time.reset_index(drop = True)

    # Finding the mean of horizontal distance between peaks.
    mean_distance = np.mean(np.diff(new_peaks_der_ecg))

    # Setting the minimum threshold for horizontal distance to identify the R peaks among the peaks we've already found.
    threshold_horizontal = mean_distance * TIME_DISTANCE_PERC

    # Finding the indices of the R peaks in the derivative signal.
    R_peaks_der, _ = scs.find_peaks(der_ecg, height = threshold_signal, distance = threshold_horizontal)

# --------------------------------------------------------- ECG --------------------------------------------------------

    # Creating an array to find the R peaks in the ECG signal.
    R_peaks = np.empty([len(R_peaks_der) - 1])

    # Between each two R peaks in the derivative signal, the R peak in the ECG signal resides in the first 20% distance.
    for i in range(0, len(R_peaks)):
        # Creating an array that holds the ECG values between two R peaks in the derivative signal.
        range_ecg = ecg[R_peaks_der[i]: R_peaks_der[i + 1]]

        # Selecting the first 20% of that range.
        percentage = np.round(len(range_ecg) * 0.2)

        # Finding the index of the max value of ECG signal within the selected range, and doing the proper conversions.
        max_ecg = np.array(list(np.where(range_ecg == np.max(range_ecg[0: int(percentage)]))))

        # The index of that peak will be the index of R peak in the derivative signal plus the index of the max value.
        R_peaks[i] = R_peaks_der[i] + max_ecg[0, 0]

    # Converting the R peaks indices to integer.
    R_peaks = R_peaks.astype(np.int64)

# ======================================================================================================================
# ======================================================= P WAVE =======================================================
# ======================================================================================================================

# ----------------------------------------------------- Derivative -----------------------------------------------------

    # Creating an array to find the P peaks in the derivative signal, based on R peaks in the derivative signal.
    P_peaks_der = np.empty([len(R_peaks_der) - 1])

    # Between each two R peaks in the derivative signal, the P peaks reside in the 60-90% horizontal range.
    for i in range(0, len(P_peaks_der)):
        # Creating an array that holds the derivative values between two R peaks in the derivative signal.
        range_der = der_ecg[R_peaks_der[i]: R_peaks_der[i + 1]]

        # Selecting the range within 60-90% of that range.
        percentage_h = np.round(len(range_der) * 0.8)
        percentage_l = np.round(len(range_der) * 0.5)

        # Finding the index of the max value within the selected range, and doing the proper conversions.
        max_der = np.array(list(np.where(range_der == np.max(range_der[int(percentage_l): int(percentage_h)]))))

        # The index of that peak will be the index of R peak in the derivative signal plus the index of the max value.
        P_peaks_der[i] = R_peaks_der[i] + max_der[0, 0]

    # Converting the P peaks indices to integer.
    P_peaks_der = P_peaks_der.astype(np.int64)

# --------------------------------------------------------- ECG --------------------------------------------------------

    # Creating an array to find the P peaks in the ECG signal.
    P_peaks = np.empty([len(P_peaks_der)])

    # Between each two P peaks in the derivative signal, the P peak in the ECG signal resides in the first 20% distance.
    for i in range(0, len(P_peaks) - 1):
        # Creating an array that holds the ECG values between two P peaks in the derivative signal.
        range_ecg = ecg[P_peaks_der[i]: P_peaks_der[i + 1]]

        # Selecting the first 20% of that range.
        percentage = np.round(len(range_ecg) * 0.2)

        # Finding the index of the max value of ECG signal within the selected range, and doing the proper conversions.
        max_ecg = np.array(list(np.where(range_ecg == np.max(range_ecg[0: int(percentage)]))))

        # The index of that peak will be the index of P peak in the derivative signal plus the index of the max value.
        P_peaks[i] = P_peaks_der[i] + max_ecg[0, 0]

    # Converting the P peaks indices to integer.
    P_peaks = P_peaks.astype(np.int64)

# ======================================================================================================================
# ======================================================= T WAVE =======================================================
# ======================================================================================================================

# ----------------------------------------------------- Derivative -----------------------------------------------------

    # Creating an array to find the T peaks in the derivative signal, based on R peaks in the derivative signal.
    T_peaks_der = np.empty([len(R_peaks_der) - 1])

    # Between each two R peaks in the derivative signal, the T peaks reside in the 10-50% horizontal range.
    for i in range(0, len(T_peaks_der)):
        # Creating an array that holds the derivative values between two R peaks in the derivative signal.
        range_der = der_ecg[R_peaks_der[i]:R_peaks_der[i + 1]]

        # Selecting the range within 10-50% of that range.
        percentage_h = np.round(len(range_der) * 0.5)
        percentage_l = np.round(len(range_der) * 0.1)

        # Finding the index of the max value within the selected range, and doing the proper conversions.
        max_der = np.array(list(np.where(range_der == np.max(range_der[int(percentage_l): int(percentage_h)]))))

        # The index of that peak will be the index of R peak in the derivative signal plus the index of the max value.
        T_peaks_der[i] = R_peaks_der[i] + max_der[0, 0]

    # Converting the T peaks indices to integer.
    T_peaks_der = T_peaks_der.astype(np.int64)

# --------------------------------------------------------- ECG --------------------------------------------------------

    # Creating an array to find the T peaks in the ECG signal.
    T_peaks = np.empty([len(T_peaks_der)])

    # Between each two T peaks in the derivative signal, the T peak in the ECG signal resides in the first 20% distance.
    for i in range(0, len(T_peaks) - 1):
        # Creating an array that holds the ECG values between two T peaks in the derivative signal.
        range_ecg = ecg[T_peaks_der[i]: T_peaks_der[i + 1]]

        # Selecting the first 20% of that range.
        percentage = np.round(len(range_ecg) * 0.2)

        # Finding the index of the max value of ECG signal within the selected range, and doing the proper conversions.
        max_ecg = np.array(list(np.where(range_ecg == np.max(range_ecg[0: int(percentage)]))))

        # The index of that peak will be the index of T peak in the derivative signal plus the index of the max value.
        T_peaks[i] = T_peaks_der[i] + max_ecg[0, 0]

    # Converting the T peaks indices to integer.
    T_peaks = T_peaks.astype(np.int64)

    return R_peaks, P_peaks, T_peaks


def plot_peaks(time, ecg, R_peaks, P_peaks, T_peaks):
    fig, ax1 = plt.subplots(figsize = (15,10))
    ax1.plot(time, ecg, color = 'k')
    ax1.plot(time[R_peaks], ecg[R_peaks], "o", color = 'r', label = 'R Peak')
    ax1.plot(time[P_peaks], ecg[P_peaks], "P", color = 'g', label = 'P Peak')
    ax1.plot(time[T_peaks], ecg[T_peaks], "X", color = 'm', label = 'T Peak')
    if isinstance(time[0], str):
        ax1.xaxis.set_major_locator(ticker.MultipleLocator(3))
        plt.setp(ax1.xaxis.get_majorticklabels(), rotation = 90)
        plt.xlabel('Time')
    else:
        plt.xlabel('Time (in redefined steps)')
    ax1.set_ylabel('Signal Value')
    plt.title('The ECG Signal with R, P, and T Peaks')
    ax1.legend()
