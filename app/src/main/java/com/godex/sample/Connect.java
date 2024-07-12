package com.godex.sample;
import com.godex.Godex;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.WifiManager;
import android.text.Spanned;

public class Connect extends Activity
{
	public static String BTaddress = ""; //ex: 5C:6B:32:AE:B8:6F
	public static String exepction = "";
	public static String WFaddress = ""; //ex: 192.168.0.1
	public boolean N = false;


	private Button   printButton,
			connectUSBButton;

	private EditText mOutEditText, tbMTU, tbPacketSize;
	private TextView txtVersion;
	public class CustomTypefaceSpan extends TypefaceSpan {

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
	private float newSpacing = 0;
	private int newSpacingRound = 0;
	@SuppressLint("MissingInflatedId")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		// Hide ActionBar Icon
		getActionBar().setDisplayShowHomeEnabled(false);
		Godex.debug(3);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		WifiManager wifi = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//		EditText inputText = findViewById(R.id.inputText);



		Godex.getMainContext(Connect.this);
		try {
			if (N = Godex.openport(null, 3)) {
//				TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//				exceptionTextView.setText("USB Connected Successfully!");
				Toast.makeText(getApplicationContext(), "USB Connected", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Connect.this, FrontPlateActivity.class);
				startActivity(intent);
			} else {
//				TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//				exceptionTextView.setText("USB Connect fail");

				Toast.makeText(getApplicationContext(), "USB Connect fail", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Connect.this, FrontPlateActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			exepction = "exception"+e.getMessage();
			Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//			TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//			exceptionTextView.setText(exepction);
		}
//		CheckBox myCheckBox = findViewById(R.id.checkBox);


//		TextWatcher textWatcher = new TextWatcher() {
//
//			@Override
//			public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//			}
//
//			@SuppressLint("SetTextI18n")
//			public void afterTextChanged(Editable editable) {
//				String userInput = editable.toString();
//				SpannableString spannableString = new SpannableString(userInput);
//				float totalSpacing = 0;
//				int numberOfCharachters = 0;
//				int spaceBtwChar = 0;
//				float finalLength = 0;
//				float spaceLength = 0;
//				float startingPoint = 0;
//				float finalCharLength = 0;
//				for (int i = 0; i < userInput.length(); i++) {
//					char currentChar = userInput.charAt(i);
//
//					if (currentChar == 'I' || currentChar == '1') {
//
//						spannableString.setSpan(new CustomTypefaceSpan("sans-serif", 0.14f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//						totalSpacing += 0.14f;
//
//					} else if (currentChar == ' ') {
//
//						spannableString.setSpan(new CustomTypefaceSpan("sans-serif", 0.33f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//						totalSpacing += 0.33f;
//					} else {
//
//						spannableString.setSpan(new CustomTypefaceSpan("sans-serif", 0.50f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//						totalSpacing += 0.50f;
//
//					}
//					numberOfCharachters = i-2;
//					spaceBtwChar = numberOfCharachters*11;
//
//				}
//				finalLength = totalSpacing*100+spaceBtwChar;
//				finalCharLength = finalLength+10;
//
//				spaceLength = (520-finalCharLength)/2;
//				startingPoint = spaceLength+finalCharLength;
//				newSpacing = startingPoint*8;
//				long roundedValue = Math.round(newSpacing);
//				newSpacingRound = (int) roundedValue;
//				TextView displayTextViewFinal = findViewById(R.id.displayTextViewFinal);
//				displayTextViewFinal.setText("Character length: " + finalLength + " mm");
//				TextView displayTextViewFinalAdjusted = findViewById(R.id.displayTextViewFinalAdjusted);
//				displayTextViewFinalAdjusted.setText("Adjustment length: 10 mm");
//				TextView displayTextViewTotal = findViewById(R.id.displayTextViewTotal);
//				displayTextViewTotal.setText("Final Character length: " + finalCharLength + " mm");
//				TextView displayTextView = findViewById(R.id.displayTextView);
//				displayTextView.setText("Space Length: " + spaceLength + " mm");
//				TextView displayTextViewAgain = findViewById(R.id.displayTextViewAgain);
//				displayTextViewAgain.setText("Starting point of character: " + startingPoint + " mm");
//
//				TextView displayTextViewSecond = findViewById(R.id.displayTextViewSecond);
//				displayTextViewSecond.setText("Starting point of character: " + newSpacing + " points");
//
//
//
//			}
//		};

//		inputText.addTextChangedListener(textWatcher);

//		myCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//
//			String userInput = inputText.getText().toString();
//
//			if (isChecked) {
//
//			} else {
//			}
//		});



//		connectUSBButton = (Button) findViewById(R.id.BTUsb);
//		connectUSBButton.setOnClickListener(new OnClickListener() {
//
//
//			@Override
//			public void onClick(View v) {
//				try {
//					if (N = Godex.openport(null, 3)) {
//						TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//						exceptionTextView.setText("USB Connected Successfully!");
//						Toast.makeText(getApplicationContext(), "USB Connected", Toast.LENGTH_SHORT).show();
//					} else {
//						TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//						exceptionTextView.setText("USB Connect fail");
//						Toast.makeText(getApplicationContext(), "USB Connect fail", Toast.LENGTH_SHORT).show();
//					}
//				} catch (Exception e) {
//					exepction = "exception"+e.getMessage();
//					Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//					TextView exceptionTextView = findViewById(R.id.exceptionTextView);
//					exceptionTextView.setText(exepction);
//				}
//			}
//		});

		Typeface customFont = Typeface.createFromAsset(getAssets(), "Fatplates.TTF");


//		TextView customFontText = findViewById(R.id.customFontText);

//		customFontText.setTypeface(customFont);

//		printButton = (Button) findViewById(R.id.printUsb);

//		printButton.setOnClickListener( new OnClickListener() {
//
//			public void onClick(View v) {
//
//				boolean isChecked = myCheckBox.isChecked();
//				Godex.setup("50","8","2","1","3","0");
//				Godex.sendCommand("^L");
//				Godex.Bar_1D(Godex.BarCodeType.Code39,0,0,2,6,80,0, Godex.Readable.Bottom_Left,"1234");
//				Godex.Bar_QRCode(0,140,1,1,"M",8,5,0,"12345678");
//				Godex.Bar_DataMatrix(0,280,6,"0","1234567890");
//				String userInput = inputText.getText().toString();
//
//				String message = isChecked ? "Checked" : "Unchecked";
//
//
//				String prnFileChecked = "^XSETCUT,DOUBLECUT,0\n" +
//						"^Q520,0,0\n" +
//						"^W108\n" +
//						"^H6\n" +
//						"^P1\n" +
//						"^S4\n" +
//						"^AT\n" +
//						"^C1\n" +
//						"^R0\n" +
//						"~Q+0\n" +
//						"^O0\n" +
//						"^D0\n" +
//						"^E18\n" +
//						"~R255\n" +
//						"^L\n" +
//						"Dy2-me-dd\n" +
//						"Th:m:s\n" +
//						"Y32,67,RoundRect0\n" +
//						"Dy2-me-dd\n" +
//						"Th:m:s\n" +
//						"ATA,74,"+newSpacingRound+",801,801,0,3E,A,0,"+userInput+"\n" +
//						"ATB,787,294,17,17,0,3E,F,0,BB2 2AU | BSAU145E\n" +
//						"ATB,781,456,25,25,0,3E,F,0,Nom Plates\n" +
//						"E";
//
//				String prnFileUnchecked = "^XSETCUT,DOUBLECUT,0\n" +
//						"^Q520,0,0\n" +
//						"^W108\n" +
//						"^H6\n" +
//						"^P1\n" +
//						"^S4\n" +
//						"^AT\n" +
//						"^C1\n" +
//						"^R0\n" +
//						"~Q+0\n" +
//						"^O0\n" +
//						"^D0\n" +
//						"^E18\n" +
//						"~R255\n" +
//						"^L\n" +
//						"Dy2-me-dd\n" +
//						"Th:m:s\n" +
//						"Dy2-me-dd\n" +
//						"Th:m:s\n" +
//						"ATA,74,"+newSpacingRound+",801,801,0,3E,A,0,"+userInput+"\n" +
//						"ATB,787,294,17,17,0,3E,F,0,BB2 2AU | BSAU145E\n" +
//						"ATB,781,456,25,25,0,3E,F,0,Nom Plates\n" +
//						"E";
//				Godex.sendCommand( isChecked?prnFileChecked:prnFileUnchecked);
//
//				Godex.sendCommand("E");
//			}
//		});

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



}