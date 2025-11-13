EdgeDemo - Real-time edge detection Android app (skeleton)
---------------------------------------------------------

What you have:
- Android app module with Camera2 capture, JNI bridge, native C++ using OpenCV (Canny),
  and a GLRenderer scaffold that uploads processed RGBA data as an OpenGL texture.

Important:
- You must add the OpenCV Android SDK to the project and adjust app/CMakeLists.txt OpenCV_DIR
  or configure OpenCV as a module/library.
- Build & run on a physical Android device (camera access required).
- Android Studio + NDK + CMake are required to build native code.

To build:
1. Open the project in Android Studio.
2. Install required SDK/NDK (via SDK Manager).
3. Add OpenCV Android SDK into the project or set OpenCV_DIR in app/CMakeLists.txt.
4. Build and run on a device.

Notes:
- The GLRenderer file is a scaffold; for a full working renderer implement EGL context creation,
  shader compilation and proper render loop using a SurfaceTexture-backed EGL surface.
