package com.androiddesdecero.seguridadencriptarandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private EditText etTexto, etPassword;
    private TextView tvTexto;
    private Button btEncriptar, btDesEncriptar, btApiEncriptada;
    private String textoSalida;
    ///
    TextView tv;
    Button button;
    Intent myfile;
    Uri file2cipher=null;//archivo a cifrar

    //String apiKeyEncriptada ="0SPrEK0JntQ2qCm9cPEabw==";
    //String passwordEncriptacion = "gdsawr";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyGenerator keygen= null;
        try {
            keygen = KeyGenerator.getInstance("AES","BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        keygen.init(128);
            final SecretKey key = keygen.generateKey();


        etTexto = findViewById(R.id.mainActivityEtTexto);
        //etPassword = findViewById(R.id.mainActivityEtPassword);
        tvTexto = findViewById(R.id.mainActivityTvTexto);

        /*btApiEncriptada = findViewById(R.id.mainActivityBtApiEncriptada);
        btApiEncriptada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    textoSalida = desencriptar(apiKeyEncriptada, key);
                    tvTexto.setText(textoSalida);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
*/
        tv=(TextView)findViewById(R.id.Tpath);
        button=(Button)findViewById(R.id.Bfile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myfile=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                myfile.setType("*/*");
                startActivityForResult(myfile,10);
            }
        });
        btEncriptar = findViewById(R.id.mainActivityBtEncriptar);
        btEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    textoSalida = encriptar(etTexto.getText().toString(), key);
                    tvTexto.setText(textoSalida);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
//00000000000000009d
        btDesEncriptar = findViewById(R.id.mainActivityBtDesencritar);
        btDesEncriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    textoSalida = desencriptar(textoSalida, key);
                    tvTexto.setText(textoSalida);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private String desencriptar(String datos, SecretKey key) throws Exception{
        //SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
        String datosDesencriptadosString = new String(datosDesencriptadosByte);
        return datosDesencriptadosString;
    }

    private String encriptar(String datos, SecretKey key) throws Exception{
        //SecretKeySpec secretKey = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
        ////hola
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Uri uri=null;
        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK){
                    uri=data.getData();
                    String path=data.getData().getPath();
                    tv.setText(path);
                    file2cipher=uri;
                    Log.d("uri",uri.toString());
                    //alterDocument(uri);
                }
                break;
        }
    }
    private void alterDocument(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().
                    openFileDescriptor(uri, "rw");
            Log.d("file d",pfd.toString());
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(("Overwritten by MyCloud at " +
                    System.currentTimeMillis() + "\n").getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    private SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }*/
}
