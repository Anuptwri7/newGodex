package com.godex.sample;

import com.godex.Godex;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.WifiManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Connect extends Activity {
	// Class variables
	public static String BTaddress = ""; //ex: 5C:6B:32:AE:B8:6F
	public static String exepction = "";
	public static String WFaddress = ""; //ex: 192.168.0.1
	public boolean N = false;
	private LinearLayout container;
	private LinearLayout containerYellow;
	private Button printButton, connectUSBButton ,logoutButton,printPDFButton;;
	private EditText mOutEditText, tbMTU, tbPacketSize;
	private TextView txtVersion;
	String userInput, userInput2;
	private TextView numberPlateText;
	private float newSpacing = 0;
	private int newSpacingRound = 0;
	private boolean isBorderApplied = false;
	private static final int PERMISSION_REQUEST_CODE = 101;
	private LinearLayout containerLayout;
	public class CustomTypefaceSpan extends android.text.style.TypefaceSpan {
		private final float spacing;

		public CustomTypefaceSpan(String family, float spacing) {
			super(family);
			this.spacing = spacing;
		}

		public void updateDrawState(TextPaint paint) {
			super.updateDrawState(paint);
			paint.setLetterSpacing(spacing);
		}
	}

	@SuppressLint("MissingInflatedId")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);
		container = findViewById(R.id.container);
		containerYellow = findViewById(R.id.containerYellow);
//		numberPlateText = findViewById(R.id.numberPlateText);
		logoutButton = findViewById(R.id.logoutButton);
		SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
		long remainingDays = sharedPreferences.getLong("remaining_days", -1);
		TextView remainingDaysTextView = findViewById(R.id.remaining_days_text_view);
		remainingDaysTextView.setText("Remaining days: " + remainingDays);
		try {
			if (N = Godex.openport(null, 3)) {
				Toast.makeText(getApplicationContext(), "USB Connected", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "USB Connect fail", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			exepction = "exception" + e.getMessage();
			Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Connect.this, Tool.class);

				startActivity(intent);
//				finish();
			}
		});
		container.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white)); // Set container background color
		// Hide ActionBar Icon
		if (getActionBar() != null) {
			getActionBar().setDisplayShowHomeEnabled(false);
		}
		Godex.debug(3);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		WifiManager wifi = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		EditText inputText = findViewById(R.id.inputText);
//		EditText inputText2 = findViewById(R.id.inputText2);
//		inputText2.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
//
//			@Override
//			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
//
//			@Override
//			public void afterTextChanged(Editable editable) {
//				userInput2 = editable.toString();
//				updateTextViews();
//			}
//		});
		Godex.getMainContext(Connect.this);

		CheckBox myCheckBox = findViewById(R.id.checkBox);
		myCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			Log.d("Check", "check:" + isChecked);
			if (isChecked) {
				addBorderInsideContainer();
			} else {
				removeBorderInsideContainer();
			}
		});

		inputText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

			@SuppressLint("SetTextI18n")
			@Override
			public void afterTextChanged(Editable editable) {
				 userInput = editable.toString();
				SpannableString spannableString = new SpannableString(userInput);
				float totalSpacing = 0;
				int numberOfCharacters = 0;
				int spaceBtwChar = 0;
				float finalLength = 0;
				float spaceLength = 0;
				float startingPoint = 0;
				float finalCharLength = 0;

				for (int i = 0; i < userInput.length(); i++) {
					char currentChar = userInput.charAt(i);

					if (currentChar == 'I' || currentChar == '1') {
						spannableString.setSpan(new CustomTypefaceSpan("sans-serif", 0.14f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						totalSpacing += 0.14f;
					} else if (currentChar == ' ') {
						spannableString.setSpan(new CustomTypefaceSpan("sans-serif", 0.33f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						totalSpacing += 0.33f;
					} else {
						spannableString.setSpan(new CustomTypefaceSpan("sans-serif", 0.50f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						totalSpacing += 0.50f;
					}
					numberOfCharacters = i - 2;
					spaceBtwChar = numberOfCharacters * 11;
				}

				finalLength = totalSpacing * 100 + spaceBtwChar;
				finalCharLength = finalLength + 10;
				spaceLength = (520 - finalCharLength) / 2;
				startingPoint = spaceLength + finalCharLength;
				newSpacing = startingPoint * 8;
				long roundedValue = Math.round(newSpacing);
				newSpacingRound = (int) roundedValue;

				// Update your centered TextView
				TextView displayTextViewCentered = findViewById(R.id.displayTextViewCentered);
				TextView displayTextViewCenteredYellow = findViewById(R.id.displayTextViewCenteredYellow);
				Drawable borderDrawable = getResources().getDrawable(R.drawable.border_background);

				myCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
					if (isChecked) {
						// Add border when checkbox is checked
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							displayTextViewCentered.setBackground(borderDrawable);
							displayTextViewCenteredYellow.setBackground(borderDrawable);
						} else {
							displayTextViewCentered.setBackgroundDrawable(borderDrawable);
							displayTextViewCenteredYellow.setBackgroundDrawable(borderDrawable);
						}
					} else {
						// Remove border when checkbox is unchecked
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							displayTextViewCentered.setBackground(null);
							displayTextViewCenteredYellow.setBackground(null);
						} else {
							displayTextViewCentered.setBackgroundDrawable(null);
							displayTextViewCenteredYellow.setBackgroundDrawable(null);
						}
					}
				});

				displayTextViewCentered.setText(userInput);
				displayTextViewCenteredYellow.setText(userInput);

			}
		});

		myCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			String userInput = inputText.getText().toString();
			Log.d("Check", "checked:" + isChecked);
			if (isChecked) {
				addBorderInsideContainer();
			} else {
				removeBorderInsideContainer();
			}
		});


