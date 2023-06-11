package com.example.ihmandroidalerte;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Runnable r = new Runnable() {
        @Override
        public void run() {
            serveurNotifications s = new serveurNotifications();
            String str = s.receptionPacket();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView txtView = findViewById(R.id.txtView);
                    TableLayout tblLayout = findViewById(R.id.tblLayout);
                    TextView txtView2 = findViewById(R.id.txtView2);
                    if (str.equals("")) {
                        txtView.setVisibility(View.VISIBLE);
                        tblLayout.setVisibility(View.GONE);
                        txtView2.setVisibility(View.GONE);
                        txtView.setText("Aucune notification reçue.");
                    }
                    else {
                        txtView.setVisibility(View.GONE);
                        tblLayout.setVisibility(View.VISIBLE);
                        txtView2.setVisibility(View.VISIBLE);
                        tblLayout.removeAllViews();
                        String[] dataList = str.split("[,\n]");
                        for(int i = 1; i < dataList.length; i += 2) {
                            TableRow ligne = new TableRow(tblLayout.getContext());
                            // colonne type
                            TextView type = new TextView(tblLayout.getContext());
                            type.setText(dataList[i - 1]);
                            type.setPadding(5, 5, 5, 5);
                            type.setTextSize(20);
                            TableRow.LayoutParams typeParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);
                            type.setLayoutParams(typeParams);
                            ligne.addView(type);
                            // colonne nombre
                            TextView nombre = new TextView(tblLayout.getContext());
                            nombre.setText(dataList[i]);
                            nombre.setPadding(5, 5, 5, 5);
                            nombre.setTextSize(20);
                            TableRow.LayoutParams nombreParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                            nombre.setLayoutParams(nombreParams);
                            ligne.addView(nombre);
                            tblLayout.addView(ligne);
                        }
                    }
                    Button btn = findViewById(R.id.btn);
                    btn.setVisibility(View.VISIBLE);
                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread(r);
        t.start();
        TextView txtView = findViewById(R.id.txtView);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtView.setText("En attente de notifications...");
                btn.setVisibility(View.GONE);
                Thread t = new Thread(r);
                t.start();
            }
        });
    }
}