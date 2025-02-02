package com.slavic.hangman;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String[] word;
    private static String cur, pos = "", usd = "";
    private static byte att = 0, bad = 1;
    private static boolean stp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<String> read1 = new ArrayList<String>();
        try{
            InputStream mfl = MainActivity.this.getResources().openRawResource(R.raw.hangwords);
            BufferedReader bf = new BufferedReader(new InputStreamReader(mfl));
            String sline = bf.readLine();
            while(sline != null) {
                read1.add(sline);
                sline = bf.readLine();
            }
            bf.close();
        }catch (IOException ex){ex.printStackTrace();}
        word = read1.toArray(new String[0]);
        cur = word[(int) (Math.random()* word.length)];

        //code here:
        TextView marq = findViewById(R.id.marqueetxt);
        marq.setSelected(true);
        ImageView pic = findViewById(R.id.imageView);

        TextView lets = findViewById(R.id.textView2);
        String tmp = ""; for (byte c = 0;c<cur.length();c++){tmp += "_";}
        lets.setText(tmp);
        TextView wrn = findViewById(R.id.textView0);
        EditText inp = findViewById(R.id.input); //System.out.println(event);

        inp.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(inp.getText().length()>=1){
                    View view = MainActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            if(!stp && inp.getText().length()>0 && !usd.contains(inp.getText())){
                usd += inp.getText();
                boolean fnd = false;
                for (byte c = 0;c<cur.length();c++)
                { if(cur.charAt(c) == inp.getText().charAt(0)){fnd = true; pos += c;} }
                if(fnd){
                    String tmp12 = ""; for (byte c = 0; c<cur.length(); c++){
                        if(pos.contains(""+c) && lets.getText().charAt(c) == '_'){
                            tmp12 += inp.getText();
                            att++;
                        }else{
                            tmp12 += lets.getText().charAt(c);
                        }
                    }
                    lets.setText(tmp12);
                }else{
                    wrn.setText(wrn.getText()+""+inp.getText()+",");
                    bad++;
                    pic.setImageResource(getDrawableId("hangres"+bad));
                }
                if(bad>=7){
                    lets.setText("You Lose! The word was: "+cur);
                    stp = true;
                }
                if(att>=cur.length()){
                    lets.setText("You Win!");
                    stp = true;
                }
                inp.setText("");
            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(v -> {
            cur = word[(int) (Math.random()* word.length)];
            System.out.println(cur);
            pos = "";
            usd = "";
            stp = false;
            att = 0;
            bad = 1;
            pic.setImageResource(R.drawable.hangres1);
            String tmp1 = ""; for (byte c = 0; c<cur.length(); c++){
                tmp1 += "_";}
            lets.setText(tmp1);
            wrn.setText("Wrong letters: ");
        });
    }

    private int getDrawableId(String name){
        try {
            Field fld = R.drawable.class.getField(name);
            return fld.getInt(null);
        } catch (Exception e) {e.printStackTrace();}
        return -1;
    }
}