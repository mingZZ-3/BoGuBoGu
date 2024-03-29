package com.graduate.howtospeak.view.practice


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
import android.media.ExifInterface
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.speech.*
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.graduate.howtospeak.R
import com.graduate.howtospeak.Retrofit.RetrofitBuilder
import com.graduate.howtospeak.utils.DeviceOrientation
import com.graduate.howtospeak.view.main.MainActivity
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


//   cd /srv/django_aws_test/media/tmp/



@Suppress("DEPRECATION")
class PracticeActivity : AppCompatActivity() {

    //====== 변수 ======//
    // 버튼 tag 값에 따른 처리
    private lateinit var assist_view: ImageView
    private lateinit var vowel_getby: String

    // Image/stt 결과 전달
    private var stringUrl: String = ""
    private var stt_result: String = ""

    // surfaceview
    private lateinit var mSurfaceView: SurfaceView
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
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 90) }


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


    // ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice)

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }

        // 액티비티간 전달할 값들 위함
        val intent_result = Intent(this, Practice_Result::class.java)

        // STT
        var stt_intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        stt_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,packageName)
        stt_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        stt_intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        setListener()


        // surface Preview
        initSensor()
        initSurfaceView()


        // decibel
        stt_decibelbar = findViewById(R.id.stt_decibel) as ProgressBar


        // 학습 보조 이미지 설정
        assist_view = findViewById(R.id.assist_Imageview)
        vowel_getby = intent.getStringExtra("vowel_tolearn").toString()

        when (vowel_getby) {
            //a
            "a" -> assist_view.setImageResource(R.drawable.assist_a)
            "ga" -> assist_view.setImageResource(R.drawable.assist_a)
            "na" -> assist_view.setImageResource(R.drawable.assist_a)
            "da" -> assist_view.setImageResource(R.drawable.assist_a)

            //eo
            "eo" -> assist_view.setImageResource(R.drawable.assist_eo)
            "geo" -> assist_view.setImageResource(R.drawable.assist_eo)
            "neo" -> assist_view.setImageResource(R.drawable.assist_eo)
            "deo" -> assist_view.setImageResource(R.drawable.assist_eo)

            //i
            "i" -> assist_view.setImageResource(R.drawable.assist_i)
            "gi" -> assist_view.setImageResource(R.drawable.assist_i)
            "ni" -> assist_view.setImageResource(R.drawable.assist_i)
            "di" -> assist_view.setImageResource(R.drawable.assist_i)

            //o
            "o" -> assist_view.setImageResource(R.drawable.assist_o)
            "go" -> assist_view.setImageResource(R.drawable.assist_o)
            "no" -> assist_view.setImageResource(R.drawable.assist_o)
            "do" -> assist_view.setImageResource(R.drawable.assist_o)

            //u
            "u" -> assist_view.setImageResource(R.drawable.assist_u)
            "gu" -> assist_view.setImageResource(R.drawable.assist_u)
            "nu" -> assist_view.setImageResource(R.drawable.assist_u)
            "du" -> assist_view.setImageResource(R.drawable.assist_u)

            //e
            "e" -> assist_view.setImageResource(R.drawable.assist_e)
            "ge" -> assist_view.setImageResource(R.drawable.assist_e)
            "ne" -> assist_view.setImageResource(R.drawable.assist_e)
            "de" -> assist_view.setImageResource(R.drawable.assist_e)

            //eu
            "eu" -> assist_view.setImageResource(R.drawable.assist_eu)
            "geu" -> assist_view.setImageResource(R.drawable.assist_eu)
            "neu" -> assist_view.setImageResource(R.drawable.assist_eu)
            "deu" -> assist_view.setImageResource(R.drawable.assist_eu)

            else -> assist_view.setImageResource(R.drawable.assist_a)
        }



        // 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mtResult1.setOnClickListener {
            startActivity(intent_result)
        }
        mtPractice_vowel.setOnClickListener {
            // 일반 학습 및 심화 학습 전환 구분
            when (vowel_getby) {
                // 기본 모음 학습 메뉴로
                "a" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }
                "eo" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }
                "u" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }
                "o" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }
                "i" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }
                "e" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }
                "eu" -> {
                    val intent = Intent(this, Practice_Vowel::class.java)
                    startActivity(intent) }

                // 심화 학습 메뉴로
                "ga" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "na" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "da" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }

                "geo" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "neo" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "deo" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }

                "gu" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "nu" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "du" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }

                "go" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "no" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "do" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }

                "gi" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "ni" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "di" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }

                "ge" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "ne" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "de" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }

                "geu" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "neu" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
                "deu" -> {
                    val intent = Intent(this, Practice_Enrichment::class.java)
                    startActivity(intent) }
            }

        }

        // 연습 시작 버튼
        /*
        STT 먼저 실행 --4초--> 음성 녹화
        마이크에는 한번에 하나만 접근 가능하므로 순차접근하도록 구성함.
        STT는 실행되고 자동 종료됨.
         */
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

                //STT & record 실행
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

        // 연습 종료 버튼
        /*
        1. 녹음 종료 및 사진 촬영
        2. 액티비티간 전달해야하는 값들 전달
         */
        bt_rstop.setOnClickListener {
            CoroutineScope(Dispatchers.Main).async {
                // recording 중지
                async {
                    stopRecording()
                    takePicture()
                    delay(1000)
                }.await()

                // 버튼, 결과값 등 전달
                async {
                    intent_result.putExtra("Vowel_bt", vowel_getby)
                    intent_result.putExtra("STT_Result", stt_result)
                    intent_result.putExtra("Record_path", recordoutput)
                    Log.d("record_path2", recordoutput.toString())
                    intent_result.putExtra("ImageUri", stringUrl)
                    Log.d("uri_check", stringUrl)
                }.await()
            }
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

        Log.d("recod_path1", recordoutput.toString())

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


    // ============= camera surfaceview part
    private fun initSensor() {
        mSurfaceView = findViewById(R.id.camera_preview)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
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

            mDeviceRotation = ORIENTATIONS[deviceOrientation.orientation]
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

    private fun insertImage(
        cr: ContentResolver,
        source: Bitmap?,
        title: String?,
        description: String?
    ): String {
        val values = ContentValues()

        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

        var url: Uri? = null

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            stringUrl = url.toString()
            Log.d("image_uri_send", stringUrl!!)
            //intent_test.putExtra("ImageUri", stringUrl)


            if (source != null) {
                val imageOut: OutputStream? = cr.openOutputStream(url!!)

                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 20, imageOut)
                    // send picture to server - Retroifit
                    val post_image = CoroutineScope(Dispatchers.IO).launch {
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

                        var uploadFile : MultipartBody.Part

                        when (vowel_getby) {
                            "a" -> uploadFile = MultipartBody.Part
                                .createFormData("a", post_filename, requestBody)
                            "i" -> uploadFile = MultipartBody.Part
                                .createFormData("i", post_filename, requestBody)
                            "o" -> uploadFile = MultipartBody.Part
                                .createFormData("o", post_filename, requestBody)
                            "u" -> uploadFile = MultipartBody.Part
                                .createFormData("u", post_filename, requestBody)
                            "e" -> uploadFile = MultipartBody.Part
                                .createFormData("e", post_filename, requestBody)
                            else -> uploadFile = MultipartBody.Part
                                .createFormData("image", post_filename, requestBody)
                        }
                        Log.d("server check::", uploadFile.toString())


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

        return stringUrl
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


