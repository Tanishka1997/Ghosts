package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private SimpleDictionary simpledictionary;
    private String word="";
    private TextView gameStatus;
    private TextView textview;
    private Button challenge;
    private Button restart;
    private String possibleWord;
    private TextView Score;
    private int score_val=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        Score=(TextView) findViewById(R.id.score);
        challenge=(Button) findViewById(R.id.challenge_button);
        restart=(Button) findViewById(R.id.restart_button);
        textview=(TextView) findViewById(R.id.ghostText);
        gameStatus=(TextView) findViewById(R.id.gameStatus);
        try {
            InputStream inputStream=getAssets().open("words.txt");
            simpledictionary=new SimpleDictionary(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        computerTurn();

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpledictionary.isWord(word)) {
                    gameStatus.setText("You Won");
                    challenge.setEnabled(false);
                    score_val+=10;
                    Score.setText("Your Score"+"\n"+score_val);
                }
                else {
                    gameStatus.setText("You Lose.Word possible is:"+possibleWord);
                    challenge.setEnabled(false);
                }
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               challenge.setEnabled(true);
                userTurn=false;
                word="";
                computerTurn();
                onStart(null);
            }
        });

      onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode>=KeyEvent.KEYCODE_A && keyCode<=KeyEvent.KEYCODE_Z){
            char letter = event.getDisplayLabel();
            textview=(TextView) findViewById(R.id.ghostText);
            gameStatus=(TextView) findViewById(R.id.gameStatus);
            textview.append((letter+"").toLowerCase());
            word=textview.getText().toString();
                userTurn=false;
                computerTurn();
        }
        else
        gameStatus.setText("Inavalid key Pressed!!");

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        userTurn = false;
        TextView label = (TextView) findViewById(R.id.gameStatus);

        // Do computer turn stuff then make it the user's turn again

        if(simpledictionary.isWord(word.toLowerCase())&&word.length()>=4){
            gameStatus.setText("Computer Won.");
            challenge.setEnabled(false);
            return;
        }
        else {

            if (word.equals("")){
                possibleWord=simpledictionary.getAnyWordStartingWith(null);
                textview.setText(possibleWord.substring(0,3));
                word=possibleWord.substring(0,3);
                return;
            }
            else{
                possibleWord=simpledictionary.getAnyWordStartingWith(word.toLowerCase());
                if(possibleWord==null){
                 gameStatus.setText("You can't bluff computer.You Lose");
                 challenge.setEnabled(false);
                return;
                 }
                else {
                textview.setText(possibleWord.substring(0,word.length()+1));
                word=possibleWord.substring(0,word.length()+1);
            }
          }
        }

        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        /*TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");*/
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

}
