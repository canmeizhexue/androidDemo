package com.canmeizhexue.common.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.canmeizhexue.common.R;
import com.canmeizhexue.common.utils.ContextUtils;

/**
 * 字母索引
 * 
 * @author silence
 *
 */
public class LetterListView extends View {

	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	char[] letters = { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	int choose = -1;
	Paint paint = new Paint();
	boolean showBkg = false;

	public LetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LetterListView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(Color.parseColor("#40000000"));
		}

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / letters.length;
		for (int i = 0; i < letters.length; i++) {
			paint.setColor(getResources().getColor(R.color.letter_listview_text_color));
			paint.setTextSize(ContextUtils.dp2px(getContext(), 14));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			if (i == choose) {
				paint.setColor(getResources().getColor(R.color.letter_listview_text_color));
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(String.valueOf(letters[i])) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(String.valueOf(letters[i]), xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * letters.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c) {
				if (c >= 0 && c < letters.length) {

					choose = c;
					invalidate();
					if (listener != null) {
						listener.onTouchingLetterChanged(letters[c]);
					}
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c) {
				if (c > 0 && c < letters.length) {

					choose = c;
					invalidate();
					if (listener != null) {
						listener.onTouchingLetterChanged(letters[c]);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(char ch);
	}

}