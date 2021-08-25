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
import com.graduate.howtospeak.Retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.*
import java.nio.ByteBuffer
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore


@Suppress("DEPRECATION")
class PracticeActivity : AppCompatActivity() {

    // ======== 버튼 값 전달
    private lateinit var textView_vowel: TextView
    private lateinit var vowel_getby: String

    // surfaceview 시도중 -- done
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
        ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 180)
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 270)
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 0)
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 90)
    }


    // 이미지 post
    var url_postImg: Uri? = null


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


        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        mtResult1.setOnClickListener {
            val intent = Intent(this, Practice_Result::class.java)
            startActivity(intent)

            CoroutineScope(Dispatchers.Main).launch {

            }
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
            "a" -> textView_vowel.text = "ㅏ 학습 영상 영역"
            "eo" -> textView_vowel.text = "ㅓ 학습 영상 영역"
            "i" -> textView_vowel.text = "ㅣ 학습 영상 영역"
            "o" -> textView_vowel.text = "ㅗ 학습 영상 영역"
            "u" -> textView_vowel.text = "ㅜ 학습 영상 영역"
            "e" -> textView_vowel.text = "ㅐ/ㅔ 학습 영상 영역"
            else -> textView_vowel.text = "error"
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

    private fun startRecording(): Boolean {

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


        // 0804 봉인
        val fileName_voice: String = Date().time.toString() + ".mp3"
        recordoutput = Environment.getExternalStorageDirectory().absolutePath + "/files/" + fileName_voice //내장메모리 밑에 위치


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
    }

    // file name
    private fun FileName(): String {
        val sdf = SimpleDateFormat("yyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.mp4"
    }

    /*
    //powerDb = 20 * log10(getAmplitude() / referenceAmp);
    // ============ decibel level
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

    // ============== camera surfaceview part
    private fun initSensor() {
        mSurfaceView = findViewById(R.id.camera_preview)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        //var deviceOrientation: DeviceOrientation
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

        mSurfaceViewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    @TargetApi(19)
    fun initCameraAndPreview() {
        val handlerThread = HandlerThread("CAMERA2")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
        val mainHandler = Handler(mainLooper)
        try {
            val mCameraId = "" + CameraCharacteristics.LENS_FACING_BACK // front-후면 카메라 사용
            val mCameraManager = this.getSystemService(CAMERA_SERVICE) as CameraManager
            val characteristics = mCameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val largestPreviewSize: Size = map!!.getOutputSizes(ImageFormat.JPEG)[0]
            Log.i(
                "LargestSize",
                largestPreviewSize.width.toString() + " " + largestPreviewSize.height)

            setAspectRatioTextureView(largestPreviewSize.height, largestPreviewSize.width)
            mImageReader = ImageReader.newInstance(
                largestPreviewSize.width,
                largestPreviewSize.height,
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

    private val ImageAvailableListener =
        ImageReader.OnImageAvailableListener { reader ->
            val image: Image = reader.acquireNextImage()
            val buffer: ByteBuffer = image.planes.get(0).getBuffer()
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
                        //CaptureRequest.CONTROL_AE_MODE_OFF
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                    )
                    // added
                    /*
                    mPreviewBuilder.set(
                        CaptureRequest.SENSOR_EXPOSURE_TIME,
                        1000000000 / 80
                    )
                     */

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
        insertImage(getContentResolver(), bitmap, "Img" + System.currentTimeMillis(), "")
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
        var test_Onputstream : OutputStream //+

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (source != null) {
                val imageOut: OutputStream? = cr.openOutputStream(url!!)
                //+
                val immm = ByteArrayOutputStream()

                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 20, imageOut)
                    source.compress(Bitmap.CompressFormat.JPEG, 20, immm)  //+
                    //test_bitmap.compress(Bitmap.CompressFormat.JPEG, 20, immm)  //+
                    Log.d("retrofit_image_check", "사진 bitmap : "+source)

                    // 코루틴 :  서버 전송

                    val test_post_image = CoroutineScope(Dispatchers.IO).launch {
                        var post_filename = Date().time.toString()
                        post_filename = "PostImg_" + post_filename + ".jpeg"
                        Log.d("post_img_name : ", post_filename)

                        val requestBody: RequestBody = RequestBody.create(
                            MediaType.parse("image/jpg"), immm.toByteArray())
                        Log.d("retrofit_image_check", "사진 requestBody : "+requestBody)
                        val uploadFile =
                            MultipartBody.Part.createFormData("postImg", post_filename, requestBody)

                        RetrofitBuilder.api.post_image(uploadFile)
                        Log.d("Post", "line done")
                    }



                    /*
                    //==== retrofit  //+
                    var post_filename = Date().time.toString()
                    post_filename = "/IMG_AD_" + post_filename + ".jpeg"
                    Log.d("post_img_name : ", post_filename)

                    val requestBody: RequestBody = RequestBody.create(
                        MediaType.parse("image/jpg"), immm.toByteArray())
                    Log.d("retrofit_image_check", "사진 requestBody : "+requestBody)
                    val uploadFile =
                        MultipartBody.Part.createFormData("postImg", post_filename, requestBody)

                    RetrofitBuilder.api.post_image(uploadFile)
                    Log.d("Post", "line done")
                    //=====

                     */


                }catch (e:Exception){
                    Log.d("Post", "error")
                    e.printStackTrace()
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
            Log.d("retrofit_image_check", "사진 url : " + stringUrl)  //+

        }

        return stringUrl
    }


    /* --> cor
    fun SaveImageTask(data: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            var bitmap: Bitmap? = null

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    bitmap = getRotatedBitmap(data[0], mDeviceRotation)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                insertImage(getContentResolver(), bitmap, "" + System.currentTimeMillis(), "")

            }
        }

    }

     */

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
        //mSurfaceView.layoutParams = FrameLayout.LayoutParams(viewWidth, viewHeight)
        //mSurfaceView.layoutParams = FrameLayout.LayoutParams(320, 480)
    }
    //==============================================
}