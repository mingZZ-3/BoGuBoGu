package com.graduate.howtospeak


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.media.ExifInterface
import android.media.ImageReader
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.SparseIntArray
import android.view.SurfaceHolder
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice.*
import java.io.File
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class PracticeActivity : AppCompatActivity() {

    // ======== 버튼 값 전달
    private lateinit var textView_vowel: TextView
    private lateinit var vowel_getby: String

    // take pic
    var camera: Camera? = null

    // ======== camera preview

    private lateinit var mSurfaceViewHolder: SurfaceHolder
    private lateinit var mImageReader: ImageReader
    private lateinit var mCameraDevice: CameraDevice
    private lateinit var mPreviewBuilder: CaptureRequest.Builder
    private lateinit var mSession: CameraCaptureSession

    private var mHandler: Handler? = null

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private lateinit var mSensorManager: SensorManager

    private val deviceOrientation: DeviceOrientation by lazy { DeviceOrientation() }
    private var mHeight: Int = 0
    private var mWidth:Int = 0

    var mCameraId = CAMERA_BACK

    companion object
    {
        const val CAMERA_BACK = "1"
        const val CAMERA_FRONT = "0"

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270)
        }
    }

    // =======================


    // voice
    private var mRecorder: MediaRecorder? = null
    private var recordoutput: String? = null
    private var isRecording: Boolean = false
    private var recordingStopped: Boolean = false
    // video
    private val REQUEST_VIDEO_CAPTURE_CODE = 1
    private var videoUri : Uri? = null
    private var isVideoPlaying = false

    // take pic
    private var file: File? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice)


        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        mtResult1.setOnClickListener {
            val intent = Intent(this, Practice_Result::class.java)
            startActivity(intent) }

        mtPractice_vowel.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent) }


        // take pic 실행
        bt_rstop.setOnClickListener {
            takePicture()
        }





        // surface Preview 실행
        initSensor()
        initView()


        // vowel 버튼값 전달
        textView_vowel = findViewById(R.id.learning_View) as TextView
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



/*
        // play button
        bt_rstart.setOnClickListener {
            //val recordVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

            /*
            // video part
            val videoFile = File(
                File("${filesDir}/video").apply {
                    if(!this.exists()){
                        this.mkdirs()
                    }
                },
                FileName())
            videoUri = FileProvider.getUriForFile(
                this,
                "com.graduate.howtospeak.fileprovider",
                videoFile
            )
            recordVideoIntent.resolveActivity(packageManager)?.also{
                recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                startActivityForResult(recordVideoIntent, REQUEST_VIDEO_CAPTURE_CODE)
            }
            //
             */

            // voice part
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission is not granted
                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)

            } else {
                //startRecording()
            }
        }

        bt_rstop.setOnClickListener {
           // stopRecording()
        }

 */




    }


    // ============= voice recording
    private fun stopRecording() {
        if(isRecording){
            mRecorder?.reset()
            mRecorder?.release()
            isRecording = false
            Toast.makeText(this, "중지 되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "레코딩 상태가 아닙니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {
        val fileName: String = Date().time.toString() + ".mp3"
        recordoutput = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + fileName //내장메모리 밑에 위치
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
        } catch (e: IllegalStateException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
    }


    /*
    // file name
    private fun FileName() : String{
        val sdf = SimpleDateFormat("yyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.mp4"
    }
     */


    // ============== camera preview 함수

    private fun initSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun initView() {
        with(DisplayMetrics()){
            windowManager.defaultDisplay.getMetrics(this)
            mHeight = heightPixels
            mWidth = widthPixels
        }

        mSurfaceViewHolder = camera_preview.holder
        mSurfaceViewHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                initCameraAndPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mCameraDevice.close()
            }

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
            // toast("카메라를 열지 못했습니다.")
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
                //필요할 경우 플래시가 자동으로 켜짐
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


    // ============== take pic
    private fun takePicture() {
        if (mCameraDevice == null) return
        // Create a CaptureRequest.Builder for taking photos
        val captureRequestBuilder: CaptureRequest.Builder
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // The imageReader surface as the target of CaptureRequest.Builder
            captureRequestBuilder.addTarget(mImageReader.getSurface())
            // auto focus
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // automatic exposure
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            // Get the phone direction
            val rotation = getWindowManager().getDefaultDisplay().getRotation()
            // Calculate the direction of the photo based on the device orientation
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            //photograph
            val mCaptureRequest : CaptureRequest = captureRequestBuilder.build()
            mSession.capture(mCaptureRequest, null, mHandler)
        } catch ( e : CameraAccessException) {
            e.printStackTrace();
        }
    }




}

