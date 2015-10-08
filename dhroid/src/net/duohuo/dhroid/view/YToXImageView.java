package net.duohuo.dhroid.view;


import net.duohuo.dhroid.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 根据宽度高度比例显示的imageview
 * @author duohuo-jinghap
 *
 */
public class YToXImageView extends ImageView {
	public YToXImageView(Context context) {
		super(context);
	}
	public YToXImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = getContext().obtainStyledAttributes(attrs,
				R.styleable.YToXImageView);
		heightToWidth = ta.getFloat(R.styleable.YToXImageView_heightToWidth, 0);
	}

	float heightToWidth = 0;


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public float getHeightToWidth() {
		return heightToWidth;
	}

	public void setHeightToWidth(float heightToWidth) {
		this.heightToWidth = heightToWidth;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (heightToWidth > 0) {
			int widthMode = MeasureSpec.getMode(widthMeasureSpec);
			int widthSize = MeasureSpec.getSize(widthMeasureSpec);
			int heightSize = (int) (widthSize * heightToWidth);
			if (widthMode != MeasureSpec.EXACTLY) {
				widthMode = MeasureSpec.EXACTLY;
			}
			setMeasuredDimension(widthSize, heightSize);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		if (heightToWidth < 0) {
			heightToWidth = (float) bm.getHeight() / (float) bm.getWidth();
		}
		invalidate();
	}
}
