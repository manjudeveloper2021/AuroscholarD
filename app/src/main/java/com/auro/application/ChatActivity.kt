package com.auro.application

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.auro.application.core.application.AuroApp
import com.auro.application.core.database.AuroAppPref
import com.auro.application.core.database.PrefModel
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity
import com.bumptech.glide.Glide
import com.yugasa.yubobotsdk.YuboBot
import com.yugasa.yubobotsdk.listeners.YuboBotAdapterListener
import com.yugasa.yubobotsdk.model.BotLanguage
import com.yugasa.yubobotsdk.utils.FileUtils
import com.yugasa.yubobotsdk.utils.YuboBotConstants
import com.yugasa.yubobotsdk.utils.YuboBotUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    private val TAG = "ChatActivity"
    private lateinit var recycler: RecyclerView
    private lateinit var etSendMsg: EditText
    private lateinit var imgSend: ImageView
    private lateinit var imageSend: ImageView
    private lateinit var typingJson: JSONObject
    private lateinit var llSelfRpl: LinearLayout
    private lateinit var typingInd: ImageView
    private lateinit var typingInd1: ImageView
    private lateinit var yuboBot: YuboBot
    private lateinit var toolbar: Toolbar

    private var languageList = ArrayList<BotLanguage>()
    private var language = "en"

    //    private var clientId: String = "cardse_chatbot"
    private var clientId: String = "auro_scholar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainchat)
