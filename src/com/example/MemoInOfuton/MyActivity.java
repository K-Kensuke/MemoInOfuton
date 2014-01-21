/*
 * Copyright (c) 2014. Kensuke Kousaka
 *
 * This software is released under the MIT License
 *
 * http://opensource.org/licenses/mit-license.php
 */

package com.example.MemoInOfuton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * メインアクティビティ
 */
public class MyActivity extends Activity
	{
		// デバッグ用タグ
		private static final String TAG = MyActivity.class.getSimpleName();

		private static final String COMMAND_START = "メモ開始";
		private static final String COMMAND_EXIT = "メモ終了";

		// どのIntentからstartActivityForResultが呼び出されたのかを判断するため
		private static final int REQUEST_CODE = 1;
		private static final int RECEIVE_MEMO_CODE = 2;

		/**
		 * Called when the activity is first created.
		 */
		@Override
		public void onCreate (Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.main);

				Button recognizerButton = (Button) findViewById(R.id.recognizer);
				recognizerButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick (View v)
						{
							recognizer();
						}
				});


			}


		/**
		 * 音声認識Activityを起動する
		 */
		private void recognizer()
			{
				// 音声認識Activityを起動するためのIntentを設定する
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

				startActivityForResult(intent, REQUEST_CODE);
			}


		/**
		 * startActivityForResultで起動させたActivityがfinish()により破棄された時にコールされる
		 * @param requestCode startActivityForResultの第二引数で指定した値が渡される
		 * @param resultCode 起動先のActivity.setResultの第一引数が渡される
		 * @param data 起動先Activityから送られてくるIntent
		 */
		@Override
		protected void onActivityResult (int requestCode, int resultCode, Intent data)
			{
				super.onActivityResult(requestCode, resultCode, data);

				// requestCodeを確認して，自分が発行したIntentの結果であれば処理を行う
				if ((requestCode == REQUEST_CODE) && (resultCode == RESULT_OK))
					{
						// 結果はArrayListで返ってくる（入力に対してこれですか？的な候補がリストで返ってくる．）
						ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

						for (int i = 0; i < results.size(); i++)
							{
								if (results.get(i) == COMMAND_START)
									{
										Log.d(TAG, "COMMAND");

										// TODO スタートコマンドを受け取ったら，別のプロセスIDで再度音声入力アクティビティを実行する
										Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
										intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
										startActivityForResult(intent, RECEIVE_MEMO_CODE);

										break;
									}
								else if (results.get(i) == COMMAND_EXIT)
									{
										Log.d(TAG, "COMMAND_EXIT");
										break;
									}
							}

						// コマンド文字列が入力されなかった場合
						// 音声入力をさせる
						Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
						intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
						startActivityForResult(intent, REQUEST_CODE);
					}
				else if ((requestCode == RECEIVE_MEMO_CODE) && (resultCode == RESULT_OK))
					{
						ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

						// TODO メモして，再度REQUEST_CODEで音声入力アクティビティを実行する
						if (!WriteData.writeDataToSdCard(getPackageName(), "RecordData.dat", results.get(0)))
							{
								Log.e(TAG, "Error while output data");
								return;
							}

						Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
						intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
						startActivityForResult(intent, REQUEST_CODE);
					}
				else
					{
						Log.e(TAG, "Error while starting RecognizerActivity");
					}
			}
	}