//		connectUSBButton = (Button) findViewById(R.id.BTUsb);
//		connectUSBButton.setOnClickListener(new View.OnClickListener() {
//
//
//
//			public void onClick(View v) {
//					try {
//						if (N = Godex.openport(null, 3)) {
////							TextView exceptionTextView = findViewById(R.id.exceptionTextView);
////							exceptionTextView.setText("USB Connected Successfully!");
//							Toast.makeText(getApplicationContext(), "USB Connected", Toast.LENGTH_SHORT).show();
//						} else {
////							TextView exceptionTextView = findViewById(R.id.exceptionTextView);
////							exceptionTextView.setText("USB Connect fail");
//							Toast.makeText(getApplicationContext(), "USB Connect fail", Toast.LENGTH_SHORT).show();
//						}
//					} catch (Exception e) {
//						exepction = "exception"+e.getMessage();
//						Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
////						TextView exceptionTextView = findViewById(R.id.exceptionTextView);
////						exceptionTextView.setText(exepction);
//					}
//			}
//		});

		Typeface customFont = Typeface.createFromAsset(getAssets(), "Fatplates.TTF");
		printPDFButton = findViewById(R.id.printPDF);
		printPDFButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				generatePDF();
//				if (ActivityCompat.checkSelfPermission(Connect.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
//						return;
//					}
//				}
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//				Uri uri = Uri.parse("/");
//				intent.setDataAndType(uri, "application/pdf");
//				startActivityForResult(Intent.createChooser(intent, "Open PDF"),1);
//				Godex.printPDF(filePath,203,90,60,0, Godex.ImageDithering.None);
//				Godex.printPDF(filePath,1,1,203,180,60,60, Godex.ImageDithering.None);
			}
		});


//		TextView customFontText = findViewById(R.id.customFontText);

