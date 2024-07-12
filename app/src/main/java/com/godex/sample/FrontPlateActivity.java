package com.godex.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.godex.Godex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FrontPlateActivity extends Activity {

    private Spinner spinnerPlateSize;
    public static String exepction = "";
    public static String WFaddress = ""; //ex: 192.168.0.1
    public boolean N = false;
    private Button buttonPrint,logoutButton;
    private TextView textViewLetters, textViewDealerInfo, textViewPlateDimensions, textViewLettersRear, textViewDealerInfoRear,bsauText,bsauTextRear;
    private EditText editTextNumberPlate, editTextDealerInfo , editBsauText;
    private CheckBox checkBoxShowBorder;
    private FrameLayout frameLayoutDealerInfo;
    private FrameLayout frameLayoutDealerInfoRear;
    private String[] plateSizes = {"Size 1", "Size 2", "Size 3"};
    private String dealerName = "DealerName";
    private String postCode = "12345";
    private String font = "sans-serif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_plate);
        getActionBar().hide();
//        spinnerPlateSize = findViewById(R.id.spinnerPlateSize);
        checkBoxShowBorder = findViewById(R.id.checkBoxShowBorder);
        logoutButton = findViewById(R.id.logoutButtonNumberPlate);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long remainingDays = sharedPreferences.getLong("remaining_days", -1);
        TextView remainingDaysTextView = findViewById(R.id.remaining_days_text_view_fromApi);
        remainingDaysTextView.setText("Remaining days: " + remainingDays);
        textViewLetters = findViewById(R.id.textViewLetters);
        Typeface customFont = ResourcesCompat.getFont(this, R.font.charleswrightbold);
        textViewLetters.setTypeface(customFont);
        textViewLettersRear = findViewById(R.id.textViewLettersRear);
        textViewLettersRear.setTypeface(customFont);
        textViewDealerInfo = findViewById(R.id.textViewDealerInfo);
        textViewDealerInfo.setTypeface(customFont);
        textViewDealerInfoRear = findViewById(R.id.textViewDealerInfoRear);
        textViewDealerInfoRear.setTypeface(customFont);
        textViewPlateDimensions = findViewById(R.id.textViewPlateDimensions);
        editTextNumberPlate = findViewById(R.id.editTextNumberPlate);
        editTextDealerInfo = findViewById(R.id.editTextDealerInfo);
        editBsauText = findViewById(R.id.editTextBSAU);
        frameLayoutDealerInfo = findViewById(R.id.frameLayoutDealerInfo);
        frameLayoutDealerInfo.setVisibility(View.GONE);

        frameLayoutDealerInfoRear = findViewById(R.id.frameLayoutDealerInfoRear);
        frameLayoutDealerInfoRear.setVisibility(View.GONE);

        updateTextViewLetters(editTextNumberPlate.getText().toString());
        updateTextViewDealerInfo(editTextDealerInfo.getText().toString());
        textViewLetters.setTypeface(Typeface.create(font, Typeface.BOLD));
        textViewDealerInfo.setTypeface(Typeface.create(font, Typeface.NORMAL));

        bsauText = findViewById(R.id.textViewBsauFront);
        Typeface customFontBsau = ResourcesCompat.getFont(this, R.font.charleswrightbold);
        bsauText.setTypeface(customFontBsau);

        bsauTextRear = findViewById(R.id.textViewBsauRear);
        Typeface customFontBsauRear = ResourcesCompat.getFont(this, R.font.charleswrightbold);
        bsauTextRear.setTypeface(customFontBsauRear);


        try {
						if (N = Godex.openport(null, 3)) {
//							TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//							exceptionTextView.setText("USB Connected Successfully!");
							Toast.makeText(getApplicationContext(), "USB Connected", Toast.LENGTH_SHORT).show();
						} else {
//							TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//							exceptionTextView.setText("USB Connect fail");
							Toast.makeText(getApplicationContext(), "USB Connect fail", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						exepction = "exception"+e.getMessage();
						Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//						TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//						exceptionTextView.setText(exepction);
					}
        editTextNumberPlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTextViewLetters(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextDealerInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateFrameLayoutVisibility(s.toString());
                updateTextViewDealerInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        editBsauText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTextBsau(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				finish();
            }
        });

        checkBoxShowBorder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                findViewById(R.id.containerFront).setBackgroundResource(R.drawable.border);
                findViewById(R.id.containerFrontRear).setBackgroundResource(R.drawable.border_rear);
            } else {
                findViewById(R.id.containerFront).setBackgroundResource(R.drawable.border_with_condition);
                findViewById(R.id.containerFrontRear).setBackgroundResource(R.drawable.border_with_condition_rear);
            }
        });

        buttonPrint = findViewById(R.id.buttonPrint);
        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

//        spinnerPlateSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedSize = plateSizes[position];
//                textViewPlateDimensions.setText("Selected size: " + selectedSize);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }
    private void updateFrameLayoutVisibility(String text) {
        if (text == null || text.isEmpty()) {
            frameLayoutDealerInfo.setVisibility(View.GONE);
            frameLayoutDealerInfoRear.setVisibility(View.GONE);
        } else {
            frameLayoutDealerInfo.setVisibility(View.VISIBLE);
            frameLayoutDealerInfoRear.setVisibility(View.VISIBLE);
        }}
    private void updateTextViewLetters(String text) {
        textViewLetters.setText(text);
        textViewLetters.setTypeface(Typeface.create(font, Typeface.BOLD));
        textViewLettersRear.setText(text);
        textViewLettersRear.setTypeface(Typeface.create(font, Typeface.BOLD));
    }

    private void updateTextViewDealerInfo(String text) {
        textViewDealerInfo.setText(text);
        textViewDealerInfo.setTypeface(Typeface.create(font, Typeface.NORMAL));

        textViewDealerInfoRear.setText(text);
        textViewDealerInfoRear.setTypeface(Typeface.create(font, Typeface.NORMAL));
    }
    private void updateTextBsau(String text) {
        bsauText.setText(text);
        bsauText.setTypeface(Typeface.create(font, Typeface.NORMAL));
        bsauTextRear.setText(text);
        bsauTextRear.setTypeface(Typeface.create(font, Typeface.NORMAL));
    }

    private void generatePDF() {
        // Inflate the view to be rendered in the PDF
        View view = LayoutInflater.from(this).inflate(R.layout.xml_to_pdf, null);

        // Find the TextViews inside the inflated view
        TextView textViewLetters = view.findViewById(R.id.textViewLetters);
        TextView textViewDealerInfo = view.findViewById(R.id.textViewDealerInfo);
        TextView textViewBsauInfo = view.findViewById(R.id.textViewBsauFront);

        // Update TextViews with current EditText values
        textViewLetters.setText(editTextNumberPlate.getText().toString());
        textViewDealerInfo.setText(editTextDealerInfo.getText().toString());
        textViewBsauInfo.setText(editBsauText.getText().toString());

        // Measure and layout the view
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int height = view.getMeasuredHeight();
        view.layout(0, 0, width, height);

        // Create a PDF document
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Render the view on the PDF page's canvas
        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Save the PDF to a file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
        File file = new File(downloadsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            if (ActivityCompat.checkSelfPermission(FrontPlateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                    return;
                }
            }
            String filePath = file.getAbsolutePath();
            Godex.printPDF(filePath, 203, 90, 60, 0, Godex.ImageDithering.None);
            Godex.printPDF(filePath, 1, 1, 203, 180, 60, 60, Godex.ImageDithering.None);
            Toast.makeText(this, "PDF saved at: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}
