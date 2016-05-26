import matplotlib.pyplot as plt
import numpy as np



xData = np.array([
[ -399.0 , -449.5675732010887],
[ -391.0 , -444.7602422671128],
[ -386.0 , -441.8641703845566],
[ -375.0 , -429.7385731019654],
[ -394.0 , -450.4408946793549],
[ -382.0 , -436.1244733834499],
[ -396.0 , -450.0221678072791],
[ -376.0 , -431.5857113547235],
[ -396.0 , -449.0332506894850],
[ -398.0 , -449.5148685437621],
])
yData = np.array([
 [ -397.0 ,   -417.6659735823490],
 [ -371.0 ,   -393.8187728383506],
 [ -393.0 ,   -417.1127962961609],
 [ -371.0 ,   -395.0716501986705],
 [ -377.0 ,   -401.4584505250672],
 [ -380.0 ,   -405.0977877152062],
 [ -391.0 ,   -414.8786974124526],
 [ -378.0 ,   -405.3321427791841],
 [ -373.0 ,   -397.9591092122567],
 [ -384.0 ,   -406.4458618699321],
 ])
zData = np.array([
 [1003.0  , 1154.145268237333],
 [1001.0  , 1158.186932237028],
 [1009.0  , 1170.495452960122],
 [1005.0  , 1165.395978188569],
 [1019.0  , 1181.852626016076],
 [1005.0  , 1166.728458339210],
 [1005.0  , 1165.296269206738],
 [1018.0  , 1185.025504519256],
 [1018.0  , 1179.529598576631],
 [1008.0  , 1162.884828805642],
 ])
 
x = np.arange(0,10,1);

inputTrans = xData[:,0]
outputTrans = xData[:,1]
plt.plot(x, outputTrans)
plt.plot(x, inputTrans)

plt.xlabel('Input translations X')
plt.ylabel('Output translations')
plt.title('Translation comparison')
plt.grid(True)
plt.savefig("Translation X.png")
plt.show()

inputTrans  = yData[:,0]
outputTrans = yData[:,1]
plt.plot(x, outputTrans)
plt.plot(x, inputTrans)

plt.xlabel('Input translations Y')
plt.ylabel('Output translations')
plt.title('Translation comparison')
plt.grid(True)
plt.savefig("Translation Y.png")
plt.show()

inputTrans  = zData[:,0]
outputTrans = zData[:,1]
plt.plot(x, outputTrans)
plt.plot(x, inputTrans)

plt.xlabel('Input translations Z')
plt.ylabel('Output translations')
plt.title('Translation comparison')
plt.grid(True)
plt.savefig("Translation Z.png")
plt.show()
