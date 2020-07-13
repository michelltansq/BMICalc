package sg.edu.rp.c346.id19030685.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView tvDate, tvBMI, tvCategory;
    EditText etWeight, etHeight;
    Button btnCalculate, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvDate = findViewById(R.id.lastCalculatedDate);
        tvBMI = findViewById(R.id.lastCalculatedBMI);
        tvCategory = findViewById(R.id.bmiCategory);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String height= etHeight.getText().toString();
                String weight = etWeight.getText().toString();
                float calcHeight = Float.parseFloat(height);
                float calcWeight = Float.parseFloat(weight);

                float bmi = calcWeight/ (calcHeight * calcHeight);

                if(bmi < 18.5) {
                    tvCategory.setText(R.string.underweight);
                } else if (bmi < 24.9) {
                    tvCategory.setText(R.string.normal);
                } else if (bmi < 29.9) {
                    tvCategory.setText(R.string.overweight);
                } else {
                    tvCategory.setText(R.string.obese);
                }

                Calendar now = Calendar.getInstance();
                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        (now.get(Calendar.MONTH)+1) + "/" +
                        now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);
                tvDate.setText(R.string.lastCalcDate + datetime);
                tvBMI.setText(R.string.lastCalcBMI + String.format("%.2f",bmi));
                etHeight.setText("");
                etWeight.setText("");

                save();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etWeight.setText("");
                etHeight.setText("");
                tvBMI.setText(R.string.lastCalcBMI);
                tvDate.setText(R.string.lastCalcDate);
                tvCategory.setText("");
            }
        });
    };

    protected void save() {
        String bmi = tvBMI.getText().toString();
        String date = tvDate.getText().toString();
        String category = tvCategory.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();

        prefEdit.putString("date", date);
        prefEdit.putString("bmi", bmi);
        prefEdit.putString("category", category);
    }

    @Override
    protected void onPause() {
        super.onPause();

        float weight = Float.parseFloat(etWeight.getText().toString());
        float height = Float.parseFloat(etHeight.getText().toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putFloat("weight", weight);
        prefEdit.putFloat("height", height);

        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String date = prefs.getString("date","Last Calculated Date:");
        String bmi = prefs.getString("bmi","Last Calculated BMI");
        String category = prefs.getString("category","");
        tvDate.setText(date);
        tvBMI.setText(bmi);
        tvCategory.setText(category);
    }
}