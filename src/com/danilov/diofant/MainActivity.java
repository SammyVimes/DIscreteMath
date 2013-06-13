package com.danilov.diofant;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener{
	ListView lv;
	TableLayout table;
	int rowCount = 0;
	int curTab = 0;
	ArrayList<EditText> editableTextViews = new ArrayList<EditText>();
	Button solveChinese;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		table = (TableLayout)findViewById(R.id.table);
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("0");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Диофант");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("1");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Китайская теорема");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTabByTag("0");
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String tabId) {
				curTab = new Integer(tabId);
				if(curTab == 1){
					if(rowCount == 0){
						addRow();
						addRow();
					}
				}
			}
        	
        });
		joke();
		Button solve = (Button)findViewById(R.id.button1);
		Button refresh = (Button)findViewById(R.id.button2);
		Button add = (Button)findViewById(R.id.buttonAddRow);
		Button delete = (Button)findViewById(R.id.buttonDeleteRow);
		solveChinese = (Button)findViewById(R.id.solveChinese);
		solve.setOnClickListener(this);
		add.setOnClickListener(this);
		delete.setOnClickListener(this);
		refresh.setOnClickListener(this);
		solveChinese.setOnClickListener(this);
		lv = (ListView)findViewById(R.id.listView1);
		lv.setVisibility(ListView.INVISIBLE);
	}
	
	
	private void solveChinese(){
		ArrayList<String> equations = getEquationsFromTable();
		if(equations == null){
			return;
		}
		ChineseEquation ce = new ChineseEquation(equations);
		ce.solve();
		String answer = ce.getAnswer();
		String answerToDisplay = "Решение:\n" + answer;
		easyDialog(answerToDisplay);
	}
	
	private ArrayList<String> getEquationsFromTable(){
		ArrayList<String> equations = new ArrayList<String>();
		int size = editableTextViews.size();
		for(int i = 0; i < size/3; i++){
			for(int j = 0; j < 3; j++){
				EditText et = editableTextViews.get((i * 3) + j);
				String tmp = et.getText().toString();
				if(!checkString(tmp)){
					return null;
				}
				equations.add(tmp);
			}
		}
		return equations;
	}
	
	private void addRow(){
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		TableRow rowTitle = new TableRow(this);
		int etWidth = width/3;
		EditText et = null;;
		for(int i = 0; i < 3; i++){
			et = new EditText(this);
			et.setWidth(etWidth);
			et.setInputType(InputType.TYPE_CLASS_DATETIME);
			editableTextViews.add(et);
			rowTitle.addView(et);
		}
		table.addView(rowTitle);
		rowCount++;
	}
	
	private void deleteRow(){
		if(rowCount < 3){
			return;
		}
		int childCount = table.getChildCount();
		table.removeViewAt(childCount - 1);
		int size = editableTextViews.size();
		for(int i = 0; i < 3; i++){
			editableTextViews.remove(size - (1 + i));
		}
		rowCount--;
	}
	
	@SuppressLint("UseValueOf")
	private void solve(){
		EditText ev1 = (EditText)findViewById(R.id.editText1);
		EditText ev2 = (EditText)findViewById(R.id.editText2);
		EditText ev3 = (EditText)findViewById(R.id.editText3);
		String tmp = (ev1.getText().toString());
		if(!checkString(tmp)){
			return;
		}
		int a = new Integer(tmp);
		tmp = (ev2.getText().toString());
		if(!checkString(tmp)){
			return;
		}
		int b = new Integer(tmp);
		tmp = (ev3.getText().toString());
		if(!checkString(tmp)){
			return;
		}
		int c = new Integer(tmp);
		Equation equation = new Equation(a, b, c);
		equation.solve();
		TextView tv = (TextView)findViewById(R.id.textView2);
		tv.setText(equation.getAnswer());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, equation.getSteps());
		lv.setVisibility(ListView.VISIBLE);
		lv.setAdapter(adapter);
	}
	
	private boolean checkString(String str){
		boolean isNormalString = true;
		if(str.equals("")){
			isNormalString = false;
			toaster("Пустая строка");
		}
		if(str.contains(".")){
			isNormalString = false;
			toaster("Точки не нужны");
		}
		return isNormalString;
	}
	
	public void refresh(){
		EditText ev1 = (EditText)findViewById(R.id.editText1);
		EditText ev2 = (EditText)findViewById(R.id.editText2);
		EditText ev3 = (EditText)findViewById(R.id.editText3);
		ev1.setText("");
		ev2.setText("");
		ev3.setText("");
		lv.setVisibility(ListView.INVISIBLE);
		joke();
	}
	
	public void joke(){
		String[] jokes = {"Спокойствие\nСейчас всё решим", "Надо было готовиться"
				, "Самому не решить?", "Наверное, ты хочешь себя проверить", "Всё тлен", "Есть многое на свете, друг Горацио..."};
		int rand = new Random().nextInt(jokes.length);
		TextView tv = (TextView)findViewById(R.id.textView2);
		tv.setText(jokes[rand]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_exit:
				super.onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	public void toaster(String text){
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private void easyDialog(String message){
		EasyDialogFragment dialog = new EasyDialogFragment();
		dialog.setMessage(message);
		dialog.show(getSupportFragmentManager(), "dlg");
	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		switch(v.getId()){
		case R.id.button1:
			solve();
			break;
		case R.id.button2:
			refresh();
			break;
		case R.id.buttonAddRow:
			addRow();
			break;
		case R.id.buttonDeleteRow:
			deleteRow();
			break;
		case R.id.solveChinese:
			solveChinese();
			break;
		}
			
	}

}
