package com.example.fondosdepantalla.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fondosdepantalla.MainActivityAdministrador;
import com.example.fondosdepantalla.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarAdmin extends Fragment {


   TextView FechaRegistro;
   EditText Correo,Password,Nombres,Apellidos,Edad;
   Button Registrar;

   FirebaseAuth auth;
   ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_registrar_admin, container, false);
       //iNICIALIZAR VARIABLES
       FechaRegistro=view.findViewById(R.id.FechaRegistro);
       Correo=view.findViewById(R.id.Correo);
       Password=view.findViewById(R.id.Password);
       Nombres=view.findViewById(R.id.Nombres);
       Apellidos=view.findViewById(R.id.Apellidos);
       Edad=view.findViewById(R.id.Edad);

       Registrar=view.findViewById(R.id.Registrar);

       auth=FirebaseAuth.getInstance();

       //Fecha del sistema
        Date date=new Date();
        SimpleDateFormat fecha=new SimpleDateFormat("d 'de' MMMM 'del' yyyy");//14 DEL ENERO DEL 2021
        String SFecha=fecha.format(date);//Convertimos la fecha a string
        FechaRegistro.setText(SFecha);

        //Al CLICK EN REGISTRAR
        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //convertinos a string EDITTEXT Correo y contraseña
                String correo=Correo.getText().toString();
                String pass=Password.getText().toString();
                String nombre=Nombres.getText().toString();
                String apellidos=Apellidos.getText().toString();
                String edad=Edad.getText().toString();

                if(correo.equals("") || pass.equals("")||nombre.equals("")||apellidos.equals("")|| edad.equals("")){

                    Toast.makeText(getActivity(), "Pro favor rellene loc campos", Toast.LENGTH_SHORT).show();
                }else {
                    //VALIDANDO TODOS LOS CAMPOS OBLIGATORIOS
                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        Correo.setError("Correo invalido");
                        Correo.setFocusable(true);

                    } else if (pass.length()<6) {
                        Password.setError("Contraseña debe ser mayor o igual a 6");
                    }else{
                        RegistrarAdministradores(correo,pass);
                    }

                }



            }
        });

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);


        return view;
    }
    //metodo para registrar administradores
    private void RegistrarAdministradores(String correo, String pass) {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //si el administrador fue creado0
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user=auth.getCurrentUser();
                            assert user !=null;
                            //Convertir a cadena los datos de la administracion
                            String UID=user.getUid();
                            String correo=Correo.getText().toString();
                            String pass=Password.getText().toString();
                            String nombre=Nombres.getText().toString();
                            String apellidos=Apellidos.getText().toString();
                            String edad=Edad.getText().toString();
                            int EdadInt=Integer.parseInt(edad);

                            HashMap<Object, Object>Administradores=new HashMap<>();
                            Administradores.put("UID",UID);

                            Administradores.put("CORREO",correo);
                            Administradores.put("PASSWORD",pass);
                            Administradores.put("NOMBRES",nombre);
                            Administradores.put("APELLIDOS",apellidos);
                            Administradores.put("EDAD",EdadInt);
                            Administradores.put("IMAGEN","");

                            //Inicializar FireDatabase
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("BASE DE DATOS ADMINISTRADORES");
                            reference.child(UID).setValue(Administradores);
                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                            getActivity().finish();



                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha Ocurrido un error", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}