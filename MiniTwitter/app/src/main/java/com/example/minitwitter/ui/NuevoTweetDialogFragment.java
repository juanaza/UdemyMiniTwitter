package com.example.minitwitter.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;

public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {
    private ImageView imageViewAvatar, imageViewClose;
    private Button buttonTwittear;
    private EditText editTextMensaje;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.nuevo_tweet_full_dialog, container, false);

        buttonTwittear = view.findViewById(R.id.buttonTwittear);
        buttonTwittear.setOnClickListener(this);

        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);

        imageViewClose = view.findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(this);

        editTextMensaje = view.findViewById(R.id.editTextMensaje);

        //Seteamos la imagen del usuario
        String photoUrl = SharedPreferencesManager.getStringValue(Constantes.PREF_PHOTOURL);
        if(!photoUrl.isEmpty()){
            Glide.with(getActivity())
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                    .into(imageViewAvatar);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String mensaje = editTextMensaje.getText().toString();
        if(id == R.id.buttonTwittear){
            if(mensaje.isEmpty()){
                Toast.makeText(getActivity(), "Debe escribir un texto en el mensaje", Toast.LENGTH_SHORT).show();
            }
            else{
                TweetViewModel tweetViewModel = ViewModelProviders
                        .of(getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertTweet(mensaje);
                getDialog().dismiss();
            }
        }
        else if(id == R.id.imageViewClose){
            if(!mensaje.isEmpty()){
                showDialogConfirm();
            }
            else{
                getDialog().dismiss();
            }
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea realmente eliminar el tweet? El mensaje se eliminará")
                .setTitle("Cancelar tweet");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getDialog().dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
