package br.ufma.nca.ergonomics.socketjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.FloatBuffer;

public class SendDataActivity extends AppCompatActivity {
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button enviar = (Button) findViewById(R.id.btUpload);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Example of a call to a native method
                TextView tv = (TextView) findViewById(R.id.reply_server);
                tv.setText("iai");

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        setContentView(R.layout.activity_send_data);
        TextView tv = (TextView) findViewById(R.id.reply_server);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
}
*/
    ImageView image;
    TextView response;
    Button buttonCapture;
    String ServerAddress = "192.168.0.16";
    String ServerPort = "30000";
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;


    private static final String TAG = SendDataActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        Log.d("CameraDemo", dir);
        File newdir = new File(dir);
        newdir.mkdirs();


        buttonCapture = (Button) findViewById(R.id.btnCapture);
        response = (TextView) findViewById(R.id.responseTextView);
        image = (ImageView) findViewById(R.id.imageView);


        buttonCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.
                count++;
                String file = dir+count+".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                    Log.e("CameraDemo", e.getMessage());
                }

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });

        /*
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });
        response.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(!(s.equals("Failed transmitting the message") || s.equals("Success transmitting the message"))) {
                    response.setText(verifyDataSent(s.toString(), print5LastPoints(testCloud, 7500)));
                }
            }
        });
        */
    }
    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            byte [] imgbyte = new byte[0];
            //Log.d("CameraDemo", data.toUri(0));
            String filepath = "/storage/emulated/0/Pictures/picFolder/1.jpg";
            image.setImageBitmap(BitmapFactory.decodeFile(filepath));
            File imagefile = new File(filepath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                imgbyte = new byte[fis.available()];
                fis.read(imgbyte);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Bitmap bm = BitmapFactory.decodeStream(fis);
            //imgbyte = getBytesFromBitmap(bm);

            Log.d("CameraDemo", "Pic saved");
            Log.d("CameraDemo", "Tamanho do buffer enviado:" + Integer.toString(imgbyte.length));
            byte[] output; //float2Byte(testCloud);
            Client myClient = new Client(ServerAddress
                    , Integer.parseInt(ServerPort)
                    , response
                    , imgbyte);
            myClient.execute();
        }
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}