language = AuroAppPref.INSTANCE.modelInstance.userLanguageCode
        recycler = findViewById(R.id.rcTest)
        etSendMsg = findViewById(R.id.et_sendMsg)
        imgSend = findViewById(R.id.imgSend)
        imageSend = findViewById(R.id.image)
        typingInd = findViewById(R.id.typingInd)
        typingInd1 = findViewById(R.id.typingInd1)
        llSelfRpl = findViewById(R.id.llSelfRpl)
        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        Glide.with(this)
            .load(R.drawable.typing_view)
            .into(typingInd)

        Glide.with(this)
            .load(R.drawable.typing_view)
            .into(typingInd1)

        imageSend.setOnClickListener {

            val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this)

            val arrayAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
            with(
                receiver = arrayAdapter,
                block = {
                    add("Camera")
                    add("Gallery")
                },
            )

            builderSingle.setAdapter(arrayAdapter) { _, which ->
                val strName = arrayAdapter.getItem(which)
                when {
                    strName.equals("Camera") -> {
                        if(checkPermission()){
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            pickImageCamera.launch(intent)
                        }else{
                            requestPermission()
                        }
                    }
                    strName.equals("Gallery") -> {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageGallery.launch(intent)
                    }
                    else -> {

                    }
                }
            }
            builderSingle.show()

        }

        imgSend.setOnClickListener {
            if (etSendMsg.text.toString().trim().isNotEmpty()) {
                yuboBot.callBotApi(etSendMsg.text.toString())
                etSendMsg.setText("")
                etSendMsg.text.clear()
            } else {
                Toast.makeText(this, "Please enter the your message", Toast.LENGTH_LONG).show()
            }
        }

        etSendMsg.addTextChangedListener(object : TextWatcher {
            var isTyping = false
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            private var timer = Timer()
            private val DELAY: Long = 1500 // milliseconds
            override fun afterTextChanged(s: Editable) {
                try {
                    if (!isTyping) {
                        isTyping = true
                    }
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                isTyping = false
                                typingJson = JSONObject()
                                try {
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        },
                        DELAY
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        setUpYuboBot()
    }


    @SuppressLint("HardwareIds", "NotifyDataSetChanged")
    private fun setUpYuboBot() {
        val id: String =
            "android_" +
        Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        ).toString()
        val map = HashMap<String, Any>()
        map["client_id"] = clientId                                    //  mandatory
        map["id"] = YuboBotConstants.YUBO_DEVICE_ID_2                  //  mandatory
        map["yubo_id"] = id+"_"+AuroAppPref.INSTANCE.modelInstance.studentData.userId+"_"+clientId                                          //  mandatory
        map["appUserId"] = AuroAppPref.INSTANCE.modelInstance.studentData.userId                                        //  mandatory
        map["type"] = "Android"                                        //  mandatory
        map["lang"] = language                                         //  mandatory
        map["clear_chat"] = false
        //  mandatory
        map["from"] = "amit@gmail.com"                                 //  mandatory
        map["name"] = "Amit Kumar"                                     //  mandatory
        map["phone"] = "9717291746"                                    //  mandatory
        map["rate_charges"] = "Rates/Charges are : 5% of value"        //  mandatory
        map["kyc_status"] = YuboBotConstants. CARD_SE_KYC_REJECTED      //  mandatory
        map["bearer_token"] = "bearerToken"                            //  mandatory
        map["base_url"] = "baseurl"
        yuboBot = YuboBot(this, map, object : YuboBot.SetTypingAnimation {
            override fun scrollPosition(scrollToBottom: Int, typeOption: Boolean?) {
                recycler.scrollToPosition(scrollToBottom)
                recycler.smoothScrollToPosition(scrollToBottom)
                if (typeOption == true) {
                    llSelfRpl.visibility = View.VISIBLE
                } else {
                    llSelfRpl.visibility = View.GONE
                    YuboBotUtil.hideKeyboard(this@ChatActivity)
                }
            }

            override fun showAnimation(boolean: Boolean, userType: Int) {
                if (userType == 1) {
                    if (boolean) {
                        typingInd1.visibility = View.VISIBLE
                    } else {
                        typingInd1.visibility = View.GONE
                    }
                } else {
                    if (boolean) {
                        typingInd.visibility = View.VISIBLE
                    } else {
                        typingInd.visibility = View.GONE
                    }
                }
            }

            override fun onBotLanguage(botLanguages: ArrayList<BotLanguage>) {
                languageList.addAll(botLanguages)
            }
        }, recycler)
        yuboBot.yuboBotChatAdapter!!.setUserLayout(R.layout.bot_user_item_layout)
//        yuboBot.yuboBotChatAdapter!!.setBotLayout(R.layout.bot_chat_item_layout)
        yuboBot.yuboBotChatAdapter!!.setBotRepliesLayout(R.layout.bot_reply_item_layout)
//        yuboBot.yuboBotChatAdapter!!.setDimensionPixelSize(com.yugasa.yubobotsdk.R.dimen._0sdp)
        recycler.adapter = yuboBot.yuboBotChatAdapter     // Set adapter to the recycler view
        recycler.adapter!!.notifyDataSetChanged()

        yuboBot.setYuboBotListener(object : YuboBotAdapterListener {
            override fun onClickYuboBotView(key: Int, data: HashMap<Int, Any>) {
            }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                yuboBot.restartBot()
            }
//            R.id.item2 -> {
//                LanguageDialog(
//                    this,
//                    language,
//                    languageList,
//                    object : LanguageDialog.BottomSheetListener {
//                        override fun onSelectLanguage(item: BotLanguage) {
//                            language = item.languageCode
//                            yuboBot.changeBotLanguage(item.languageCode)
//                        }
//                    }
//                ).showDialog()
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var pickImageGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                typingInd1.visibility = View.VISIBLE
                val data: Intent? = result.data
                data.let {
                    val imageUri = data!!.data
                    yuboBot.fileUploadApi(imageUri!!)
                }
            }
        }


    private var pickImageCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data: Intent? = result.data
                try {
                    var thumbnail = data!!.extras!!.get("data") as Bitmap
                    val bytes = ByteArrayOutputStream()
                    thumbnail = Bitmap.createScaledBitmap(thumbnail, 512, 512, false)
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val destination =
                        File(filesDir, System.currentTimeMillis().toString() + ".jpg")
                    val fo: FileOutputStream
                    try {
                        destination.createNewFile()
                        val fileUri = Uri.fromFile(destination)                            //file is converted into uri
                        Log.e("Profile  ", fileUri.path!!)
                        fo = FileOutputStream(destination)
                        fo.write(bytes.toByteArray())
                        fo.close()
                        val file = FileUtils.getFile(this, fileUri)
                        yuboBot.fileUploadApi(file)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

    fun checkPermission() : Boolean{
        val isPermissionGranted: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED )
            {
                isPermissionGranted = true
            } else {
                isPermissionGranted = true
                Log.e("DB", "PERMISSION GRANTED")
            }
      }
        else {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED )
            {
                isPermissionGranted = true
            } else {
                isPermissionGranted = true
                Log.e("DB", "PERMISSION GRANTED")
            }
        }

        return isPermissionGranted


    }
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 100
            )
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA

            ), 100)
        }
    }

}