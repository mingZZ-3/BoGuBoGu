package com.graduate.howtospeak


import android.Manifest
import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.media.ExifInterface
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore


@Suppress("DEPRECATION")
class PracticeActivity : AppCompatActivity() {

    // ======== 버튼 값 전달
    private lateinit var textView_vowel: TextView
    private lateinit var vowel_getby: String

    // take pic __ delete
    //var camera: Camera2? = null
/*
    // ======== camera preview
    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mSurfaceViewHolder: SurfaceHolder
    private lateinit var mImageReader: ImageReader
    private lateinit var mCameraDevice: CameraDevice
    private lateinit var mPreviewBuilder: CaptureRequest.Builder
    private lateinit var mSession: CameraCaptureSession
    private var mDeviceRotation: Int ?= 0

    private var mHandler: Handler? = null

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private lateinit var mSensorManager: SensorManager

    private val deviceOrientation: DeviceOrientation by lazy { DeviceOrientation() }
    private var mHeight: Int = 0
    private var mWidth:Int = 0

    private var mCameraId = CAMERA_BACK

    companion object
    {
        const val CAMERA_BACK = "1"
        const val CAMERA_FRONT = "0"

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 90)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 180)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 270)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 0)

            /* 기존1
            ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270)
             */
        }
    }
    */
    // 시도중
    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mCameraPreview: CameraPreview
    private lateinit var mSurfaceViewHolder: SurfaceHolder
    private lateinit var mImageReader: ImageReader
    private var mCameraDevice: CameraDevice? = null
    private lateinit var mPreviewBuilder: CaptureRequest.Builder
    private lateinit var mSession: CameraCaptureSession
    private var mDeviceRotation: Int? = 0

    private var mHandler: Handler? = null

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private lateinit var mSensorManager: SensorManager

    private val deviceOrientation: DeviceOrientation by lazy { DeviceOrientation() }
    private var mDSI_height: Int = 0
    private var mDSI_width: Int = 0

    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0)
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90)
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180)
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270)
    }

    // =======================


    // voice
    private var mRecorder: MediaRecorder? = null

    //private var recordoutput: File? =null
    var recordoutput: String? = null
    private var isRecording: Boolean = false
    private var recordingStopped: Boolean = false
    // take pic
    //private var file: File? = null
    // dB
    //private var dBvalue: Int? = 0


    // ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice)

        mSurfaceView = findViewById<SurfaceView>(R.id.camera_preview)


        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        mtResult1.setOnClickListener {
            val intent = Intent(this, Practice_Result::class.java)
            startActivity(intent)
        }

        mtPractice_vowel.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent)
        }


        // surface Preview 실행
        initSensor()
        //initView()
        initSurfaceView()


        // vowel 버튼값 전달
        textView_vowel = findViewById<TextView>(R.id.learning_View)
        vowel_getby = intent.getStringExtra("vowel_tolearn").toString()

        when (vowel_getby) {
            "a" -> textView_vowel.setText("ㅏ 학습 영상 영역")
            "eo" -> textView_vowel.setText("ㅓ 학습 영상 영역")
            "i" -> textView_vowel.setText("ㅣ 학습 영상 영역")
            "o" -> textView_vowel.setText("ㅗ 학습 영상 영역")
            "u" -> textView_vowel.setText("ㅜ 학습 영상 영역")
            "e" -> textView_vowel.setText("ㅐ/ㅔ 학습 영상 영역")
            else -> textView_vowel.setText("error")
        }


        //
        /*
        var amplitudeLevel = findViewById(R.id.amplitudelevel)
        amplitudeLevel.setProgress(dBvalue)
     */


        // play button
        bt_rstart.setOnClickListener {

            //  voice part
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //Permission is not granted
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                ActivityCompat.requestPermissions(this, permissions, 0)

            } else {
                startRecording()
            }
        }

        bt_rstop.setOnClickListener {
            // recording 중지
            stopRecording()
            // take pic 실행
            takePicture()

        }


        // amplitude level bar


    }


