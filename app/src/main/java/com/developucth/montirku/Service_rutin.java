package com.developucth.montirku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Service_rutin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_rutin);

        TextInputEditText edt_nama = findViewById(R.id.edt_nama);
        TextInputEditText edt_merek = findViewById(R.id.edt_merek);
        TextInputEditText edt_alamat = findViewById(R.id.edt_alamat);
        EditText edtMulti_catatan = findViewById(R.id.edtMulti_catatan);
        Spinner spn_layanan = findViewById(R.id.spn_layanan);

        ImageButton btn_ToServis = findViewById(R.id.btn_Toservis);

        btn_ToServis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_nama.getText().toString().isEmpty() || edt_merek.getText().toString().isEmpty()
                        || edt_alamat.getText().toString().isEmpty() || edtMulti_catatan.getText().toString().isEmpty() || spn_layanan.getSelectedItemId()==0) {
                    Toast.makeText(Service_rutin.this, "Lengkapi Semua Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        String text = "Assalamualaikum Warahmatullahi Wabarakatuh."
                                + "\nSaya mau servis motor"
                                + "\nNama : *" + edt_nama.getText().toString() + "*"
                                + "\nMerek/Jenis Motor : *" + edt_merek.getText().toString() + "*"
                                + "\nAlamat : *" + edt_alamat.getText().toString() + "*"
                                + "\nLayanan : *" + spn_layanan.getSelectedItem().toString() + "*"
                                + "\nKeluhan : *" + edtMulti_catatan.getText().toString() + "*";

                        String toNumber = "6282293963119"; // Replace with mobile phone number without +Sign or leading zeros, but with country code



                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                        startActivity(intent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }


        });
    }
}