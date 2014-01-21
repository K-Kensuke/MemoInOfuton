/*
 * Copyright (c) 2014. Kensuke Kousaka
 *
 * This software is released under the MIT License
 *
 * http://opensource.org/licenses/mit-license.php
 */

package com.example.MemoInOfuton;

import android.os.Environment;
import android.util.Log;

import java.io.*;

/**
 * Created by kousaka on 2014/01/21.
 */
public class WriteData
	{
		private static final String TAG = WriteData.class.getSimpleName();

		public static boolean writeDataToSdCard (String folderName, String fileName, String data)
			{
				// SDカードのマウント確認
				String status = Environment.getExternalStorageState();

				if (!status.equals(Environment.MEDIA_MOUNTED))
					{
						// マウントされていない場合
						Log.e(TAG, "SD Card is not mounted");
						return false;
					}

				// SDカードのパスを取得
				String SD_PATH = Environment.getExternalStorageDirectory().getPath();

				// SDカードにフォルダを作成
				String FOLDER_PATH = SD_PATH + File.separator + folderName;

				File file = new File(FOLDER_PATH);

				// フォルダが存在していない場合，フォルダを作成する
				if (!file.exists())
					{
						// フォルダがない場合
						Log.d(TAG, "Folder is not exist");

						if (!file.mkdirs())
							{
								Log.d(TAG, "Create Folder is failed");
								return false;
							}
					}

				String filePath = FOLDER_PATH + File.separator + fileName;
				file = new File(filePath);

				try
					{
						FileOutputStream fileOutputStream = new FileOutputStream(file, true); // trueにして，追記モード
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
						BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

						bufferedWriter.append(data);
						bufferedWriter.close();

						// 書き込み完了
						return true;
					}
				catch (FileNotFoundException e)
					{
						e.printStackTrace();
						return false;
					}
				catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
						return false;
					}
				catch (IOException e)
					{
						e.printStackTrace();
						return false;
					}
			}
	}
