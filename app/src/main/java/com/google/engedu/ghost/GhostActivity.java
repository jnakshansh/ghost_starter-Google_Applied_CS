package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String wordFragment = "";
    private TextView wordTextView;
    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        wordTextView = (TextView)findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        try {
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char pressedKey = (char) event.getUnicodeChar();
        pressedKey = Character.toLowerCase(pressedKey);

        if(pressedKey >= 'a' && pressedKey <= 'z'){
            wordFragment = wordFragment.concat(event.getDisplayLabel() + "");
            wordFragment = wordFragment.toLowerCase();
            wordTextView.setText(wordFragment);
//            if(dictionary.isWord(wordFragment.toLowerCase())){
//                label.setText("Is a word");
//            }
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void challenge(View view){
        if(wordFragment.length() >= 4) {
            if (dictionary.isWord(wordFragment)) {
                label.setText("You win");
            } else {
                String nextWord = dictionary.getAnyWordStartingWith(wordFragment);
                if(nextWord == null) {
                    label.setText("You win");
                }
                else{
                    label.setText(nextWord + ". You loose");
                }
            }
        }
        else{
            label.setText("Word Fragment is too small to challenge");
        }
    }

    private void computerTurn() {
        label.setText(COMPUTER_TURN);
        userTurn = false;
        if(dictionary.isWord(wordFragment.toLowerCase())){
            label.setText("Computer Wins");
        }
        else {
            String nextWord = dictionary.getAnyWordStartingWith(wordFragment.toLowerCase());
            if(nextWord == null){
                if(wordFragment.length() >= 4) {
                    label.setText("Computer challenged you. Computer wins");
                }
                else{
                    wordFragment = (wordFragment + "r");
                    wordTextView.setText(wordFragment);
                }
            }
            else{
                wordFragment = nextWord.substring(0,wordFragment.length()+1);
                wordTextView.setText(wordFragment);
                userTurn = true;
                label.setText(USER_TURN);
            }
        }

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        wordFragment = "";
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
