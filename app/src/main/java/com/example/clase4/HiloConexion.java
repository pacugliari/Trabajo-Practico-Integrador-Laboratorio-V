package com.example.clase4;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class HiloConexion extends Thread {
    Handler colaMensaje;

    String url;

    boolean esFoto;

    int position;

    public static final int IMG = 2;

    public static final int NOTICIAS = 1;

    public HiloConexion(Handler colaMensaje, String url, boolean esFoto) {
        this.colaMensaje = colaMensaje;
        this.url = url;
        this.esFoto = esFoto;
    }

    public HiloConexion(Handler colaMensaje, String url, boolean esFoto, int position) {
        this.colaMensaje = colaMensaje;
        this.url = url;
        this.esFoto = esFoto;
        this.position = position;
    }


    @Override
    public void run() {
        ConexionHTTP c = new ConexionHTTP();
        byte[] respuesta = c.getInfo(this.url);

        //Log.d("Noticias", new String(respuesta));
        Message msj = new Message();

        if (this.esFoto) {
            msj.obj = respuesta;
            msj.arg1 = IMG;
            msj.arg2 = this.position;
        } else {
            List<Noticia> noticias = null;
            try {
                noticias = XmlParser.parserNoticias(new String(respuesta));
            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            msj.obj = noticias;
            msj.arg1 = NOTICIAS;

        }
        colaMensaje.sendMessage(msj);//AGREGA A LA COLA DE MENSAJES
    }
}
