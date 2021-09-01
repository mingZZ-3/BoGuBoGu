package com.graduate.howtospeak


import android.Manifest
import android.annotation.TargetApi
import android.content.*
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
import android.media.AudioManager
import android.media.ExifInterface
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.speech.*
import android.speech.tts.Voice
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.graduate.howtospeak.Retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice.*
import kotlinx.coroutines.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.roundToInt


//   cd /srv/django_aws_test/media/tmp/

@Suppress("DEPRECATION")
class PracticeActivity : AppCompatActivity() {

    // ======== 버튼 값 전달
    private lateinit var textView_vowel: TextView
    private lateinit var vowel_getby: String

    // surfaceview
    private lateinit var mSurfaceView: SurfaceView
    //private lateinit var mCameraPreview: CameraPreview
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
    // =======================


    // voice
    private var mRecorder: MediaRecorder? = null
    private var recordoutput: String? = null
    private var isRecording: Boolean = false
    private var recordingStopped: Boolean = false
    // STT
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener

    // decible
    private lateinit var stt_decibelbar: ProgressBar
    private var stt_dB : Int = 0
    // Image/stt result send to Activity
    var stringUrl: String? = null
    var stt_result: String = ""
    //val intent_test = Intent(this, Practice_Result::class.java)

    // ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice)

        val intent_test = Intent(this, Practice_Result::class.java)

        // STT
        var stt_intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        stt_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,packageName)
        stt_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        stt_intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        setListener()

        //

        // surface Preview
        initSensor()
        initSurfaceView()


        // decibel
        stt_decibelbar = findViewById(R.id.stt_decibel) as ProgressBar


        // vowel 버튼값 전달
        textView_vowel = findViewById(R.id.learning_View) as TextView
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

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }

        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mtResult1.setOnClickListener {
            /*
            val intent = Intent(this, Practice_Result::class.java)
            intent.putExtra("Vowel_bt", vowel_getby)
            if (intent.hasExtra("ImageUri") && intent.hasExtra("STT_Result")) {
                intent.putExtra("ImageUri", stringUrl)
                intent.putExtra("STT_Result", stt_result)
            } else {
                Log.d("Send value", "error")
            }
            //startActivity(intent)
             */
            startActivity(intent_test)
        }
        mtPractice_vowel.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent)
        }

        // 연습 시작 버튼
        bt_rstart.setOnClickListener {
            // voice Permission
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

                Toast.makeText(this, "시작되었습니다.", Toast.LENGTH_SHORT).show()

                //STT & record
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

                CoroutineScope(Dispatchers.Main).async {
                    // STT
                    async {
                        speechRecognizer.setRecognitionListener(recognitionListener)
                        speechRecognizer.startListening(stt_intent)
                        delay(4000)
                        speechRecognizer.destroy()
                    }.await()

                    // Record
                    async {
                        Log.d("record :", "start")
                        startRecording()
                    }.await()
                }
            }
        }

        // 연습 종료 버튼 - stop recording & Take picture
        bt_rstop.setOnClickListener {
            // recording 중지
            CoroutineScope(Dispatchers.Main).launch {
                stopRecording()
            }

            // take pic 실행
            takePicture()

            // image intent
            intent_test.putExtra("Vowel_bt", vowel_getby)
            intent_test.putExtra("ImageUri", stringUrl)
            intent_test.putExtra("STT_Result", stt_result)

        }
    }


