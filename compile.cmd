cls

echo "Deleting all images"
echo "========================================================================"
del D:\01_dc_robotics_3d\3d\04_wip_java_lenovo\Indi\images\TransformationStudy\*.jpg
del D:\01_dc_robotics_3d\3d\04_wip_java_lenovo\Indi\images\TransformationStudy\*.png

javac -g -d bin -classpath ./libs/sqlite-jdbc-3.8.11.2.jar;./bin/;./libs/miglayout-4.0.jar;./libs/opencv-310.jar -sourcepath src src/indi/*.java src/ui/*.java src/calibrator/*.java | more

