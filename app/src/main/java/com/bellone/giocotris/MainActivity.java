/*Una semplicissima app per giocare a tris in due*/

package com.bellone.giocotris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnDragListener {

    private ArrayList<LinearLayout> layoutCelle = null;
    private ImageView imgX = null;
    private ImageView imgCerchio = null;

    private TextView lblInfo = null;

    private int turnoDiChi;
    private boolean partitaInCorso;

    private ArrayList<Object> griglia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutCelle = new ArrayList<>();
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutPrimaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutSecondaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutTerzaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutQuartaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutQuintaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutSestaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutSettimaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutOttavaCella));
        layoutCelle.add((LinearLayout) findViewById(R.id.layoutNonaCella));

        griglia = new ArrayList<>();
        for(int i=0; i<9; i++){ griglia.add(null); }

        lblInfo = findViewById(R.id.lbInfo);

        imgX = findViewById(R.id.imgX);
        imgCerchio = findViewById(R.id.imgCerchio);

        partitaInCorso = true;
        turnoDiChi = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, 1, R.string.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            //non controllo neanche l'itemId perche intanto ho una sola voce
        resettaGriglia();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        lblInfo.setTextSize(18);
        resettaGriglia();

        imgX.setOnLongClickListener(this);
        imgCerchio.setOnLongClickListener(this);

        for(LinearLayout layout: layoutCelle){ layout.setOnDragListener(this); }
    }

    private void resettaGriglia(){
        partitaInCorso = true;
        turnoDiChi = 0;
        lblInfo.setTextSize(18);
        lblInfo.setText(R.string.inziate);

        for(int i=0; i<9; i++){ griglia.set(i, null); }
        for(LinearLayout layout: layoutCelle){
            if(layout.getChildCount() > 0){
                    //se ce una View all'interno del layout puo' essere solo l'immagine
                layout.removeView(layout.getChildAt(0));
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(partitaInCorso == true){
            ImageView immagine = (ImageView) v;
            if((turnoDiChi == 0) || (v.getId() == R.id.imgX && turnoDiChi == 2) || (v.getId() == R.id.imgCerchio && turnoDiChi == 1)){
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(immagine);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    immagine.startDragAndDrop(null, dragShadowBuilder, immagine, 0);
                }
            }
        }

        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if(partitaInCorso == true){
            LinearLayout layoutCella = (LinearLayout)v;

            if(layoutCella.getChildCount() == 0){
                int cella = 0;
                switch (layoutCella.getId()){
                    case R.id.layoutPrimaCella:
                        cella = 1;
                        break;
                    case R.id.layoutSecondaCella:
                        cella = 2;
                        break;
                    case R.id.layoutTerzaCella:
                        cella = 3;
                        break;
                    case R.id.layoutQuartaCella:
                        cella = 4;
                        break;
                    case R.id.layoutQuintaCella:
                        cella = 5;
                        break;
                    case R.id.layoutSestaCella:
                        cella = 6;
                        break;
                    case R.id.layoutSettimaCella:
                        cella = 7;
                        break;
                    case R.id.layoutOttavaCella:
                        cella = 8;
                        break;
                    case R.id.layoutNonaCella:
                        cella = 9;
                        break;
                }

                ImageView img = new ImageView(getApplicationContext());

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        layoutCella.setBackgroundColor(Color.LTGRAY);
                        layoutCella.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        layoutCella.setBackgroundColor(Color.WHITE);
                        layoutCella.invalidate();
                        return true;
                    case DragEvent.ACTION_DROP:
                        layoutCella.setBackgroundColor(Color.WHITE);
                        layoutCella.invalidate();

                        int imgResource;

                        if(((View)event.getLocalState()).getId() == R.id.imgX ){
                            imgResource = R.drawable.x;
                            turnoDiChi = 1;
                        }else{
                            imgResource = R.drawable.cerchio;
                            turnoDiChi = 2;
                        }

                        img.setImageResource(imgResource);
                        img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                        layoutCella.addView(img);

                        if (turnoDiChi == 2) { griglia.set(cella-1, true); lblInfo.setText(R.string.toccaPlayer1);
                        }else{ griglia.set(cella-1, false); lblInfo.setText(R.string.toccaPlayer2); }

                        if (controllaVittoria()){
                            lblInfo.setTextSize(25);
                            lblInfo.setText(R.string.vincitore);
                            partitaInCorso = false;
                        }

                        return true;
                    default:
                        return true;
                }
            }
        }
        return true;
    }

    private boolean controllaVittoria(){

        if( (((griglia.get(0) == griglia.get(1)) && griglia.get(0) == griglia.get(2)) && griglia.get(0) != null)
            || (((griglia.get(3) == griglia.get(4)) && griglia.get(3) == griglia.get(5)) && griglia.get(3) != null)
            || (((griglia.get(6) == griglia.get(7)) && griglia.get(6) == griglia.get(8)) && griglia.get(6) != null)
                //CONTROLLATO le RIGHE
            || (((griglia.get(0) == griglia.get(4)) && griglia.get(0) == griglia.get(8)) && griglia.get(0) != null)
            || (((griglia.get(2) == griglia.get(4)) && griglia.get(2) == griglia.get(6)) && griglia.get(2) != null)
                //CONTROLLATO le DIAGONALI
            || (((griglia.get(0) == griglia.get(3)) && griglia.get(0) == griglia.get(6)) && griglia.get(0) != null)
            || (((griglia.get(1) == griglia.get(4)) && griglia.get(1) == griglia.get(7)) && griglia.get(1) != null)
            || (((griglia.get(2) == griglia.get(5)) && griglia.get(2) == griglia.get(8)) && griglia.get(2) != null)
                //CONTROLLATO le COLONNE
        ){
            return true;
        }

        return false;
    }

}
