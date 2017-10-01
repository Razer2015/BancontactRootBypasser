package fi.razerman.bancontactrootbypasser;

/**
 * Created by Razerman on 14.10.2016.
 */


import android.content.Context;
import android.content.pm.PackageManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XC_MethodReplacement.returnConstant;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

import android.util.Log;

public class RootDetectionBypass implements IXposedHookLoadPackage {
    private static final String TAG = RootDetectionBypass.class.getSimpleName();

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("mobi.inthepocket.bcmc.bancontact")){
            Log.d(TAG, "Bancontact app detected, starting to bypass root detection!");

            final String APP_PACKAGE = "mobi.inthepocket.bcmc.bancontact";
            int versionCode = 0;
            try {
                final Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
                final Context context = (Context) callMethod(activityThread, "getSystemContext");
                versionCode = context.getPackageManager().getPackageInfo(APP_PACKAGE, 0).versionCode;
                Log.d(TAG, "Version: " + versionCode);

            } catch (PackageManager.NameNotFoundException e) {
                //Handle exception
                Log.d(TAG, "Couldn't retrieve version.");
            }

            // 2.4.1 = 20000045
            // 2.3.5.894 = 20000042

            // Native methods
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "existInstalledRootedApps", returnConstant(0));           // Check 1
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "existRootedProcessInProcessList", returnConstant(0));    // Check 2
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "existsSu", returnConstant(0));                           // Check 3
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "lookForSpecificBinaries", returnConstant(0));            // Check 4
            Log.d(TAG, "Native methods bypassed!");

            // RootedNativeDetector
            // Version: 20000046 -> o.js.smali

            // SignatureDetector
            // Version: 20000046 -> o.kV.smali

            // New way of bypassing the root detection (Untested)
            if(versionCode >= 20000046){
                boolean bypasssucceeded = true;
                Log.d(TAG, "Starting to bypass version 2.5.0 or higher");
                try {
                    findAndHookMethod("o.nv", lpparam.classLoader, "ˋ", String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String inputParam = param.args[0].toString();
                            inputParam = inputParam.replace("true", "false");
                            param.args[0] = inputParam;
                        }
                    });
                }
                catch (Throwable ex){
                    bypasssucceeded = false;
                    Log.d(TAG, "Failed to hook string one of the root detection class!");
                }
                try {
                    findAndHookMethod("o.nv", lpparam.classLoader, "ˏ", String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String inputParam = param.args[0].toString();
                            inputParam = inputParam.replace("true", "false");
                            param.args[0] = inputParam;
                        }
                    });
                }
                catch (Throwable ex){
                    bypasssucceeded = false;
                    Log.d(TAG, "Failed to hook string two of the root detection class!");
                }

                if(bypasssucceeded){
                    Log.d(TAG, "Bypassed Bancontacts root detection!");
                    return;
                }
                else{
                    Log.d(TAG, "Bancontacts root detection bypass failed!");
                }
            }

            // RootedDetector
            /*if(versionCode >= 20000046){
                try {
                    Log.d(TAG, "Starting to bypass RootedDetector version 2.5.0 or higher");
                    findAndHookMethod("o.kj", lpparam.classLoader, "ʼ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("o.kj", lpparam.classLoader, "ˊ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("o.kj", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("o.kj", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("o.kj", lpparam.classLoader, "ॱ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "RootedDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "RootedDetector failed!");
                }
            }*/
            if(versionCode >= 20000045){
                try {
                    Log.d(TAG, "Starting to bypass RootedDetector version 2.4.1 or higher");
                    findAndHookMethod("ᵥ", lpparam.classLoader, "ʻ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("ᵥ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("ᵥ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("ᵥ", lpparam.classLoader, "ˏ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("ᵥ", lpparam.classLoader, "ᐝ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "RootedDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "RootedDetector failed!");
                }
            }
            else if(versionCode >= 20000042){
                try {
                    Log.d(TAG, "Starting to bypass RootedDetector version 2.3.5.894 or higher");
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ʻ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ˏ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ᐝ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "RootedDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "RootedDetector failed!");
                }
            }
            else{
                try {
                    Log.d(TAG, "Starting to bypass RootedDetector version lower than 2.3.5.894");
                    findAndHookMethod("ʟ", lpparam.classLoader, "ˊ", returnConstant(false));                                    // Check 1
                    findAndHookMethod("ʟ", lpparam.classLoader, "ˋ", returnConstant(false));                                    // Check 2
                    findAndHookMethod("ʟ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("ʟ", lpparam.classLoader, "ˎ", returnConstant(false));                                    // Check 4
                    Log.d(TAG, "RootedDetector bypassed!");
                }
                catch (Throwable e){
                    Log.d(TAG, "RootedDetector failed, trying a new one!");
                }
            }

            // EmulatorDetector
            if(versionCode >= 20000045){
                try {
                    Log.d(TAG, "Starting to bypass EmulatorDetector version 2.4.1 or higher");
                    findAndHookMethod("ﺩ", lpparam.classLoader, "ʻ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("ﺩ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("ﺩ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("ﺩ", lpparam.classLoader, "ˏ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("ﺩ", lpparam.classLoader, "ᐝ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "EmulatorDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "EmulatorDetector failed!");
                }
            }
            else if(versionCode >= 20000042){
                try {
                    Log.d(TAG, "Starting to bypass EmulatorDetector version 2.3.5.894 or higher");
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ʻ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˏ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ᐝ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "EmulatorDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "EmulatorDetector failed!");
                }
            }
            else{
                try {
                    Log.d(TAG, "Starting to bypass EmulatorDetector version lower than 2.3.5.894");
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˊ", returnConstant(false));                                    // Check 1
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˋ", returnConstant(false));                                    // Check 2
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˎ", returnConstant(false));                                    // Check 4
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "EmulatorDetector bypassed!");
                }
                catch (Throwable e){
                    Log.d(TAG, "EmulatorDetector failed!");
                }
            }

            // DebuggerDetector (USB Debugging)
            if(versionCode >= 20000045){
                try {
                    Log.d(TAG, "Starting to bypass DebuggerDetector version 2.4.1 or higher");
                    findAndHookMethod("ﯦ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 1
                    Log.d(TAG, "DebuggerDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "DebuggerDetector failed!");
                }
            }
            else if(versionCode >= 20000042){
                try {
                    Log.d(TAG, "Starting to bypass DebuggerDetector version 2.3.5.894 or higher");
                    findAndHookMethod("ĭ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 1
                    Log.d(TAG, "DebuggerDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "DebuggerDetector failed!");
                }
            }
            else{
                try {
                    Log.d(TAG, "Starting to bypass DebuggerDetector version lower than 2.3.5.894");
                    findAndHookMethod("ﺫ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 1
                    Log.d(TAG, "DebuggerDetector bypassed!");
                }
                catch (Throwable e){
                    Log.d(TAG, "DebuggerDetector failed!");
                }
            }

            // NOT WORKING, I don't know how to succeed in this because there is super.onResume(); call inside.
            // Visual USB Debugging pop up warning (different check) which is not preventing anything but just popping up.
            //findAndHookMethod("mobi.inthepocket.bcmc.bancontact.activities.BaseActivity", lpparam.classLoader, "ʾ", returnConstant(true));     // Check 1

            Log.d(TAG, "Bypassed Bancontacts root detection!");
        }
    }
}
