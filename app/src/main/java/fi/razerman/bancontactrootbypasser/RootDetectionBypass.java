package fi.razerman.bancontactrootbypasser;

/**
 * Created by Razerman on 14.10.2016.
 */


import android.util.Log;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XC_MethodReplacement.returnConstant;

public class RootDetectionBypass implements IXposedHookLoadPackage {
    private static final String TAG = RootDetectionBypass.class.getSimpleName();

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("mobi.inthepocket.bcmc.bancontact")){
            Log.d(TAG, "Bancontact app detected, starting to bypass root detection!");

            // Native methods
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "existInstalledRootedApps", returnConstant(0));           // Check 1
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "existRootedProcessInProcessList", returnConstant(0));    // Check 2
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "existsSu", returnConstant(0));                           // Check 3
            findAndHookMethod("com.awl.bfi.ssm.NativeAdapter", lpparam.classLoader, "lookForSpecificBinaries", returnConstant(0));            // Check 4
            Log.d(TAG, "Native methods bypassed!");

            // RootedDetector
            try{
                findAndHookMethod("ʟ", lpparam.classLoader, "ˊ", returnConstant(false));                                    // Check 1
                findAndHookMethod("ʟ", lpparam.classLoader, "ˋ", returnConstant(false));                                    // Check 2
                findAndHookMethod("ʟ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 3
                findAndHookMethod("ʟ", lpparam.classLoader, "ˎ", returnConstant(false));                                    // Check 4
                Log.d(TAG, "Old RootedDetector bypassed!");
            }
            catch (Throwable e){
                Log.d(TAG, "Old RootedDetector failed, trying a new one!");
                try{
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ʻ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ˏ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("Ϊ", lpparam.classLoader, "ᐝ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "New RootedDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "New RootedDetector failed!");

                }
            }

            // EmulatorDetector
            try{
                findAndHookMethod("Ӏ", lpparam.classLoader, "ˊ", returnConstant(false));                                    // Check 1
                findAndHookMethod("Ӏ", lpparam.classLoader, "ˋ", returnConstant(false));                                    // Check 2
                findAndHookMethod("Ӏ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 3
                findAndHookMethod("Ӏ", lpparam.classLoader, "ˎ", returnConstant(false));                                    // Check 4
                findAndHookMethod("Ӏ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 5
                Log.d(TAG, "Old EmulatorDetector bypassed!");
            }
            catch (Throwable e){
                Log.d(TAG, "Old EmulatorDetector failed, trying a new one!");
                try{
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ʻ", android.content.Context.class, returnConstant(false));     // Check 1
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 2
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 3
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ˏ", android.content.Context.class, returnConstant(false));     // Check 4
                    findAndHookMethod("Ӏ", lpparam.classLoader, "ᐝ", android.content.Context.class, returnConstant(false));     // Check 5
                    Log.d(TAG, "New EmulatorDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "New EmulatorDetector failed!");

                }
            }

            // DebuggerDetector (USB Debugging)
            try{
                findAndHookMethod("ﺫ", lpparam.classLoader, "ˋ", android.content.Context.class, returnConstant(false));     // Check 1
                Log.d(TAG, "Old DebuggerDetector bypassed!");
            }
            catch (Throwable e){
                Log.d(TAG, "Old DebuggerDetector failed, trying a new one!");
                try{
                    findAndHookMethod("ĭ", lpparam.classLoader, "ˎ", android.content.Context.class, returnConstant(false));     // Check 1
                    Log.d(TAG, "New DebuggerDetector bypassed!");
                }
                catch (Throwable ex){
                    Log.d(TAG, "New DebuggerDetector failed!");

                }
            }

            // NOT WORKING, I don't know how to succeed in this because there is super.onResume(); call inside.
            // Visual USB Debugging pop up warning (different check) which is not preventing anything but just popping up.
            //findAndHookMethod("mobi.inthepocket.bcmc.bancontact.activities.BaseActivity", lpparam.classLoader, "ʾ", returnConstant(true));     // Check 1

            Log.d(TAG, "Bypassed Bancontacts root detection!");
        }
    }
}
