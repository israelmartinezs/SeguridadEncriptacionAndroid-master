package com.androiddesdecero.seguridadencriptarandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText etTexto, etPassword;
    private TextView tvTexto;
    private Button btEncriptar, btDesEncriptar, btApiEncriptada;
    private String textoSalida;


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
/*
    private SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }*/
}
