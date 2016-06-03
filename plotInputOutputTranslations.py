import matplotlib.pyplot as plt
import numpy

fname = ".\images\inputOutputComparison.csv";

a= numpy.loadtxt(fname, 'float', comments='#', delimiter=None, converters=None, skiprows=0, usecols=None, unpack=False, ndmin=0)
print('Size of the array a ')
print(a.size)
numberOfImages = a.size/(2*6) # 2d array of 6 blocks
print('Number of images for the plotting function')
print(numberOfImages)
imageIndex = numpy.arange(0,numberOfImages,1)

# analyse bias in the translations.
i=0
biasX = numpy.zeros(numberOfImages);
biasY = numpy.zeros(numberOfImages);
biasZ = numpy.zeros(numberOfImages);
#last element is not reached in python
biasIndex=0
for i in range(0*numberOfImages+1, 1*numberOfImages+1,1):
    biasX[biasIndex] = a[i-1,0]-a[i-1,1]
    print(" X translation of image {a1} in = {a2} out = {a3} diff = {a4}".format(a1=i,a2=a[i-1,0],a3=a[i-1,1],a4=biasX[i-1]) )
    biasIndex = biasIndex+1

biasIndex=0
for i in range(1*numberOfImages+1, 2*numberOfImages+1,1):
    biasY[biasIndex] = a[i-1,0]-a[i-1,1]
    print(" Y translation of image {a1} in = {a2} out = {a3} diff = {a4}".format(a1=i,a2=a[i-1,0],a3=a[i-1,1],a4=biasY[biasIndex]) )
    biasIndex = biasIndex+1

biasIndex=0
for i in range(2*numberOfImages+1, 3*numberOfImages+1,1):
    biasZ[biasIndex] = a[i-1,0]-a[i-1,1]
    print(" Z translation of image {a1} in = {a2} out = {a3} diff = {a4}".format(a1=i,a2=a[i-1,0],a3=a[i-1,1],a4=biasZ[biasIndex]) )
    biasIndex = biasIndex+1


# Biases plot
p1 = plt.plot(imageIndex, biasX,'-o',label='Bias in X')
p2 = plt.plot(imageIndex, biasY,'-o',label='Bias in Y')
p3 = plt.plot(imageIndex, biasZ,'-o',label='Bias in Z')
plt.xlabel('Image index')
plt.ylabel('Biases')
plt.title('Biases  x y z = Input - retrieved')
plt.grid(True)
plt.legend(loc='best')
plt.savefig("pythonOutput\Biases xyz.png")
plt.show()
 
# # X
# startIndex = numberOfImages*0
# endIndex   = numberOfImages + startIndex
# p1 = plt.plot(imageIndex, a[startIndex:endIndex,0],'-o',label='Translation Tx used to generate images')
# p2 = plt.plot(imageIndex, a[startIndex:endIndex,1],'-o',label='Translation Tx estimated from images')
# plt.xlabel('Input translations x')
# plt.ylabel('Output translations')
# plt.title('Translation comparison, x')
# plt.grid(True)
# plt.legend(loc='best')
# plt.savefig("pythonOutput\Translation x.png")
# plt.show()
# 
# # Y
# startIndex = numberOfImages*1
# endIndex   = numberOfImages + startIndex
# p1 = plt.plot(imageIndex, a[startIndex:endIndex,0],'-o',label='Translation Ty used to generate images')
# p2 = plt.plot(imageIndex, a[startIndex:endIndex,1],'-o',label='Translation Ty estimated from images')
# plt.xlabel('Input translations Y')
# plt.ylabel('Output translations')
# plt.title('Translation comparison, Y')
# plt.grid(True)
# plt.legend(loc='best')
# plt.savefig("pythonOutput\Translation Y.png")
# plt.show()
# 
# # Z
# startIndex = numberOfImages*2
# endIndex   = numberOfImages + startIndex
# p1 = plt.plot(imageIndex, a[startIndex:endIndex,0],'-o',label='Translation Tz used to generate images')
# p2 = plt.plot(imageIndex, a[startIndex:endIndex,1],'-o',label='Translation Tz estimated from images')
# plt.xlabel('Input translations Z')
# plt.ylabel('Output translations')
# plt.title('Translation comparison, Z')
# plt.grid(True)
# plt.legend(loc='best')
# plt.savefig("pythonOutput\Translation Z.png")
# plt.show()
# 
# # Rot X
# startIndex = numberOfImages*3
# endIndex   = numberOfImages + startIndex
# p1 = plt.plot(imageIndex, a[startIndex:endIndex,0],'-o',label='Rotation Rx used to generate images')
# p2 = plt.plot(imageIndex, +57.3*a[startIndex:endIndex,1],'-o',label='Rotation Rx estimated from images')
# plt.xlabel('Input Rotations R X')
# plt.ylabel('Output Rotations R X')
# plt.title('Rotation comparison, R X')
# plt.grid(True)
# plt.legend(loc='best')
# plt.savefig("pythonOutput\Rotations R X.png")
# plt.show()
# 
# # Rot Y
# startIndex = numberOfImages*4
# endIndex   = numberOfImages + startIndex
# p1 = plt.plot(imageIndex, a[startIndex:endIndex,0],'-o',label='Rotation Ry used to generate images')
# p2 = plt.plot(imageIndex, +57.3*a[startIndex:endIndex,1],'-o',label='Rotation Ry estimated from images')
# plt.xlabel('Input Rotations R Y')
# plt.ylabel('Output Rotations R Y')
# plt.title('Rotation comparison, R Y')
# plt.grid(True)
# plt.legend(loc='best')
# plt.savefig("pythonOutput\Rotations R Y.png")
# plt.show()
# 
# 
# # Rot Z
# startIndex = numberOfImages*5
# endIndex   = numberOfImages + startIndex
# p1 = plt.plot(imageIndex, a[startIndex:endIndex,0],'-o',label='Rotation Rz used to generate images')
# p2 = plt.plot(imageIndex, +57.3*a[startIndex:endIndex,1],'-o',label='Rotation Rz estimated from images')
# plt.xlabel('Input Rotations R Z')
# plt.ylabel('Output Rotations R Z')
# plt.title('Rotation comparison, R Z')
# plt.grid(True)
# plt.legend(loc='best')
# plt.savefig("pythonOutput\Rotations R Z.png")
# plt.show()
# 