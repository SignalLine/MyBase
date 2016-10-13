package com.rilin.lzy.mybase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rilin.lzy.mybase.R;


public class LoadingDialog extends Dialog {
	private Context context = null;
	private static LoadingDialog LoadingDialog = null;

	private ImageView i1, i2, i3;
	private Animation a1, a2, a3;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		init();
	}

	private void init() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.fragment_loading_dialog, null, true);
		a1 = AnimationUtils.loadAnimation(context, R.anim.anim_loading);
		i1 = (ImageView) view.findViewById(R.id.gear_0);

		a2 = AnimationUtils.loadAnimation(context, R.anim.anim_loading_reverse);
		i2 = (ImageView) view.findViewById(R.id.gear_1);

		a3 = AnimationUtils.loadAnimation(context, R.anim.anim_loading);
		i3 = (ImageView) view.findViewById(R.id.gear_2);
		setContentView(view);
	}

	public static LoadingDialog createDialog(Context context) {
		LoadingDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
		LoadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		LoadingDialog.getWindow().getAttributes().width = -1;
		return LoadingDialog;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dismiss();
		}
	};

	public void remove() {
		handler.sendMessageDelayed(handler.obtainMessage(), 5000);
	}

	// public void onWindowFocusChanged(boolean hasFocus) {
	//
	// if (LoadingDialog == null) {
	// return;
	// }
	//
	// ImageView imageView = (ImageView) LoadingDialog
	// .findViewById(R.id.loadingImageView);
	// AnimationDrawable animationDrawable = (AnimationDrawable) imageView
	// .getBackground();
	// animationDrawable.start();
	// }
	@Override
	public void dismiss() {
		super.dismiss();
		i1.clearAnimation();
		i2.clearAnimation();
		i3.clearAnimation();

	}

	@Override
	public void show() {
		super.show();
		i1.startAnimation(a1);
		i2.startAnimation(a2);
		i3.startAnimation(a3);

	}

}