// ================================== functions ==================================== //

    // ============= voice recording
    private fun stopRecording() {
        if (isRecording) {
            mRecorder?.reset()
            mRecorder?.release()
            isRecording = false
            Toast.makeText(this, "중지 되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "레코딩 상태가 아닙니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {

        // 기존
        val fileName: String = Date().time.toString() + ".mp3"
        recordoutput =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/" + fileName //내장메모리 밑에 위치

        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource((MediaRecorder.AudioSource.MIC))
        mRecorder?.setOutputFormat((MediaRecorder.OutputFormat.MPEG_4))
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder?.setOutputFile(recordoutput)

        try {
            mRecorder?.prepare()
            mRecorder?.start()
            isRecording = true
            Toast.makeText(this, "시작되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        /* 0818 봉인
        // 0804 봉인
        val fileName: String = Date().time.toString() + ".mp3"
        recordoutput = Environment.getExternalStorageDirectory().absolutePath + "/files/" + fileName //내장메모리 밑에 위치


        try {
            mRecorder = MediaRecorder()

            mRecorder?.setAudioSource((MediaRecorder.AudioSource.MIC))

            //0804 added
            /*
            mRecorder?.setOutputFormat((MediaRecorder.OutputFormat.THREE_GPP))
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder?.setOutputFile(recordoutput!!.getAbsolutePath())
            */
            mRecorder?.setOutputFormat((MediaRecorder.OutputFormat.MPEG_4))
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mRecorder?.setOutputFile(recordoutput)

            mRecorder?.prepare()
            mRecorder?.start()
            isRecording = true
            return true
            //Toast.makeText(this, "시작되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException){
            stopRecording()
            e.printStackTrace()
            isRecording = false
        } catch (e: IOException){
            //mRecorder?.reset()
           // mRecorder?.release()
            mRecorder = null
            isRecording = false
            e.printStackTrace()
        }
        return false
         */
    }

    // file name
    private fun FileName(): String {
        val sdf = SimpleDateFormat("yyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.mp4"
    }

    /*
    //powerDb = 20 * log10(getAmplitude() / referenceAmp);
    // decibel level
    private fun getMaxAmplitude() : Int {
        if (mRecorder != null) {
            try {
                return mRecorder!!.getMaxAmplitude()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                return 0
            }
        } else {
            return 5
        }
    }

    private fun getMyRecAudioFile(): File {
        return recordoutput!!
    }

    private fun setMyRecAudioFile(recordoutput: File){
        this.recordoutput = recordoutput
    }

    private fun startListenAudio() {
    }

    private void startListenAudio() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRun) {
                    try {
                        if(bListener) {
                            volume = mRecorder.getMaxAmplitude(); //Get sound pressure value
                            if(volume > 0 && volume < 1000000) {
                                World.setDbCount(20 * (float)(Math.log10(volume))); //Convert sound pressure value to decibel value
                            }
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        bListener = false;
                    }
                }
            }
        });
        thread.start();
    }

     */

    // ======================================

    // ============== take pic
    /* 기존
    private fun takePicture() {


        if (null == mCameraDevice) return

        val captureRequestBuilder: CaptureRequest.Builder
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureRequestBuilder.addTarget(mImageReader.getSurface())

            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)

            // 0804 added
            //mDeviceRotation = ORIENTATIONS.get(deviceOrientation.getOrientation())
            val rotation = getWindowManager().getDefaultDisplay().getRotation()

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            val mCaptureRequest : CaptureRequest = captureRequestBuilder.build()
            mSession.capture(mCaptureRequest, null, mHandler)


        } catch ( e : CameraAccessException) {
            e.printStackTrace()
        }
    }
     */

    // 시도중

    // https://webnautes.tistory.com/822
    // ======================================

    // ============== camera surfaceview part
/*
    private fun initSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun initView() {
        // 0819 봉인
        with(DisplayMetrics()){
            windowManager.defaultDisplay.getMetrics(this)
            mHeight = heightPixels
            mWidth = widthPixels
        }


        // 0819 추가
        //mSurfaceView = findViewById<SurfaceView>(R.id.camera_preview)

        mSurfaceViewHolder = camera_preview.holder
        mSurfaceViewHolder.addCallback(object : SurfaceHolder.Callback {
            // 뷰 생성 시점
            override fun surfaceCreated(holder: SurfaceHolder) {
                initCameraAndPreview()
            }
            // 뷰 소멸 시점
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mCameraDevice.close()
            }
            // 뷰 변동 시점(화면 회전)
            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int,
                width: Int, height: Int
            ) {

            }
        })

        btn_convert.setOnClickListener { switchCamera() }
    }

    private fun switchCamera() {
        when(mCameraId){
            CAMERA_BACK -> {
                mCameraId = CAMERA_FRONT
                mCameraDevice.close()
                openCamera()
            }
            else -> {
                mCameraId = CAMERA_BACK
                mCameraDevice.close()
                openCamera()
            }
        }
    }


    fun initCameraAndPreview() {
        val handlerThread = HandlerThread("CAMERA2")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)

        openCamera()
/*
        private val = object : imageReader.OnImageAvailableListener m)m
        @Override
        fun onImageAvailable(reader: ImageReader) {
            /*
            var image: Image = reader.acquireNextImage()
            var buffer: ByteBuffer = image.planes[0].getBuffer()
            var bytes: byte
             */
            var image: Image = reader.acquireNextImage()
            var buffer = image.planes[0].buffer
            var bytes = ByteArray(buffer.remaining())
            Buffer.get(bytes) //Save the byte array from the buffer

            var bitmap : Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        }
*/

    }

    private fun openCamera() {
        try {
            val mCameraManager = this.getSystemService(CAMERA_SERVICE) as CameraManager
            val characteristics = mCameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            val largestPreviewSize = map!!.getOutputSizes(ImageFormat.JPEG)[0]
            setAspectRatioTextureView(largestPreviewSize.height, largestPreviewSize.width)

            mImageReader = ImageReader.newInstance(
                largestPreviewSize.width,
                largestPreviewSize.height,
                ImageFormat.JPEG,
                7
            )

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) return



            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler)


        } catch (e: CameraAccessException) {
            //toast("카메라를 열지 못했습니다.")
        }
    }

    private val deviceStateCallback = object : CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            try {
                takePreview()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCameraDevice.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            // toast("카메라를 열지 못했습니다.")
        }
    }

    @Throws(CameraAccessException::class)
    fun takePreview() {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mPreviewBuilder.addTarget(mSurfaceViewHolder.surface)
        mCameraDevice.createCaptureSession(
            listOf(mSurfaceViewHolder.surface, mImageReader.surface), mSessionPreviewStateCallback, mHandler
        )
    }

    private val mSessionPreviewStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            mSession = session
            try {
                // Key-Value 구조로 설정
                // 오토포커싱이 계속 동작
                mPreviewBuilder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                // 필요할 경우 플래시가 자동으로 켜짐
                mPreviewBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                )
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        }


        override fun onConfigureFailed(session: CameraCaptureSession) {
            Toast.makeText(this@PracticeActivity, "카메라 구성 실패", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onResume() {
        super.onResume()

        mSensorManager.registerListener(
            deviceOrientation.eventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI
        )
        mSensorManager.registerListener(
            deviceOrientation.eventListener, mMagnetometer, SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(deviceOrientation.eventListener)
    }

    private fun setAspectRatioTextureView(ResolutionWidth: Int, ResolutionHeight: Int) {
        if (ResolutionWidth > ResolutionHeight) {
            val newWidth = mWidth
            val newHeight = mWidth * ResolutionWidth / ResolutionHeight
            updateTextureViewSize(newWidth, newHeight)

        } else {
            val newWidth = mWidth
            val newHeight = mWidth * ResolutionHeight / ResolutionWidth
            updateTextureViewSize(newWidth, newHeight)
        }

    }

    private fun updateTextureViewSize(viewWidth: Int, viewHeight: Int) {
        //Log.d("ViewSize", "TextureView Width : $viewWidth TextureView Height : $viewHeight")
        camera_preview.layoutParams = FrameLayout.LayoutParams(viewWidth, viewHeight)
    }

    // ======================================
*/
    private fun initSensor() {
        mSurfaceView = findViewById<SurfaceView>(R.id.camera_preview)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        var deviceOrientation: DeviceOrientation
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(
            deviceOrientation.eventListener,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
        mSensorManager.registerListener(
            deviceOrientation.eventListener,
            mMagnetometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(deviceOrientation.eventListener)
    }

    fun initSurfaceView() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        mDSI_height = displayMetrics.heightPixels
        mDSI_width = displayMetrics.widthPixels

        mSurfaceViewHolder = mSurfaceView.holder
        mSurfaceViewHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                initCameraAndPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                if (mCameraDevice != null) {
                    mCameraDevice!!.close()
                    mCameraDevice = null
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }
        })
    }

    @TargetApi(19)
    fun initCameraAndPreview() {
        val handlerThread = HandlerThread("CAMERA2")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
        val mainHandler = Handler(mainLooper)
        try {
            val mCameraId = "" + CameraCharacteristics.LENS_FACING_BACK // 후면 카메라 사용
            val mCameraManager = this.getSystemService(CAMERA_SERVICE) as CameraManager
            val characteristics = mCameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val largestPreviewSize: Size = map!!.getOutputSizes(ImageFormat.JPEG)[0]
            Log.i(
                "LargestSize",
                largestPreviewSize.getWidth().toString() + " " + largestPreviewSize.getHeight()
            )
            setAspectRatioTextureView(largestPreviewSize.getHeight(), largestPreviewSize.getWidth())
            mImageReader = ImageReader.newInstance(
                largestPreviewSize.getWidth(),
                largestPreviewSize.getHeight(),
                ImageFormat.JPEG,  /*maxImages*/
                7
            )
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler)
        } catch (e: CameraAccessException) {
            Toast.makeText(this, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private val OnImageAvailableListener =
        ImageReader.OnImageAvailableListener { reader ->
            val image: Image = reader.acquireNextImage()
            val buffer: ByteBuffer = image.getPlanes().get(0).getBuffer()
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            //SaveImageTask().execute(bitmap)
            insertImage(getContentResolver(), bitmap, "" + System.currentTimeMillis(), "")
        }

    private val deviceStateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            try {
                takePreview()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            if (mCameraDevice != null) {
                mCameraDevice!!.close();
                mCameraDevice = null;
            }
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            Toast.makeText(this@PracticeActivity, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT).show();
        }

    }
    private val cameraOpenCloseLock = Semaphore(1)

    @Throws(CameraAccessException::class)
    fun takePreview() {
        mPreviewBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mPreviewBuilder.addTarget(mSurfaceViewHolder.surface)
        mCameraDevice!!.createCaptureSession(
            Arrays.asList(
                mSurfaceViewHolder.surface,
                mImageReader.surface
            ), mSessionPreviewStateCallback, mHandler
        )
    }

    private val mSessionPreviewStateCallback: CameraCaptureSession.StateCallback =
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                mSession = session
                try {
                    mPreviewBuilder.set(
                        CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )
                    mPreviewBuilder.set(
                        CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                    )
                    mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Toast.makeText(this@PracticeActivity, "카메라 구성 실패", Toast.LENGTH_SHORT).show()
            }
        }

    private val mSessionCaptureCallback: CaptureCallback = object : CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            mSession = session
            unlockFocus()
        }

        override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
        ) {
            mSession = session
        }

        override fun onCaptureFailed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            failure: CaptureFailure
        ) {
            super.onCaptureFailed(session, request, failure)
        }
    }

    fun takePicture() {
        try {
            val captureRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE) //用来设置拍照请求的request
            captureRequestBuilder.addTarget(mImageReader.surface)
            captureRequestBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            captureRequestBuilder.set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            )


            // 화면 회전 안되게 고정시켜 놓은 상태에서는 아래 로직으로 방향을 얻을 수 없어서
            // 센서를 사용하는 것으로 변경
            //deviceRotation = getResources().getConfiguration().orientation;
            mDeviceRotation = ORIENTATIONS[deviceOrientation.orientation]
            Log.d("@@@", mDeviceRotation.toString() + "")
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mDeviceRotation)
            val mCaptureRequest = captureRequestBuilder.build()
            mSession.capture(mCaptureRequest, mSessionCaptureCallback, mHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private val mOnImageAvailableListener = OnImageAvailableListener { reader ->
        val image = reader.acquireNextImage()
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer[bytes]
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        //SaveImageTask().execute(bitmap)
        insertImage(getContentResolver(), bitmap, "" + System.currentTimeMillis(), "")
    }

    open fun getRotatedBitmap(bitmap: Bitmap, degrees: Int): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0) return bitmap
        val m = Matrix()
        m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    private fun unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewBuilder.set(
                CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
            )
            mPreviewBuilder.set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            )
            mSession.capture(
                mPreviewBuilder.build(), mSessionCaptureCallback,
                mHandler
            )
            // After this, the camera will go back to the normal state of preview.
            mSession.setRepeatingRequest(
                mPreviewBuilder.build(), mSessionCaptureCallback,
                mHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    open fun insertImage(
        cr: ContentResolver,
        source: Bitmap?,
        title: String?,
        description: String?
    ): String? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        var url: Uri? = null
        var stringUrl: String? = null /* value to be returned */
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (source != null) {
                val imageOut: OutputStream? = cr.openOutputStream(url!!)
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 20, imageOut)

                    /*
                    //==== retrofit
                    val requestBody: RequestBody = RequestBody.create(
                        MediaType.parse("image/jpg"),
                        byteArrayOutputStream.toByteArray()
                    )
                    val uploadFile =
                        MultipartBody.Part.createFormData("postImg", file.getName(), requestBody)
                    //=====
                     */

                } finally {
                    imageOut!!.close()
                }
            } else {
                url?.let { cr.delete(it, null, null) }
                url = null
            }
        } catch (e: java.lang.Exception) {
            if (url != null) {
                cr.delete(url, null, null)
                url = null
            }
        }
        if (url != null) {
            stringUrl = url.toString()
        }
        return stringUrl
    }

    /*
    private class SaveImageTask : AsyncTask<Bitmap, Void?, Void?>() {
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //Toast.makeText(this, "사진을 저장하였습니다.", Toast.LENGTH_SHORT).show()
        }


        override fun doInBackground(vararg data: Bitmap): Void? {
            var bitmap: Bitmap? = null
            try {
                bitmap = getRotatedBitmap(data[0], mDeviceRotation)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            insertImage(getContentResolver(), bitmap, "" + System.currentTimeMillis(), "")


            return null
        }
    }

     */


    private fun setAspectRatioTextureView(ResolutionWidth: Int, ResolutionHeight: Int) {
        if (ResolutionWidth > ResolutionHeight) {
            val newWidth = mDSI_width
            val newHeight = mDSI_width * ResolutionWidth / ResolutionHeight
            updateTextureViewSize(newWidth, newHeight)
        } else {
            val newWidth = mDSI_width
            val newHeight = mDSI_width * ResolutionHeight / ResolutionWidth
            updateTextureViewSize(newWidth, newHeight)
        }
    }

    private fun updateTextureViewSize(viewWidth: Int, viewHeight: Int) {
        Log.d("@@@", "TextureView Width : $viewWidth TextureView Height : $viewHeight")
        mSurfaceView.layoutParams = FrameLayout.LayoutParams(viewWidth, viewHeight)
    }


}