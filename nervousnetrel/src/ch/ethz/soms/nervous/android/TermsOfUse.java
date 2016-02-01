package ch.ethz.soms.nervous.android;

import ch.ethz.soms.nervousnet.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

/**
 * This class allows the app to display the Terms of Use Dialog. 
 * Conditions for showing the Terms of Use Dialog:
 * - New Install
 * - New Version Update
 * - User not accepted the Terms.
 */
public class TermsOfUse {

	private Activity mActivity;
	private String TERMS_PREFIX = "TERMS_";
	
	
	public TermsOfUse(Activity context){
		this.mActivity = context;
	}
	
	public void showTerms(){
		
		final String versionKey;
		PackageInfo pkgInfo = null;
		try {
			
			pkgInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(),0);
			
		} catch (NameNotFoundException e) {
			Log.d(MainActivity.LOG_TAG,"TermsOfUse : Version Code could not be retrieved for checking if Terms of Use has been shown.");
			e.printStackTrace();
		}
		versionKey = TERMS_PREFIX + pkgInfo.versionCode;
		boolean termsShownFlag = PreferenceManager.getDefaultSharedPreferences(mActivity).getBoolean(versionKey, false);
		
		if(!termsShownFlag && pkgInfo != null){
			// Show the Eula
            String title = mActivity.getString(R.string.app_name) + " v" + pkgInfo.versionName;
 
            //Includes the updates as well so users know what changed.
            String message = "\n\n" + mActivity.getString(R.string.terms_of_use);
 
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton(R.string.button_label_accept, new Dialog.OnClickListener() {
 
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mActivity).edit();
                            editor.putBoolean(versionKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        	Intent intent = new Intent(mActivity, MainActivity.class);
        					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        					
        					mActivity.startActivity(intent);
        				
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {
 
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the EULA
                        	((StartUpActivity)mActivity).finish();
                            
                        }
 
                    });
            builder.setCancelable(false);
            
            AlertDialog alert =  builder.create();
            alert.show();

            alert.getWindow().getAttributes();

            TextView textView = (TextView) alert.findViewById(android.R.id.message);
            textView.setTextSize(12);
		}else{
			Intent intent = new Intent(mActivity, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			
			mActivity.startActivity(intent);
			mActivity.finish();
			
		}
		
	}
	
}