// ================================== functions ==================================== //

    // ============= voice recording part
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
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    // ============= STT part
    private fun setListener() {
        recognitionListener = object: RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("STT", "음성인식 준비 완료")
            }
            override fun onBeginningOfSpeech() {
                Log.d("STT", "음성 감지 시작")
            }

            // decibel
            override fun onRmsChanged(rmsdB: Float) {
                //Log.d("STT_dB", rmsdB.toString())
                stt_dB = (20* (log10(rmsdB/0.00002))).toInt()
                stt_decibelbar.progress = stt_dB


            }
            override fun onBufferReceived(buffer: ByteArray?) {

            }
            override fun onEndOfSpeech() {

            }
            override fun onError(error: Int) {
                var message: String

                when (error) {
                    SpeechRecognizer.ERROR_AUDIO ->
                        Log.e("STT_error", "오디오 에러")
                    SpeechRecognizer.ERROR_CLIENT ->
                        Log.e("STT_error", "클라이언트 에러")
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                        Log.e("STT_error", "퍼미션 없음")
                    SpeechRecognizer.ERROR_NETWORK ->
                        Log.e("STT_error", "네트워크 에러")
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                        Log.e("STT_error", "네트워크 타임아웃")
                    SpeechRecognizer.ERROR_NO_MATCH ->
                        Log.e("STT_error", "찾을 수 없음")
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                        Log.e("STT_error", "RECOGNIZER가 바쁨")
                    SpeechRecognizer.ERROR_SERVER ->
                        Log.e("STT_error", "서버가 이상함")
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                        Log.e("STT_error", "말하는 시간초과")
                    else ->
                        Log.e("STT_error", "알 수 없는 오류")
                }
            }

            override fun onResults(results: Bundle?) {
                var matches: ArrayList<String> = results?.getStringArrayList(SpeechRecognizer
                    .RESULTS_RECOGNITION) as ArrayList<String>
                Log.d("STT_result", matches.toString())
                stt_result = matches.toString()
            }
            override fun onPartialResults(partialResults: Bundle?) {
            }
            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        }
    }

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
            val mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT // front-후면 카메라 사용
            val mCameraManager = this.getSystemService(CAMERA_SERVICE) as CameraManager
            val characteristics = mCameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val largestPreviewSize: Size = map!!.getOutputSizes(ImageFormat.JPEG)[0]
            //Log.i( "LargestSize", largestPreviewSize.width.toString() + " " + largestPreviewSize.height)

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
    //private val cameraOpenCloseLock = Semaphore(1)

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
            //Log.d("DeviceRotation", mDeviceRotation.toString() + "")
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
    ) {
        val values = ContentValues()

        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

        var url: Uri? = null
        //var stringUrl: String? = null /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            stringUrl = url.toString()
            Log.d("image_uri_send", stringUrl!!)

            if (source != null) {
                val imageOut: OutputStream? = cr.openOutputStream(url!!)

                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 20, imageOut)
                    // 사진 서버 전송
                    val test_post_image = CoroutineScope(Dispatchers.IO).launch {
                        var post_filename = Date().time.toString()
                        post_filename = "PostImg_" + post_filename + ".jpeg"
                        Log.d("post_img_name : ", post_filename)

                        var post_inputstream :InputStream? = null
                        try{
                            post_inputstream = contentResolver.openInputStream(url!!)
                        } catch (e:Exception){
                            e.printStackTrace()
                        }
                        var post_bitmap : Bitmap = BitmapFactory.decodeStream(post_inputstream)
                        val post_byteArrayOutputStream = ByteArrayOutputStream()
                        post_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, post_byteArrayOutputStream)

                        val requestBody : RequestBody = RequestBody
                            .create(MediaType.parse("image/*"), post_byteArrayOutputStream.toByteArray())
                        val uploadFile = MultipartBody.Part
                            .createFormData("image", post_filename, requestBody)

                        RetrofitBuilder.api.post_image(uploadFile).enqueue(object: Callback<String>{
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("Post_result", "실패,,,ㅠㅠㅠ")
                                t.printStackTrace()
                            }
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                Log.d("Post_result","성공,,,!!")
                            }
                        })
                    }

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
    }


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
    //Log.d("@@@", "TextureView Width : $viewWidth TextureView Height : $viewHeight")
    //mSurfaceView.layoutParams = FrameLayout.LayoutParams(viewWidth, viewHeight)
    }
    //==============================================

}


