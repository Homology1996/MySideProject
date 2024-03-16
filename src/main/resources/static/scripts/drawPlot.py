#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import numpy as np
import matplotlib.pyplot as plt

def generateCoordinate(line, array):
  split = line.split(",")
  for num in split:
    array.append(int(num))

def main(inputPath, outputPath):
  file = open(inputPath, "r")
  # Get data
  food = file.readline()
  clothes = file.readline()
  entertainment = file.readline()
  accommodation = file.readline()
  transportation = file.readline()
  ratio = file.readline()
  foodCoordinates = []
  clothesCoordinates = []
  entertainmentCoordinates = []
  accommodationCoordinates = []
  transportationCoordinates = []
  ratioCoordinates = []
  generateCoordinate(food, foodCoordinates)
  generateCoordinate(clothes, clothesCoordinates)
  generateCoordinate(entertainment, entertainmentCoordinates)
  generateCoordinate(accommodation, accommodationCoordinates)
  generateCoordinate(transportation, transportationCoordinates)
  generateCoordinate(ratio, ratioCoordinates)
  file.close()
  # Draw plot
  fig, axs = plt.subplots(2, 3, figsize=(18, 9))
  # Food
  axs[0, 0].plot(range(len(foodCoordinates)), foodCoordinates)
  axs[0, 0].set_xticks([], [])
  axs[0, 0].set_title("Food")
  axs[0, 0].set_ylabel("Expense")
  axs[0, 0].set_xlabel("Date")
  # Clothes
  axs[0, 1].plot(range(len(clothesCoordinates)), clothesCoordinates)
  axs[0, 1].set_xticks([], [])
  axs[0, 1].set_title("Clothes")
  axs[0, 1].set_ylabel("Expense")
  axs[0, 1].set_xlabel("Date")
  # Entertainment
  axs[0, 2].plot(range(len(entertainmentCoordinates)), entertainmentCoordinates)
  axs[0, 2].set_xticks([], [])
  axs[0, 2].set_title("Entertainment")
  axs[0, 2].set_ylabel("Expense")
  axs[0, 2].set_xlabel("Date")
  # Accommodation
  axs[1, 0].plot(range(len(accommodationCoordinates)), accommodationCoordinates)
  axs[1, 0].set_xticks([], [])
  axs[1, 0].set_title("Accommodation")
  axs[1, 0].set_ylabel("Expense")
  axs[1, 0].set_xlabel("Date")
  # Transportation
  axs[1, 1].plot(range(len(transportationCoordinates)), transportationCoordinates)
  axs[1, 1].set_xticks([], [])
  axs[1, 1].set_title("Transportation")
  axs[1, 1].set_ylabel("Expense")
  axs[1, 1].set_xlabel("Date")
  # Ratio
  axs[1, 2].bar(range(len(ratioCoordinates)), ratioCoordinates)
  axs[1, 2].set_xticks([], [])
  axs[1, 2].set_title("Expense")
  axs[1, 2].set_ylabel("Ratio")
  axs[1, 2].set_xlabel("Type")
  if (len(ratioCoordinates) == 5):
    xticks = ["fo", "cl", "en", "ac", "tr"]
    axs[1, 2].set_xticks(range(len(ratioCoordinates)), xticks)
  # Save figure
  fig.savefig(outputPath)
  plt.close(fig)  

# 這裡對應到在command輸入指令啟動本程式
if __name__ == '__main__':
  systemArgument = sys.argv
  fileName = systemArgument[0]
  inputPath = systemArgument[1]
  outputPath = systemArgument[2]
  main(inputPath, outputPath)
  
# 使用方法
# 在command輸入python3 drawPlot.py 座標文件路徑 圖片路徑