//		customFontText.setTypeface(customFont);

		printButton = (Button) findViewById(R.id.printUsb);

		printButton.setOnClickListener( new View.OnClickListener() {

			public void onClick(View v) {

				boolean isChecked = myCheckBox.isChecked();
				Godex.setup("50","8","2","1","3","0");
				Godex.sendCommand("^L");
				Godex.Bar_1D(Godex.BarCodeType.Code39,0,0,2,6,80,0, Godex.Readable.Bottom_Left,"1234");
				Godex.Bar_QRCode(0,140,1,1,"M",8,5,0,"12345678");
				Godex.Bar_DataMatrix(0,280,6,"0","1234567890");
				String userInput = inputText.getText().toString();

				String message = isChecked ? "Checked" : "Unchecked";


				String prnFileChecked = "^XSETCUT,DOUBLECUT,0\n" +
						"^Q520,0,0\n" +
						"^W108\n" +
						"^H6\n" +
						"^P1\n" +
						"^S4\n" +
						"^AT\n" +
						"^C1\n" +
						"^R0\n" +
						"~Q+0\n" +
						"^O0\n" +
						"^D0\n" +
						"^E18\n" +
						"~R255\n" +
						"^L\n" +
						"Dy2-me-dd\n" +
						"Th:m:s\n" +
						"Y32,67,RoundRect0\n" +
						"Dy2-me-dd\n" +
						"Th:m:s\n" +
						"ATA,74,"+newSpacingRound+",801,801,0,3E,A,0,"+userInput+"\n" +
						"ATB,787,294,17,17,0,3E,F,0,BB2 2AU | BSAU145E\n" +
						"ATB,781,456,25,25,0,3E,F,0,Nom Plates\n" +
						"E";

				String prnFileUnchecked = "^XSETCUT,DOUBLECUT,0\n" +
						"^Q520,0,0\n" +
						"^W108\n" +
						"^H6\n" +
						"^P1\n" +
						"^S4\n" +
						"^AT\n" +
						"^C1\n" +
						"^R0\n" +
						"~Q+0\n" +
						"^O0\n" +
						"^D0\n" +
						"^E18\n" +
						"~R255\n" +
						"^L\n" +
						"Dy2-me-dd\n" +
						"Th:m:s\n" +
						"Dy2-me-dd\n" +
						"Th:m:s\n" +
						"ATA,74,"+newSpacingRound+",801,801,0,3E,A,0,"+userInput+"\n" +
						"ATB,787,294,17,17,0,3E,F,0,BB2 2AU | BSAU145E\n" +
						"ATB,781,456,25,25,0,3E,F,0,Nom Plates\n" +
						"E";
				Godex.sendCommand( isChecked?prnFileChecked:prnFileUnchecked);

				Godex.sendCommand("E");
			}
		});

	}
	private void addBorderInsideContainer() {
		// Get the border drawable
		Drawable borderDrawable = ContextCompat.getDrawable(this, R.drawable.style);

		// Set padding to the container (adjust as per your design)
		int padding = getResources().getDimensionPixelSize(R.dimen.border_padding);

		// Adjust padding to create space inside the container for the border
		int borderPadding = padding + getResources().getDimensionPixelSize(R.dimen.border_padding);

		container.setPadding(borderPadding, borderPadding, borderPadding, borderPadding);
		containerYellow.setPadding(borderPadding, borderPadding, borderPadding, borderPadding);

		// Set a layer drawable with yellow background and border drawable for containerYellow
		Drawable[] layersYellow = new Drawable[2];
		layersYellow[0] = new ColorDrawable(Color.parseColor("#f0b40c")); // Yellow background
		layersYellow[1] = borderDrawable;
		LayerDrawable layerDrawableYellow = new LayerDrawable(layersYellow);

		// Set white background and border drawable for container
		Drawable[] layers = new Drawable[2];
		layers[0] = new ColorDrawable(Color.WHITE); // White background
		layers[1] = borderDrawable;
		LayerDrawable layerDrawable = new LayerDrawable(layers);

		container.setBackground(layerDrawable);
		containerYellow.setBackground(layerDrawableYellow);
	}

	private void removeBorderInsideContainer() {
		// Set only white background for container
		Drawable[] layers = new Drawable[1];
		layers[0] = new ColorDrawable(Color.WHITE);
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		container.setBackground(layerDrawable);
		container.setPadding(0, 0, 0, 0); // Reset padding if needed

		// Set only yellow background for containerYellow
		Drawable[] layersYellow = new Drawable[1];
		layersYellow[0] = new ColorDrawable(Color.parseColor("#f0b40c")); // Yellow background
		LayerDrawable layerDrawableYellow = new LayerDrawable(layersYellow);
		containerYellow.setBackground(layerDrawableYellow);
		containerYellow.setPadding(0, 0, 0, 0); // Reset padding if needed
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onRestart()
	{
		super.onRestart();
	}

	@Override
	public void onStart()
	{
		super.onStart();

	}

	@Override
	public void onResume()
	{
		super.onResume();
	}
	private void generatePDF() {

		float dpi = 300;
		float mmToInch = 0.0393701f;
		int widthPx = (int) (520 * mmToInch * dpi);
		int heightPx = (int) (110 * mmToInch * dpi);

		// Create a new document
		PdfDocument document = new PdfDocument();

		// Create a page description
		PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(widthPx, heightPx, 1).create();

		// Start a page
		PdfDocument.Page page = document.startPage(pageInfo);

		// Get the canvas of the page
		Canvas canvas = page.getCanvas();

		// Draw a white background
		Paint paint = new Paint();
		paint.setColor(android.graphics.Color.WHITE);
		canvas.drawRect(0, 0, widthPx, heightPx, paint);

		// Draw "number plate" text in the center
		paint.setColor(android.graphics.Color.BLACK);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(60); // Adjust text size as needed
		float x = widthPx / 2;
		float y = heightPx / 2 - (paint.descent() + paint.ascent()) / 2; // Center vertically
		canvas.drawText(userInput, x, y, paint);

		// Finish the page
		document.finishPage(page);

		// Write the document content to a file
		String directoryPath = Environment.getExternalStorageDirectory().getPath() + "/Documents/";
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String filePath = directoryPath + "numberplate.pdf";
		File file = new File(filePath);
		try {
			document.writeTo(new FileOutputStream(file));
			Toast.makeText(this, "PDF generated at: " + filePath, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		// Close the document
		document.close();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				generatePDF();
			} else {
				Toast.makeText(this, "Permission denied to write to external storage", Toast.LENGTH_SHORT).show();
			}
		}
	}
	private void updateTextViews() {

	}
	private void setPaddingAboveBorder(TextView textView) {
		int paddingTop = 15;
		textView.setPadding(textView.getPaddingLeft(), paddingTop, textView.getPaddingRight(), textView.getPaddingBottom());
	}
}