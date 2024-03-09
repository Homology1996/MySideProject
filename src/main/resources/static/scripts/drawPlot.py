#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys
import numpy as np
import matplotlib.pyplot as plt

def main(imagePath, coordinates):
  fig = plt.figure(figsize = (10, 10))
  plt.clf()
  ax = fig.add_subplot(1, 1, 1)
  length = len(coordinates)
  ax.bar(range(length), coordinates)
  if (length == 5):
    xticks = ["food", "clothes", "entertainment", "accommodation", "transportation"]
    ax.set_title("Expense")
    ax.set_ylabel("Ratio")
    ax.set_xlabel("Type")
    ax.set_xticks(range(length), xticks)
  fig.savefig(imagePath)
  plt.close(fig)  

## 這裡對應到在command輸入指令啟動本程式
if __name__ == '__main__':
  userInput = sys.argv
  fileName = userInput[0]
  imagePath = userInput[1]
  if (len(userInput) > 2):
    coordinate = []
    for num in userInput:
      if num.isnumeric():
        coordinate.append(int(num))
    main(imagePath, coordinate)
    print("complete")
  else:
    print("error")
  
## 使用方法
## 在command輸入python3 drawPlot.py /home/bill/桌面/plot.png 1 2 1 2 1
  
