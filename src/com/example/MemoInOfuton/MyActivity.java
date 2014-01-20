/*
 * Copyright (c) 2014. Kensuke Kousaka
 *
 * This software is released under the MIT License
 *
 * http://opensource.org/licenses/mit-license.php
 */

package com.example.MemoInOfuton;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity
	{
		/**
		 * Called when the activity is first created.
		 */
		@Override
		public void onCreate (Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.main);
			}
	}
