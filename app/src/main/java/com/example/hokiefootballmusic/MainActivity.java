package com.example.hokiefootballmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.hokiefootballmusic.playingScreen.MUSIC_PLAYING;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {




    Spinner mainSongSpinner, overlapA,overlapB,overlapC;
    Button switchbutton;
    String main,lapA,lapB,lapC;
    SeekBar seekA,seekB,seekC;
    String lastMusic = null;

    int seekAp,seekBp,seekCp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b1 = this.getIntent().getExtras();
        if(b1 != null) {
            lastMusic = b1.getString("lastMusic");
        }

        mainSongSpinner = (Spinner) findViewById(R.id.mainSongSpinner);
        overlapA = (Spinner) findViewById(R.id.overlapA);
        overlapB = (Spinner) findViewById(R.id.overlapB);
        overlapC = (Spinner) findViewById(R.id.overlapC);
        switchbutton = (Button) findViewById(R.id.switchButton);

        seekA =  findViewById(R.id.seekBarA);
        seekB =  findViewById(R.id.seekBarB);
        seekC =  findViewById(R.id.seekBarC);
        seekA.setOnSeekBarChangeListener(this);
        seekB.setOnSeekBarChangeListener(this);
        seekC.setOnSeekBarChangeListener(this);

        main = "Go Tech Go";
        lapA = "cheering";
        lapB = "cheering";
        lapC = "cheering";
        seekAp = 0 ;
        seekBp = 0 ;
        seekCp = 0 ;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mainSong_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterOverlap = ArrayAdapter.createFromResource(this,
                R.array.overlapSong_array, android.R.layout.simple_spinner_item);
        adapterOverlap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainSongSpinner.setAdapter(adapter);
        overlapA.setAdapter(adapterOverlap);
        overlapB.setAdapter(adapterOverlap);
        overlapC.setAdapter(adapterOverlap);
        mainSongSpinner.setOnItemSelectedListener(this);
        overlapA.setOnItemSelectedListener(this);
        overlapB.setOnItemSelectedListener(this);
        overlapC.setOnItemSelectedListener(this);
    }


    public void switchClicked(View view){
        Intent intent = new Intent(this, playingScreen.class);
        ArrayList<Integer> backMusic = new ArrayList<Integer>();
        ArrayList<Integer> backtime = new ArrayList<Integer>();
        ArrayList<String> backName = new ArrayList<String>();
        backName.add("cheering");
        backName.add("clapping");
        backName.add("lest go hokies");

        for(String i : backName){
            if(lapA.equals(i)){
                backMusic.add(backName.indexOf(i)+3);
            }
            if(lapB.equals(i)){
                backMusic.add(backName.indexOf(i)+3);
            }
            if(lapC.equals(i)){
                backMusic.add(backName.indexOf(i)+3);
            }
        }
        backtime.add(seekAp);
        backtime.add(seekBp);
        backtime.add(seekCp);
        intent.putExtra("main",main);
        intent.putExtra("backtime",backtime);
        intent.putExtra("backMusic",backMusic);
        if(lastMusic != null) {
            intent.putExtra("lastMusic", lastMusic);
        }
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String current = (String) parent.getItemAtPosition(position);

        if (parent.getId() == R.id.mainSongSpinner){
            main = current;
        }
        if (parent.getId() == R.id.overlapA){
            lapA = current;
        }
        if (parent.getId() == R.id.overlapB){
            lapB = current;
        }
        if (parent.getId() == R.id.overlapC) {
            lapC = current;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekBarA:
                seekAp = progress;
                break;
            case R.id.seekBarB:
                seekBp = progress;
                break;
            case R.id.seekBarC:
                seekCp = progress;
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}