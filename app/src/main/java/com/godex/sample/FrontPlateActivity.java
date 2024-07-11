package com.godex.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.godex.Godex;
import com.godex.sample.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FrontPlateActivity extends Activity {

    private Spinner spinnerPlateSize;
    public static String exepction = "";
    public static String WFaddress = ""; //ex: 192.168.0.1
    public boolean N = false;
    private Button buttonPrint;
    private TextView textViewLetters, textViewDealerInfo, textViewPlateDimensions, textViewLettersRear, textViewDealerInfoRear;
    private EditText editTextNumberPlate, editTextDealerInfo;
    private CheckBox checkBoxShowBorder;

    private String[] plateSizes = {"Size 1", "Size 2", "Size 3"};
    private String dealerName = "DealerName";
    private String postCode = "12345";
    private String font = "sans-serif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_plate);

        spinnerPlateSize = findViewById(R.id.spinnerPlateSize);
        checkBoxShowBorder = findViewById(R.id.checkBoxShowBorder);

        textViewLetters = findViewById(R.id.textViewLetters);
        textViewLettersRear = findViewById(R.id.textViewLettersRear);
        textViewDealerInfo = findViewById(R.id.textViewDealerInfo);
        textViewDealerInfoRear = findViewById(R.id.textViewDealerInfoRear);
        textViewPlateDimensions = findViewById(R.id.textViewPlateDimensions);
        editTextNumberPlate = findViewById(R.id.editTextNumberPlate);
        editTextDealerInfo = findViewById(R.id.editTextDealerInfo);

        updateTextViewLetters(editTextNumberPlate.getText().toString());
        updateTextViewDealerInfo(editTextDealerInfo.getText().toString());
        textViewLetters.setTypeface(Typeface.create(font, Typeface.BOLD));
        textViewDealerInfo.setTypeface(Typeface.create(font, Typeface.NORMAL));
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
                updateTextViewDealerInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
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

        spinnerPlateSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSize = plateSizes[position];
                textViewPlateDimensions.setText("Selected size: " + selectedSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

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

    private void generatePDF() {
        // Inflate the view to be rendered in the PDF
        View view = LayoutInflater.from(this).inflate(R.layout.xml_to_pdf, null);

        // Find the TextViews inside the inflated view
        TextView textViewLetters = view.findViewById(R.id.textViewLetters);
        TextView textViewDealerInfo = view.findViewById(R.id.textViewDealerInfo);

        // Update TextViews with current EditText values
        textViewLetters.setText(editTextNumberPlate.getText().toString());
        textViewDealerInfo.setText(editTextDealerInfo.getText().toString());

        // Measure and layout the view
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
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
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//				Uri uri = Uri.parse("/");
//				intent.setDataAndType(uri, "application/pdf");
//				startActivityForResult(Intent.createChooser(intent, "Open PDF"),1);
                String filePath = file.getAbsolutePath();
				Godex.printPDF(filePath,203,90,60,0, Godex.ImageDithering.None);
				Godex.printPDF(filePath,1,1,203,180,60,60, Godex.ImageDithering.None);
//            Toast.makeText(this, "PDF saved at: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
