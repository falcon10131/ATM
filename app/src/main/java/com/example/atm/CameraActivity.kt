package com.example.atm
//Camera換成*代表可以直接打所有的權限
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_camera.*
import java.util.jar.Manifest

class CameraActivity : AppCompatActivity() {
    private var imageUri: Uri? = null

    companion object{
        val REQUEST_CAMERA = 100
        val REQUEST_CAPTURE = 500
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //自體檢查是否有Camera的權限
        val permission = ActivityCompat.checkSelfPermission(this,CAMERA)
        if (permission == PackageManager.PERMISSION_GRANTED)
        {
            //如果已經有權限執行這塊
            takePhoto()
        } else {//如果還沒有拿到權限執行這塊
            //向使用者要求權限(可以拿取多個,這邊範例是拿CAMERA,WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this,
                arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                REQUEST_CAMERA)
        }

    }
    private fun takePhoto(){
        Toast.makeText(this,"PERMISSION_GRANTED",Toast.LENGTH_SHORT).show()
        //跳轉到相機
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //ContentValues將會包含了相片的標題跟敘述
        val value = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE,"My title")
            put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION")
        }
        //contentResolver類似資料庫表個的標題,在此標題內置imageUri的位置
        imageUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
        camera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(camera,REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAPTURE){
            imageView.setImageURI(imageUri)
        }
    }

    //不管按下接受或是拒絕都會跑到onRequestPermissionsResult,再去判斷接受或拒絕的判斷
    override fun onRequestPermissionsResult(
        requestCode: Int,//自定義的Code
        permissions: Array<out String>, //權限們(看你拿幾個)
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                takePhoto()
            } else {
              //TODO:
            }
        }
    }
}
